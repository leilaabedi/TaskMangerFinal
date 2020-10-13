package com.maktab.taskmangerfinal.repository;


import com.maktab.taskmangerfinal.model.State;
import com.maktab.taskmangerfinal.model.Task;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public interface ITaskRepository extends Serializable {

    List<Task> getTasks();
    Task getSingleTask(UUID taskId);
    void insertTask(Task task);
    void updateTask(Task task);
    void removeSingleTask(Task task);
    void removeTasks();
    List<Task> getTasksList(State state);
    void addTaskToDo(Task task);
    void addTaskDone(Task task);
    void addTaskDoing(Task task);
}
