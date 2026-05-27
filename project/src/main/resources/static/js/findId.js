function sendEmail() {
    var email = document.getElementById("email").value.trim();
    
    if (email === '') {
        alert('이메일을 입력하세요.');
        return;
    }

    fetch('/member/find-id', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email: email })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        if (data.success) {
            alert('아이디가 이메일로 전송되었습니다.');
        } else {
            alert('아이디 찾기에 실패했습니다.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('서버와의 통신 중 오류가 발생했습니다.');
    });
}
