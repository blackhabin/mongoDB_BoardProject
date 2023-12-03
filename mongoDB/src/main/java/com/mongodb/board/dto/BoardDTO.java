package com.mongodb.board.dto;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "board")
public class BoardDTO {

	
	//몽고db에서 _id데이터를 가져와서 no에 처리하도록 지시한다.
	@Id
	private ObjectId _id;
	private int no;	
	private String title,content, writer;
	
	//데이터를 입력하면 날짜형 데이터로 자동 바꿈
	//@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDateTime writedate;
	
	private int hit;
	
	
	
    // getter and setter for _id
    public String get_id() { 
        return _id.toHexString();
    }
    
    public void set_id(String _id) {
        this._id = new ObjectId(_id);
    }
	
	// getter. setter
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public LocalDateTime getWritedate() {
		return writedate;
	}
	public void setWritedate(LocalDateTime writedate) {
		this.writedate = writedate;
	}
	public int getHit() {
		return hit;
	}
	public void setHit(int hit) {
		this.hit = hit;
	}
	
	// toString
	@Override
	public String toString() {
		return "BoardDTO [no=" + no + ", title=" + title + ", content=" + content + ", writer=" + writer
				+ ", writeDate=" + writedate + ", hit=" + hit + "]";
	}
	
}