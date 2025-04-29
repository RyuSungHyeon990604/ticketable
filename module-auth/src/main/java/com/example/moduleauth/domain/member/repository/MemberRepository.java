package com.example.moduleauth.domain.member.repository;

import java.util.Optional;

import com.example.moduleauth.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {
	
	boolean existsByEmail(String email);
	
	boolean existsByIdAndEmail(Long memberId, String email);
	
	@Query("select m from Member m where m.email = :email and m.deletedAt is null")
	Optional<Member> findByEmail(@Param("email") String email);
	
	@Query("select m from Member m where m.id = :memberId and m.deletedAt is null")
	Optional<Member> findMemberById(@Param("memberId") Long memberId);
}
