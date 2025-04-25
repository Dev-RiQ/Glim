window.onload = () => {
    const container = document.getElementById("subscription-info");
    const customerUid = "user_001"; // 실제 사용자 ID에 맞게 수정

    // 포트원 초기화
    IMP.init("imp77335438");

    // 구독 상태 조회
    fetch(`http://localhost:8081/api/v1/billing/info?customerUid=${customerUid}`)
        .then((res) => res.json())
        .then((data) => {
            const {billingStartDate, cardName } = data;
            const billingDay = new Date(billingStartDate).getDate();
            const today = new Date().getDate();

            const stillActive = data.active || (billingDay > today);

            if (stillActive) {
                container.innerHTML = `
                    <p>✅ 현재 구독 중입니다.</p>
                    <p>💳 카드사: ${cardName}</p>
                    <p>🗓️ 매달 ${billingDay}일 자동 결제</p>
                    <button onclick="cancelSubscription()">정기결제 해지하기</button>
                `;
            } else {
                container.innerHTML = `
                    <p>❌ 현재 구독 중이 아닙니다.</p>
                    <button id="register-btn">카드 등록하기</button>
                `;

                const btn = document.getElementById("register-btn");
                btn.addEventListener("click", () => {
                    IMP.request_pay({
                        pg: "tosspayments.iamporttest_4",
                        pay_method: "card",
                        customer_uid: customerUid,
                        name: "정기결제 카드 등록",
                        amount: 100,
                        buyer_email: "test@test.com",
                        buyer_name: "홍길동",
                        buyer_tel: "01012345678"
                    }, function (rsp) {
                        if (rsp.success) {
                            fetch(`https://localhost:8081/api/v1/billing/register?impUid=${rsp.imp_uid}&customerUid=${rsp.customer_uid}`, {
                                method: "POST"
                            })
                                .then(res => res.json())
                                .then(data => {
                                    alert("카드 등록 성공: " + JSON.stringify(data));
                                    location.reload();
                                });
                        } else {
                            alert("카드 등록 실패: " + rsp.error_msg);
                        }
                    });
                });
            }
        })
        .catch((err) => {
            container.innerHTML = `<p style="color:red;">불러오기 오류: ${err.message}</p>`;
        });
};

// 정기결제 해지 함수
function cancelSubscription() {
    const customerUid = "user_001";
    fetch(`https://localhost:8081/api/v1/billing/cancel?customerUid=${customerUid}`, {
        method: "POST"
    })
        .then(res => res.text())
        .then(msg => {
            alert("✅ " + msg);
            location.reload();
        });
}
