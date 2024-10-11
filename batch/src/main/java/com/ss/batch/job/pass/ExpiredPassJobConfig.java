package com.ss.batch.job.pass;

import java.time.LocalDateTime;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ss.batch.entity.PassEntity;
import com.ss.batch.entity.PassStatus;

// 이용권이 만료되었을 때 배치작업을 설정하는 클래스
@Configuration
public class ExpiredPassJobConfig {

	// 데이터를 한꺼번에 처리할 수 있는 사이즈
	private final int CHUNK_SIZE = 5;
	
	// JOB을 생성하는 팩토리
	private final JobBuilderFactory jobBuilderFactory;
	
	// Step을 생성할 수있는 팩토리
	private final StepBuilderFactory stepBuilderFactory;
	
	// JPA와 데이터베이스를 연결하고 관리하는 객체 생성
	private final EntityManagerFactory entityManagerFactory;

	public ExpiredPassJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
			EntityManagerFactory entityManagerFactory) {
		super();
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
		this.entityManagerFactory = entityManagerFactory;
	}
	
	// JOB
	// 배치 작업을 말하고 여러개의 step을 가질 수 있다.
	// 실행시 여러 step 순서대로 처리를 한다.
	@Bean
	public Job expiredPassJob() {
		return this.jobBuilderFactory.get("expiredPassJob") // 배치작업을 생성해서 이름을 저장!
				.start(expiredPassStep()) // step을 실행하는 메서드!
				.build(); // JOB생성한다.
	}
	
	// step
	// <PassEntity, PassEntity> 입력, 출력 데이터 타입
	// 첫번째 제네릭 타입 - 데이터베이스에서 데이터를 읽어올때 타입
	// 두번쨰 제네릭 타입 - 데이터베이스에서 데이터를 처리하거나 수정된 데이터나 추가된 데이터를 저장
	@Bean
	public Step expiredPassStep() {
		return this.stepBuilderFactory.get("expiredPassStep")
				.<PassEntity,PassEntity>chunk(CHUNK_SIZE)
				.reader(expiredPassItemReader())	// 읽어오기
				.processor(expiredPassItemProcessor())	// 데이터를 처리
				.writer(expiredPassItemWriter())		// 저장
				.build();
	}
	
	//JpaCursorItemReader
	@Bean
	@StepScope // step실행될때마다 새로운 객체를 생성하도록 설정하는 어노테이션
	public JpaCursorItemReader<PassEntity> expiredPassItemReader(){
		
		return new JpaCursorItemReaderBuilder<PassEntity>()
				.name("expiredPassItemReader") // ItemReader ㅇㅕ러개중 리더를 구분
				.entityManagerFactory(entityManagerFactory) //JPA를 통해서 데이터베이스에 연결하고 관리
				// 상태(status)가 진행중이며 종료일시(endedAt)이 현재 시점보다 과거일 만료대상
				.queryString("select p from PassEntity p where p.status = :status and p.ended_at <= :endedAt")
				.parameterValues(Map.of("status",PassStatus.PROGRESSED,"endedAt",LocalDateTime.now()))
				.build();
	}
	
	@Bean
	public ItemProcessor<PassEntity,PassEntity> expiredPassItemProcessor(){
		return new ItemProcessor<PassEntity,PassEntity>(){
			
			@Override
			public PassEntity process(PassEntity item) {
				// 실제 처리하는 내용
				// 상태 현재 이용중에서 만료!
				// 만료일자도 현재 날짜를 기준으로! 수정
				item.setStatus(PassStatus.EXPIRED);
				item.setExpired_at(LocalDateTime.now());
				
				return item;
			}
		};
	}
	
	@Bean
	public JpaItemWriter<PassEntity> expiredPassItemWriter(){
		
		JpaItemWriter<PassEntity> writer = new JpaItemWriter<PassEntity>();
		writer.setEntityManagerFactory(entityManagerFactory);
		return writer;
		
	}
	

}
