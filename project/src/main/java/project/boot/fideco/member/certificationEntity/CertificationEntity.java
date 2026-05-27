package project.boot.fideco.member.certificationEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "certification")
@Table(name = "certification")
public class CertificationEntity {
@Id
@Column(name = "member_id")
private String memberId;
@Column(name = "member_email")
private String memberEmail;
@Column(name = "certification_number")
private String certificationNumber;
}
