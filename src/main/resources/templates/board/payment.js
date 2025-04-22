function requestPayment() {
    const errorDiv = document.getElementById('error-message');
    const retryButton = document.getElementById('retry-button');
    errorDiv.style.display = 'none';
    retryButton.style.display = 'none';

    IMP.init('imp12345678'); // 가맹점 식별코드
    IMP.request_pay({
        pg: 'mobilians', // 모빌리언스 PG사
        pay_method: 'phone', // 휴대폰 소액결제
        merchant_uid: 'order_' + new Date().getTime(), // 고유 주문 번호
        name: 'K-POP 앨범', // 상품명
        amount: 15000, // 결제 금액
        buyer_email: 'fan@example.com',
        buyer_name: '팬덤',
        buyer_tel: '010-1234-5678' // 필수
    }, function (response) {
        if (response.success) {
            fetch('/api/payment/verify', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    imp_uid: response.imp_uid,
                    merchant_uid: response.merchant_uid
                })
            })
                .then(res => res.json())
                .then(data => {
                    if (data.message === '결제 성공') {
                        alert('결제 완료! 주문이 성공적으로 처리되었습니다.');
                        window.location.href = '/order-success'; // 성공 페이지로 리다이렉트
                    } else {
                        errorDiv.textContent = data.message;
                        errorDiv.style.display = 'block';
                        retryButton.style.display = 'inline-block';
                    }
                })
                .catch(err => {
                    errorDiv.textContent = '서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.';
                    errorDiv.style.display = 'block';
                    retryButton.style.display = 'inline-block';
                });
        } else {
            let errorMsg = response.error_msg;
            if (errorMsg.includes('한도')) {
                errorMsg = '휴대폰 결제 한도를 초과했습니다. 다른 결제 수단을 선택해주세요.';
            } else if (errorMsg.includes('전화번호')) {
                errorMsg = '유효한 전화번호를 입력해주세요.';
            }
            errorDiv.textContent = `결제 실패: ${errorMsg}`;
            errorDiv.style.display = 'block';
            retryButton.style.display = 'inline-block';
        }
    });
}