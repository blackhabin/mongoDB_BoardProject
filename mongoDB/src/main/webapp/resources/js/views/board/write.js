/**
 * write.js
 */

// 게시글 작성 함수
function goWrite() {
   	var title = document.querySelector('input[name="title"]').value;
    var writer = document.querySelector('input[name="writer"]').value;
    var content = document.querySelector('textarea[name="content"]').value;
	var fileInput = document.querySelector('input[type="file"]');
	
	// fileInput.files.length > 0 조건을 통해 사용자가 파일을 첨부했는지 확인하고, 
	// 첨부했다면 첫 번째 파일을 file 변수에 저장. 파일을 첨부하지 않았다면 file 변수에는 null이 저장
	var file = fileInput.files.length > 0 ? fileInput.files[0] : null;
	
	// 사용자가 입력한 제목, 작성자, 내용이 유효한지 검증
    if (!validate(title, writer, content)) {
        return;
    }
	
	// FormData 객체를 생성하여 title, writer, content, file 항목들을 추가
 	var formData = new FormData();
    formData.append('title', title);
    formData.append('writer', writer);
    formData.append('content', content);
    formData.append('file', file);

    $.ajax({
	    url: '/savePost',
	    data: formData,
	    processData: false,  // processData와 
	    contentType: false,  // contentType 옵션이 false로 설정된 이유는 FormData 객체를 그대로 전송하기 위함
	    type: 'POST',
	    success: function(){
	        $.get('board', function(data)   {
	            $('body').empty();
	            $('body').html(data);
	            console.log("글 작성 후 list.jsp 화면으로 이동");
	        });   
	    },
	    error: function(){
	        alert("글 작성에 실패했습니다.");
	    }
    });
}

function validate(title, writer, content) {
    // 제목이 입력되었는지 검사
    if (title.trim() == ''){
        alert("제목을 입력해주세요");
        return false;
    }

    // 제목의 길이가 50자를 넘지 않는지 검사
    if (title.length > 50){
        alert("제목은 50자를 넘을 수 없습니다");
        return false;
    }

    // 작성자가 입력되었는지 검사
    if (writer.trim() == ''){
        alert("작성자를 입력해주세요");
        return false;
    }

    // 작성자의 길이가 30자를 넘지 않는지 검사
    if (writer.length > 30){
        alert("작성자는 30자를 넘을 수 없습니다");
        return false;
    }

    // 내용이 입력되었는지 검사
    if (content.trim() == ''){
        alert("내용을 입력해주세요");
        return false;
    }

    // 내용의 길이가 500자를 넘지 않는지 검사
    if (content.length > 500){
        alert("내용은 500자를 넘을 수 없습니다");
        return false;
    }

    // 모든 검증이 통과하면 true를 반환
    return true;
}