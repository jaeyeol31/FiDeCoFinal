document.addEventListener('DOMContentLoaded', function() {
    const token = getCookie('jwtToken'); // JWT 토큰을 쿠키에서 가져옴

    console.log('JWT Token:', token); // JWT 토큰 값을 출력

    // DOM에서 네비게이션 바 요소 가져오기
    const loginItem = document.getElementById('loginItem');
    const logoutItem = document.getElementById('logoutItem');
    const myPageItem = document.getElementById('myPage');
    const signupItem = document.getElementById('signupItem');
    const memberListItem = document.getElementById('memberListItem');
    const productSaveItem = document.getElementById('productSaveItem');
    const productListItem = document.getElementById('productListItem');
    const noticeSelectItem = document.getElementById('noticeSelectItem');
    const cartItem = document.getElementById('cartItem');
    const cartLink = document.getElementById('cartLink');
    const adminItem = document.getElementById('adminItem');

    // 상품 목록 및 게시판 항목은 항상 보이도록 설정
    productListItem.style.display = 'block';
    noticeSelectItem.style.display = 'block';

    if (token) {
        const decodedToken = parseJwt(token); // JWT 토큰을 디코딩하여 사용자 정보 추출

        // 사용자의 ID가 'sub' 필드에 포함되어 있다고 가정
        const memberId = decodedToken.sub; // JWT 토큰에 'sub' 필드가 사용자 ID를 가짐
        const id = decodedToken.id; // JWT 토큰에 'id' 필드가 숫자형 ID를 가짐
        const userRole = decodedToken.auth; // JWT 토큰에 'auth' 필드가 사용자 역할을 가짐

        console.log('Decoded Token:', decodedToken); // 디코딩된 토큰 콘솔 출력
        console.log('Member ID:', memberId); // 멤버 ID 콘솔 출력
        console.log('User ID:', id); // 유저 ID 콘솔 출력
        console.log('User Role:', userRole); // 사용자 역할 콘솔 출력

        // 로그인 및 로그아웃 아이템 표시 조정
        if (loginItem && logoutItem && myPageItem && signupItem && cartItem && adminItem) {
            loginItem.style.display = 'none';
            logoutItem.style.display = 'block';
            signupItem.style.display = 'none'; // 로그인하면 회원 가입 항목 숨기기
            cartItem.style.display = 'block'; // 로그인 시 장바구니 항목 보이기
            cartLink.href = `/cart/${memberId}`; // 장바구니 링크 설정

            if (id) {
                myPageItem.querySelector('a').href = `${window.location.origin}/member/myPage/${id}`;
                myPageItem.style.display = 'block';

                if (userRole === 'ROLE_USER') {
                    // 유저 역할일 때 추가 설정이 필요한 경우 여기에 추가
                } else if (userRole === 'ROLE_ADMIN') {
                    memberListItem.style.display = 'block';
                    productSaveItem.style.display = 'block';
                    adminItem.style.display = 'block'; // 관리자 페이지 링크 표시
                }
            } else {
                console.error('토큰에서 사용자 ID를 찾을 수 없습니다.');
            }
        }
    } else {
        if (loginItem && logoutItem && myPageItem && signupItem && cartItem && adminItem) {
            loginItem.style.display = 'block';
            logoutItem.style.display = 'none';
            signupItem.style.display = 'block';
            myPageItem.style.display = 'none';
            cartItem.style.display = 'none'; // 로그아웃 상태에서는 장바구니 항목 숨기기
            adminItem.style.display = 'none'; // 로그아웃 상태에서는 관리자 페이지 링크 숨기기
        }
    }

    // 로그아웃 버튼 클릭 이벤트 처리
    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', function(event) {
            event.preventDefault();
            deleteCookie('jwtToken'); // 쿠키에서 JWT 토큰 삭제
            fetch('/log-out', {
                method: 'GET',
                credentials: 'include'
            }).then(() => {
                window.location.href = '/'; // 로그아웃 후 리다이렉션
            }).catch(error => {
                console.error('로그아웃 실패:', error);
            });
        });
    }
});

// JWT 토큰을 파싱하여 JSON 객체로 변환하는 함수
function parseJwt(token) {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));

        return JSON.parse(jsonPayload);
    } catch (e) {
        console.error('토큰 파싱 중 오류 발생:', e);
        return null;
    }
}

// 쿠키에서 특정 이름의 값을 가져오는 함수
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
    return null;
}

// 쿠키에서 특정 이름의 값을 삭제하는 함수
function deleteCookie(name) {
    document.cookie = name + '=; Max-Age=0; path=/; domain=' + window.location.hostname;
}
