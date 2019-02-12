package id.co.maminfaruq.mynotesapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import id.co.maminfaruq.mynotesapp.entity.Note;

import static android.provider.BaseColumns._ID;
import static id.co.maminfaruq.mynotesapp.db.DatabaseContract.NoteColumns.DATE;
import static id.co.maminfaruq.mynotesapp.db.DatabaseContract.NoteColumns.DESCRIPTION;
import static id.co.maminfaruq.mynotesapp.db.DatabaseContract.NoteColumns.TITLE;
import static id.co.maminfaruq.mynotesapp.db.DatabaseContract.TABLE_NOTE;

public class NoteHelper {
    private static final String DATABASE_NAME = TABLE_NOTE;
    private static DatabaseHelper databaseHelper;
    private static NoteHelper INSTANCE;

    private static SQLiteDatabase database;

    private NoteHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static NoteHelper getINSTANCE(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NoteHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();

        if (database.isOpen())
            database.close();
    }

    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> arrayList = new ArrayList<>();
        Cursor cursor = database.query(TABLE_NOTE, null,
                null,
                null,
                null,
                null,
                _ID + " ASC ",
                null);
        cursor.moveToFirst();
        Note note;
        if (cursor.getCount() > 0) {
            do {
                note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                note.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)));
                note.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));

                arrayList.add(note);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insertNote(Note note) {
        ContentValues values = new ContentValues();
        values.put(TITLE, note.getTitle());
        values.put(DESCRIPTION, note.getDescription());
        values.put(DATE, note.getDate());
        return database.insert(TABLE_NOTE, null, values);
    }

    public int updateNote(Note note) {
        ContentValues values = new ContentValues();
        values.put(TITLE, note.getTitle());
        values.put(DESCRIPTION, note.getDescription());
        values.put(DATE, note.getDate());
        return database.update(TABLE_NOTE, values, _ID + " = '" + note.getId() + "'", null);
    }

    public int deleteNote(int id) {
        return database.delete(TABLE_NOTE, _ID + " = '" + id + "'", null);
    }
}
