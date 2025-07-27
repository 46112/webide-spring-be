package com.withquery.webide_spring_be.domain.file.service;

import com.withquery.webide_spring_be.domain.file.dto.FileTreeNode;

public interface FileService {

	void createRootDirectory(Long projectId, String projectName);

	FileTreeNode getFileTree(Long projectId);
}
