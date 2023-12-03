package com.mongodb.stats.controller;

import java.util.List;

import javax.inject.Inject;

import org.bson.Document;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mongodb.stats.dao.StatsDAO;
import com.mongodb.stats.dto.ModuleCountDTO;


@Controller
public class StatsController {
	
	@Inject
	private StatsDAO dao;
	
	@RequestMapping("/stats/logModule.do")
	public String moduleCount(Model model){
		// Model에 데이터를 담는다. : 자동적으로 request에 담아준다.
		model.addAttribute
		("data", getArrayString(dao.moduleCount()));
		//jsp => /WEB-INF/views/ + stats/logModule + .jsp
		return "stats/logModule";
	} // end of moduleCount()//end of moduleCount()
	
	//차트에 필요한 문자열을 만들어 내는 메서드(하이차트의 소스 구조, 배열과 비슷)
//	String getArrayString(List<ModuleCountDTO> list) {
//		String str = "[";
//        // getModule():모듈명, getValue(): 방문 수 
//		for(ModuleCountDTO dto : list) {
//			str +="['" + dto.getModule()+"'," + dto.getValue() + "],";
//		}
//		// 데이터 마지막의 ","를 제거한다.
//		str = str.substring(0,str.length()-1); 
//		str +="]";
//		
//		// 작성된 데이터 확인
//		System.out.println("str" + str);
//		
//		return str;
//	}
	
	String getArrayString(List<Document> list) {
	    String str = "[";
	    for(Document doc : list) {
	        str +="['" + doc.getString("module") + "'," + doc.getInteger("value") + "],";
	    }
	    str = str.substring(0,str.length()-1); 
	    str +="]";
	    
	    System.out.println("str" + str);
	    
	    return str;
	}
	
} // end of StatsController class