const express = require("express")
const bodyParser = require("body-parser")
const PortOne = require("@portone/server-sdk")

const portone = PortOne.PortOneClient({ secret: process.env.V2_API_SECRET })

const app = express()

app.use(
    "/api/payment/webhook",
    bodyParser.text({
        type: "application/json",
    }),
)
app.use(bodyParser.json())

const items = new Map([
    [
        "shoes",
        {
            name: "신발",
            price: 1000,
            currency: "KRW",
        },
    ],
])

app.get("/api/item", (req, res) => {
    const id = "shoes"
    res.json({
        id,
        ...items.get(id),
    })
})

app.post("/api/payment/complete", async (req, res, next) => {
    try {
        const { paymentId } = req.body
        if (typeof paymentId !== "string")
            return res.status(400).send("올바르지 않은 요청입니다.").end()
        const payment = await syncPayment(paymentId)
        if (!payment) return res.status(400).send("결제 동기화에 실패했습니다.")
        res.status(200).json({
            status: payment.status,
        })
    } catch (e) {
        next(e)
    }
})

const paymentStore = new Map()
async function syncPayment(paymentId) {
    if (!paymentStore.has(paymentId)) {
        paymentStore.set(paymentId, {
            status: "PENDING",
        })
    }
    const payment = paymentStore.get(paymentId)
    let actualPayment
    try {
        actualPayment = await portone.payment.getPayment({ paymentId })
    } catch (e) {
        if (e instanceof PortOne.Errors.PortOneError) return false
        throw e
    }
    if (actualPayment.status === "PAID") {
        if (!verifyPayment(actualPayment)) return false
        if (payment.status === "PAID") return payment
        payment.status = "PAID"
        console.info("결제 성공", actualPayment)
    } else {
        return false
    }
    return payment
}

function verifyPayment(payment) {
    if (payment.customData == null) return false
    const customData = JSON.parse(payment.customData)
    const item = items.get(customData.item)
    if (item == null) return false
    return (
        payment.orderName === item.name &&
        payment.amount.total === item.price &&
        payment.currency === item.currency
    )
}

app.post("/api/payment/webhook", async (req, res, next) => {
    try {
        let webhook
        try {
            webhook = await PortOne.Webhook.verify(
                process.env.V2_WEBHOOK_SECRET,
                req.body,
                req.headers,
            )
        } catch (e) {
            if (e instanceof PortOne.Webhook.WebhookVerificationError)
                return res.status(400).end()
            throw e
        }
        if ('data' in webhook && 'paymentId' in webhook.data)
            await syncPayment(webhook.data.paymentId)
        res.status(200).end()
    } catch (e) {
        next(e)
    }
})

const server = app.listen(8081, "localhost", () => {
    console.log("server is running on", server.address())
})