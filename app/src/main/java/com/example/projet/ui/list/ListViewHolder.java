package com.example.projet.ui.list;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet.R;

public class ListViewHolder extends RecyclerView.ViewHolder {

    ImageView img;
    TextView titre;
    TextView date;

    // @itemView: recyclerview_item_layout.xml
    public ListViewHolder(@NonNull View itemView) {
        super(itemView);

        this.img = (ImageView) itemView.findViewById(R.id.imageview);
        this.titre = (TextView) itemView.findViewById(R.id.textView_titre);
        this.date = (TextView) itemView.findViewById(R.id.textView_date);
    }
}
