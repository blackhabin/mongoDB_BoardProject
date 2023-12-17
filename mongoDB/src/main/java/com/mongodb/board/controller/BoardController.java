package com.mongodb.board.controller;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.mongodb.board.dao.BoardDAO;
import com.mongodb.board.service.BoardService;

//자동으로 생성되는 객체- class위에 선언한다.
//단, servlet-context.xml에 <context:component-scan base=package="">안에 선언이 되어 있어야 함
//@controller, @Service, @Repository, @Component, @RestController
@Controller
public class BoardController {

	@Autowired
	private BoardService boardService;
	
	
	/**
	 * 게시물 리스트, 검색창 호출 메서드
	 * @param model
	 * @return "board/list";
	 */
	@RequestMapping("/")
	public String index() {
		
		System.out.println("보드컨트롤러 리스트");
		
		return "index";
	}
	
	
	/**
	 * 게시물 리스트, 검색창 호출 메서드
	 * @param model
	 * @return "board/list";
	 */
//	@RequestMapping("/board")
//	public String list(Model model) {
//		
//		System.out.println("보드컨트롤러 리스트");
//		model.addAttribute("list", boardService.list());
//		return "board/list";
//	}
	
	@RequestMapping("/board")
	public String board() {
	    return "board/list";
	}
	
	@RequestMapping("/board/data")
	@ResponseBody
	public JSONObject list() {
	    List<Map<String, Object>> list = boardService.list();
	    JSONObject response = new JSONObject();
	    response.put("total", 1); // 전체 페이지 수
	    response.put("page", 1); // 현재 페이지 번호
	    response.put("records", list.size()); // 전체 레코드 수
	    System.out.println("보드리스트 컨트트");
	    
	    JSONArray rows = new JSONArray(); // jsonArray
	    for (int i = 0; i < list.size(); i++) {
	    	JSONObject row = new JSONObject();  // jasonObject
	        row.put("id", i + 1); // 행의 ID
	        Map<String, Object> item = list.get(i);

	        row.put("no", item.get("no")); // 각 셀의 값
	        row.put("title", item.get("title"));
	        row.put("writer", item.get("writer"));
	        row.put("content", item.get("content"));
	        
	        // 날짜 포맷팅
	        Date writedate = (Date)item.get("writedate");
	        LocalDateTime localDateTime = convertToLocalDateTimeViaInstant(writedate);
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	        String formattedDate = localDateTime.format(formatter);
	        row.put("writedate", formattedDate);
	        
	        row.put("hit", item.get("hit"));

			   
	       
	        rows.add(row);
	    }
	    response.put("rows", rows);
	    return response;
//	    return null;
	}
	
	
	
	/**
	 * 게시물 상세화면 호출 메서드
	 * @param model
	 * @param request
	 * @return "board/detail";
	 */
	@RequestMapping("/detail.do")
	public String detailPost(Model model,HttpServletRequest request) {
	     
		// 1. 세션으로 번호를 받는 방식
		HttpSession session = request.getSession();
	     int no = Integer.parseInt(session.getAttribute("no").toString());	    
	    
		Map<String, Object> board = boardService.detailPost(no);
		
		model.addAttribute("board", board);
		System.out.println("보드컨트롤러 상세화면");
		
		return "board/detail";
		
	}
	
	
	/**
	 * 게시물 작성 폼 호출 메서드
	 * @return"board/write";
	 */
	@RequestMapping("/write.do")
	public String boardWrite() {
		return "board/write";
	}
	

	/**
	 * 게시물 작성 메서드
	 * @param title
	 * @param content
	 * @param writer
	 * @param file
	 * @return new ResponseEntity<>("{\"status\":\"SUCCESS\"}", HttpStatus.OK);
	 * @throws IOException
	 */
	@RequestMapping(value = "/savePost", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> savePost(@RequestParam("title") String title, 
										   @RequestParam("content") String content, 
										   @RequestParam("writer") String writer, 
										   @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
	    boardService.savePost(title, content, writer, file);
	    System.out.println("보드컨트롤러 세이브");
	    return new ResponseEntity<>("{\"status\":\"SUCCESS\"}", HttpStatus.OK);
	}
	
	

	
	/**
	 * 게시물 수정 폼 호출 메서드
	 * @param model
	 * @param request
	 * @return "board/modify";
	 */
	@RequestMapping("/modify.do")
	public String modifyBoard(Model model, HttpServletRequest request) {
	    HttpSession session = request.getSession();
	    int no = Integer.parseInt(session.getAttribute("no").toString());
	    
		model.addAttribute("board", boardService.detailPost(no));
		System.out.println("보드컨트롤러 업데이트 화면");
		return "board/modify";
	}
	
	
	/**
	 * 게시물 수정 메서드
	 * @param no
	 * @param title
	 * @param content
	 * @param writer
	 * @param file
	 * @return new ResponseEntity<>("{\"status\":\"SUCCESS\"}", HttpStatus.OK);
	 * @throws IOException
	 */
	@RequestMapping(value = "/updatePost", method = RequestMethod.POST)
	public ResponseEntity<String> updatePost(@RequestParam("no") int no,
	                         @RequestParam("title") String title,
	                         @RequestParam("content") String content,
	                         @RequestParam("writer") String writer,
	                         @RequestParam(value = "fileId", required = false) MultipartFile file) throws IOException {
		
	    boardService.updatePost(no, title, content, writer, file);
	    System.out.println("보드컨트롤러 업데이트");
	    return new ResponseEntity<>("{\"status\":\"SUCCESS\"}", HttpStatus.OK);
	}
	
	
	/**
	 * 파일 삭제 메서드
	 * @param fileId
	 * @param no
	 * @return new ResponseEntity<>("{\"status\":\"SUCCESS\"}", HttpStatus.OK);
	 */
	@RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> deleteFile(String fileId, Integer no) {
	    try {
	        if (no == null) {
	        	System.out.println("번호 null 실패");
	            return new ResponseEntity<>("{\"status\":\"FAILURE\"}", HttpStatus.OK);
	        }
	        System.out.println("성공");
	        boardService.deleteFile(fileId, no);
	        return new ResponseEntity<>("{\"status\":\"SUCCESS\"}", HttpStatus.OK);

	    } catch (Exception e) {
	    	System.out.println("예외 발생 실패");
	        return new ResponseEntity<>("{\"status\":\"FAILURE\"}", HttpStatus.OK);
	    }
	}

	
	/**
	 * 게시물 삭제 메서드
	 * @param no
	 * @return "redirect:/board";
	 */
	@RequestMapping(value = "/deletePost", method = RequestMethod.POST)
	public String deletePost(@RequestParam("no") int no) {
	    boardService.deletePost(no);
	    return "redirect:/board";
	}


	
	/**
	 * 검색 메서드 
	 * @param keyword
	 * @return boardService.searchPost(keyword);
	 */
	@RequestMapping(value = "/searchPost")
	@ResponseBody
	public List<Map<String, Object>> searchPost(@RequestParam String keyword) {
		System.out.println("검색 컨트롤러");
	    return boardService.searchPost(keyword);
	}
	
	
	/**
	 * 게시물 번호를 통해 세션에 저장하는 메서드
	 * @param request
	 * @param body
	 * @return new ResponseEntity<>("Success", HttpStatus.OK);
	 */
	@RequestMapping(value = "/setSessionNo", method = RequestMethod.POST)
	public ResponseEntity<String> setSessionNo(HttpServletRequest request, @RequestBody Map<String, String> body) {
	    
		// 1. 세션으로 저장해서 반환
		HttpSession session = request.getSession();
	    
	    //session.setAttribute("no", body.get("no"));     2) 윈도우에서만 되는 코드(리눅스에서는 작동 x)
	    
	    String no = body.get("no");
	    session.setAttribute("no", no);	    
	    System.out.println("세션 번호 받기- 세션 ID: " + session.getId() + ", no: " + no);
	    return new ResponseEntity<>("Success", HttpStatus.OK);
	}
	
	/**
	 * 리스트 엑셀 다운로드를 위한 메서드
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/downloadExcel", method = RequestMethod.GET)
	@ResponseBody
	public void downloadExcel(HttpServletResponse response) throws IOException {

		// 워크북 생성
		// HSSFWorkbook : .xls 확장자를 가진, 엑셀 2003 버전까지의 파일 형식을 지원. 최대 65536행, 256열까지의 데이터만 다룸
		// XSSFWorkbook : .xlsx 확장자를 가진, 엑셀 2007 버전 이후의 파일 형식을 지원. 최대 1048576행, 16384열까지의 데이터를 다룸.
	    Workbook workbook = new XSSFWorkbook();
	    // 시트 생성
	    Sheet sheetList = workbook.createSheet("게시판 리스트");
	    // 열 생성
	    Row row = null;    
	    // 데이터 열 번호(0번째는 헤더로 쓸것이기 때문에 1부터 시작)
	    int rowNo = 1; 

	    // 헤더 생성
	    Row headerRow = sheetList.createRow(0);
	    headerRow.createCell(0).setCellValue("번호");
	    headerRow.createCell(1).setCellValue("제목");
	    headerRow.createCell(2).setCellValue("작성자");
	    headerRow.createCell(3).setCellValue("내용");
	    headerRow.createCell(4).setCellValue("작성일");
	    headerRow.createCell(5).setCellValue("수정일");
	    headerRow.createCell(6).setCellValue("조회수");

	    // 게시판 리스트 가져오기
	    List<Map<String, Object>> list = boardService.list();

	    
	    // 날짜 형식의 셀 스타일 생성
	    CreationHelper createHelper = workbook.getCreationHelper();
	    CellStyle dateStyle = workbook.createCellStyle();
	    dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));

	    // 헤드 셀 데이터 채우기
	    for (Map<String, Object> map : list) {
	        row = sheetList.createRow(rowNo++);
	        row.createCell(0).setCellValue((Integer) map.get("no"));
	        row.createCell(1).setCellValue((String) map.get("title"));
	        row.createCell(2).setCellValue((String) map.get("writer"));
	        row.createCell(3).setCellValue((String) map.get("content"));
	        
	        Cell writedateCell = row.createCell(4);
	        writedateCell.setCellValue((Date) map.get("writedate"));
	        writedateCell.setCellStyle(dateStyle); // 날짜 형식 적용
	        
	        // 수정일자도 추가해봄
	        Cell modifydateCell = row.createCell(5);
	        
	        if(map.get("modifydate") != null) {
	            modifydateCell.setCellValue((Date)map.get("modifydate"));
	        } else {
	        	modifydateCell.setCellValue("");
	        }
	        modifydateCell.setCellStyle(dateStyle);
	        
	        row.createCell(6).setCellValue((Integer) map.get("hit"));
	    }
	    
//	    // 날짜를 가져오기 위해 작성일 정보는 Date 타입에서 String 타입으로 변환한 후에 셀에 쓰도록 수정
//	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    
//	    // 데이터 채우기
//	    for (Map<String, Object> map : list) {
//	        row = sheetList.createRow(rowNo++);
//	        row.createCell(0).setCellValue((Integer) map.get("no"));
//	        row.createCell(1).setCellValue((String) map.get("title"));
//	        row.createCell(2).setCellValue((String) map.get("writer"));
//	        row.createCell(3).setCellValue(sdf.format((Date) map.get("writedate")));
//	        row.createCell(4).setCellValue((Integer) map.get("hit"));
//	    }

	    // 엑셀 파일을 다운로드 한다.
	    // "application/vnd.ms-excel" :.xls 확장자를 가진, 엑셀 2003 버전까지의 파일 형식.
	    // "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" : .xlsx 확장자를 가진, 엑셀 2007 버전 이후의 파일 형식.
	    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	    response.setHeader("Content-Disposition", "attachment; filename=boardList_data.xlsx");
	    
	    workbook.write(response.getOutputStream());
	    // 메모리 정리
	    workbook.close();
	    System.out.println("워크북 쓴 후");
	}
	
	
	/**
	 * 엑셀 업로드를 위한 메서드
	 * @param file
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/uploadExcel", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file, Model model) throws IOException {
		try {
		// 엑셀 파일을 읽어오기 위해 워크북 생성
		Workbook workbook = new XSSFWorkbook(file.getInputStream());
		
		// 첫번째 시트 가져오기
		Sheet sheetList = workbook.getSheetAt(0);
		
		// 데이터 읽기
		for (Row row : sheetList) {
			
			// (첫번째 행은 헤더이므로 건너뛰어야 한다)
			if(row.getRowNum() == 0) {
				continue;
			}
			
			//int no = (int)row.getCell(0).getNumericCellValue();
			//String title = row.getCell(1).getStringCellValue();
			//String writer = row.getCell(2).getStringCellValue();
			//String content = row.getCell(3).getStringCellValue();
			//LocalDateTime writedate = convertToLocalDateTimeViaInstant(row.getCell(4).getDateCellValue());
			//LocalDateTime modifydate = convertToLocalDateTimeViaInstant(row.getCell(5).getDateCellValue());
			//int hit = (int)row.getCell(6).getNumericCellValue();
			
			Cell noCell = row.getCell(0);
			int no;
			if (noCell == null || noCell.getCellType() == Cell.CELL_TYPE_BLANK) {
			    no = 0;
			} else {
			    if (noCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			        no = (int) noCell.getNumericCellValue();
			    } else {
			        throw new IllegalStateException("번호 셀 타입이 숫자가 아닙니다.");
			    }
			}
			
			Cell titleCell = row.getCell(1);
			String title;
			if (titleCell == null || titleCell.getCellType() == Cell.CELL_TYPE_BLANK) {
			    throw new IllegalStateException("제목은 빈 값이거나 null일 수 없습니다.");
			} else {
			    if (titleCell.getCellType() == Cell.CELL_TYPE_STRING) {
			        title = titleCell.getStringCellValue();
			    } else if (titleCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			    		double numericTitle = titleCell.getNumericCellValue();
			    		title = Double.toString(numericTitle);
			    		if (title.endsWith(".0")) {
					        title = title.substring(0, title.length() - 2); // ".0" 제거
					    }
				} else {
			        throw new IllegalStateException("제목 셀의 타입이 문자열 또는 숫자가 아닙니다.");
			    }
			}
			
			Cell writerCell = row.getCell(2);
			String writer;
			if (writerCell == null || writerCell.getCellType() == Cell.CELL_TYPE_BLANK) {
				throw new IllegalStateException("작성자는 빈 값이거나 null일 수 없습니다.");
			} else {
			    if (writerCell.getCellType() == Cell.CELL_TYPE_STRING) {
			        writer = writerCell.getStringCellValue();
			    } else if (writerCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			    	double numericWriter = writerCell.getNumericCellValue();
			    	writer = Double.toString(numericWriter);
			    	if (writer.endsWith(".0")) {
			    		writer = writer.substring(0, writer.length() - 2); // ".0" 제거
			    	}
			    } else {
			        throw new IllegalStateException("작성자 셀의 타입이 문자열 또는 숫자가 아닙니다.");
			    }
			}
			
			Cell contentCell = row.getCell(3);
			String content;
			if (contentCell == null || contentCell.getCellType() == Cell.CELL_TYPE_BLANK) {
				throw new IllegalStateException("내용은 빈 값이거나 null일 수 없습니다.");
			} else {
			    if (contentCell.getCellType() == Cell.CELL_TYPE_STRING) {
			    	content = contentCell.getStringCellValue();
			    } else if (contentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			    	double numericContent = contentCell.getNumericCellValue();
			    	content = Double.toString(numericContent);
			    	if (content.endsWith(".0"));
			    		content = content.substring(0, content.length() -2); //".0"제거 
			    } else {
			        throw new IllegalStateException("내용 셀의 타입이 문자열 또는 숫자가 아닙니다.");
			    }
			}
			
			Cell writedateCell = row.getCell(4);
			LocalDateTime writedate;
			if (writedateCell == null || writedateCell.getCellType() == Cell.CELL_TYPE_BLANK) {
			    throw new IllegalStateException("작성 날짜는 빈 값이거나 null일 수 없습니다.");
			} else {
			    if (writedateCell.getCellType() == Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(writedateCell)) {
			        writedate = convertToLocalDateTimeViaInstant(writedateCell.getDateCellValue());
			    } else {
			        throw new IllegalStateException("작성 날짜 셀의 타입이 날짜 형식이 아닙니다.");
			    }
			}


			Cell modifydateCell = row.getCell(5);
			LocalDateTime modifydate;
			if (modifydateCell == null || modifydateCell.getCellType() == Cell.CELL_TYPE_BLANK) {
			    modifydate = null;
			} else {
			    if (modifydateCell.getCellType() == Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(writedateCell)) {
			    	modifydate = convertToLocalDateTimeViaInstant(modifydateCell.getDateCellValue());
			    } else {
			        throw new IllegalStateException("수정 날짜 셀의 타입이 날짜 형식이 아닙니다.");
			    }
			}

			Cell hitCell = row.getCell(6);
			int hit;
			if (hitCell == null || hitCell.getCellType() == Cell.CELL_TYPE_BLANK) {
			    hit = 0;
			} else {
			    if (hitCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			        hit = (int) hitCell.getNumericCellValue();
			    } else {
			        throw new IllegalStateException("조회수 셀 타입이 숫자가 아닙니다.");
			    }
			}

			// 서비스 메서드를 호출해서 데이터 저장
			boardService.savePost(title, content, writer, null);
			System.out.println("엑셀업로드 컨트롤러 데이터 저장");
			
		}
			// 워트북 닫기
			workbook.close();
		
			return ResponseEntity.ok().body("Success");
	    } catch (IllegalStateException e) {
	        return ResponseEntity.created(
	        		URI.create("/board"))
	        		.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE + ";charset=" + StandardCharsets.UTF_8)
	        		.body(e.getMessage());
	    } catch (IOException e) {
	        return ResponseEntity
	        		.status(HttpStatus.INTERNAL_SERVER_ERROR)
	        		.body("파일 처리 중 오류 발생");
	    }
	}
	
	// Date to LocalDateTime
	public LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
	    return dateToConvert.toInstant()
	      .atZone(ZoneId.systemDefault())
	      .toLocalDateTime();
	}
	

}