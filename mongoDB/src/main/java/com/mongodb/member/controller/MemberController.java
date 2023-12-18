package com.mongodb.member.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.member.service.MemberService;

@Controller
public class MemberController {

	@Autowired
	private MemberService memberService;
	
	/**
	 * 회원가입 폼 호출 메서드
	 * @return "member/join";
	 */
	@RequestMapping("/join.do")
	public String memberJoing() {
		return "member/join";
	}
	
	/**
	 * 회원가입 메서드
	 * @param id
	 * @param password
	 * @return  new ResponseEntity<>("{\"status\":\"SUCCESS\"}", HttpStatus.OK);
	 */
	@RequestMapping(value="/saveMember", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> saveMember(@RequestParam("id") String id,
											 @RequestParam("password") String password) {
		memberService.saveMember(id, password);
		System.out.println("member컨트롤러 가입완료");
		return new ResponseEntity<>("{\"status\":\"SUCCESS\"}", HttpStatus.OK);
	}
	
	@RequestMapping("/login.do")
	public String memberLogin() {
		return "member/login";
	}
	
	@RequestMapping(value = "/loginCheck", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, Object>> loginCheck(@RequestParam String id, @RequestParam String password) {
		System.out.println("로그인 체크 컨트롤러");
		return memberService.loginCheck(id, password);
	}
	
}
