package com.example.SpringBootAssignment.repository;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.SpringBootAssignment.SpringBootAssignmentApplication;
import com.example.SpringBootAssignment.entity.IncidentEntity;

@Repository
public interface EntityRepository extends JpaRepository<IncidentEntity, UUID> {
	
	List<IncidentEntity> findByAssetName(String assetName);

	@Query("select distinct assetName from IncidentEntity")
	List<String> getDistinctAssetName();

}
