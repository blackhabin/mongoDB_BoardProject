package com.mongodb.stats.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.stereotype.Repository;

import com.mongodb.stats.dto.ModuleCountDTO;

//자동 생성되어지는 객체-클래스 위에 선언
//<context:component-scan base-package="package">안에 선언이 되어 있어야 함
//@Controller, @Service, @Repository, @Component, @RestController가 자동 생성
@Repository
public class StatsDAO {
	
	@Inject
	private MongoTemplate mongoTemplate;
	
//	public List<ModuleCountDTO> moduleCount() {
//	    Aggregation agg = Aggregation.newAggregation(
//	       Aggregation.group("uri").count().as("value"),
//	       Aggregation.project("value").and("_id").as("module")
//	    );
//
//	    AggregationResults<ModuleCountDTO> results = mongoTemplate.aggregate(agg, "log", ModuleCountDTO.class);
//	    
//	    List<ModuleCountDTO> list = results.getMappedResults();
//	    return list;
//	}
	
    public List<Document> moduleCount() {
        Aggregation agg = Aggregation.newAggregation(
           Aggregation.group("uri").count().as("value"),
           Aggregation.project("value").and("_id").as("module")
        );

        AggregationResults<Document> results = mongoTemplate.aggregate(agg, "log", Document.class);
        
        List<Document> list = results.getMappedResults();
        return list;
    }
}