package com.example.homeworktodolist;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

public class Note
{
     public static ArrayList<Note> noteArrayList = new ArrayList<>();
     public static String NOTE_EDIT_EXTRA = "noteEdit";

    private int id;
    private String title;
    private String description;
    private String status;
    private String subject;
    private String deadline;
    private String owndeadline;
    private Date deleted;


    public Note(int id, String title, String description, String status, String subject,  String deadline, String owndeadline, Date deleted) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.subject = subject;
        this.deadline = deadline;
        this.owndeadline = owndeadline;
        this.deleted = deleted;
    }

    public Note(int id, String title, String description, String status, String subject, String deadline, String owndeadline) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.subject = subject;
        this.deadline = deadline;
        this.owndeadline = owndeadline;
        deleted = null;
    }

    public static Note getNoteForID(int passedNoteID)
    {
        for (Note note : noteArrayList) {
            if (note.getId() == passedNoteID)
                return note;
        }
        return null;
    }

    public static ArrayList<Note> nonDeletedNotes()
    {
        ArrayList<Note> nonDeleted = new ArrayList<>();
        for (Note note : noteArrayList)
        {
            if (note.getDeleted() == null)
                nonDeleted.add(note);
        }

        return nonDeleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean getStatus() {
            Log.d("donetodo", String.valueOf(status));
            boolean status1;
            if (status.equals("TODO")) {
                status1 = false;
            } else if (status.equals("DONE")) {
                status1 = true;
            } else {
                status1 = false;
            }
            return status1;
    }

    public void setStatus(String status) {
        if (status.equals("TODO")) {
            this.status = "0";
        } else if (status.equals("DONE")) {
            this.status = "1";
        }
    }
        public void setStatusButton(String status) {
            this.status = status;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getOwnDeadline() {
        return owndeadline;
    }

    public void setOwnDeadline(String owndeadline) {
        this.owndeadline = owndeadline;
    }

    public Date getDeleted() {
        return deleted;
    }

    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    }
}
