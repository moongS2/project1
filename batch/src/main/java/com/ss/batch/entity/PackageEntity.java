package com.ss.batch.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="package")
public class PackageEntity extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long packSeq; // 패키지 ID
	
	private String packageName;	// 패키지이름
	private Integer count;	// 횟수
	private Integer period; //기간
}
