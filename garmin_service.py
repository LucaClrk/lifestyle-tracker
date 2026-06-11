import os
from flask import Flask, jsonify, request
from garminconnect import Garmin
from datetime import date

app = Flask(__name__)

EMAIL = os.environ.get("GARMIN_EMAIL", "")
PASSWORD = os.environ.get("GARMIN_PASSWORD", "")
TOKENSTORE = os.environ.get("GARMIN_TOKENS", "/garmin-tokens")

_client = None


def get_client():
    global _client
    if _client is not None:
        return _client

    api = Garmin()
    try:
        api.login(TOKENSTORE)
        _client = api
    except Exception:
        api = Garmin(email=EMAIL, password=PASSWORD)
        api.login()
        try:
            os.makedirs(TOKENSTORE, exist_ok=True)
            api.garth.dump(TOKENSTORE)
        except Exception:
            pass
        _client = api
    return _client


@app.route("/garmin/activities")
def get_activities():
    limit = request.args.get("limit", 10, type=int)
    try:
        activities = get_client().get_activities(0, limit)
        return jsonify(activities)
    except Exception as e:
        _reset_client()
        return jsonify({"error": str(e)}), 503


@app.route("/garmin/stats")
def get_stats():
    today = date.today().isoformat()
    try:
        stats = get_client().get_stats(today)
        return jsonify(stats)
    except Exception as e:
        _reset_client()
        return jsonify({"error": str(e)}), 503


@app.route("/garmin/sleep")
def get_sleep():
    today = date.today().isoformat()
    try:
        sleep = get_client().get_sleep_data(today)
        return jsonify(sleep)
    except Exception as e:
        _reset_client()
        return jsonify({"error": str(e)}), 503


@app.route("/garmin/heartrate")
def get_heartrate():
    today = date.today().isoformat()
    try:
        hr = get_client().get_heart_rates(today)
        return jsonify(hr)
    except Exception as e:
        _reset_client()
        return jsonify({"error": str(e)}), 503


@app.route("/health")
def health():
    return jsonify({"status": "ok"})


def _reset_client():
    global _client
    _client = None


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
