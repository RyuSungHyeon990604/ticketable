//package com.example.moduleticket.domain.member.repository;
//
//import com.example.moduleticket.domain.member.entity.Member;
//import java.util.Optional;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//public interface MemberRepository extends JpaRepository<Member, Long> {
//
//	boolean existsByEmail(@Param("email") String email);
//
//	@Query("select m from Member m where m.email = :email and m.deletedAt is null")
//	Optional<Member> findByEmail(@Param("email") String email);
//
//	@Query("select m from Member m where m.id = :memberId and m.deletedAt is null")
//	Optional<Member> findMemberById(@Param("memberId") Long memberId);
//}
