package com.maktab.taskmangerfinal.repository;

import android.database.Cursor;
import android.database.CursorWrapper;


import com.maktab.taskmangerfinal.database.TaskDBSchema;
import com.maktab.taskmangerfinal.model.User;

import java.util.UUID;

public class UserCursorWrapper extends CursorWrapper {

    public UserCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public User getUser(){

        UUID uuidString = UUID.fromString(getString(getColumnIndex(TaskDBSchema.UserTable.Cols.UUID)));
        String userName = getString(getColumnIndex(TaskDBSchema.UserTable.Cols.USERNAME));
        String userPassword = getString(getColumnIndex(TaskDBSchema.UserTable.Cols.PASSWORD));

        return new User(uuidString, userName, userPassword);
    }
}