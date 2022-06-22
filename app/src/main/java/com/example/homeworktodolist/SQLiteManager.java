package com.example.homeworktodolist;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SQLiteManager extends SQLiteOpenHelper
{
    private static SQLiteManager sqLiteManager;

    private static final String DATABASE_NAME = "HomeworkDB5";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Homework";

    private static final String ID_FIELD = "id";
    private static final String TITLE_FIELD = "title";
    private static final String DESC_FIELD = "desc";
    private static final String STAT_FIELD = "stat";
    private static final String SUB_FIELD = "sub";
    private static final String DEADL_FIELD = "deadl";
    private static final String OWNDEADL_FIELD = "owndeadl";

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

    public SQLiteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteManager instanceOfDatabase(Context context)
    {
        if(sqLiteManager == null)
            sqLiteManager = new SQLiteManager(context);

        return sqLiteManager;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
            StringBuilder sql;
            sql = new StringBuilder()
                    .append("CREATE TABLE ")
                    .append(TABLE_NAME)
                    .append("(")
                    .append(ID_FIELD)
                    .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                    .append(TITLE_FIELD)
                    .append(" TEXT, ")
                    .append(DESC_FIELD)
                    .append(" TEXT, ")
                    .append(STAT_FIELD)
                    .append(" TEXT, ")
                    .append(SUB_FIELD)
                    .append(" TEXT, ")
                    .append(DEADL_FIELD)
                    .append(" TEXT, ")
                    .append(OWNDEADL_FIELD)
                    .append(" TEXT)");

            sqLiteDatabase.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
//        switch (oldVersion)
//        {
//            case 1:
//                sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + NEW_COLUMN + " TEXT");
//        }
    }

    public void addNoteToDatabase(Note note)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE_FIELD, note.getTitle());
        contentValues.put(DESC_FIELD, note.getDescription());
        contentValues.put(STAT_FIELD, note.getStatus());
        contentValues.put(SUB_FIELD, note.getSubject());
        contentValues.put(DEADL_FIELD, note.getDeadline());
        contentValues.put(OWNDEADL_FIELD, note.getOwnDeadline());

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }

    public void populateNoteListArrayTODO()
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String stat;

        Note.noteArrayList.clear();
        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE stat = '0' ORDER BY " + OWNDEADL_FIELD + ", " + DEADL_FIELD, null))
        {
            if(result.getCount() != 0)
            {
                while (result.moveToNext())
                {
                    int id = result.getInt(0);
                    String title = result.getString(1);
                    String desc = result.getString(2);
                    if(result.getString(3).equals("0")){
                        stat = "TODO";
                    }else if (result.getString(3).equals("1")){
                        stat = "DONE";
                    }else {
                        stat = "TODO";
                    }
                    String sub = result.getString(4);
                    String deadl = result.getString(5);
                    String owndeadl = result.getString(6);
                    Note note = new Note(id,title,desc,stat,sub,deadl,owndeadl);
                    Note.noteArrayList.add(note);
                }
            }
        }
    }
    public void populateNoteListArrayDONE()
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String stat;

        Note.noteArrayList.clear();
        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE stat = '1' ORDER BY " + OWNDEADL_FIELD + ", " + DEADL_FIELD, null))
        {
            if(result.getCount() != 0)
            {
                while (result.moveToNext())
                {
                    int id = result.getInt(0);
                    String title = result.getString(1);
                    String desc = result.getString(2);
                    if(result.getString(3).equals("0")){
                        stat = "TODO";
                    }else if (result.getString(3).equals("1")){
                        stat = "DONE";
                    }else {
                        stat = "TODO";
                    }
                    Log.d("note", stat);
                    String sub = result.getString(4);
                    String deadl = result.getString(5);
                    String owndeadl = result.getString(6);
                    Note note = new Note(id,title,desc,stat,sub,deadl,owndeadl);
                    Note.noteArrayList.add(note);
                }
            }
        }
    }

    public void updateNoteInDB(Note note)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE_FIELD, note.getTitle());
        contentValues.put(DESC_FIELD, note.getDescription());
        contentValues.put(STAT_FIELD, note.getStatus());
        contentValues.put(SUB_FIELD, note.getSubject());
        contentValues.put(DEADL_FIELD, note.getDeadline());
        contentValues.put(OWNDEADL_FIELD, note.getOwnDeadline());

        sqLiteDatabase.update(TABLE_NAME, contentValues, ID_FIELD + " =? ", new String[]{String.valueOf(note.getId())});
    }

    public void deleteNoteFromDB(Note note) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME, ID_FIELD + " =? ", new String[]{String.valueOf(note.getId())});
    }

    private String getStringFromDate(Date date)
    {
        if(date == null)
            return null;
        return dateFormat.format(date);
    }
    private Date getDateFromString(String string)
    {
        try
        {
            return dateFormat.parse(string);
        }
        catch (ParseException | NullPointerException e)
        {
            return null;
        }
    }
}
