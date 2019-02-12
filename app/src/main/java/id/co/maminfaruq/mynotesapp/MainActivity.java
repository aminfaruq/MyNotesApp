package id.co.maminfaruq.mynotesapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import id.co.maminfaruq.mynotesapp.adapter.NoteAdapter;
import id.co.maminfaruq.mynotesapp.db.NoteHelper;
import id.co.maminfaruq.mynotesapp.entity.Note;

import static id.co.maminfaruq.mynotesapp.NoteAddUpdateActivity.REQUEST_UPDATE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,LoadNotesCallback{

    private RecyclerView rvNotes;
    private ProgressBar progressBar;
    private FloatingActionButton fabAdd;
    private static final String EXTRA_STATE = "EXTRA_STATE";
    private NoteAdapter adapter;
    private NoteHelper noteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Notes");
        }

        rvNotes = findViewById(R.id.rvNotes);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        rvNotes.setHasFixedSize(true);

        noteHelper = NoteHelper.getINSTANCE(getApplicationContext());

        noteHelper.open();

        progressBar = findViewById(R.id.progressBar);
        fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(this);

        adapter = new NoteAdapter(this);
        rvNotes.setAdapter(adapter);

        if (savedInstanceState == null){
            new LoadNotesAsync(noteHelper,this).execute();
        }else {
            ArrayList<Note>list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);

            if (list != null){
                adapter.setListNote(list);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE,adapter.getListNote());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fabAdd){
            Intent intent = new Intent(MainActivity.this,NoteAddUpdateActivity.class);
            startActivityForResult(intent,NoteAddUpdateActivity.REQUEST_ADD);
        }
    }

    @Override
    public void preExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void postExecute(ArrayList<Note> notes) {
        progressBar.setVisibility(View.INVISIBLE);
        adapter.setListNote(notes);
    }

    private static class LoadNotesAsync extends AsyncTask<Void,Void,ArrayList<Note>>{
        private final WeakReference<NoteHelper>weakNoteHelper;
        private final WeakReference<LoadNotesCallback>weakCallBack;

        private LoadNotesAsync(NoteHelper noteHelper,LoadNotesCallback callback) {
            weakNoteHelper = new WeakReference<>(noteHelper);
            weakCallBack = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallBack.get().preExecute();
        }

        @Override
        protected ArrayList<Note> doInBackground(Void... voids) {
            return weakNoteHelper.get().getAllNotes();
        }

        @Override
        protected void onPostExecute(ArrayList<Note> notes) {
            super.onPostExecute(notes);
            weakCallBack.get().postExecute(notes);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null){
            if (requestCode == NoteAddUpdateActivity.REQUEST_ADD){
                if (resultCode == NoteAddUpdateActivity.RESULT_ADD){
                    Note note = data.getParcelableExtra(NoteAddUpdateActivity.EXTRA_NOTE);

                    adapter.addItem(note);
                    rvNotes.smoothScrollToPosition(adapter.getItemCount() - 1);
                    showSnackbarMessage("Satu item berhasil ditambahkan");
                }
            }
            else if (requestCode == REQUEST_UPDATE){
                if (resultCode == NoteAddUpdateActivity.RESULT_UPDATE){
                    Note note = data.getParcelableExtra(NoteAddUpdateActivity.EXTRA_NOTE);
                    int position = data.getIntExtra(NoteAddUpdateActivity.EXTRA_POSITION,0);

                    adapter.updateItem(position,note);
                    rvNotes.smoothScrollToPosition(position);

                    showSnackbarMessage("Satu item berhasil diubah");
                }
                else if (resultCode == NoteAddUpdateActivity.RESULT_DELETE){
                    int position = data.getIntExtra(NoteAddUpdateActivity.EXTRA_POSITION,0);

                    adapter.removeItem(position);
                    showSnackbarMessage("Satu item berhasil dihapus");
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noteHelper.close();
    }

    private void showSnackbarMessage(String message){
        Snackbar.make(rvNotes,message,Snackbar.LENGTH_SHORT).show();
    }
}
