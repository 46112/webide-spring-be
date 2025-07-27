package com.withquery.webide_spring_be.domain.file.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.withquery.webide_spring_be.domain.file.dto.FileContentResponse;
import com.withquery.webide_spring_be.domain.file.dto.FileContentSaveRequest;
import com.withquery.webide_spring_be.domain.file.dto.FileCreateRequest;
import com.withquery.webide_spring_be.domain.file.dto.FileResponse;
import com.withquery.webide_spring_be.domain.file.dto.FileTreeNode;
import com.withquery.webide_spring_be.domain.file.dto.FileUpdateRequest;
import com.withquery.webide_spring_be.domain.file.entity.File;
import com.withquery.webide_spring_be.domain.file.entity.FileType;
import com.withquery.webide_spring_be.domain.file.repository.FileRepository;
import com.withquery.webide_spring_be.domain.project.entity.Project;
import com.withquery.webide_spring_be.domain.project.repository.ProjectRepository;
import com.withquery.webide_spring_be.domain.project.service.ProjectService;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FileServiceImpl implements FileService {

	private final FileRepository fileRepository;
	private final ProjectService projectService;
	private final ProjectRepository projectRepository;

	@Override
	public void createRootDirectory(Long projectId, String projectName) {

		Project project = projectRepository.getReferenceById(projectId);

		File rootDir = File.builder()
			.project(project)
			.name(projectName)
			.type(FileType.DIRECTORY)
			.path("/")
			.parentId(null)
			.build();

	}

	@Override
	public FileTreeNode getFileTree(Long projectId) {

		List<File> files = fileRepository.findByProjectIdOrderByPathAsc(projectId);
		FileTreeNode root = buildFileTree(files);

		return root != null ? root : new FileTreeNode();
	}

	@Override
	public FileResponse createFile(Long projectId, FileCreateRequest request) {
		if (!projectService.existsProject(projectId)) {
			throw new RuntimeException();
		}

		File parentDir = null;
		if (request.getParentId() != null) {
			parentDir = fileRepository.findByIdAndProjectId(request.getParentId(), projectId)
				.orElseThrow();

			if (parentDir.getType() != FileType.DIRECTORY) {
				throw new RuntimeException();
			}
		}

		String path = buildFilePath(parentDir, request.getName());

		if (fileRepository.existsByProjectIdAndPath(projectId, path)) {
			throw new RuntimeException();
		}

		Project project = projectRepository.getReferenceById(projectId);

		File file = File.builder()
			.project(project)
			.name(request.getName())
			.type(request.getType())
			.path(path)
			.parentId(request.getParentId())
			.content(request.getType() == FileType.FILE ? "" : null)
			.build();

		File savedFile = fileRepository.save(file);

		return FileResponse.from(savedFile);
	}

	@Override
	public FileResponse updateFile(Long projectId, FileUpdateRequest request) {
		File file = fileRepository.findByIdAndProjectId(request.getId(), projectId)
			.orElseThrow();

		File newParentDir = null;
		if (request.getNewParentId() != null) {
			newParentDir = fileRepository.findByIdAndProjectId(request.getNewParentId(), projectId)
				.orElseThrow();
		}

		String newPath = buildFilePath(newParentDir, request.getNewName());

		if (!file.getPath().equals(newPath) && fileRepository.existsByProjectIdAndPath(projectId, newPath)) {
			updateChildrenPaths(file, newPath);
		}

		file.updateFile(request.getNewName(), newPath, request.getNewParentId());

		return FileResponse.from(file);
	}

	@Override
	public void deleteFile(Long projectId, Long fileId) {

		File file = fileRepository.findByIdAndProjectId(fileId, projectId)
			.orElseThrow();

		if (file.getParentId() == null) {
			throw new RuntimeException();
		}

		fileRepository.delete(file);
	}

	@Override
	public FileContentResponse getFileContent(Long projectId, Long fileId) {

		File file = fileRepository.findByIdAndProjectId(fileId, projectId)
			.orElseThrow();

		if (file.getType() != FileType.FILE) {
			throw new RuntimeException();
		}

		return FileContentResponse.from(file);
	}

	@Override
	public void saveFileContent(Long projectId, FileContentSaveRequest request) {

		File file = fileRepository.findByIdAndProjectId(request.getFileId(), projectId)
			.orElseThrow();

		if (file.getType() != FileType.FILE) {
			throw new RuntimeException();
		}

		file.updateContent(request.getContent());
	}

	@Override
	public void deleteAllProjectFiles(Long projectId) {

	}

	private FileTreeNode buildFileTree(List<File> files) {
		Map<Long, FileTreeNode> nodeMap = new HashMap<>();
		FileTreeNode root = null;

		for (File file : files) {
			FileTreeNode node = FileTreeNode.from(file);
			nodeMap.put(file.getId(), node);

			if (file.getParentId() == null) {
				root = node;
			}
		}

		for (File file : files) {
			if (file.getParentId() != null) {
				FileTreeNode parent = nodeMap.get(file.getParentId());
				FileTreeNode child = nodeMap.get(file.getId());

				if (parent != null && child != null) {
					parent.addChild(child);
				}
			}
		}

		return root;
	}

	private String buildFilePath(File parentDir,
		@NotBlank(message = "파일/디렉토리 이름은 필수입니다.") @Size(max = 255, message = "파일/디렉토리 이름은 255자를 초과할 수 없습니다.") String fileName) {
		if (parentDir == null) {
			return "/" + fileName;
		}

		String parentPath = parentDir.getPath();
		if (parentPath.endsWith("/")) {
			return parentPath + fileName;
		} else {
			return parentPath + "/" + fileName;
		}
	}

	private void updateChildrenPaths(File directory, String newPath) {
		List<File> children = fileRepository.findByParentId(directory.getId());

		for (File child : children) {
			String oldChildPath = child.getPath();
			String newChildPath = oldChildPath.replace(directory.getPath(), newPath);
			child.updatePath(newChildPath);

			if (child.getType() == FileType.DIRECTORY) {
				updateChildrenPaths(child, newChildPath);
			}
		}
	}

}
