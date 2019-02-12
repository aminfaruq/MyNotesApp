package id.co.maminfaruq.mynotesapp;

import android.view.View;

public class CustomOnItemClickListener implements View.OnClickListener {
    private int position ;
    private final OnItemClickCallback onItemClickCallback;

    public CustomOnItemClickListener(int position, OnItemClickCallback onItemClickCallback) {
        this.position = position;
        this.onItemClickCallback = onItemClickCallback;
    }


    @Override
    public void onClick(View v) {
        onItemClickCallback.onItemClicked(v,position);
    }

    public interface OnItemClickCallback{
        void onItemClicked(View view,int poition);
    }
}
