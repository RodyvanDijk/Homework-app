package com.example.homeworktodolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
{
    private Integer TimeClick = 0;
    private ListView noteListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        loadFromDBToMemory();
        setNoteAdapter();
        setOnClickListener();
        TimeClick = 0;
    }

    @Override
    protected void onStart(){
        super.onStart();
        loadFromDBToMemory();
        TimeClick = 0;
    }

    private void initWidgets()
    {
            noteListView = findViewById(R.id.noteListView);
    }

    private void loadFromDBToMemory() {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.populateNoteListArrayTODO();
    }

    private void setNoteAdapter() {
        NoteAdapter noteAdapter = new NoteAdapter(getApplicationContext(), Note.nonDeletedNotes());
        noteListView.setAdapter(noteAdapter);
    }

    private void setOnClickListener()
    {
        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                if(TimeClick == 0) {
                    TimeClick = 1;
                Note selectedNote = (Note) noteListView.getItemAtPosition(position);
                Intent editNoteIntent = new Intent(getApplicationContext(), NoteDetailActivity.class);
                editNoteIntent.putExtra(Note.NOTE_EDIT_EXTRA, selectedNote.getId());
                startActivity(editNoteIntent);
                }else {
                    return;
                }
            }
        });
    }

    public void newNote(View view) {
        if(TimeClick == 0) {
            TimeClick = 1;
            Intent newNoteIntent = new Intent(this, NoteDetailActivity.class);
            startActivity(newNoteIntent);
        }else {
            return;
        }
    }

    public void goToDonePage(View view) {
        if(TimeClick == 0) {
            TimeClick = 1;
        Intent DonePage = new Intent(this, SecondActivity.class);
        startActivity(DonePage);
        }else {
            return;
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setNoteAdapter();
        TimeClick = 0;
    }

}