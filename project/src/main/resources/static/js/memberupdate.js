document.addEventListener("DOMContentLoaded", function () {
    const updateForm = document.getElementById("updateForm");
    const memberPw = document.getElementById("memberPw");
    const confirmMemberPw = document.getElementById("confirmMemberPw");
    const detailAddress = document.getElementById("sample4_detailAddress");
    const phoneCategory = document.getElementById("memberPhoneCategory");
    const phoneMiddle = document.getElementById("memberPhoneMiddle");
    const phoneLast = document.getElementById("memberPhoneLast");
    const updateButton = document.getElementById("updateButton");
    const memberName = document.getElementById("memberName");

    const pwMessage = document.getElementById("pwMessage");
    const confirmPwMessage = document.getElementById("confirmPwMessage");
    const detailAddressMessage = document.getElementById("detailAddressMessage");
    const phoneMessage = document.getElementById("phoneMessage");
    const nameMessage = document.getElementById("nameMessage");

    let isPasswordValid = true;
    let isPasswordMatch = true;
    let isDetailAddressValid = true;
    let isPhoneNumberValid = true;
    let isNameValid = true;

    const validatePassword = () => {
        const password = memberPw.value;
        const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]{8,}$/;

        if (!password) {
            pwMessage.textContent = "비밀번호를 입력해주세요.";
            isPasswordValid = false;
        } else if (!passwordRegex.test(password)) {
            pwMessage.textContent = "비밀번호는 숫자, 영문, 특수문자를 포함하여 8자 이상이어야 합니다.";
            isPasswordValid = false;
        } else {
            pwMessage.textContent = "";
            isPasswordValid = true;
        }
    };

    const validateConfirmPassword = () => {
        if (memberPw.value !== confirmMemberPw.value) {
            confirmPwMessage.textContent = "비밀번호가 일치하지 않습니다.";
            isPasswordMatch = false;
        } else {
            confirmPwMessage.textContent = "";
            isPasswordMatch = true;
        }
    };

    const validateDetailAddress = () => {
        if (!detailAddress.value.trim()) {
            detailAddressMessage.textContent = "상세 주소를 입력해주세요.";
            isDetailAddressValid = false;
        } else {
            detailAddressMessage.textContent = "";
            isDetailAddressValid = true;
        }
    };

    const validatePhoneNumber = () => {
        const phoneRegex = /^[0-9]{4}$/;

        if (!phoneRegex.test(phoneMiddle.value) || !phoneRegex.test(phoneLast.value)) {
            phoneMessage.textContent = "유효한 전화번호를 입력해주세요.";
            isPhoneNumberValid = false;
        } else {
            phoneMessage.textContent = "";
            isPhoneNumberValid = true;
        }
    };

    const validateName = () => {
        const nameRegex = /^[가-힣]{2,}$/;
        if (!nameRegex.test(memberName.value)) {
            nameMessage.textContent = "이름은 한글로 2자 이상이어야 합니다.";
            isNameValid = false;
        } else {
            nameMessage.textContent = "";
            isNameValid = true;
        }
    };

    const validateForm = () => {
        validatePassword();
        validateConfirmPassword();
        validateDetailAddress();
        validatePhoneNumber();
        validateName();

        updateButton.disabled = !(isPasswordValid && isPasswordMatch && isDetailAddressValid && isPhoneNumberValid && isNameValid);
    };

    memberPw.addEventListener("input", () => {
        validatePassword();
        validateConfirmPassword();
        validateForm();
    });

    confirmMemberPw.addEventListener("input", () => {
        validateConfirmPassword();
        validateForm();
    });

    detailAddress.addEventListener("input", () => {
        validateDetailAddress();
        validateForm();
    });

    phoneMiddle.addEventListener("input", () => {
        validatePhoneNumber();
        validateForm();
    });

    phoneLast.addEventListener("input", () => {
        validatePhoneNumber();
        validateForm();
    });

    memberName.addEventListener("input", () => {
        validateName();
        validateForm();
    });

    // JWT 토큰에서 사용자 권한 확인
    const token = getCookie('jwtToken'); // JWT 토큰 가져오기 (예시로 쿠키 사용)
    if (token) {
        const decodedToken = parseJwt(token);
        const userRole = decodedToken.auth;
        
        if (userRole === 'ROLE_ADMIN') {
            document.getElementById('authField').style.display = 'block';
        }
    }

    updateForm.addEventListener("submit", function (event) {
        event.preventDefault();
        if (!isPasswordValid || !isPasswordMatch || !isDetailAddressValid || !isPhoneNumberValid || !isNameValid) {
            return;
        }

        const formData = {
            id: document.getElementById('id').value,
            memberId: document.getElementById('memberId').value,
            memberName: document.getElementById('memberName').value,
            memberPw: document.getElementById('memberPw').value,
            memberAddress: `${document.getElementById('sample4_postcode').value}, ${document.getElementById('sample4_roadAddress').value}, ${document.getElementById('sample4_jibunAddress').value}, ${document.getElementById('sample4_detailAddress').value}, ${document.getElementById('sample4_extraAddress').value}`,
            memberPhone: `${document.getElementById('memberPhoneCategory').value}-${document.getElementById('memberPhoneMiddle').value}-${document.getElementById('memberPhoneLast').value}`,
            memberEmail: document.getElementById('memberEmail').value,
            memberType: document.getElementById('memberType').value,
            memberAuth: document.getElementById('memberAuth').value
        };

        fetch(`/member/update/${formData.id}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        })
        .then(response => response.json())
        .then(data => {
            if (data.code === 'SU') {
                alert('회원 정보가 성공적으로 수정되었습니다.');
                window.location.href = '/';
            } else {
                alert('회원 정보 수정에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('회원 정보 수정 중 오류가 발생했습니다.');
        });
    });

    validateForm();
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
