curl -X POST http://localhost:8080/payments \
-H "Content-Type: application/json" \
-d '{
"amount": 42.50,
"currency": "USD",
"status": "RECEIVED"
}'