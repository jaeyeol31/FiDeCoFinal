package project.boot.fideco.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import project.boot.fideco.dto.request.auth.SignUpRequestDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "member")
@Table(name = "member")
public class MemberEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private Long id; // 자동 생성되는 회원 ID

    @Column(name = "member_id")
    private String memberId;

    @Column(name = "member_name")
    private String memberName;

    @Column(name = "member_pw")
    private String memberPw;

    @Column(name = "member_address")
    private String memberAddress;

    @Column(name = "member_phone")
    private String memberPhone;

    @Column(name = "member_email")
    private String memberEmail;

    @Column(name = "member_auth")
    private String memberAuth; // 권한

    @Column(name = "member_type")
    private String memberType; //로그인 방식
	
	public MemberEntity(SignUpRequestDTO dto) {
		this.memberId = dto.getMemberId();
        this.memberName = dto.getMemberName();
        this.memberPw = dto.getMemberPw();
        this.memberAddress = dto.getMemberAddress();
        this.memberPhone = dto.getMemberPhone();
        this.memberEmail = dto.getMemberEmail();
        this.memberAuth = "ROLE_USER"; // 권한의 기본 유저로
        this.memberType = "app"; // 로그인 방식 
	}
	
}