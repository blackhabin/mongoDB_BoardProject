<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<meta charset="UTF-8">
	<title>Error Page</title>
	<body>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
	<script>
		var errorMessage = '${errorMessage}';
	    if (errorMessage) {
	        alert(errorMessage);
	        $.ajax({
	            url: "/board",
	            type: "GET",
	            success: function(response) {
	                $('body').html(response);
	            }
	        });
	    }
	</script>
	</body>
</html>