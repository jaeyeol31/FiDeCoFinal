    document.addEventListener('DOMContentLoaded', function() {
        const token = getCookie('jwtToken');
        if (!token) {
            alert('로그인이 필요합니다.');
            window.location.href = '/log-in';
            return;
        }

        const decodedToken = parseJwt(token);
        const memberId = decodedToken.id;

        fetch(`/cart/${memberId}`, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('장바구니를 불러오는 데 문제가 발생했습니다.');
            }
        })
        .then(data => {
            const cartItemsContainer = document.getElementById('cart-items');
            let totalPrice = 0;

            data.cartItems.forEach(item => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td><input type="checkbox" class="select-item" /></td>
                    <td>${item.productId}</td>
                    <td>${item.quantity}</td>
                `;
                cartItemsContainer.appendChild(row);
                totalPrice += item.quantity * item.price; // 적절히 조정
            });

            document.getElementById('total-price').textContent = totalPrice.toLocaleString();
        })
         .catch(error => {
            console.error('Error:', error);
            alert('장바구니를 불러오는 데 문제가 발생했습니다.');
            window.location.href = '/log-in';
        }); 

        function getCookie(name) {
            const value = `; ${document.cookie}`;
            const parts = value.split(`; ${name}=`);
            if (parts.length === 2) return parts.pop().split(';').shift();
            return null;
        }

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
    });