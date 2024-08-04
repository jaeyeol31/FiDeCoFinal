package project.boot.fideco.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class NoticeDTO {
	private int notice_num;
	private String notice_title;
	private String notice_content;
	private String notice_writer;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate notice_registday;
	private int notice_hit;
	
	private String filename;
	private String filepath;
}
