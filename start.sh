#!/bin/bash
set -e

echo "Starting Garmin service..."
/opt/venv/bin/python3 /garmin_service.py &

echo "Waiting for Garmin service to be ready..."
for i in $(seq 1 15); do
    if curl -sf http://localhost:5000/health > /dev/null 2>&1; then
        echo "Garmin service is ready."
        break
    fi
    sleep 1
done

echo "Starting Spring Boot..."
exec java -jar /app.jar
