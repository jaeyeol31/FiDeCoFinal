document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("additionalInfoForm");
    const memberName = document.getElementById("memberName");
    const detailAddress = document.getElementById("sample4_detailAddress");
    const nameMessage = document.getElementById("nameMessage");
    const detailAddressMessage = document.getElementById("detailAddressMessage");
    const submitButton = document.getElementById("submitButton");

    let isNameValid = false;
    let isDetailAddressValid = false;

    const validateName = () => {
        const nameRegex = /^[가-힣]{2,}$/;
        if (!nameRegex.test(memberName.value)) {
            nameMessage.textContent = "이름은 한글로 2자 이상이어야 합니다.";
            isNameValid = false;
        } else {
            nameMessage.textContent = "";
            isNameValid = true;
        }
        toggleSubmitButton();
    };

    const validateDetailAddress = () => {
        if (!detailAddress.value.trim()) {
            detailAddressMessage.textContent = "상세 주소를 입력해주세요.";
            isDetailAddressValid = false;
        } else {
            detailAddressMessage.textContent = "";
            isDetailAddressValid = true;
        }
        toggleSubmitButton();
    };

    const toggleSubmitButton = () => {
        submitButton.disabled = !(isNameValid && isDetailAddressValid);
    };

    memberName.addEventListener("input", validateName);
    detailAddress.addEventListener("input", validateDetailAddress);

    form.addEventListener("submit", function (event) {
        event.preventDefault();
        if (!isNameValid || !isDetailAddressValid) {
            return;
        }

        const formData = {
            memberId: document.getElementById('memberId').value,
            memberEmail: document.getElementById('memberEmail').value,
            memberPhone: document.getElementById('memberPhone').value,
            memberName: memberName.value,
            memberAddress: `${document.getElementById('sample4_postcode').value}, ${document.getElementById('sample4_roadAddress').value}, ${document.getElementById('sample4_jibunAddress').value}, ${detailAddress.value}, ${document.getElementById('sample4_extraAddress').value}`
        };

        fetch("/member/additional-info", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(formData)
        })
        .then(response => response.json())
        .then(data => {
            if (data.code === 'SU') {
                alert('추가 정보가 성공적으로 저장되었습니다.');
                window.location.href = '/';
            } else {
                alert('추가 정보 저장에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('추가 정보 저장 중 오류가 발생했습니다.');
        });
    });

    validateName();
    validateDetailAddress();
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
