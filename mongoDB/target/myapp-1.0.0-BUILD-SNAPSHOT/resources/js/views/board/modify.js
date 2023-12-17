/**
 * modify.js
 */

$(document).ready(function() {
	
    // 'existFile' 요소가 있고 페이지가 처음 로드된 경우에만 실행
    if($("#existFile").length !== 0 && !sessionStorage.getItem("reloaded")) {
		console.log("확인용")
        sessionStorage.setItem("reloaded", true);
        $.ajax({
            type: 'POST',
            url: '/setSessionNo',
            contentType: 'application/json',
            data: JSON.stringify({ no: $('#no').val() }),
            success: function() {
                $.ajax({
                    url: 'modify.do',
                    type: 'GET',
                    dataType: 'json',
                    success: function(data) {
                        // 필요한 데이터를 처리하여 페이지에 반영
                        $('#title').val(data.title);
                        $('#content').val(data.content);
                    }
                });
                console.log("modify reload 연결 성공");
            }
        });
    } else {
        sessionStorage.removeItem("reloaded");
    }

    // 파일 삭제 버튼 클릭 이벤트 핸들러
    $('#btnDelete').off('click').on('click', function() {
        var fileId = $(this).data('file-id');
        var no = $('#no').val();
        var fileElement = $(this).parent();
        $.ajax({
            url: '/deleteFile',
            method: 'POST',
            data: { fileId: fileId, no: no },
            dataType: 'json',
            success: function(response) {
			    if (response.status === 'SUCCESS') {
					console.log("파일 삭제");
			        alert('파일이 성공적으로 삭제되었습니다.');
			        fileElement.remove();
			    } else {
			        alert('파일 삭제에 실패했습니다.');
			    }
			},
			error: function(error) {
			    console.log(error);
			    alert('파일 삭제에 실패했습니다.');
			}
        });
    });
});

// 글 수정 처리 함수
function goModify(frm) {
    var title = frm.title.value.trim();
    var writer = frm.writer.value.trim();
    var content = frm.content.value.trim();

    if (!title){
        alert("제목을 입력해주세요");
        return false;
    }
    if (!writer){
        alert("작성자를 입력해주세요");
        return false;
    }
    if (!content){
        alert("내용을 입력해주세요");
        return false;
    }
    
	var data = new FormData(frm); // form 데이터를 FormData 객체로 만듦
	$.ajax({
		url: '/updatePost',
		data: data,
		processData: false,
		contentType: false,
		type: 'POST',
		success: function() {
			$.get('detail.do', function(data) {
				$('body').empty;
				$('body').html(data);
				console.log('글 수정 후 list.jsp 화면으로 이동');
			});
		},
		error: function(){
			alert("글 수정에 실패했습니다.");
		}
	});
}

$(document).ready(function() {

	// delete 호출 함수
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
});