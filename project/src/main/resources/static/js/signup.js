let isIdChecked = false;
let isEmailVerified = false;
let isPasswordMatch = false;
let isPasswordValid = false;
let isNameValid = false;
let isPhoneValid = false;
let isDetailAddressValid = false;

// 아이디 중복확인
function checkDuplicate() {
    var memberId = document.getElementById("memberId").value.trim();
    var memberIdMessageElement = document.getElementById("memberIdMessage");

    // 아이디 유효성 검사
    var idPattern = /^[a-zA-Z0-9]+$/;
    if (!idPattern.test(memberId)) {
        memberIdMessageElement.classList.add('text-danger');
        memberIdMessageElement.textContent = '아이디는 영문자 또는 숫자 조합이어야 합니다.';
        isIdChecked = false;
        updateSignupButtonState();
        return;
    }

    if (memberId === '') {
        alert('아이디를 입력하세요.');
        return;
    }

    fetch('/member/id-check', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ memberId: memberId })
    })
    .then(response => response.json())
    .then(data => {
        switch (data.code) {
            case 'VF':
                alert('아이디를 입력하세요.');
                break;
            case 'DI':
                memberIdMessageElement.classList.add('text-danger');
                memberIdMessageElement.textContent = '이미 사용중인 아이디입니다.';
                document.getElementById("memberId").readOnly = false;
                isIdChecked = false;
                break;
            case 'DBE':
                alert('데이터베이스 오류입니다.');
                break;
            case 'SU':
                memberIdMessageElement.classList.remove('text-danger');
                memberIdMessageElement.classList.add('text-success');
                memberIdMessageElement.textContent = '사용가능한 아이디입니다.';
                document.getElementById("memberId").readOnly = true;
                isIdChecked = true;
                break;
            default:
                break;
        }
        updateSignupButtonState();
    })
    .catch(error => console.error('Error:', error));
}

// 이메일 중복확인
function checkEmailDuplicate(email, callback) {
    fetch('/member/email-check', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ memberEmail: email })
    })
    .then(response => response.json())
    .then(data => {
        if (data.code === 'SU') {
            callback(true);
        } else {
            callback(false);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        callback(false);
    });
}

// 이메일 인증번호 전송
function sendEmailVerification() {
    var memberId = document.getElementById('memberId').value; 
    var memberEmail = document.getElementById('memberEmail').value;

    if (!isIdChecked) {
        alert('먼저 아이디 중복확인을 하세요.');
        return;
    }

    if (memberEmail.trim() === '') {
        alert('이메일을 입력하세요.');
        return;
    }

    checkEmailDuplicate(memberEmail, function(isAvailable) {
        if (isAvailable) {
            var data = {
                memberId: memberId,
                memberEmail: memberEmail
            };

            fetch('/member/email-certification', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                if (data.code === 'SU') {
                    alert('이메일 인증번호가 성공적으로 발송되었습니다.');
                } else {
                    alert('이메일 인증번호 발송에 실패했습니다.');
                }
                updateSignupButtonState();
            })
            .catch(error => {
                console.error('Error:', error);
                alert('서버와의 통신 중 오류가 발생했습니다.');
            });
        } else {
            alert('이미 사용 중인 이메일입니다.');
        }
    });
}

// 이메일 인증 확인
function checkCertification() {
    var memberId = document.getElementById("memberId").value.trim();
    var memberEmail = document.getElementById("memberEmail").value.trim();
    var certificationNumber = document.getElementById("certificationNumber").value.trim();
    var certificationMessageElement = document.getElementById("certificationMessage");

    if (memberId === '' || memberEmail === '' || certificationNumber === '') {
        alert('인증번호를 입력하세요.');
        return;
    }

    fetch('/member/check-certification', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            memberId: memberId,
            memberEmail: memberEmail,
            certificationNumber: certificationNumber
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('네트워크 응답이 올바르지 않음');
        }
        return response.json();
    })
    .then(data => {
        switch (data.code) {
            case 'SU':
                certificationMessageElement.classList.remove('text-danger');
                certificationMessageElement.classList.add('text-success');
                certificationMessageElement.textContent = '인증에 성공하였습니다.';
                isEmailVerified = true;
                document.getElementById('memberEmail').readOnly = true; // 이메일 입력창 읽기 전용으로 변경
                break;
            case 'CF':
                certificationMessageElement.classList.add('text-danger');
                certificationMessageElement.textContent = '인증에 실패하였습니다.';
                isEmailVerified = false;
                break;
            case 'DBE':
                alert('데이터베이스 오류입니다.');
                isEmailVerified = false;
                break;
            default:
                break;
        }
        updateSignupButtonState();
    })
    .catch(error => {
        console.error('Error:', error);
        alert('인증에 실패하였습니다.');
        isEmailVerified = false;
    });
}

// 비밀번호 유효성 검사
function validatePassword() {
    var password = document.getElementById("memberPw").value;
    var pwMessageElement = document.getElementById("pwMessage");

    var pattern = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*\W).{8,20}$/;
    if (!pattern.test(password)) {
        pwMessageElement.classList.add('text-danger');
        pwMessageElement.textContent = '비밀번호는 대소문자 + 숫자 + 특수문자 조합으로 8 ~ 20자리이어야 합니다.';
        isPasswordValid = false;
    } else {
        pwMessageElement.classList.remove('text-danger');
        pwMessageElement.classList.add('text-success');
        pwMessageElement.textContent = '사용 가능한 비밀번호입니다.';
        isPasswordValid = true;
    }
    updateSignupButtonState();
}

// 비밀번호 확인
function checkPasswordMatch() {
    var password = document.getElementById("memberPw").value;
    var confirmPassword = document.getElementById("confirmMemberPw").value;
    var confirmPwMessageElement = document.getElementById("confirmPwMessage");

    if (password !== confirmPassword) {
        confirmPwMessageElement.classList.add('text-danger');
        confirmPwMessageElement.textContent = '비밀번호가 일치하지 않습니다.';
        isPasswordMatch = false;
    } else {
        confirmPwMessageElement.classList.remove('text-danger');
        confirmPwMessageElement.classList.add('text-success');
        confirmPwMessageElement.textContent = '비밀번호가 일치합니다.';
        isPasswordMatch = true;
    }
    updateSignupButtonState();
}

// 이름 유효성 검사
function validateName() {
    var memberName = document.getElementById("memberName").value;
    var nameMessageElement = document.getElementById("nameMessage");

    var namePattern = /^[가-힣]{2,}$/;
    if (!namePattern.test(memberName)) {
        nameMessageElement.classList.add('text-danger');
        nameMessageElement.textContent = '이름은 한글로 2자 이상이어야 합니다.';
        isNameValid = false;
    } else {
        nameMessageElement.classList.remove('text-danger');
        nameMessageElement.classList.add('text-success');
        nameMessageElement.textContent = '사용 가능한 이름입니다.';
        isNameValid = true;
    }
    updateSignupButtonState();
}

// 전화번호 유효성 검사
function validatePhone() {
    var phoneCategory = document.getElementById("memberPhoneCategory").value;
    var phoneMiddle = document.getElementById("memberPhoneMiddle").value;
    var phoneLast = document.getElementById("memberPhoneLast").value;
    var phoneMessageElement = document.getElementById("phoneMessage");

    var phonePattern = /^[0-9]{4}$/;
    if (!phonePattern.test(phoneMiddle) || !phonePattern.test(phoneLast)) {
        phoneMessageElement.classList.add('text-danger');
        phoneMessageElement.textContent = '전화번호는 4자리 숫자로 입력해야 합니다.';
        isPhoneValid = false;
    } else {
        phoneMessageElement.classList.remove('text-danger');
        phoneMessageElement.classList.add('text-success');
        phoneMessageElement.textContent = '사용 가능한 전화번호입니다.';
        isPhoneValid = true;
    }
    updateSignupButtonState();
}

// 상세주소 유효성 검사
function validateDetailAddress() {
    var detailAddress = document.getElementById("sample4_detailAddress").value;
    var detailAddressMessageElement = document.getElementById("detailAddressMessage");

    if (detailAddress.trim() === '') {
        detailAddressMessageElement.classList.add('text-danger');
        detailAddressMessageElement.textContent = '상세주소를 입력하세요.';
        isDetailAddressValid = false;
    } else {
        detailAddressMessageElement.classList.remove('text-danger');
        detailAddressMessageElement.classList.add('text-success');
        detailAddressMessageElement.textContent = '상세주소가 입력되었습니다.';
        isDetailAddressValid = true;
    }
    updateSignupButtonState();
}

// 회원가입 버튼 활성화 조건 확인
function updateSignupButtonState() {
    const memberIdElement = document.getElementById('memberId');
    const memberEmailElement = document.getElementById('memberEmail');
    const certificationNumberElement = document.getElementById('certificationNumber');
    const memberPwElement = document.getElementById('memberPw');
    const confirmMemberPwElement = document.getElementById('confirmMemberPw');
    const memberNameElement = document.getElementById('memberName');
    const postcodeElement = document.getElementById('sample4_postcode');
    const roadAddressElement = document.getElementById('sample4_roadAddress');
    const jibunAddressElement = document.getElementById('sample4_jibunAddress');
    const detailAddressElement = document.getElementById('sample4_detailAddress');
    const extraAddressElement = document.getElementById('sample4_extraAddress');
    const phoneCategoryElement = document.getElementById('memberPhoneCategory');
    const phoneMiddleElement = document.getElementById('memberPhoneMiddle');
    const phoneLastElement = document.getElementById('memberPhoneLast');

    if (memberIdElement && memberEmailElement && certificationNumberElement && memberPwElement && confirmMemberPwElement && 
        memberNameElement && postcodeElement && roadAddressElement && jibunAddressElement && detailAddressElement && 
        extraAddressElement && phoneCategoryElement && phoneMiddleElement && phoneLastElement) {
        
        const memberId = memberIdElement.value.trim();
        const memberEmail = memberEmailElement.value.trim();
        const certificationNumber = certificationNumberElement.value.trim();
        const memberPw = memberPwElement.value.trim();
        const confirmMemberPw = confirmMemberPwElement.value.trim();
        const memberName = memberNameElement.value.trim();
        const postcode = postcodeElement.value.trim();
        const roadAddress = roadAddressElement.value.trim();
        const jibunAddress = jibunAddressElement.value.trim();
        const detailAddress = detailAddressElement.value.trim();
        const extraAddress = extraAddressElement.value.trim();
        const phoneCategory = phoneCategoryElement.value;
        const phoneMiddle = phoneMiddleElement.value.trim();
        const phoneLast = phoneLastElement.value.trim();

        if (isIdChecked && isEmailVerified && isPasswordMatch && isPasswordValid &&
            isNameValid && isPhoneValid && isDetailAddressValid && memberId && memberEmail && certificationNumber && 
            memberPw && confirmMemberPw && memberName && postcode && roadAddress && jibunAddress && 
            detailAddress && extraAddress && phoneCategory && phoneMiddle && phoneLast) {
            document.getElementById('signupButton').disabled = false;
        } else {
            document.getElementById('signupButton').disabled = true;
        }
    }
}

// 폼 필드 변경 시 회원가입 버튼 상태 업데이트
document.getElementById('memberPw').addEventListener('input', validatePassword);
document.getElementById('memberPw').addEventListener('input', checkPasswordMatch);
document.getElementById('confirmMemberPw').addEventListener('input', checkPasswordMatch);
document.getElementById('memberName').addEventListener('input', validateName);
document.getElementById('memberPhoneMiddle').addEventListener('input', validatePhone);
document.getElementById('memberPhoneLast').addEventListener('input', validatePhone);
document.getElementById('sample4_detailAddress').addEventListener('input', validateDetailAddress);

// 폼 전송 시 JSON 형식으로 데이터를 생성하여 서버로 전송
document.getElementById('signupForm').addEventListener('submit', function (event) {
    event.preventDefault();

    var formData = {
        memberId: document.getElementById('memberId').value,
        memberName: document.getElementById('memberName').value,
        memberPw: document.getElementById('memberPw').value,
        memberAddress: `${document.getElementById('sample4_postcode').value}, ${document.getElementById('sample4_roadAddress').value}, ${document.getElementById('sample4_jibunAddress').value}, ${document.getElementById('sample4_detailAddress').value}, ${document.getElementById('sample4_extraAddress').value}`,
        memberPhone: `${document.getElementById('memberPhoneCategory').value}-${document.getElementById('memberPhoneMiddle').value}-${document.getElementById('memberPhoneLast').value}`,
        memberEmail: document.getElementById('memberEmail').value,
        certificationNumber: document.getElementById('certificationNumber').value
    };

    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/member/signup', true);
    xhr.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');
    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                console.log('Signup successful');
                window.location.href = '/log-in';
            } else {
                console.error('Error signing up:', xhr.status);
                // 오류 발생 시 처리를 추가할 수 있습니다.
            }
        }
    };
    xhr.send(JSON.stringify(formData));
});
