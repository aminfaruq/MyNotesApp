package id.co.maminfaruq.mynotesapp;

import java.util.ArrayList;

import id.co.maminfaruq.mynotesapp.entity.Note;

public interface LoadNotesCallback {
    void preExecute();
    void postExecute(ArrayList<Note>notes);
}
