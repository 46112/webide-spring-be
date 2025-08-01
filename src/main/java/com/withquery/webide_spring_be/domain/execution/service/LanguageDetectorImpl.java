package com.withquery.webide_spring_be.domain.execution.service;

import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class LanguageDetectorImpl implements LanguageDetector {

	private static final Map<String, String> EXTENSION_TO_LANGUAGE = Map.ofEntries(
		Map.entry("js", "javascript"),
		Map.entry("ts", "typescript"),
		Map.entry("jsx", "javascript"),
		Map.entry("tsx", "typescript"),
		Map.entry("html", "html"),
		Map.entry("css", "css"),
		Map.entry("py", "python"),
		Map.entry("java", "java"),
		Map.entry("cpp", "cpp"),
		Map.entry("cc", "cpp"),
		Map.entry("cxx", "cpp"),
		Map.entry("c", "c"),
		Map.entry("go", "go"),
		Map.entry("rs", "rust"),
		Map.entry("sql", "sql")
	);


	private static final Map<String, Set<String>> LANGUAGE_ALIASES = Map.of(
		"javascript", Set.of("js", "node", "nodejs"),
		"typescript", Set.of("ts"),
		"python", Set.of("py", "python3"),
		"cpp", Set.of("c++", "cxx"),
		"mysql", Set.of("sql"),
		"postgresql", Set.of("postgres", "psql"),
		"mongodb", Set.of("mongo")
	);

	private static final Set<String> SUPPORTED_LANGUAGES = Set.of(
		"javascript", "typescript", "python", "java", "cpp", "c",
		"go", "rust", "sql", "mysql", "postgresql", "mongodb"
	);

	@Override
	public String detectLanguageFromExtension(String filename) {
		if (filename == null || !filename.contains(".")) {
			return null;
		}

		String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
		return EXTENSION_TO_LANGUAGE.get(extension);
	}

	@Override
	public String detectLanguageFromCode(String code) {
		if (code == null || code.trim().isEmpty()) {
			return null;
		}

		code = code.trim();

		if (code.contains("console.log") || code.contains("function") ||
			code.contains("const ") || code.contains("let ") || code.contains("var ")) {
			if (code.contains(": string") || code.contains(": number") || code.contains("interface ")) {
				return "typescript";
			}
			return "javascript";
		}

		if (code.contains("print(") || code.contains("def ") ||
			code.contains("import ") || code.contains("from ") ||
			code.startsWith("#!/usr/bin/env python") || code.startsWith("# -*- coding:")) {
			return "python";
		}

		if (code.contains("public class") || code.contains("public static void main") ||
			code.contains("System.out.println") || code.contains("package ")) {
			return "java";
		}

		if (code.contains("#include") || code.contains("std::") || code.contains("cout <<")) {
			if (code.contains("std::") || code.contains("cout") || code.contains("cin")) {
				return "cpp";
			}
			return "c";
		}

		if (code.contains("package main") || code.contains("func main()") ||
			code.contains("fmt.Print") || code.contains("import (")) {
			return "go";
		}

		if (code.contains("fn main()") || code.contains("println!") ||
			code.contains("use std::") || code.contains("cargo")) {
			return "rust";
		}

		if (code.toUpperCase().contains("SELECT ") || code.toUpperCase().contains("INSERT ") ||
			code.toUpperCase().contains("UPDATE ") || code.toUpperCase().contains("DELETE ") ||
			code.toUpperCase().contains("CREATE TABLE")) {
			return "sql";
		}

		return null;
	}

	@Override
	public String normalizeLanguage(String language) {
		if (language == null) {
			return null;
		}

		String normalized = language.toLowerCase().trim();

		if (SUPPORTED_LANGUAGES.contains(normalized)) {
			return normalized;
		}

		for (Map.Entry<String, Set<String>> entry : LANGUAGE_ALIASES.entrySet()) {
			if (entry.getValue().contains(normalized)) {
				return entry.getKey();
			}
		}

		return null;
	}

	@Override
	public boolean isSupported(String language) {
		String normalized = normalizeLanguage(language);
		return normalized != null;
	}

	@Override
	public Set<String> getSupportedLanguages() {
		return Set.copyOf(SUPPORTED_LANGUAGES);
	}

	@Override
	public boolean isExecutable(String language) {
		String normalized = normalizeLanguage(language);
		if (normalized == null) {
			return false;
		}

		Set<String> nonExecutableLanguages = Set.of();
		return !nonExecutableLanguages.contains(normalized);
	}

	@Override
	public String detectLanguage(String filename, String code) {
		String fromExtension = detectLanguageFromExtension(filename);
		if (fromExtension != null && isSupported(fromExtension)) {
			return fromExtension;
		}

		String fromCode = detectLanguageFromCode(code);
		if (fromCode != null && isSupported(fromCode)) {
			return fromCode;
		}

		return null;
	}
}
