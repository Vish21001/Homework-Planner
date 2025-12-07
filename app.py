rom flask import Flask, render_template, request, jsonify
import json
import os

app = Flask(__name__)
TASKS_FILE = "tasks.json"

# Load tasks
if os.path.exists(TASKS_FILE):
    with open(TASKS_FILE, "r") as f:
        tasks = json.load(f)
else:
    tasks = []

def save_tasks():
    with open(TASKS_FILE, "w") as f:
        json.dump(tasks, f, indent=4)

@app.route("/")
def index():
    return render_template("index.html")

@app.route("/tasks", methods=["GET", "POST", "PUT", "DELETE"])
def handle_tasks():
    global tasks
    if request.method == "GET":
        return jsonify(tasks)
    data = request.get_json()
    if request.method == "POST":
        tasks.append(data)
    elif request.method == "PUT":
        idx = data["index"]
        tasks[idx]["completed"] = data["completed"]
    elif request.method == "DELETE":
        idx = data["index"]
        tasks.pop(idx)
    save_tasks()
    return jsonify({"status": "success"})

if __name__ == "__main__":
    app.run(debug=True)
