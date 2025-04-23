//package com.example.moduleticket.domain.member.entity;
//
//import com.example.modulecommon.entity.Timestamped;
//import com.example.moduleticket.domain.member.role.MemberRole;
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.EnumType;
//import jakarta.persistence.Enumerated;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import java.time.LocalDateTime;
//import lombok.AccessLevel;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Getter
//@Entity
//public class Member extends Timestamped {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//
//	@Column(length = 50, unique = true)
//	private String email;
//	private String password;
//	@Column(length = 20)
//	private String name;
//
//	@Column(length = 10)
//	@Enumerated(EnumType.STRING)
//	private MemberRole role;
//
//	private LocalDateTime deletedAt;
//
//	@Builder
//	public Member(String email, String password, String name, MemberRole role) {
//		this.email = email;
//		this.password = password;
//		this.name = name;
//		this.role = role;
//	}
//
//	public void memberDelete() {
//		this.deletedAt = LocalDateTime.now();
//	}
//
//	public void changePassword(String password) {
//		this.password = password;
//	}
//
//	private Member(Long id) {
//		this.id = id;
//	}
//
//	public static Member fromAuth(Long authId) {
//		return new Member(authId);
//	}
//}
