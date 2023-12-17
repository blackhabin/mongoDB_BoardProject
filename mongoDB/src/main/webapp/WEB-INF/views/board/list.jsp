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
		<link rel="stylesheet" href="/resources/css/views/common/jquery-ui.css" />
		<link rel="stylesheet" href="/resources/css/views/common/ui.jqgrid.css" />

		<script src="/resources/js/views/common/jquery.min.js"></script>
		<script src="/resources/js/views/common/jquery-ui.min.js"></script>
		<script src="/resources/js/views/common/grid.locale-kr.js"></script>
		<script src="/resources/js/views/common/jquery.jqGrid.js"></script>
	</head>
	
	<body>
		<div id="content">
			<div class="grid-wrapper">
				<table id="jqGrid"></table>
				<div id="jqGridNavi"></div>
			</div>
			<input type="button" class="btn btn-outline-primary" value="글쓰기" name="btnWrite" id="btnWrite">
		</div>

		<div class="input-group mb-3">
	    	<span class="input-group-text">검색</span>
	    	<div class="form-floating">
	        	<input type="text" class="form-control" name="search" id="search" placeholder="검색" />
	    	</div>
	    	<input type="button" class="btn btn-outline-primary" name="btnSearch" value="검색" id="btnSearch" />

			<button type="button" class="btn btn-outline-primary" id="btnDownload">엑셀 다운로드</button>
		</div>

		<div class="input-group mb-3" style="float: right;">
			<form id="uploadForm" enctype="multipart/form-data">
			  <input type="file" id="file" name="file" accept=".xlsx" required>
			  <button type="button" id="btnUpload">엑셀 업로드</button>
			</form>
		</div>
	
	
	<script src="/resources/js/views/board/list.js"></script>
	
	</body>
</html>