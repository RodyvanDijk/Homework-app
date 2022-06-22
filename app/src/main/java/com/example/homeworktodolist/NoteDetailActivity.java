package com.example.homeworktodolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class NoteDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText titleEditText, descEditText, subEditText;
    private Spinner statEditText;
    private DatePicker deadlEditText;
    private DatePicker owndeadlEditText;
    private Button deleteButton, setNoteDoneButton, setNoteTodoButton;
    private Note selectedNote;
    private String deadlineDateString;
    private String owndeadlineDateString;
    private Note getSelectedNote;
    private Integer TimeClick = 0;

    String[] courses = { "TODO", "DONE" };

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        initWidgets();
        checkForEditNote();

            Spinner spino = findViewById(R.id.statusEditText);
            spino.setOnItemSelectedListener(this);

            ArrayAdapter<String> ad
                    = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_spinner_item,
                    courses);

            ad.setDropDownViewResource(
                    android.R.layout
                            .simple_spinner_dropdown_item);

            spino.setAdapter(ad);

            if (selectedNote != null) {
                if (selectedNote.getStatus() == false) {
                    statEditText.setSelection(ad.getPosition("TODO"));
                } else if (selectedNote.getStatus() == true) {
                    statEditText.setSelection(ad.getPosition("DONE"));
                } else {
                    statEditText.setSelection(ad.getPosition("TODO"));
                }
            }
    }

    @Override
    protected void onStart(){
        super.onStart();
        TimeClick = 0;
    }

    private void updateDatePicker(){
        String dateDeadl = String.valueOf(selectedNote.getDeadline());
        String [] dateParts = dateDeadl.split("-");
        String yearString = dateParts[0];
        String monthString = dateParts[1];
        String dayString = dateParts[2];
        int year = Integer.parseInt(yearString);
        int month = Integer.parseInt(monthString)- 1;
        int day = Integer.parseInt(dayString);

        String dateOwnDeadl = String.valueOf(selectedNote.getOwnDeadline());
        String [] datePartsOD = dateOwnDeadl.split("-");
        String ownYearString = datePartsOD[0];
        String ownMonthString = datePartsOD[1];
        String ownDayString = datePartsOD[2];
        int ownyear = Integer.parseInt(ownYearString);
        int ownmonth = Integer.parseInt(ownMonthString)- 1;
        int ownday = Integer.parseInt(ownDayString);

        deadlEditText.updateDate(year, month, day);
        owndeadlEditText.updateDate(ownyear, ownmonth, ownday);
    }

    private void initWidgets() {
        titleEditText = findViewById(R.id.titleEditText);
        descEditText = findViewById(R.id.descriptionEditText);
        statEditText = findViewById(R.id.statusEditText);
        setNoteDoneButton = findViewById(R.id.setNoteDoneButton);
        setNoteTodoButton = findViewById(R.id.setNoteTodoButton);
        subEditText = findViewById(R.id.subjectEditText);
        deadlEditText = findViewById(R.id.deadlineEditText);
        owndeadlEditText = findViewById(R.id.owndeadlineEditText);
        deleteButton = findViewById(R.id.deleteNoteButton);
    }

    private void checkForEditNote() {
        Intent previousIntent = getIntent();

        int passedNoteID = previousIntent.getIntExtra(Note.NOTE_EDIT_EXTRA, -1);
        selectedNote = Note.getNoteForID(passedNoteID);

        if (selectedNote != null) {
            updateDatePicker();
            titleEditText.setText(selectedNote.getTitle());
            descEditText.setText(selectedNote.getDescription());
            statEditText.setSelected(selectedNote.getStatus());
            subEditText.setText(selectedNote.getSubject());
            int day = deadlEditText.getDayOfMonth();
            int month = deadlEditText.getMonth();
            int year = deadlEditText.getYear();
            int ownday = owndeadlEditText.getDayOfMonth();
            int ownmonth = owndeadlEditText.getMonth();
            int ownyear = owndeadlEditText.getYear();

            if(day < 10 && month < 10){
                deadlineDateString = year + "-" + "0" + month + "-" + "0" + day;
            } else if(day < 10 && month > 10){
                deadlineDateString = year + "-" + month + "-" + "0" + day;
            } else if(day > 10 && month < 10){
                deadlineDateString = year + "-" + "0" + month + "-" + day;
            }else {
                deadlineDateString = year + "-" + month + "-" + day;
            }

            if(ownday < 10 && ownmonth < 10){
                owndeadlineDateString = ownyear + "-" + "0" + ownmonth + "-" + "0" + ownday;
            } else if(ownday < 10 && ownmonth > 10){
                owndeadlineDateString = ownyear + "-" + ownmonth + "-" + "0" + ownday;
            } else if(ownday > 10 && ownmonth < 10){
                owndeadlineDateString = ownyear + "-" + "0" + ownmonth + "-" + ownday;
            }else {
                owndeadlineDateString = ownyear + "-" + ownmonth + "-" + ownday;
            }
            statEditText.setVisibility(View.GONE);
            if(selectedNote.getStatus()) {
                setNoteDoneButton.setVisibility(View.GONE);
            }else {
                setNoteTodoButton.setVisibility(View.GONE);
            }
        } else {
            deleteButton.setVisibility(View.GONE);
            setNoteTodoButton.setVisibility(View.GONE);
            setNoteDoneButton.setVisibility(View.GONE);

        }
    }

    public void saveNote(View view) {
            Log.d("test", "saveNote");
        if(TimeClick == 0) {
            TimeClick = 1;
        if (TextUtils.isEmpty(titleEditText.getText()) || TextUtils.isEmpty(descEditText.getText())) {
            if (TextUtils.isEmpty(titleEditText.getText())) {
                titleEditText.setError("Je moet een titel in vullen");
                TimeClick = 0;
            }
            if (TextUtils.isEmpty(descEditText.getText())) {
                descEditText.setError("Je moet descriptie in vullen");
                TimeClick = 0;
            }
        }else {
            Log.d("test", "good");
                int day = deadlEditText.getDayOfMonth();
                int month = deadlEditText.getMonth() + 1;
                int year = deadlEditText.getYear();
                int ownday = owndeadlEditText.getDayOfMonth();
                int ownmonth = owndeadlEditText.getMonth() + 1;
                int ownyear = owndeadlEditText.getYear();

                if(day < 10 && month < 10){
                    deadlineDateString = year + "-" + "0" + month + "-" + "0" + day;
                } else if(day < 10 && month > 10){
                    deadlineDateString = year + "-" + month + "-" + "0" + day;
                } else if(day > 10 && month < 10){
                    deadlineDateString = year + "-" + "0" + month + "-" + day;
                }else {
                    deadlineDateString = year + "-" + month + "-" + day;
                }

                if(ownday < 10 && ownmonth < 10){
                    owndeadlineDateString = ownyear + "-" + "0" + ownmonth + "-" + "0" + ownday;
                } else if(ownday < 10 && ownmonth > 10){
                    owndeadlineDateString = ownyear + "-" + ownmonth + "-" + "0" + ownday;
                } else if(ownday > 10 && ownmonth < 10){
                    owndeadlineDateString = ownyear + "-" + "0" + ownmonth + "-" + ownday;
                }else {
                    owndeadlineDateString = ownyear + "-" + ownmonth + "-" + ownday;
                }

                SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
                String title = String.valueOf(titleEditText.getText());
                String desc = String.valueOf(descEditText.getText());
                String stat = statEditText.getSelectedItem().toString();
                String sub = String.valueOf(subEditText.getText());
                String deadl = deadlineDateString;
                String owndeadl = owndeadlineDateString;

                if (selectedNote == null) {
                    int id = Note.noteArrayList.size();
                    Note newNote = new Note(id, title, desc, stat, sub, deadl, owndeadl);
                    Note.noteArrayList.add(newNote);
                    sqLiteManager.addNoteToDatabase(newNote);
                    Log.d("test", "new");
                } else {
                    selectedNote.setTitle(title);
                    selectedNote.setDescription(desc);
                    selectedNote.setSubject(sub);
                    selectedNote.setDeadline(deadl);
                    selectedNote.setOwnDeadline(owndeadl);
                    sqLiteManager.updateNoteInDB(selectedNote);
                    Log.d("test", "update");
                }
                finish();
            }
        }else {
            Log.d("test", "bad");
            return;
        }
        }

    public void deleteNote(View view) {
        selectedNote.setDeleted(new Date());
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.deleteNoteFromDB(selectedNote);
        finish();
        }
    public void setNoteDone(View view){
            selectedNote.setStatusButton("DONE");
            SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
            sqLiteManager.updateNoteInDB(selectedNote);
            Log.d("notedone", String.valueOf(selectedNote.getStatus()));
            finish();
    }
    public void setNoteTodo(View view){
        selectedNote.setStatusButton("TODO");
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.updateNoteInDB(selectedNote);
        Log.d("notedone", String.valueOf(selectedNote.getStatus()));
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}