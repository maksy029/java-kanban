package ru.practicum.task_tracker.tasks;

public class Subtask extends Task {
    private Long epicId;


    public Subtask(String name, String desc, String status, Long epicId) {
        super(name, desc, status);
        this.epicId = epicId;
    }

    public Subtask(Long id, String name, String desc, String status, Long epicId) {
        super(name, desc, status);
        this.epicId = epicId;
    }

    public Long getEpicId() {
        return epicId;
    }

    public void setEpicId(Long epicId) {
        this.epicId = epicId;
        
    }
}



