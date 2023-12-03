<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>	
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Insert title here</title>
	</head>
		<body>
			<h1>index.jsp</h1>
			<input type="button" value="작성글 목록" style="float: right;" onclick="loadBoardList();">

			<script>
				function loadBoardList() {
				    var xhr = new XMLHttpRequest();
				    xhr.open("GET", "board/list.do", true);
				    xhr.onreadystatechange = function () {
				        if (xhr.readyState == 4 && xhr.status == 200) {
				            document.body.innerHTML = xhr.responseText;
				        }
				    }
				    xhr.send();
				}
			</script>
			<!-- <script src="/js/views/home/home.js"></script> -->
		</body>
</html>
