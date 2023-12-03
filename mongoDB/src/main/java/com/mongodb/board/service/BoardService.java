package com.mongodb.board.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.board.dao.BoardDAO;
import com.mongodb.gridfs.GridFSFile;

@Service
public class BoardService {

	@Autowired
	private BoardDAO boardDao;
	
//	public List<BoardDTO> list() {
//		System.out.println(getClass().getSimpleName());
//		return dao.list();
//	}
	
	// 게시물 리스트 메서드
	public List<Map<String, Object>> list() {
		System.out.println(getClass().getSimpleName());
		return boardDao.list();
	}
	
	
	// 게시물 작성 메서드
	public void savePost(String title, String content, String writer, MultipartFile file) throws IOException {
		boardDao.savePost(title, content, writer, file);
	}
	
	//게시물 상세보기 메서드
	public Map<String, Object> detailPost(int no) {
		return boardDao.detailPost(no);
	}
	
	// 게시물 수정 메서드
	public void updatePost(int no, String title, String content, String writer, MultipartFile file) throws IOException {
		boardDao.updatePost(no, title, content, writer, file);
	}
	  
	
	// 게시물 삭제 메서드
	public Map<String, Object> deletePost(int no) {
	    return boardDao.deletePost(no);
	}
	
	// 파일 삭제 메서드
	public void deleteFile(String fileId, int no) {
        boardDao.deleteFile(fileId, no);
    }
	
	
	// 게시물 검색 메서드
	public List<Map<String, Object>> searchPost(String keyword) {
        return boardDao.searchPost(keyword);
    }
	
	
	// FileDownloadController에서 호출(파일 정보 불러오기)
    public GridFSFile getFile(String fileId) {
        return boardDao.getFile(fileId);
    }
	
}
