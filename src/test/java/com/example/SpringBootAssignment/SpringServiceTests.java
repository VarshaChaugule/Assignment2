package com.example.SpringBootAssignment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.SpringBootAssignment.entity.IncidentEntity;
import com.example.SpringBootAssignment.model.IncidentResult;
import com.example.SpringBootAssignment.repository.EntityRepository;
import com.example.SpringBootAssignment.schedule.JobRunSchedule;
import com.example.SpringBootAssignment.service.SpringService;



@RunWith(SpringRunner.class)
@SpringBootTest

public class SpringServiceTests {

	
	@InjectMocks
	private SpringService service;

	@Mock
	private EntityRepository repo;

	@Before
	private void init() {
		MockitoAnnotations.initMocks(this);
	}	
	
	
	

	@Test
	public void getResult() {
		
		
		List<IncidentEntity> arrayList = new ArrayList<IncidentEntity>();
		arrayList.add(TestUtils.getEntities(1));
		
		List<String> assetName = new ArrayList<String>();
		assetName.add(TestUtils.ASSET_NAME);
		when(repo.getDistinctAssetName()).thenReturn(assetName);
		when(repo.findByAssetName(Mockito.anyString())).thenReturn(arrayList);
		IncidentResult incidentResult=service.getResults(TestUtils.ASSET_NAME, LocalDate.now());
		assertEquals(TestUtils.ASSET_NAME, incidentResult.getAssetName());
	}

	@Test
	public void getIncidentMapByDate() {

		ArrayList<IncidentEntity> arrayList = new ArrayList<IncidentEntity>();
		arrayList.add(TestUtils.getEntities(1));
		List<String> assetName = new ArrayList<String>();
		assetName.add(TestUtils.ASSET_NAME);
		when(repo.getDistinctAssetName()).thenReturn(assetName);
		when(repo.findByAssetName(Mockito.anyString())).thenReturn(arrayList);
//		Mockito.doReturn(Mockito.anyMap()).when(service).getIncidentMapByDate(Mockito.any());
		assertEquals(1, service.getIncidentMapByDate(LocalDate.now()).size());
	}
	
	}
