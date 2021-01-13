package com.example.SpringBootAssignment.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SpringBootAssignment.entity.IncidentEntity;
import com.example.SpringBootAssignment.model.IncidentResult;
import com.example.SpringBootAssignment.repository.EntityRepository;
import com.example.SpringBootAssignment.schedule.JobRunSchedule;

@Service
public class SpringService {
	private static final Logger LOGGER = LoggerFactory.getLogger(JobRunSchedule.class);
	
	private static final String DELIMITER = ";";
	private EntityRepository entityRepository;
	private Clock clock;

	@Autowired
	public SpringService(EntityRepository entityRepository, Clock clock) {
		this.entityRepository = entityRepository;
		this.clock = clock;
	}

	private static IncidentEntity getIncidentEntity(String line) {
		try {
			LOGGER.info("Inside getIncidentEntity method");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			String[] split = line.split(DELIMITER);
			String assetName = split[0];

			Date dateTime = formatter.parse(split[1]);
			LocalDateTime startDate = Instant.ofEpochMilli(dateTime.getTime()).atZone(ZoneId.systemDefault())
					.toLocalDateTime();
			dateTime = formatter.parse(split[2]);
			LocalDateTime endDate = Instant.ofEpochMilli(dateTime.getTime()).atZone(ZoneId.systemDefault())
					.toLocalDateTime();
			Integer serverity = Integer.parseInt(split[3]);
			IncidentEntity entity = new IncidentEntity(assetName, startDate, endDate, serverity);
			return entity;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		LOGGER.info("Ends getIncidentEntity method");
		return null;
	}

	public void saveRecord() throws IOException {
		LOGGER.info("Inside saveRecord method");
		
		Reader in = new FileReader("src/main/resources/DataIngestion-input.csv");
		try (BufferedReader reader = new BufferedReader(in)) {
			Stream<String> lines = reader.lines();
			Stream<String> skiped = lines.skip(1);
			List<IncidentEntity> list = skiped.map(SpringService::getIncidentEntity).filter(Objects::nonNull)
					.collect(Collectors.toList());
			entityRepository.deleteAll();
			entityRepository.saveAll(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		LOGGER.info("Ends saveRecord method");
		
	}

	public Map<String, IncidentResult> getIncidentMapByDate(LocalDate date) {
		LOGGER.info("Inside getIncidentMapByDate method");
		
		Map<String, IncidentResult> resultMap = new HashMap<String, IncidentResult>();
		List<IncidentResult> incidents = entityRepository.getDistinctAssetName().stream()
				.map(assetName -> getResults(assetName, date)).collect(Collectors.toList());
		incidents.forEach(incident -> {
			resultMap.put(incident.getAssetName(), incident);
		});
		LOGGER.info("Ends getIncidentMapByDate method");
		
		return resultMap;

	}

	public IncidentResult getResults(String assetName, LocalDate date) {
		
		
		LOGGER.info("Inside getResults method"+assetName);
		
		List<IncidentEntity> findByAssetName = entityRepository.findByAssetName(assetName);
		LocalDateTime atStartOfDay = date.atStartOfDay();
		LocalDateTime atEndOfDay = date.atTime(LocalTime.MAX);
		long totalSecond = Duration.between(atStartOfDay, atEndOfDay).getSeconds();

		Predicate<IncidentEntity> isGreaterThan = incidentEntity -> incidentEntity.getStartTime().isAfter(atStartOfDay)
				|| incidentEntity.getStartTime().equals(atStartOfDay);
		Predicate<IncidentEntity> isLessThan = incidentEntity -> incidentEntity.getEndTime().isBefore(atEndOfDay)
				|| incidentEntity.getEndTime().equals(atEndOfDay);

		List<IncidentEntity> listByDay = findByAssetName.stream().filter(isGreaterThan.and(isLessThan))
				.collect(Collectors.toList());

		List<IncidentEntity> serverityOne = listByDay.stream()
				.filter(incidentEntity -> incidentEntity.getSeverity() == 1).collect(Collectors.toList());

		long totalDownTime = serverityOne.stream().mapToLong(SpringService::getDuration).sum();

		double perUptime = (((double) (totalSecond - totalDownTime)) / (double) totalSecond);
		int rating = listByDay.stream().mapToInt(SpringService::getWeightage).sum();
		int totalIncident = listByDay.size();

		IncidentResult incidentResult = new IncidentResult(assetName, totalIncident, perUptime, totalIncident);
		incidentResult.setAssetName(assetName);
		incidentResult.setTotalIncident(totalIncident);
		incidentResult.setTotalUptime(perUptime * 100);
		incidentResult.setRating(rating);

		LOGGER.info("Ends getResults method");
		
		return incidentResult;
	}

	private static Long getDuration(IncidentEntity incidentEntity) {
		
		LOGGER.info("Inside getDuration method");
		return Duration.between(incidentEntity.getStartTime(), incidentEntity.getEndTime()).getSeconds();
		
	}

	private static Integer getWeightage(IncidentEntity incidentEntity) {

		LOGGER.info("Inside getWeightage method");
		return getWeight(incidentEntity.getSeverity());
	}

	private static int getWeight(int severity) {
		switch (severity) {
		case 1: {
			return 30;
		}
		case 2:
		case 3:
			return 10;
		}
		return 0;
	}

	public List<String> getAssetNames() {
		LOGGER.info("Inside getAssetNames method");
		return entityRepository.getDistinctAssetName();

	}

}
