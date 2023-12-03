package com.mongodb.file.controller;

import com.mongodb.board.service.BoardService;
import com.mongodb.gridfs.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.commons.io.IOUtils;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class FileDownloadController {
	
    @Autowired
    private BoardService boardService;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    /**
     * 파일 다운로드를 위한 메서드
     * @param fileId
     * @param response
     * @throws IOException
     */
//    @RequestMapping(value = "/downloadFile/{fileId}", method = RequestMethod.GET)
//    public void downloadFile(@PathVariable String fileId, HttpServletResponse response) throws IOException {
//        GridFSFile file = boardService.getFile(fileId);
//
//        if (file == null) {
//            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            return;
//        }
//
//        response.setContentType(file.getContentType());
//        response.setHeader("Content-Disposition", "attachment; filename=" + file.getFilename());
//
//        try {
//            IOUtils.copy(gridFsTemplate.getResource(file.getFilename()).getInputStream(), response.getOutputStream());
//        } catch (IOException e) {
//            throw new RuntimeException("파일 다운로드 에러", e);
//        }
//    }
    
    /**
     * 파일 다운로드를 위한 메서드
     * @param fileId
     * @return
     */
    @RequestMapping(value = "/downloadFile/{fileId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId){
        
        
    	GridFSFile file = boardService.getFile(fileId);
    	System.out.println("파일다운로드 컨트롤러 호출1");
        if (file == null) {
        	System.out.println("파일 NO GET: " + fileId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
        	System.out.println("파일 GET: " + file.getFilename());
        }
        
        System.out.println("파일다운로드 컨트롤러 호출1");
        // 콘텐트 타입을 설정
        //response.setContentType(file.getContentType());
        // 다운로드 하는 명령을 함 / 파일 이름을 설정
       // response.setHeader("Content-Disposition", "attachment; filename=" + file.getFilename());

        // gridFsTemplate의 getResource를 호출하여 실제 파일의 내용을 resource 변수에 초기화
        Resource resource = gridFsTemplate.getResource(file.getFilename());
        
        System.out.println("파일다운로드 컨트롤러 호출2");
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(resource);
    }
}





