curl -X POST hhtp://localhost:8088/payments \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 42.50,
    "currency": "USD",
    "status": "RECEIVED"
    }'