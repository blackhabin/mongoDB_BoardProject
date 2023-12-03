package com.mongodb.stats.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

//module, value:mapReduce 처리를 하면 나오는 값이 value로 정해져 있음
public class ModuleCountDTO {

	//mpaReduce에서 key로 사용되는 것이 ID로 인식되도록 annotation지정
	
	@Id
	@Field("_id")
	private String module;
	
	@Field("value")
	private int value;
	
	
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "ModuleCountDTO [module=" + module + ", value=" + value + "]";
	}
	
}