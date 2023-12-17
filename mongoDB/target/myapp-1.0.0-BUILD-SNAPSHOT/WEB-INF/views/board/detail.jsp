<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>글 상세보기</title>
</head>
	<style>
		h2 { 
			text-align: center;}
	  	table { 
	  		width: 100%;}
	  	textarea { 
	  		width: 100%;}
	 	#content {
			display: block;
			width: 30%;
			margin: auto;
		}
		
	    div.button-container {
	        display: flex;
	        justify-content: flex-start;
	    }
	    div.button-container button {
	        margin-right: 5px;
	    }
	    .myButton {
        	width: 80px;  /* 버튼의 너비 */
        	height: 30px;  /* 버튼의 높이 */
    	}
	</style>
	<body>
	
		<h2>게시판</h2>
		<br><br><br>
			<div id="content">
				<table border="1">
					<tr>
						<td>
							제목: ${board.title }
							<span style="float: right; margin-right: 10px;"> 조회수: ${board.hit }</span>
						</td>
					</tr>
					<tr>
						<td>
							작성자: ${board.writer }
							<span style="float: right; margin-right: 10px;"><fmt:formatDate value="${board.writedate }" pattern="yyyy.MM.dd"/></span>
						</td>
					</tr>
					<tr>
					    <td>첨부파일: 
					        <c:if test="${not empty board.file}">
					           <form id="fileDownloadForm" action="/downloadFile" method="GET">
						        	<input type="text" name="filename" value="${board.file.filename}">
						        	<button type="submit" id="btnFileDownload" name="fileId" data-no="${board.file._id}">다운로드</button>
	   			        	   <!-- <a href="/downloadFile/${board.file._id}">${board.file.filename}</a> -->
					           </form>
							</c:if>
					    </td>
					</tr>
					<tr>
						<td><div style="height: 300px; margin: 10px; display: inline-block">${board.content }</div></td>
					</tr>
				</table>
				
				
				<div class="button-container">
				    <button type="button" class="modify-link myButton" data-no="${board.no}">수정</button>
				    <form id="deleteForm" action="/deletePost" method="POST">
				        <input type="hidden" name="no" value="${board.no}">
				        <button type="submit" class="myButton" id="btnDeletePost" data-no="${board.no}">삭제</button>
				    </form>
					<input type="button" value="작성글 목록" class="myButton" style="float: right;" id="btnBoardList"> 
				</div>
			</div>
			
			<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
			<script src="/resources/js/views/board/detail.js"></script>
	</body>
</html>