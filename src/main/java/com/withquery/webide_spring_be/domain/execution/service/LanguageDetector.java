package com.withquery.webide_spring_be.domain.execution.service;

import java.util.Set;

public interface LanguageDetector {

	String detectLanguageFromExtension(String filename);

	String detectLanguageFromCode(String code);

	String normalizeLanguage(String language);

	boolean isSupported(String language);

	Set<String> getSupportedLanguages();

	boolean isExecutable(String language);
	
	String detectLanguage(String filename, String code);
}
