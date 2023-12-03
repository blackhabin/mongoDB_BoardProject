package com.mongodb.interceptor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SaveLogInterceptor extends HandlerInterceptorAdapter {
	
	//몽고DB처리를 위해서 바로 처리가능하도록 MongoTemplate을 선언하여 넣어준다.
	// @Inject : 이용할(의존:Dependency) 객체 주입(Inject) --> DI : JAVA
	// @Autowired : 이용할(의존:Dependency) 객체 주입(Inject) --> DI : Spring
	@Autowired
	private MongoTemplate mongoTemplate;
	
		//처리해야할 내용의 앞에서 처리되는 인터셉터 정의 메서드
		@Override
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
				throws Exception {
			
			System.out.println(getClass().getSimpleName()+".preHandle()");
			
			//log에 저장할 데이터를 수집-모듈명,요청 URI, IP, log날짜
			String uri = request.getServletPath();
			
			//uri가 main이 아닌경우에만 처리
			if(!uri.equals("/main/main.do")
					|| !uri.equals("/stats/logModule.do"))  {
				
				//출력: /board/list.do
				System.out.println("path:" +uri); 
				
				String module=uri.substring(1);
				//출력: board/list.do
				System.out.println(module);						 
				
				
				// module 문자열에 "/"가 없을 경우 -1을 반환하기 때문에 "/"가 있는지 확인하는 if 로직을 추가함.
				if (module.contains("/")) {
				    module = module.substring(0, module.indexOf("/"));
				    System.out.println("module: " + module);
				} else {
				    System.out.println("No / found in module: " + module);
				}
				//출력: board
				System.out.println("module(board)"+ module);			
				
				
				String ip= request.getRemoteAddr();
				Date logDate=new Date();
				
//				//객체를 만들어 데이터를 저장한다.
//				LogDTO dto=new LogDTO();
//				dto.setModule(module);
//				dto.setUri(uri);
//				dto.setIp(ip);
//				dto.setLogDate(logDate);
//				
//				//몽고디비에 저장한다.
//				//mongoTemplate.insert(dto,컬렉션 이름);
//				mongoTemplate.insert(dto,"log");
				
				Map<String, Object> logData = new HashMap<String, Object>();
				logData.put("module", module);
				logData.put("uri", uri);
				logData.put("ip", ip);
				logData.put("logDate", logDate); 
				
				//몽고디비에 저장한다.
				mongoTemplate.insert(logData, "log");
				
			}
			
			return super.preHandle(request, response, handler);
		}
}
