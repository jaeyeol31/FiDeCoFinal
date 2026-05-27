// 기존 fetch를 래핑하여 모든 요청에 JWT 토큰을 포함시키기
const originalFetch = window.fetch;

window.fetch = async (url, options = {}) => {
    options.credentials = 'include'; // 쿠키를 포함시키기 위해 설정

    // 기존 헤더와 병합하여 Content-Type 헤더 추가
    options.headers = {
        ...options.headers,
        'Content-Type': 'application/json'
    };

    return originalFetch(url, options);
};

// 로그인 폼 제출 이벤트 리스너
document.getElementById('loginForm').addEventListener('submit', function (event) {
    event.preventDefault();

    var formData = {
        memberId: document.getElementById('memberId').value,
        memberPw: document.getElementById('memberPw').value
    };

    fetch('/log-in', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData),
        credentials: 'include' // 쿠키를 포함시키기 위해 설정
    })
    .then(response => {
        if (!response.ok) {
            if (response.status === 401) {
                return response.json().then(data => {
                    throw new Error(data.message || '아이디나 비밀번호가 틀렸습니다.');
                });
            }
            throw new Error('네트워크 응답이 올바르지 않음');
        }
        return response.json();
    })
    .then(data => {
        if (data.code === 'SU') {
            console.log('로그인 성공');
            window.location.href = '/';
        } else {
            console.error('로그인 실패:', data.message);
            alert('로그인에 실패했습니다.');
        }
    })
    .catch(error => {
        console.error('오류 발생:', error);
        alert(error.message);
    });
});
