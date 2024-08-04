package project.boot.fideco.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CommentResponseDTO {
	private Long id;
	private String commentWriter;
	private String commentContents;
	private LocalDateTime commentCreatedTime;
}
