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
	<body>
		<h2 style="text-align: center;">회원가입</h2><br><br><br>
		
		<!-- form이 아예 없이 업로드 -->
		<!--  <form action="/savePost" method="post" enctype="multipart/form-data">-->
			<table border="1" cellpadding="0" cellspacing="0">
				<tr>
					<td bgcolor="orange" width="70">아이디</td>
					<td align="left"><input type="text" id="id" name="id" style="width: 40%;" placeholder="아이디"/></td>
				</tr>
				<tr>
					<td bgcolor="orange">암호</td>
					<td align="left"><input type="text" id="password" name="password" style="width: 20%;" placeholder="비밀번호"/></td>
				</tr>
				<tr>
					<td colspan="2" align="center">
					<input id="subBtn" type="button" value="회원가입" style="float: right;" onclick="goJoin(this.form)"/>
					</td>
				</tr>
			</table>
		<!--</form>  -->

	<script src="/resources/js/views/member/join.js"></script>
	</body>
</html>