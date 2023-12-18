/**
 * detail.js
 */



$(document).ready(function() {

	// list.js 가 로드 되면 함수 실행
	$.getScript("resources/js/views/board/list.js", function() {
		
		// 수정 페이지 로드 함수
		console.log("getScript 호출");
		$('.modify-link').click(function(e) {
			e.preventDefault();
			$.ajax({
				type: 'POST',
				url: '/setSessionNo',
				contentType: 'application/json',
				data: JSON.stringify({
					no: $(this).data('no')
				}),
				success: function() {
					$.get('modify.do', function(data) {
						/*
	                    $('body').empty();  // 기존의 DOM과 이벤트 핸들러 제거
	                    $('body').html(data);
	                    // modify.do의 내용이 완전히 로드된 후에 modify.js를 로드
	                    $.getScript('/resources/js/views/board/modify.js', function() {
	                        console.log("modify.js 로드 성공");
	                    });
						*/
						
						dialog.html(data);
				        dialog.dialog("open");
				    });
					console.log("modify 연결 성공");
				}
			});
		});
		
		// 리스트 로드 함수
		$("#btnBoardList").click(function(e){
			e.preventDefault();
			
			/* window.opener 방식
			window.opener.location.href='board'; // 새로고침하면서 리스트로 이동
			window.opener.$('body').load('board'); 
			window.close(); 
		    */

			/* dialog 방식 */
			 $('body').load('board');
	         // Dialog를 닫기
	         closeDialog();
			
			
			/*
		    $.get('board', function(data) {
		    	$('body').html(data); // 페이지의 전체 내용 변경
		    console.log("작성글 목록 동기화");
			});
			*/
		
		});
		
		// 게시물 삭제 함수
		$('#btnDeletePost').click(function(e) {
	        e.preventDefault();
	        var no = $("#deleteForm input[name='no']").val();
	        if (confirm("정말 삭제하시겠습니까?")) {
	            $.ajax({
	                type: 'POST',
	                url: '/deletePost',
	                data: {
	                    no: no
	                },
	                success: function() {
	                    $('body').load('board');
	                    // Dialog를 닫기
	                    closeDialog();
	                    console.log("삭제 후 - list 연결 성공");
	                },
	                error: function() {
	                    console.log("삭제 요청 실패");
	                }
	            });
	        }
	    });
	
		
		// 파일 다운로드를 위한 ajax 호출
		/** 
		$('a[href^="/downloadFile"]').on('click', function(e) {
	    e.preventDefault(); // 기본 링크 클릭 동작을 막습니다.
		console.log("파일다운로드 ajax 호출");
	    var downloadUrl = $(this).attr('href');
	   	 	$.ajax({
		        url: downloadUrl,
		        type: 'GET',
		        success: function(response) {
		            alert('파일 다운로드가 성공적했습니다.');
		        },
		        error: function(err) {
		            alert('파일 다운로드가 실패했습니다..');
		        }
		    });
		});
		*/
		
		/**
		
			$('a[href^="/downloadFile"]').on('click', function(e) {
		    e.preventDefault(); // 기본 링크 클릭 동작을 막습니다.
		
		    var downloadUrl = $(this).attr('href');
		    var fileId = downloadUrl.split('/').pop(); // URL의 마지막 부분을 fileId로 사용
		
		    console.log("파일다운로드 ajax 호출");
		    $.ajax({
		        url: downloadUrl,
		        type: 'GET',
		        xhrFields: {
		            responseType: 'blob' // 응답을 Blob 객체로 받음
		        },
		        success: function(blob) {
		            var link = document.createElement('a');
		            link.href = window.URL.createObjectURL(blob);
		            link.download = fileId; // 파일명 설정
		            link.click();
		        },
		        error: function() {
		            alert('파일 다운로드에 실패했습니다.');
		        }
		    });
		});
		 */
		
		$('#btnFileDownload').click(function(e) {
	    e.preventDefault();
		console.log("파일다운로드 ajax 호출");
		var fileId = $("#btnFileDownload").data("no"); // 'data-no' 속성의 값을 가져옴
		var filename = $("#fileDownloadForm input[name='filename']").val();
		// var fileId = $("#fileDownloadForm button[name='fileId']").val();
		    if (confirm("다운로드 하시겠습니까?")) {
		       $.ajax({
				    type: 'GET',
				    url: `/downloadFile/${fileId}`, // fileId 변수를 URL에 포함
				    xhrFields: {
				        responseType: 'blob' // 응답을 Blob 객체로 받음
				    },
				    success: function(blob) {
				        var link = document.createElement('a');
				        link.href = window.URL.createObjectURL(blob);
				        link.download = filename; // 다운로드시 저장되어 있는 파일이름으로 기본 다운로드 설정
	                	link.click();
	                	console.log("파일 다운로드 성공");
						alert('파일 다운로드에 성공했습니다');
	            	},
				    error: function() {
				        alert('파일 다운로드에 실패했습니다.');
				    }
				});
	
		    }
		});
	});
});