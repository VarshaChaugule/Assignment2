package com.example.SpringBootAssignment;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Stream;

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
import com.example.SpringBootAssignment.schedule.JobRunSchedule;
import com.example.SpringBootAssignment.service.SpringService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JobRunScheduleTests {

	@Mock
	private SpringService springService;

	@InjectMocks
	private JobRunSchedule job;

	@Before
	private void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getScheduledJobDone() throws IOException, ParseException {
		
		final String DELIMITER = ",";
		
		Map<String, IncidentResult> incidentMapByDate = TestUtils.incidentMapByDate();
		when(springService.getIncidentMapByDate(Mockito.any())).thenReturn(incidentMapByDate);
		job.runSchedule();
		Stream<String> lines = Files.lines(Paths.get("output.csv"));
		String string = lines.skip(1).findFirst().get();
		String[] split=string.split(DELIMITER);
		assertEquals(TestUtils.ASSET_NAME, split[0]);
		assertEquals(TestUtils.UPTIME,Double.parseDouble(split[2]));
		assertEquals(1, Integer.parseInt(split[1]));
		assertEquals(1, Integer.parseInt(split[3]));
		assertTrue(Files.deleteIfExists(Paths.get("output.csv")));
		
	}

}
