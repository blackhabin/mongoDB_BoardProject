/**
 * list.js
 */

		function loadWriteForm() {
	        var xhr = new XMLHttpRequest();
	        xhr.open("GET", "board/write.do", true);
	        xhr.onreadystatechange = function () {
	            if (xhr.readyState == 4 && xhr.status == 200) {
	                document.body.innerHTML = xhr.responseText;
	            }
	        }
	        xhr.send();
	    }
	
	    function loadPostDetail(postNo) {
	        var xhr = new XMLHttpRequest();
	        xhr.open("GET", "board/detail.do?no=" + postNo, true);
	        xhr.onreadystatechange = function () {
	            if (xhr.readyState == 4 && xhr.status == 200) {
	                document.body.innerHTML = xhr.responseText;
	            }
	        }
	        xhr.send();
	    }
	
	    $(document).ready(function() {
	        // "전체글 목록" 버튼이 없다면 생성
	        if ($("#buttonAllListBtn").length === 0) {
	            var btn = $('<input/>', {
	                type: 'button',
	                value: '전체글 목록',
	                id: 'buttonAllListBtn',
	                style: 'float: right;',
	                click: function() { loadContent('/myapp/board/list.do'); }
	            });
	            $("body").append(btn);
	        }
	
	        $("#btnSearch").click(function() {
	            var keyword = $("#search").val();
	            $.get("/myapp/board/searchPost?keyword=" + keyword, function(data, status) {
	                $("tbody").empty();  // 기존의 목록 비우기
	                $.each(data, function(index, item) {
	                    var row = "<tr>" +
	                    "<td>" + item['no'] + "</td>" +
	                    "<td><a href='#' onclick='loadPostDetail(" + item['no'] + ");'>" + item['title'] + "</a></td>" +
	                    "<td>" + item['writer'] + "</td>" +
	                    "<td>" + item['writedate'] + "</td>" +
	                    "<td>" + item['hit'] + "</td>" +
	                    "</tr>";
	                    $("tbody").append(row);  // 검색 결과 추가
	                });
	            });
	        });
	    });