package com.ss.batch.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.ss.batch.entity.PackageEntity;

@SpringBootTest
public class PackageRepositoryTest {
	
	@Autowired
	private PackageRepository repo;
	
	@Test // Junit이미 스프링부트에서 제공하는 라이브러리!
	public void test_save() {
		
		// given: test를 위한 초기데이터 설정
		PackageEntity entity = new PackageEntity();
		entity.setPackageName("바디 챌린지 pt 12주");
		entity.setPeriod(84);
		
		// when: 실제로 테스트할 작업 수행(데이터베이스 저장)
		repo.save(entity);
		// then: 예상 결과를 검증(ID가 자동으로 생성)
		assertNotNull(entity.getPackSeq());
		
	}
	
	@Test
	public void test_findByCreatedAtAfter() {
		
		// given
		// 현재 시간에서 1분전 시간의 패키지 가져오기
		LocalDateTime dateTime = LocalDateTime.now().minusMinutes(1);
		
		PackageEntity pack1 = new PackageEntity();
		pack1.setPackageName("학생 전용 3개월");
		pack1.setPeriod(90);
		repo.save(pack1);
		
		PackageEntity pack2 = new PackageEntity();
		pack2.setPackageName("학생 전용 6개월");
		pack2.setPeriod(90);
		repo.save(pack2);
		
		// when: 특정 시간 이후에 생성된 패키지를 페이징 및 정렬 조건에 맞춰서 조회
		PageRequest page = PageRequest.of(0, 1, Sort.by("packSeq").descending());
		
		List<PackageEntity> result = repo.findByCreateAtAfter(dateTime,page);
		
		// then: 결과 검증
		System.out.println("사이즈반환:"+result.size());
		assertEquals(1, result.size());
		
		// 시퀀스 아이디를 기준으로 조회도 가능하다.
		assertEquals(pack2.getPackSeq(), result.get(0).getPackSeq());
				
	}

	@Test
	public void test_updateCountAndPeriod() {
		
		// given
		// 새로운 패키지 생성해서 바디프로필 이벤트 4개월 데이터베이스 저장
		PackageEntity pack1 = new PackageEntity();
		pack1.setPackageName("바디프로필 이벤트 4개월");
		pack1.setPeriod(90);
		repo.save(pack1);
		
		// when 업데이트된 데이터를 다시 조회
		// 업데이트를 할 때 updateCountAndPeriod
		pack1.setCount(30);
		pack1.setPeriod(120);
		//repo.save(pack1);
		
		int updateRows = 
		repo.updateCountAndPeriod(pack1.getPackSeq(), 30, 120);
		
		System.out.println("실행한 행:"+updateRows);
		
		System.out.println(pack1.getPackSeq());
		final PackageEntity update = repo.findById(pack1.getPackSeq()).get();
		
		System.out.println(update.toString());
		// then 업데이트된 값이 올바르게 저장되었는지 검증
		assertEquals(30, update.getCount());
		assertEquals(120, update.getPeriod());
		// 업데이트된 행의 수가 1인지 확인
		assertEquals(1, updateRows);
		
	}

}
