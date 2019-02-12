package id.co.maminfaruq.mynotesapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import id.co.maminfaruq.mynotesapp.CustomOnItemClickListener;
import id.co.maminfaruq.mynotesapp.NoteAddUpdateActivity;
import id.co.maminfaruq.mynotesapp.R;
import id.co.maminfaruq.mynotesapp.entity.Note;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private ArrayList<Note>listNote = new ArrayList<>();
    private Activity activity;

    public NoteAdapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<Note>getListNote(){
        return listNote;
    }

    public void setListNote(ArrayList<Note>listNote){

        if (listNote.size() > 0){
            this.listNote.clear();
        }
        this.listNote.addAll(listNote);

        notifyDataSetChanged();
    }

    public void addItem(Note note){
        this.listNote.add(note);
        notifyItemInserted(listNote.size() - 1);
    }

    public void updateItem(int position,Note note){
        this.listNote.set(position,note);
        notifyItemChanged(position,note);
    }

    public void removeItem(int position){
        this.listNote.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,listNote.size());
    }

    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_note,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.tvTitle.setText(listNote.get(i).getTitle());
        viewHolder.tvDescription.setText(listNote.get(i).getDescription());
        viewHolder.tvDate.setText(listNote.get(i).getDate());
        viewHolder.cvNote.setOnClickListener(new CustomOnItemClickListener(i, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int poition) {
                Intent intent = new Intent(activity,NoteAddUpdateActivity.class);
                intent.putExtra(NoteAddUpdateActivity.EXTRA_POSITION,i);
                intent.putExtra(NoteAddUpdateActivity.EXTRA_NOTE,listNote.get(i));
                activity.startActivityForResult(intent,NoteAddUpdateActivity.REQUEST_UPDATE);
            }
        }));

    }

    @Override
    public int getItemCount() {
        return listNote.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTitle,tvDate,tvDescription;
        final CardView cvNote;
         ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvDate = itemView.findViewById(R.id.tv_item_date);
            tvDescription = itemView.findViewById(R.id.tv_item_description);
            cvNote = itemView.findViewById(R.id.cv_item_note);
        }
    }
}
