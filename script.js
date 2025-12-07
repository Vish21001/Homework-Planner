let tasks = JSON.parse(localStorage.getItem("tasks")) || [];

const taskForm = document.getElementById("taskForm");
const taskTableBody = document.querySelector("#taskTable tbody");

function saveTasks() {
    localStorage.setItem("tasks", JSON.stringify(tasks));
}

function renderTasks() {
    taskTableBody.innerHTML = "";
    tasks.forEach((task, index) => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${task.subject}</td>
            <td>${task.description}</td>
            <td>${task.dueDate}</td>
            <td>${task.priority}</td>
            <td><input type="checkbox" ${task.completed ? "checked" : ""} onchange="toggleComplete(${index})"></td>
            <td><button onclick="deleteTask(${index})">Delete</button></td>
        `;
        if (task.completed) row.classList.add("completed");
        taskTableBody.appendChild(row);
    });
}

function toggleComplete(index) {
    tasks[index].completed = !tasks[index].completed;
    saveTasks();
    renderTasks();
}

function deleteTask(index) {
    tasks.splice(index, 1);
    saveTasks();
    renderTasks();
}

taskForm.addEventListener("submit", (e) => {
    e.preventDefault();
    const task = {
        subject: document.getElementById("subject").value,
        description: document.getElementById("description").value,
        dueDate: document.getElementById("dueDate").value,
        priority: document.getElementById("priority").value,
        completed: false
    };
    tasks.push(task);
    saveTasks();
    renderTasks();
    taskForm.reset();
});

// Initial render
renderTasks();
