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
	</head>
	<style>
	h2 {
		text-align: center;
	}
	table {
		width: 100%;
	}
	#outter {
		display: block;
		width: 60%;
		margin: auto;
	}
	</style>
	
	<body>게시판 리스트
	<div id=outter">
		<table border="1">
			<thead>
				<tr>
					<th>번호</th>
					<th width="50%">제목</th>
					<th>작성자</th>
					<th>작성일</th>
					<th>조회수</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${list}" var="item">
					<tr>
						<td>${item['no']}</td>
						<td><a href="#" onclick="loadPostDetail(${item['no']});">${item['title']}</a></td>
						<td>${item['writer']}</td>
						<td><fmt:formatDate value="${item['writedate']}" pattern="yyyy-MM-dd"/></td>
						<td>${item['hit']}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<input type="button" value="글쓰기" style="float: right;" onclick="loadWriteForm();">
	</div>
	
	<div class="input-group mb-3">
    	<span class="input-group-text">검색</span>
    	<div class="form-floating">
        	<input type="text" class="form-control" name="search" id="search" placeholder="search" />
    	</div>
    	<input type="button" class="btn btn-outline-primary" name="btnSearch" value="검색" id="btnSearch" />
	</div>
	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
	<script src="/resources/js/views/board/list.js"></script>
	</body>
</html>