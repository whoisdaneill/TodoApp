package com.example.myapplication.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.myapplication.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "toDoListDatabase";
    private static final String TODO_TABLE1 = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String DESC = "descr";
    private static final String DATE="date";

    private static final String STATUS = "status";

    private static final String CREATE_TODO_TABLE="CREATE TABLE todo(id INTEGER PRIMARY KEY AUTOINCREMENT, task text, descr text, date text, status integer)";
    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE1);
        // Create tables again
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertTask(ToDoModel task){
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask());
        cv.put(DESC,task.getDesc());
        cv.put(DATE,task.getDate());
        cv.put(STATUS, 0);
        db.insert(TODO_TABLE1, null, cv);
    }

    public List<ToDoModel> getAllTasks(){
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(TODO_TABLE1, null, null, null, null, null, null, null) ;
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        ToDoModel task = new ToDoModel();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setDesc(cur.getString(cur.getColumnIndex(DESC)));
                        task.setDate(cur.getString(cur.getColumnIndex(DATE)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        taskList.add(task);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskList;
    }

    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE1, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TODO_TABLE1, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }
    public void updateDate(int id, String date) {
        ContentValues cv = new ContentValues();
        cv.put(DATE, date);
        db.update(TODO_TABLE1, cv, ID + "= ?", new String[] {String.valueOf(id)});

    }
    public void updateDesc(int id, String desc) {
        ContentValues cv = new ContentValues();
        cv.put(DESC, desc);
        db.update(TODO_TABLE1, cv, ID + "= ?", new String[]{String.valueOf(id)});
    }
    public void deleteTask(int id){
        db.delete(TODO_TABLE1, ID + "= ?", new String[] {String.valueOf(id)});
    }
}
