package com.withquery.webide_spring_be.domain.file.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.withquery.webide_spring_be.domain.project.entity.Project;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "project_files")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "프로젝트 파일/디렉토리 엔티티")
public class File {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "파일 고유 ID", example = "123")
	private Long id;

	@Column(nullable = false, length = 255)
	@Schema(description = "파일/디렉토리 이름", example = "main.java", maxLength = 255)
	private String name;

	@Column(nullable = false, length = 500)
	@Schema(description = "파일/디렉토리 전체 경로", example = "/src/myProject/main.java", maxLength = 500)
	private String path;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Schema(description = "타입", example = "FILE", implementation = FileType.class)
	private FileType type;

	@Column(columnDefinition = "LONGTEXT")
	@Schema(description = "파일 내용 (디렉토리인 경우 null)", example = "public class Main {\n    public static void main(String[] args) {\n        System.out.println(\"Hello World\");\n    }\n}")
	private String content;

	@Column(name = "parent_id")
	@Schema(description = "상위 디렉토리 ID (루트인 경우 null)", example = "1")
	private Long parentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", nullable = false)
	@Schema(description = "소속 프로젝트")
	private Project project;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	@Schema(description = "파일 생성 일시", example = "2025-07-22T11:00:00")
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	@Schema(description = "파일 수정 일시", example = "2025-07-23T17:32:30")
	private LocalDateTime updatedAt;

	@Schema(hidden = true)
	public void updateName(String newName) {
		this.name = newName;
	}

	@Schema(hidden = true)
	public void updateContent(String newContent) {
		if (this.type == FileType.FILE) {
			this.content = newContent;
		}
	}

	@Schema(hidden = true)
	public void updatePath(String newPath) {
		this.path = newPath;
	}

	@Schema(hidden = true)
	public void updateParent(Long newParentId) {
		this.parentId = newParentId;
	}

	@Schema(hidden = true)
	public boolean isDirectory() {
		return this.type == FileType.DIRECTORY;
	}

	@Schema(hidden = true)
	public boolean isFile() {
		return this.type == FileType.FILE;
	}

	@Schema(hidden = true)
	public boolean isRoot() {
		return this.parentId == null;
	}

	@Schema(hidden = true)
	public String createChildPath(String childName) {
		if (this.isDirectory()) {
			return this.path.endsWith("/") ? this.path + childName : this.path + "/" + childName;
		}
		return this.path;
	}

	@Schema(hidden = true)
	public static File createFile(String name, String path, String content, Project project, Long parentId) {
		return File.builder()
			.name(name)
			.path(path)
			.type(FileType.FILE)
			.content(content)
			.parentId(parentId)
			.project(project)
			.build();
	}

	@Schema(hidden = true)
	public static File createDirectory(String name, String path, Project project, Long parentId) {
		return File.builder()
			.name(name)
			.path(path)
			.type(FileType.DIRECTORY)
			.parentId(parentId)
			.project(project)
			.build();
	}
}
