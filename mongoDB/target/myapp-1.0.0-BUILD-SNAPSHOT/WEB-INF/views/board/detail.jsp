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
	h2 { text-align: center;}
  table { width: 100%;}
  textarea { width: 100%;}
 	#outter {
		display: block;
		width: 30%;
		margin: auto;
	}
</style>
	<body>
	
		<h2>게시판</h2>
		<br><br><br>
			<div id="outter">
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
					            <a href="/myapp/board/downloadFile?fileId=${board.file._id}">${board.file.filename}</a>
							</c:if>
					    </td>
					</tr>
					<tr>
						<td><div style="height: 300px; margin: 10px; display: inline-block">${board.content }</div></td>
					</tr>
				</table>
				
				<style>
				    div.button-container {
				        display: flex;
				        justify-content: flex-start;
				    }
				    div.button-container button {
				        margin-right: 5px;
				    }
				</style>
				<div class="button-container">
				    <button type="button" onclick="location.href='modify.do?no=${board.no}';">수정</button>
				    <form action="/myapp/board/deletePost" method="POST" onsubmit="return confirm('정말 삭제하시겠습니까?');">
				        <input type="hidden" name="no" value="${board.no}">
				        <button type="submit">삭제</button>
				    </form>
					<input type="button" value="작성글 목록" style="float: right;" onclick="location.href='list.do';"> 
				</div>
			</div>
			
	</body>
</html>