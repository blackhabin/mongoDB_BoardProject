package com.mongodb.member.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {

	@Autowired(required = false)
	private GridFsTemplate gridFsTemplate;
	
	@Inject
	private MongoTemplate mongoTemplate;
	
	
	// 회원가입시 시퀀스 번호 부여
    public int getNextSequence(String seqName) {
        Query query = new Query(Criteria.where("_id").is(seqName));
        Update update = new Update().inc("seq", 1);
        FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true).upsert(true);
        Document counter = mongoTemplate.findAndModify(query, update, options, Document.class, "counterMembers");
        return counter.getInteger("seq");
    }
    
    
    // 회원가입
    public void saveMember(String id, String password) {
    	int no = getNextSequence("member");
    	
    	Document doc = new Document();
    	doc.put("no", no);
    	doc.put("id", id);
    	doc.put("password", password);
    	doc.put("joinDate", LocalDateTime.now());
    	
    	System.out.println("dao memeber 저장");
    	mongoTemplate.save(doc, "member");
    	
    }
    
    // 로그인
    public List<Map<String, Object>> loginCheck(String id, String password) {
    	Query query = new Query(new Criteria("id").is(id)
    							.and("password").is(password));
    	List<Document> docsmember = mongoTemplate.find(query, Document.class, "member");
    	List<Map<String, Object>> list = new ArrayList<>();
    	
    	for (Document doc : docsmember) {
    		Map<String, Object> map = new HashMap<>();
            map.putAll(doc);
            list.add(map);
    	}
    	if(list.size() > 0) {
    		return list;
    	} else {
    		return null;
    	}
    	
    }
	
}
