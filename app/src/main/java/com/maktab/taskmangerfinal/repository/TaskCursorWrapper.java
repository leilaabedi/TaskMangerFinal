package com.maktab.taskmangerfinal.repository;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.maktab.taskmangerfinal.database.TaskDBSchema;
import com.maktab.taskmangerfinal.model.State;
import com.maktab.taskmangerfinal.model.Task;

import java.util.Date;
import java.util.UUID;

public class TaskCursorWrapper extends CursorWrapper {

    public TaskCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Task getTask(){

        UUID uuidString = UUID.fromString(getString(getColumnIndex(TaskDBSchema.TaskTable.Cols.UUID)));
        String title = getString(getColumnIndex(TaskDBSchema.TaskTable.Cols.TITLE));
        String description = getString(getColumnIndex(TaskDBSchema.TaskTable.Cols.DESCRIPTION));
        State state = State.valueOf(getString(getColumnIndex(TaskDBSchema.TaskTable.Cols.STATE)));
        Date timeStampDate = new Date(getLong(getColumnIndex(TaskDBSchema.TaskTable.Cols.DATE)));

        return new Task(uuidString, title, description, state, timeStampDate);
    }
}
