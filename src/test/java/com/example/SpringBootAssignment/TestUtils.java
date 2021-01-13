package com.example.SpringBootAssignment;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.example.SpringBootAssignment.entity.IncidentEntity;
import com.example.SpringBootAssignment.model.IncidentResult;

public class TestUtils {

	public static final double UPTIME = 99.00;
	public static final String ASSET_NAME = "testAsset";

	public static Map<String, IncidentResult> incidentMapByDate() {
		Map<String,IncidentResult> incidentMap  = new HashMap<String, IncidentResult>();
		incidentMap.put(ASSET_NAME, getIncidentResult());
		return incidentMap;
	}

	private static IncidentResult getIncidentResult() {
		return new IncidentResult(ASSET_NAME, 1, UPTIME, 1);

	}

	public static IncidentEntity getEntities(int severity) {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new IncidentEntity(ASSET_NAME, LocalDateTime.now().minusSeconds(100), LocalDateTime.now(), severity);
	}

}
