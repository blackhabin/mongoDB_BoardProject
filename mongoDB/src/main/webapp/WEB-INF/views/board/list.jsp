<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
	
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
	</head>
	
	<body>게시판 리스트
		<div id="content">
			<table border="1">
				<thead>
					<tr>
						<th>번호</th>
						<th width="50%">제목</th>
						<th>작성자</th>
						<th>내용</th>
						<th>작성일</th>
						<th>조회수</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${list}" var="item">
						<tr>
							<td>${item['no']}</td>
							<td><a href='detail.do' class='post-link' data-no='${item.no }'>${item['title']}</a></td>
							<!--<td><a href="#" onclick="loadPostDetail(${item['no']});">${item['title']}</a></td>  -->
							
							<td>${item['writer']}</td>
							<td>${item['content']}</td>
							<td><fmt:formatDate value="${item['writedate']}" pattern="yyyy-MM-dd"/></td>
							<td>${item['hit']}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<input type="button" class="btn btn-outline-primary" value="글쓰기" name="btnWrite" id="btnWrite" style="float: right;">
		</div>
		
		<div class="input-group mb-3">
	    	<span class="input-group-text">검색</span>
	    	<div class="form-floating">
	        	<input type="text" class="form-control" name="search" id="search" placeholder="검색" />
	    	</div>
	    	<input type="button" class="btn btn-outline-primary" name="btnSearch" value="검색" id="btnSearch" />
	    	<!-- <input type="button" class="btn btn-outline-primary" value="엑셀 다운로드" onclick="location.href='/downloadExcel'"> -->
			
			<button type="button" class="btn btn-outline-primary" id="btnDownload">엑셀 다운로드</button>
		
		</div>
		<div class="input-group mb-3" style="float: right;">
			<form id="uploadForm" enctype="multipart/form-data">
			  <input type="file" id="file" name="file" accept=".xlsx" required>
			  <button type="button" id="btnUpload">엑셀 업로드</button>
			</form>
		</div>
	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
	<script src="/resources/js/views/board/list.js"></script>
	
	</body>
</html>