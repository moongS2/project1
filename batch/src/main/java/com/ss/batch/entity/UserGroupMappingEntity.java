package com.ss.batch.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="user_group_mapping")
@IdClass(UserGroupMappingId.class) // 복합키 선언
public class UserGroupMappingEntity extends BaseEntity{

	@Id
	private String userGroupId; // 사용자그룹을 구별하는 ID
	@Id
	private String userId; // 사용자 고유 아이디
	private String userGroupName; // 그룹이름
	private String description;	// 설명
}
