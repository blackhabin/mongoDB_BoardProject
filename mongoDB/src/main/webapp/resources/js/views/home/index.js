/**
 * index.js
 */

function loadBoardList() {
	location.href="/board"
}
    
$(document).ready(function() {
    console.log("board 호출");
    $('#btnBoardList').click(function(e) {
        e.preventDefault();
        $.get('board', function(data) {
            $('body').html(data); // 페이지의 전체 내용 변경
            console.log("index -> list 성공");
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