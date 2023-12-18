/**
 * write.js
 */

// 게시글 작성 함수
function goJoin() {
   	var id = document.querySelector('input[name="id"]').value;
    var password = document.querySelector('input[name="password"]').value;
	
	
	// 사용자가 입력한 제목, 작성자, 내용이 유효한지 검증
    if (!validate(id, password)) {
        return;
    }
	
 	var formData = new FormData();
    formData.append('id', id);
    formData.append('password', password);

    $.ajax({
	    url: '/saveMember',
	    data: formData,
	    processData: false,  // processData와 
	    contentType: false,  // contentType 옵션이 false로 설정된 이유는 FormData 객체를 그대로 전송하기 위함
	    type: 'POST',
	    success: function(){
	        $.get('index', function(data)   {
	            $('body').empty();
	            $('body').html(data);
	            console.log("회원 가입 후 index.jsp 화면으로 이동");
	        });   
	    },
	    error: function(){
	        alert("회원가입에 실패했습니다.");
	    }
    });
}

function validate(id, password) {
    // 제목이 입력되었는지 검사
    if (id.trim() == ''){
        alert("아이디를 입력해주세요");
        return false;
    }

    // 제목의 길이가 50자를 넘지 않는지 검사
    if (id.length > 50){
        alert("아이디는 50자를 넘을 수 없습니다");
        return false;
    }

    // 작성자가 입력되었는지 검사
    if (password.trim() == ''){
        alert("암호를 입력해주세요");
        return false;
    }

    // 작성자의 길이가 30자를 넘지 않는지 검사
    if (password.length > 30){
        alert("암호는 30자를 넘을 수 없습니다");
        return false;
    }

    // 모든 검증이 통과하면 true를 반환
    return true;
}