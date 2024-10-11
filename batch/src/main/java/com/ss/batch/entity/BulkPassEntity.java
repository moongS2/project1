package com.ss.batch.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

// 대량 이용권
// 다수의 이용자에게 이용권을 지급!
@Data
@Entity
@Table(name = "bulk_pass")
public class BulkPassEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bulkPassSeq;	// 대량이용권 순번
	private Long packageSeq;	// 패키지 순번
	private String userGroupId;	// 사용자 그룹 ID
	
	@Enumerated(EnumType.STRING)
	private BulkPassStatus status;
	private Integer count;
	
	private LocalDateTime startedAt;
	private LocalDateTime endedAt;

}
