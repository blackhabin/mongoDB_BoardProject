/**
 * home.js
 */

function loadBoardList() {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "board/list.do", true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4 && xhr.status == 200) {
            document.body.innerHTML = xhr.responseText;
        }
    }
    xhr.send();
}