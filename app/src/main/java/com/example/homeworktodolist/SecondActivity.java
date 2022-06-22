package com.example.homeworktodolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class SecondActivity extends AppCompatActivity {

    private Integer TimeClick = 0;
    private ListView noteListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initWidgets();
        loadFromDBToMemory();
        setNoteAdapter();
        setOnClickListener();
    }

    @Override
    protected void onStart(){
        super.onStart();
        loadFromDBToMemory();
        TimeClick = 0;
    }

    public void goToTodoPage(View view) {
        if(TimeClick == 0) {
            TimeClick = 1;
            Intent TodoPage = new Intent(this, MainActivity.class);
            startActivity(TodoPage);
        }else {
            return;
        }
    }

    private void loadFromDBToMemory() {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.populateNoteListArrayDONE();
    }

    private void setNoteAdapter() {
        NoteAdapter noteAdapter = new NoteAdapter(getApplicationContext(), Note.nonDeletedNotes());
        noteListView.setAdapter(noteAdapter);
    }

    private void initWidgets()
    {
        noteListView = findViewById(R.id.noteListView);
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
    @Override
    protected void onResume()
    {
        super.onResume();
        setNoteAdapter();
        TimeClick = 0;
    }
}