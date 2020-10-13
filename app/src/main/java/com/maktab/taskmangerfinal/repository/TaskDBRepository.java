package com.maktab.taskmangerfinal.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.maktab.taskmangerfinal.database.TaskDBHelper;
import com.maktab.taskmangerfinal.database.TaskDBSchema;
import com.maktab.taskmangerfinal.model.State;
import com.maktab.taskmangerfinal.model.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TaskDBRepository  {


    private static TaskDBRepository sInstance;
    private SQLiteDatabase mDatabase;
    private Context mContext;

    public static TaskDBRepository getInstance(Context context) {
        if (sInstance == null)
            sInstance = new TaskDBRepository(context);

        return sInstance;
    }

    public TaskDBRepository(Context context) {

        mContext = context.getApplicationContext();
        TaskDBHelper taskDBHelper = new TaskDBHelper(mContext);

        mDatabase = taskDBHelper.getWritableDatabase();

    }


    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        TaskCursorWrapper taskCursorWrapper = queryTaskCursor(null, null);
        if (taskCursorWrapper == null || taskCursorWrapper.getCount() == 0)
            return tasks;

        try {

            taskCursorWrapper.moveToFirst();
            while (!taskCursorWrapper.isAfterLast()) {

                Task task = taskCursorWrapper.getTask();
                tasks.add(task);

                taskCursorWrapper.moveToNext();
            }

        } finally {
            taskCursorWrapper.close();
        }

        return tasks;
    }


    public Task getSingleTask(UUID taskId) {
        String where = TaskDBSchema.TaskTable.Cols.UUID + " = ?";
        String[] whereArgs = new String[]{taskId.toString()};

        TaskCursorWrapper taskCursorWrapper = queryTaskCursor(where, whereArgs);

        if (taskCursorWrapper == null || taskCursorWrapper.getCount() == 0)
            return null;

        try {
            taskCursorWrapper.moveToFirst();
            Task task = taskCursorWrapper.getTask();
            return task;
        } finally {
            taskCursorWrapper.close();
        }
    }

    public List<Task> getSearchTask(String item) {
        List<Task> allitemList = new ArrayList<>();
        Cursor cursor;
        cursor= mDatabase.rawQuery("select * from taskTable where TITLE Like '%"+item+"%' ", null);

        if (cursor == null || cursor.getCount() == 0)
            return null;

        while (cursor.moveToNext()) {
            UUID uuidString = UUID.fromString(cursor.getString(cursor.getColumnIndex(TaskDBSchema.TaskTable.Cols.UUID)));
            String title = cursor.getString(cursor.getColumnIndex(TaskDBSchema.TaskTable.Cols.TITLE));
            String description = cursor.getString(cursor.getColumnIndex(TaskDBSchema.TaskTable.Cols.DESCRIPTION));
//            int id = cursor.getInt(cursor.getColumnIndex("ID"));
            State state = State.valueOf(cursor.getString(cursor.getColumnIndex(TaskDBSchema.TaskTable.Cols.STATE)));
           Date timeStampDate = new Date(cursor.getLong(cursor.getColumnIndex(TaskDBSchema.TaskTable.Cols.DATE)));
            Task task= new Task(uuidString, title, description, state,timeStampDate);

            allitemList.add(task);
        }
        return allitemList;




    }




    private TaskCursorWrapper queryTaskCursor(String where, String[] whereArgs) {

        Cursor cursor = mDatabase.query(
                TaskDBSchema.TaskTable.NAME,
                null,
                where,
                whereArgs,
                null,
                null,
                null);

        TaskCursorWrapper taskCursorWrapper = new TaskCursorWrapper(cursor);
        return taskCursorWrapper;
    }


    public void insertTask(Task task) {
        ContentValues values = getContentValues(task);
        mDatabase.insert(TaskDBSchema.TaskTable.NAME, null, values);
    }

    private ContentValues getContentValues(Task task) {
        ContentValues values = new ContentValues();
        values.put(TaskDBSchema.TaskTable.Cols.UUID, task.getTaskID().toString());
        values.put(TaskDBSchema.TaskTable.Cols.TITLE, task.getTaskTitle());
        values.put(TaskDBSchema.TaskTable.Cols.DESCRIPTION, task.getTaskDescription());
        values.put(TaskDBSchema.TaskTable.Cols.STATE, task.getTaskState().toString());
        values.put(TaskDBSchema.TaskTable.Cols.DATE, task.getTaskDate().getTime());
        return values;
    }


    public void updateTask(Task task) {
        ContentValues values = getContentValues(task);
//        String whereClause = Cols.UUID + " = " + task.getTaskID().toString();
        String whereClause = TaskDBSchema.TaskTable.Cols.UUID + " = ?";
        String[] whereArgs = new String[]{task.getTaskID().toString()};
        mDatabase.update(TaskDBSchema.TaskTable.NAME, values, whereClause, whereArgs);
    }


    public void removeSingleTask(Task task) {
        String whereClause = TaskDBSchema.TaskTable.Cols.UUID + " = ?";
        String[] whereArgs = new String[]{task.getTaskID().toString()};
        mDatabase.delete(TaskDBSchema.TaskTable.NAME, whereClause, whereArgs);
    }


    public List<Task> getTasksList(State state) {
        List<Task> tasks = new ArrayList<>();
        String where = TaskDBSchema.TaskTable.Cols.STATE + " = ?";
        String[] whereArgs = new String[]{state.toString()};

        TaskCursorWrapper taskCursorWrapper = queryTaskCursor(where, whereArgs);

        if (taskCursorWrapper == null || taskCursorWrapper.getCount() == 0)
            return tasks;

        try {
            taskCursorWrapper.moveToFirst();
            while (!taskCursorWrapper.isAfterLast()) {
                Task task = taskCursorWrapper.getTask();
                tasks.add(task);
                taskCursorWrapper.moveToNext();
            }

        } finally {
            taskCursorWrapper.close();
        }
        return tasks;
    }


    public void removeTasks() {
        mDatabase.delete(TaskDBSchema.TaskTable.NAME, null, null);
    }


    public void addTaskToDo(Task task) {
        if (task.getTaskState() == State.TODO) {
            ContentValues values = getContentValues(task);
            mDatabase.insert(TaskDBSchema.TaskTable.NAME, null, values);
        }
    }


    public void addTaskDone(Task task) {
        if (task.getTaskState() == State.DONE) {
            ContentValues values = getContentValues(task);
            mDatabase.insert(TaskDBSchema.TaskTable.NAME, null, values);
        }
    }


    public void addTaskDoing(Task task) {
        if (task.getTaskState() == State.DOING) {
            ContentValues values = getContentValues(task);
            mDatabase.insert(TaskDBSchema.TaskTable.NAME, null, values);
        }
    }
}
