document.addEventListener("DOMContentLoaded", function() {
    console.log("DOM fully loaded and parsed");

    // 로컬 스토리지에서 JWT 토큰 가져오기
    function getAuthToken() {
        const token = localStorage.getItem("jwtToken");
        console.log("Retrieved token from local storage:", token);
        return token;
    }

    // 인증된 요청을 보내는 비동기 함수
    async function authenticatedFetch(url, options = {}) {
        const token = getAuthToken();
        if (!token) {
            console.error("No JWT token found in local storage");
            throw new Error("No JWT token found");
        }

        options.headers = {
            ...options.headers,
            "Authorization": `Bearer ${token}`
        };

        console.log("Sending request to URL:", url);
        console.log("Request options:", options);

        try {
            const response = await fetch(url, options);
            console.log("Received response:", response);

            if (!response.ok) {
                console.error("Response not ok, status:", response.status);
                throw new Error(response.statusText);
            }

            const data = await response.json();
            console.log("Response data:", data);
            return data;
        } catch (error) {
            console.error("Fetch error:", error);
            throw error;
        }
    }

    // 공지사항 테이블 채우기
    function populateNoticeTable(data) {
        console.log("Populating notice table with data:", data);
        const tbody = document.querySelector("tbody");
        tbody.innerHTML = ""; // 기존 내용 삭제
        data.forEach(list => {
            const row = document.createElement("tr");
            row.classList.add("text-center");
            row.innerHTML = `
                <td>${list.notice_num}</td>
                <td><a href="/NoticeSelectDetail?notice_num=${list.notice_num}">${list.notice_title}</a></td>
                <td>${list.notice_writer}</td>
                <td>${list.notice_hit}</td>
            `;
            tbody.appendChild(row);
        });

        // 각 행을 클릭하면 상세페이지로 넘어가도록 설정
        document.querySelectorAll("tbody tr").forEach(row => {
            row.addEventListener("click", function() {
                const noticeNum = this.querySelector("td:first-child").textContent;
                const detailUrl = `/NoticeSelectDetail?notice_num=${noticeNum}`;
                console.log("Navigating to detail URL:", detailUrl);
                window.location.href = detailUrl;
            });
        });
    }

    // 공지사항 목록 불러오기
    async function loadNotices() {
        try {
            const data = await authenticatedFetch('/NoticeSelect', {
                method: 'GET'
            });
            populateNoticeTable(data);
        } catch (error) {
            console.error('Error loading notices:', error);
        }
    }

    // 페이지가 로드될 때 공지사항 목록을 불러옵니다.
    loadNotices();

    // 글쓰기 버튼 클릭 시 JWT 토큰을 포함하여 요청 전송
    document.getElementById("noticeInsertButton").addEventListener("click", function(event) {
        event.preventDefault();

        const jwtToken = getAuthToken();
        if (!jwtToken) {
            alert("No JWT token found. Please log in first.");
            return;
        }

        window.location.href = "/NoticeInsert?token=" + jwtToken;
    });

    // 공지사항 등록 버튼 클릭 시
    document.getElementById('submitNotice').addEventListener('click', async function (event) {
        event.preventDefault();

        const jwtToken = getAuthToken();
        if (!jwtToken) {
            alert('No JWT token found. Please log in first.');
            return;
        }

        const form = document.getElementById('sign_notice');
        const formData = new FormData(form);

        try {
            const response = await authenticatedFetch('/NoticeInsert', {
                method: 'POST',
                body: formData
            });

            if (response.code === 'SU') {
                alert('공지사항이 성공적으로 등록되었습니다.');
                window.location.href = '/NoticeSelect';
            } else {
                alert('공지사항 등록에 실패했습니다.');
            }
        } catch (error) {
            console.error('There was a problem with your fetch operation:', error);
            alert('공지사항 등록 중 오류가 발생했습니다.');
        }
    });
});
