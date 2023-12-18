<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 자바 프로그램을 태그로 사용하도록 정의-->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!--숫자나 통화, 날짜 같은 형태 맞춰주기 위하여 정의-->
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
	</head>
	<body>
		<h2 style="text-align: center;">글 수정</h2><br><br><br>
		
		<div style="width: 60%; margin: auto;">
			<form action="/updatePost" method="post" id="yourForm" enctype="multipart/form-data">
				<input type="hidden" id="no" name="no" style="width: 40%;" placeholder="번호" value="${board.no }"/>
					
				<table border="1" cellpadding="0" cellspacing="0">
					
					<tr>
						<td bgcolor="orange" width="70">제목</td>
						<td align="left"><input type="text" name="title" style="width: 40%;" placeholder="제목" value="${board.title }"/></td>
					</tr>
					<tr>
						<td bgcolor="orange">작성자</td>
						<td align="left"><input type="text" name="writer" style="width: 20%;" placeholder="작성자" value="${board.writer }" readonly/></td>
					</tr>
					<tr>
						<td bgcolor="orange">내용</td>
						<td align="left"><textarea name="content" cols="40" rows="10">${board.content}</textarea>
						</td>
					</tr>
					<tr>
						<td bgcolor="orange">파일</td>
						<td align="left">
							<input type="file" name="fileId" id="existFile" />
							<c:if test="${not empty board.file}">
								<a href="/downloadFile?fileId=${board.file._id}">${board.file.filename}</a>
								<button type="button" id="btnDelete" data-file-id="${board.file._id}">파일 삭제</button>
							</c:if>
							
						</td>
					</tr>
					<tr>
						<td colspan="2" align="center">
						<input id="subBtn" id="modifyButton" type="button" value="글 수정" style="float: right;" onclick="goModify(this.form)"/>
						</td>
					</tr>
				</table>
			</form>
		</div>
		

	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
	<script src="/resources/js/views/board/modify.js"></script>

	</body>
</html>