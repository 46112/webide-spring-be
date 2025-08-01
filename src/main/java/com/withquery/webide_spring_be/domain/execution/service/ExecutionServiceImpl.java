package com.withquery.webide_spring_be.domain.execution.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.stereotype.Service;

import com.withquery.webide_spring_be.domain.execution.dto.ExecutionRequest;
import com.withquery.webide_spring_be.domain.execution.dto.ExecutionResult;
import com.withquery.webide_spring_be.domain.execution.dto.ExecutionStatus;
import com.withquery.webide_spring_be.domain.execution.dto.ProcessResult;
import com.withquery.webide_spring_be.domain.file.dto.FileContentResponse;
import com.withquery.webide_spring_be.domain.file.service.FileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExecutionServiceImpl implements ExecutionService{

	private final FileService fileService;
	private final LanguageDetector languageDetector;

	private final ExecutorService executorService = Executors.newCachedThreadPool();


	@Override
	public ExecutionResult executeFile(Long projectId, Long fileId, ExecutionRequest request) {
		FileContentResponse fileContent = fileService.getFileContent(projectId, fileId);
		if (fileContent == null) {
			throw new RuntimeException("파일을 찾을 수 없습니다.");
		}

		String languageToUse = null;

		if (request.getLanguage() != null && !request.getLanguage().isBlank()) {
			languageToUse = request.getLanguage();
		} else {
			languageToUse = languageDetector.detectLanguage(
				fileContent.getFileName(),
				fileContent.getContent()
			);
		}

		if (languageToUse == null || languageToUse.isBlank()) {
			throw new RuntimeException("언어를 지정해야 합니다.");
		}

		ExecutionRequest fileExecutionRequest = new ExecutionRequest();
		fileExecutionRequest.setCode(fileContent.getContent());
		fileExecutionRequest.setLanguage(languageToUse);
		fileExecutionRequest.setInput(request.getInput());
		fileExecutionRequest.setOptions(request.getOptions());

		return executeCode(projectId, fileExecutionRequest);
	}

	@Override
	public ExecutionResult executeCode(Long projectId, ExecutionRequest request) {
		String executionId = UUID.randomUUID().toString();
		LocalDateTime startTime = LocalDateTime.now();

		log.info("코드 실행 시작: executionId={}, language={}, projectId={}",
			executionId, request.getLanguage(), projectId);

		String normalizedLanguage = languageDetector.normalizeLanguage(request.getLanguage());
		if (normalizedLanguage == null) {
			throw new IllegalArgumentException("지원하지 않는 언어입니다: " + request.getLanguage());
		}

		if (!languageDetector.isExecutable(normalizedLanguage)) {
			throw new IllegalArgumentException("실행할 수 없는 언어입니다: " + normalizedLanguage);
		}

		final ExecutionRequest.ExecutionOptions finalOptions =
			request.getOptions() != null ? request.getOptions() : new ExecutionRequest.ExecutionOptions();

		int timeoutSeconds = 30;
		try {
			if (finalOptions.getTimeoutSeconds() != null && finalOptions.getTimeoutSeconds() > 0) {
				timeoutSeconds = Math.min(finalOptions.getTimeoutSeconds(), 60);
			}
		} catch (Exception e) {
			log.warn("타임아웃 설정을 가져오는 중 오류 발생, 기본값 사용: {}", e.getMessage());
		}

		Future<ExecutionResult> future = executorService.submit(() ->
			executeCodeInternal(
				executionId,
				request.getCode(),
				normalizedLanguage,
				request.getInput(),
				finalOptions,
				startTime
			)
		);

		try {
			return future.get(timeoutSeconds, TimeUnit.SECONDS);
		} catch (TimeoutException e) {
			future.cancel(true);
			return ExecutionResult.builder()
				.executionId(executionId)
				.status(ExecutionStatus.TIMEOUT)
				.errorMessage("실행 시간이 " + timeoutSeconds + "초를 초과했습니다.")
				.startTime(startTime)
				.endTime(LocalDateTime.now())
				.build();
		} catch (Exception e) {
			throw new RuntimeException("코드 실행 중 시스템 오류가 발생했습니다: " + e.getMessage(), e);
		}
	}

	private ExecutionResult executeCodeInternal(
		String executionId,
		String code,
		String language,
		String input,
		ExecutionRequest.ExecutionOptions options,
		LocalDateTime startTime
	) {
		Path tempDir = null;

		try {
			tempDir = Files.createTempDirectory("webide_execution_" + executionId);

			ProcessResult processResult = switch (language) {
				case "javascript" -> executeJavaScript(code, input, tempDir, options);
				case "python"    -> executePython(code, input, tempDir, options);
				case "java"      -> executeJava(code, input, tempDir, options);
				case "cpp", "c++"-> executeCpp(code, input, tempDir, options);
				case "c"         -> executeC(code, input, tempDir, options);
				case "go"        -> executeGo(code, input, tempDir, options);
				case "rust"      -> executeRust(code, input, tempDir, options);
				default -> throw new UnsupportedOperationException("아직 지원하지 않는 언어입니다: " + language);
			};

			LocalDateTime endTime = LocalDateTime.now();
			long executionTimeMs = java.time.Duration.between(startTime, endTime).toMillis();

			return ExecutionResult.from(
				executionId,
				processResult,
				determineStatus(processResult.getExitCode(), processResult.getStderr()),
				startTime,
				endTime,
				executionTimeMs
			);

		} catch (IOException e) {
			throw new RuntimeException("임시 디렉토리 생성에 실패했습니다.", e);
		} catch (UnsupportedOperationException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("코드 실행 중 예기치 않은 오류가 발생했습니다.", e);
		} finally {
			if (tempDir != null) {
				cleanupTempDirectory(tempDir);
			}
		}
	}

	private ProcessResult executeJavaScript(String code, String input, Path tempDir,
		ExecutionRequest.ExecutionOptions options) throws Exception {
		Path codeFile = tempDir.resolve("code.js");
		Files.write(codeFile, code.getBytes());

		ProcessBuilder pb = new ProcessBuilder("node", codeFile.toString());
		return executeProcess(pb, input, options);
	}

	private ProcessResult executePython(String code, String input, Path tempDir,
		ExecutionRequest.ExecutionOptions options) throws Exception {
		Path codeFile = tempDir.resolve("code.py");
		Files.write(codeFile, code.getBytes());

		ProcessBuilder pb = new ProcessBuilder("python3", codeFile.toString());
		return executeProcess(pb, input, options);
	}

	private ProcessResult executeJava(String code, String input, Path tempDir,
		ExecutionRequest.ExecutionOptions options) throws Exception {
		String className = "Main";
		if (code.contains("public class ")) {
			int start = code.indexOf("public class ") + "public class ".length();
			int end = code.indexOf(" ", start);
			if (end == -1) end = code.indexOf("{", start);
			if (end > start) {
				className = code.substring(start, end).trim();
			}
		}

		Path codeFile = tempDir.resolve(className + ".java");
		Files.write(codeFile, code.getBytes());

		ProcessBuilder compileBuilder = new ProcessBuilder("javac", codeFile.toString());
		ProcessResult compileResult = executeProcess(compileBuilder, null, options);

		if (compileResult.getExitCode() != 0) {
			return new ProcessResult("", compileResult.getStderr(), compileResult.getExitCode());
		}

		ProcessBuilder runBuilder = new ProcessBuilder("java", "-cp", tempDir.toString(), className);
		return executeProcess(runBuilder, input, options);
	}

	private ProcessResult executeCpp(String code, String input, Path tempDir,
		ExecutionRequest.ExecutionOptions options) throws Exception {
		Path codeFile = tempDir.resolve("code.cpp");
		Path execFile = tempDir.resolve("code");
		Files.write(codeFile, code.getBytes());

		ProcessBuilder compileBuilder = new ProcessBuilder("g++", "-o", execFile.toString(), codeFile.toString());
		ProcessResult compileResult = executeProcess(compileBuilder, null, options);

		if (compileResult.getExitCode() != 0) {
			return new ProcessResult("", compileResult.getStderr(), compileResult.getExitCode());
		}

		ProcessBuilder runBuilder = new ProcessBuilder(execFile.toString());
		return executeProcess(runBuilder, input, options);
	}

	private ProcessResult executeC(String code, String input, Path tempDir,
		ExecutionRequest.ExecutionOptions options) throws Exception {
		Path codeFile = tempDir.resolve("code.c");
		Path execFile = tempDir.resolve("code");
		Files.write(codeFile, code.getBytes());

		ProcessBuilder compileBuilder = new ProcessBuilder("gcc", "-o", execFile.toString(), codeFile.toString());
		ProcessResult compileResult = executeProcess(compileBuilder, null, options);

		if (compileResult.getExitCode() != 0) {
			return new ProcessResult("", compileResult.getStderr(), compileResult.getExitCode());
		}

		ProcessBuilder runBuilder = new ProcessBuilder(execFile.toString());
		return executeProcess(runBuilder, input, options);
	}

	private ProcessResult executeGo(String code, String input, Path tempDir,
		ExecutionRequest.ExecutionOptions options) throws Exception {
		Path codeFile = tempDir.resolve("main.go");
		Files.write(codeFile, code.getBytes());

		ProcessBuilder pb = new ProcessBuilder("go", "run", codeFile.toString());
		return executeProcess(pb, input, options);
	}

	private ProcessResult executeRust(String code, String input, Path tempDir,
		ExecutionRequest.ExecutionOptions options) throws Exception {
		Path codeFile = tempDir.resolve("main.rs");
		Path execFile = tempDir.resolve("main");
		Files.write(codeFile, code.getBytes());

		ProcessBuilder compileBuilder = new ProcessBuilder("rustc", "-o", execFile.toString(), codeFile.toString());
		ProcessResult compileResult = executeProcess(compileBuilder, null, options);

		if (compileResult.getExitCode() != 0) {
			return new ProcessResult("", compileResult.getStderr(), compileResult.getExitCode());
		}

		ProcessBuilder runBuilder = new ProcessBuilder(execFile.toString());
		return executeProcess(runBuilder, input, options);
	}

	private ProcessResult executeProcess(ProcessBuilder pb, String input,
		ExecutionRequest.ExecutionOptions options) throws Exception {
		File tmpDir = new File(System.getProperty("java.io.tmpdir"));
		pb.directory(pb.directory() != null ? pb.directory() : tmpDir);

		Process process = pb.start();

		if (input != null && !input.isEmpty()) {
			try (OutputStreamWriter writer = new OutputStreamWriter(process.getOutputStream())) {
				writer.write(input);
				writer.flush();
			}
		}
		process.getOutputStream().close();

		String stdout = readStream(process.getInputStream());
		String stderr = readStream(process.getErrorStream());

		boolean finished = process.waitFor(options.getTimeoutSeconds(), TimeUnit.SECONDS);
		if (!finished) {
			process.destroyForcibly();
			throw new RuntimeException("프로세스 실행 시간 초과");
		}

		return new ProcessResult(stdout, stderr, process.exitValue());
	}

	private String readStream(InputStream inputStream) throws IOException {
		StringBuilder sb = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
		}
		return sb.toString();
	}

	private ExecutionStatus determineStatus(int exitCode, String stderr) {
		if (exitCode == 0) {
			return ExecutionStatus.SUCCESS;
		} else if (stderr.toLowerCase().contains("compile") || stderr.toLowerCase().contains("syntax")) {
			return ExecutionStatus.COMPILE_ERROR;
		} else {
			return ExecutionStatus.RUNTIME_ERROR;
		}
	}

	private ExecutionResult createErrorResult(String errorMessage) {
		return ExecutionResult.builder()
			.executionId(UUID.randomUUID().toString())
			.status(ExecutionStatus.SYSTEM_ERROR)
			.errorMessage(errorMessage)
			.startTime(LocalDateTime.now())
			.endTime(LocalDateTime.now())
			.build();
	}

	private void cleanupTempDirectory(Path tempDir) {
		try {
			Files.walk(tempDir)
				.sorted((a, b) -> b.compareTo(a))
				.forEach(path -> {
					try {
						Files.delete(path);
					} catch (IOException e) {
						log.warn("임시 파일 삭제 실패: {}", path, e);
					}
				});
		} catch (IOException e) {
			log.warn("임시 디렉토리 정리 실패: {}", tempDir, e);
		}
	}

}
