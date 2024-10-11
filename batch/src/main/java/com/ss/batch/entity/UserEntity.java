package com.ss.batch.entity;

import java.util.Map;

import javax.persistence.*;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.vladmihalcea.hibernate.type.json.JsonStringType;

import lombok.*;

@Data
@Entity
@Table(name = "user")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class UserEntity extends BaseEntity {

	@Id
	private String userId; // 아이디
	private String userName; // 이름

	@Enumerated(EnumType.STRING)
	private UserStatus status; // 사용자 상태
	private String phone; // 폰번호

	// Json형태로 저장된 데이터를 자바에서 사용하기위한
	// 구조
	// 라이브러리가 필요하다.

	@Type(type = "json")
	private Map<String, Object> meta; // 세부정보

	// 후반에 일괄적으로 고객한테 배포하기 위해서
	// 카카오톡 메시지 보낼때
	// UUID를 메타 데이터에서 추출해서 저장!

}