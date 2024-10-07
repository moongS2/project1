package com.ss.batch.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@MappedSuperclass
@Data
// 엔티티가 생성되거나 수정될 때 자동으로 생성일, 수정일을 기록할 수 있도록
// 필요한 작업을 수행 후 기록을 해준다.
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity { //baseEntity는 테이블을 생성하지않고 상속받은 엔티티테이블 밑에
									// 컬럼을 가지고가서 생성할 수 있도록! 도와주는 추상클래스!

	// 엔티티 생성시 실행!
	@CreatedDate
	@Column(name="create_at",nullable = false, updatable = false)
	private LocalDateTime createAt;
	
	// 업데이트할 때만 실행!
	@LastModifiedDate
	@Column(name = "modifed_at",nullable = false)
	private LocalDateTime modifiedAt;
}
