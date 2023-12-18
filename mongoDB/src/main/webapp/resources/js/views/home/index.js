/**
 * index.js
 */

function loadBoardList() {
	location.href="/board"
}
    
$(document).ready(function() {
    console.log("index btn 호출");
    $('#btnBoardList').click(function(e) {
		console.log("boadList 호출");
        e.preventDefault();
        $.get('board', function(data) {
            $('body').html(data); // 페이지의 전체 내용 변경
            console.log("index -> list 성공");
        });
    });
	
	$('#btnJoin').click(function(e) {
		console.log("join 호출");
		e.preventDefault();
		$.get('join.do', function(data){
			$('body').html(data);
			console.log("index -> join 성공");
		});
	});
	
	$('#btnLogin').click(function(e) {
		console.log("login 호출");
		e.preventDefault();
		$.get('login.do', function(data){
			$('body').html(data);
			console.log("index -> login 성공");
		});
	});
	
});

/*
	var xhr = new XMLHttpRequest();
	xhr.open("GET", "board/list.do", true);
	xhr.onreadystatechange = function () {
	    if (xhr.readyState == 4 && xhr.status == 200) {
	        document.body.innerHTML = xhr.responseText;
	    }
	}
	xhr.send();
	*/