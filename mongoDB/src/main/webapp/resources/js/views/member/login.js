/**
 * login.js
 */

$(function(){
	 
	 
	    //밑에 있는 id가 #btnLogin으로 맵핑되는 함수, 클릭 버튼을 누르면 컨트롤러에 있는 login_check.do로 form1에 저장된 자료를 넘긴다.
	    $("#btnLogin").click(function(){
	        $("#form1").attr("action","/loginCheck");
	        $("#form1").submit();
	    });
	});