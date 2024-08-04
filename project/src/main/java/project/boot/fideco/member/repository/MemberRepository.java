package project.boot.fideco.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import project.boot.fideco.member.entity.MemberEntity;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

	MemberEntity findByMemberId(String memberId);

	boolean existsByMemberId(String memberId);
	
	boolean existsByMemberEmail(String memberEmail);
	
	
	//아이디, 비밀번호 찾기
    Optional<MemberEntity> findByMemberEmail(String memberEmail);
    Optional<MemberEntity> findFirstByMemberEmail(String memberEmail); // 첫 번째 결과만 반환하도록 수정
    Optional<MemberEntity> findByMemberIdAndMemberEmail(String memberId, String memberEmail);

}
