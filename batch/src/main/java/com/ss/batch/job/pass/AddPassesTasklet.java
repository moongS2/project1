package com.ss.batch.job.pass;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import com.ss.batch.entity.BulkPassEntity;
import com.ss.batch.entity.BulkPassStatus;
import com.ss.batch.entity.PassEntity;
import com.ss.batch.entity.UserGroupMappingEntity;
import com.ss.batch.modelmapper.PassModelMapper;
import com.ss.batch.repository.BulkPassRepository;
import com.ss.batch.repository.PassRepository;
import com.ss.batch.repository.UserGroupMappingRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AddPassesTasklet implements Tasklet{
	
	private final PassRepository passRepository;
	private final BulkPassRepository bulkPassRepository;
	private final UserGroupMappingRepository groupRepo;


	public AddPassesTasklet(PassRepository passRepository, BulkPassRepository bulkPassRepository,
			UserGroupMappingRepository groupRepo) {
		this.passRepository = passRepository;
		this.bulkPassRepository = bulkPassRepository;
		this.groupRepo = groupRepo;
	}






	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		
		// 이용권 시작일 하루전에 usergroup 각 사용자에게 이용권을 추가한다.
		final LocalDateTime startAt = LocalDateTime.now().minusDays(1);
		
		// 아직 처리되지않은 대량의 이용권 목록을 불러온다.(읽기)
		List<BulkPassEntity> bulkPassEntities = 
				bulkPassRepository.findByStatusAndStartedAtGreaterThan(
						BulkPassStatus.READY,startAt );
		
		// 각 대량 이용권 정보를 돌면서 그룹내에 속한 사용자들을 조회하고 해당 사용자에게 이용권을 추가한다.
		int count = 0;
		
		// user group에 속한 userId들을 조회한다.
		
		// 각 userId로 이용권을 추가하는 내용
		
		for(BulkPassEntity bulkPassEntity : bulkPassEntities) {
			// 이걸 이용해서 사용자 목록을 가져옴
			List<String> userId = groupRepo.findByuserGroupId(bulkPassEntity.getUserGroupId()).stream()
			// 리스트나 컬렉션을 처리할 때 사용되는 메서드
					.map(UserGroupMappingEntity::getUserId).collect(Collectors.toList());
		
			count += addPass(userId, bulkPassEntity);
			bulkPassEntity.setStatus(BulkPassStatus);
		}
		
		log.info("tasklet: execute 이용권: {}건 추가완료 startedAt:{}",count,startAt);
		
		return RepeatStatus.FINISHED;
	}
	
	// 이용권 추가
	public int addPass(List<String> userId, BulkPassEntity bulkPassEntity) {
		
		List<PassEntity> passEntities = new ArrayList<PassEntity>();
		
		for(String userId : userIds) {
			PassEntity passEntity = PassModelMapper.toPassEntity(userId, bulkPassEntity);
		}
		
		return passRepository.saveAll().sizeAll();
	}
	
	

}
