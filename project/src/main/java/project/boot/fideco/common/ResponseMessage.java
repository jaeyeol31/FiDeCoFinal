package project.boot.fideco.common;

public interface ResponseMessage {
	
	String SUCCESS = "Success.";
	
	String VALIDATION_FAIL = "Validation failed.";
	String DUPLICATE_ID = "Duplicate Id.";
	
	String SIGN_IN_FAIL = "로그인 정보가 일치하지 않습니다.";
	String CERTIFICATION_FAIL = "Certification failed.";
	
	String MAIL_FAIL = "mail send failed.";
	String DATABASE_ERROR = "Database error.";
}
