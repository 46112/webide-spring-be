package com.withquery.webide_spring_be.domain.file.service;

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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FileServiceImpl implements FileService{

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
		return null;
	}

	@Override
	public FileResponse createFile(Long projectId, FileCreateRequest request) {
		return null;
	}

	@Override
	public FileResponse updateFile(Long projectId, FileUpdateRequest request) {
		return null;
	}

	@Override
	public void deleteFile(Long projectId, Long fileId) {

	}

	@Override
	public FileContentResponse getFileContent(Long projectId, Long fileId) {
		return null;
	}

	@Override
	public void saveFileContent(Long projectId, FileContentSaveRequest request) {

	}

	@Override
	public void deleteAllProjectFiles(Long projectId) {

	}
}
