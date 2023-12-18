package com.mongodb.member.service;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.member.dao.MemberDao;

@Service
public class MemberService {

	@Autowired
	private MemberDao memberDao;
	
	public void saveMember(String id, String password) {
		memberDao.saveMember(id, password);
	}
	
	public List<Map<String, Object>> loginCheck(String id, String password) {
		return memberDao.loginCheck(id, password);
	}
	
}
