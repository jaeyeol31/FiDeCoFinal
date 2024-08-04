package project.boot.fideco.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.boot.fideco.member.certificationEntity.CertificationEntity;

@Repository
public interface CertificationRepository extends JpaRepository<CertificationEntity, String> {

	CertificationEntity findByMemberId(String memberId);


}
