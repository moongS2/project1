package com.ss.batch.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;

import com.ss.batch.entity.BulkPassEntity;
import com.ss.batch.entity.BulkPassStatus;
import com.ss.batch.entity.PassEntity;
import com.ss.batch.entity.PassStatus;
import com.ss.batch.modelmapper.PassModelMapper;

public class PassModelMapperTest {

	@Test
	public void test_toPassEntity() {
		
		// given
		LocalDateTime now = LocalDateTime.now();
		String userId = "A10000";
		
		BulkPassEntity bulkPassEntity = new BulkPassEntity();
		
		bulkPassEntity.setPackageSeq(1L);
		bulkPassEntity.setUserGroupId("GROUP");
		bulkPassEntity.setStatus(BulkPassStatus.COMPLETE);
		bulkPassEntity.setStartedAt(now.minusDays(60));
		bulkPassEntity.setEndedAt(now);
		
		// when
		PassEntity passEntity = PassModelMapper.toPassEntity(userId, bulkPassEntity);
		
		// then
		assertEquals(1, passEntity.getPackage_seq());
		assertEquals(PassStatus.READY, passEntity.getStatus());
		assertEquals(now.minusDays(60), passEntity.get);
		assertEquals();
		
	}
}
