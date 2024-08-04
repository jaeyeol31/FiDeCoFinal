package project.boot.fideco.dto;

import lombok.Data;

@Data
public class CommentRequestDTO {
	private String commentWriter;
	private String commentContents;
	private int notice_num;
}
