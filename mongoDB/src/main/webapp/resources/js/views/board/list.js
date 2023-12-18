/**
 * list.js
 */

//  다른 js에서도 사용을 해야하기 때문에 dialog는 전역변수 선언함.
var dialog;
// 다른 js에서 사용하기 위해서 전역함수로 선언
openDialog = function(url) { // board 객체의 메서드로 선언
    board.dialog.load(url, function() {
        board.dialog.dialog("open");
    });
}


var $jqGrid = $('#jqGrid');
$(document).ready(function() {
	console.log("list js 호출");
	$jqGrid.jqGrid({
			url: '/board/data',
			jsonReader: {
		        root: 'rows'
		    },
			mtype: "GET",
			datatype: "json",
			page: 100,
			pageable: true,
			countable: true,
			sortable: true,
			domainId : "게시판 리스트",
			colNames:['번호','제목','작성자','내용','작성일','조회수'],
			colModel: [
				{align:"center", name: 'no', key: true },
				{align:"center", name: 'title'},
				/* ondblClickRow  쓸거고 meat_manage.js 참조해서 만들어보자 */
				{align:"center", name: 'writer'},
				{align:"center", name: 'content'},
				{align:"center", name: 'writedate'},
				/* java 컨트롤러단에서 날짜 포매팅을 이용해서 사용한다 */
				{align:"center", name: 'hit'}
			],
			pager: "#jqGridNavi",
			caption: "게시판 리스트",
			autowidth: true,
			rownumbers: true,
			rowNum: 15,
			cellsubmit: 'clientArray',
			rowClickFocus: false,
			rowClickColor: 'default',
			/* 상세화면 들어가기 */
			ondblClickRow: function(rowid, iRow, iCol, e) {
	            var cm = $(this).jqGrid('getGridParam', 'colModel');
				// 컬럼을 더블클릭한 경우
	            if (cm[iCol].name === 'title' 
					|| cm[iCol].name === 'no' 
					|| cm[iCol].name === 'writer'
					|| cm[iCol].name === 'content'
					|| cm[iCol].name === 'writedate'
					|| cm[iCol].name === 'hit') { 
	                $.ajax({
	                    type: 'POST',   
						url: '/setSessionNo',
	                    contentType: 'application/json',
	                    data: JSON.stringify({
	                        no: rowid
	                    }),
	                    success: function() {
	                        $.get('detail.do', function(data) {
	                            /* $('body').html(data);*/
								
								/* popup 띄우기 window.open 방식
								var url = "detail.do"
								var name = "popup test";
								var option = "width = 1200, height = 600, top = 100, left = 200, location = no"
								window.open(url, name, option);
								*/
								
								/* popup 띄우기 jquery dialog 방식 */
								dialog.html(data); // Dialog 내용 변경
			                    dialog.dialog("open");
	                        });
	                    }
	                });
	            }
	        },
			emptyrecords: '조회된 데이타가 없습니다.'
	});
	
	
	/* dialog 함수 */
    dialog = $('<div id="dialog-form"></div>').dialog({
		appendTo: 'body',
	    autoOpen: false,
	    width: 1200,
	    height: 600,
		title: '상세보기',
		// 모달 다이얼로그로 생성
	    modal: true,
	    close: function(e) {
		 	$('body').load('board');
			dialog.dialog("close");
	    }
	});
	
	$(window).bind('resize', function () {
        var width = $(".grid-wrapper:visible").eq(0).width();
        $('#jqGrid').setGridWidth(width);
     });




	
	// 글 작성 호출 함수
    console.log("write 호출");
	$('#btnWrite').click(function(e) {
        e.preventDefault();
        $.get('write.do', function(data) {
            $('body').html(data);  // 페이지의 전체 내용 변경
            console.log("list->write 성공");
        });
    });
	
	// 상세화면 호출 함수
	/*
    console.log("detail; 호출");
    $(document).on('click', '.post-link', function(e) {
        e.preventDefault();
        $.ajax({
            type: 'POST',
            url: '/setSessionNo',
            contentType: 'application/json',
            data: JSON.stringify({
                no: $(this).data('no')
            }),
            success: function() {
                $.get('detail.do', function(data) {
                    $('body').html(data);
                });
                console.log("list->detail 성공");
            }
        });
    });
    */
	
	
	// 검색 함수
    console.log("search 호출");
    // "전체글 목록" 버튼이 없다면 생성
	if ($("#buttonAllListBtn").length === 0) {
	    var btn = $('<input/>', {
	        type: 'button',
	        value: '전체글 목록',
	        id: 'buttonAllListBtn',
	        style: 'float: left;',
	        click: function(e) {
	            e.preventDefault();
	            $.get('board', function(data) {
	                $('body').html(data); // 페이지의 전체 내용 변경
	                console.log("전체글 목록 동기화");
	            }).fail(function() {
	                console.log("에러 발생: 페이지를 불러오는 데 실패했습니다.");
	            });
	        }
	    });
	    console.log("search 후 전체글 목록 불러오기");
	    $("body").append(btn);
	}


	//검색 버튼 클릭시 나타나는 화면 호출 함수
    $("#btnSearch").click(function() {
        var keyword = $("#search").val();
        $.get("/searchPost?keyword=" + keyword, function(data) {
            $("tbody").empty();  // 기존의 목록 비우기
            $.each(data, function(index, item) {
                var row = "<tr>" +
                    "<td>" + item['no'] + "</td>" +
                    "<td><a href='/board' class='post-link' data-no='" + item['no'] + "'>" + item['title'] + "</a></td>" +
                    "<td>" + item['writer'] + "</td>" +
					"<td>" + item['content'] + "</td>" +
                    "<td>" + formatDate(item['writedate']) + "</td>" +  // 날짜 포맷팅 적용
                    "<td>" + item['hit'] + "</td>" +
                    "</tr>";
                $("tbody").append(row);  // 검색 결과 추가
            });
            console.log("검색 성공");
        });
    });
	
	// 엑셀 다운로드 ajax 호출
	$("#btnDownload").click(function(event) {
    event.preventDefault();
	    if (confirm("다운로드 하시겠습니까?")) {
	        $.ajax({
	            url: '/downloadExcel',
	            method: 'GET',
	            xhrFields: {
	                responseType: 'blob'
	            },
	            success: function(blob) {
	                var link = document.createElement('a');
	                link.href = window.URL.createObjectURL(blob);
	                link.download = "boardList_data.xlsx";
	                link.click();
	                alert('엑셀 다운로드에 성공했습니다');
	            },
	            error: function(xhr, status, error) {
	                console.error('서버 응답:', xhr);
	                console.error('오류 상태:', status);
	                console.error('오류 메시지:', error);
	                alert('엑셀 다운로드에 실패했습니다.');
	            }
	        });
	    }
	});

 

	
	/** 
	$("#btnDownload").click(function(event) {
    event.preventDefault();
	    if (confirm("다운로드 하시겠습니까?")) {
	        var xhr = new XMLHttpRequest();
	        xhr.open('GET', '/downloadExcel', true);
	        xhr.responseType = 'blob';
	        xhr.onload = function(e) {
	            if (this.status == 200) {
	                var blob = new Blob([this.response], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'});
	                var link = document.createElement('a');
	                link.href = window.URL.createObjectURL(blob);
	                link.download = "boardList_data.xlsx";
	                link.click();
	                alert('엑셀 다운로드에 성공했습니다');
	            }
	        };
	        xhr.onerror = function() {
	            alert('엑셀 다운로드에 실패했습니다.');
	        };
	        xhr.send();
	    }
	});
	*/
	


	// 엑셀 업로드 ajax 호출
	
    $("#btnUpload").click(function(event){
        event.preventDefault();
        var form = $('#uploadForm')[0];
        var data = new FormData(form);
        $.ajax({
            type: "POST",
            enctype: 'multipart/form-data',
            url: "/uploadExcel",
            data: data,
            processData: false,
            contentType: false,
            success: function (data) {
                // 성공 시 동작
                alert("엑셀 파일 업로드 성공");
				$.get('board', function(data) {
                $('body').html(data);
            });
            },
            error: function (e) {
              	alert(e.responseText); 
            }
        });
    });
   
});


// 날짜 포맷팅을 위한 함수
function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2) 
        month = '0' + month;
    if (day.length < 2) 
        day = '0' + day;

    return [year, month, day].join('-');
}

