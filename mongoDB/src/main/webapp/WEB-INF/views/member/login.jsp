<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>로그인 페이지</title>
	</head>
		<body>
			<h2>로그인하세요</h2>
			 
			<form name ="form1" id="form1" method="post">
				<table border="1" style="width: 400px"> 
					<tr>
				        <td>아이디</td>
				        <td><input type="text" name="id" id = "id" /></td>
				    </tr>
				    <tr>
				        <td>비밀번호</td>
				        <td><input type="password" name="password" id = "password" /></td>
				    </tr>
				    <tr>
				        <td colspan="2" align="center">    
				            <input type="button" id="btnLogin" value="로그인" />
				 
				        </td>
				    </tr>
				</table>
			</form>
		<script src="/resources/js/views/member/login.js"></script>
		</body>
</html>
