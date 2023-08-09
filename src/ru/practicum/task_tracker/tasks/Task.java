package ru.practicum.task_tracker.tasks;

public class Task {
    protected Long id;
    protected String name;
    protected String desc;
    protected String status;

    public Task(String name, String desc, String status) {
        this.name = name;
        this.desc = desc;
        this.status = status;
    }

    public Task(Long id, String name, String desc, String status) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
