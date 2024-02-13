
// 클릭 이벤트 추가
document.getElementById('tabList').addEventListener('click', function(e) {
    var target = e.target;
    if (target.tagName === 'A') {
        e.preventDefault();
        var tabName = target.getAttribute('data-tab');
        var contents = document.getElementsByClassName('container');
        for (var i = 0; i < contents.length; i++) {
            contents[i].style.display = 'none';
        }
        document.getElementById(tabName).style.display = 'flex';
        e.currentTarget.className += " active";

        // 모든 탭에서 active class 빼버리기, #기호는 id를 뜻함
        var tabs = document.querySelectorAll('#tabList a');
        for (var i = 0; i < tabs.length; i++) {
            tabs[i].classList.remove('active');
        }

        // 클릭한 탭에 class 추가해버리기
        target.classList.add('active');
    }
});
