package com.example.projet.ui.list;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<ListViewHolder> {


    private ArrayList<MyData> data;
    private Context context;
    private LayoutInflater mLayoutInflater;

    public CustomRecyclerViewAdapter(Context context, ArrayList<MyData> datas ) {
        this.context = context;
        this.data = datas;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public void updateData(ArrayList<MyData> datas) {
        this.data = datas;
        notifyDataSetChanged();
    }

    @Override
    public ListViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        // Inflate view du recyclerview_item_layout.xml
        View recyclerViewItem = mLayoutInflater.inflate(R.layout.recyclerview_item_layout, parent, false);

        recyclerViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRecyclerItemClick( (RecyclerView)parent, v);
            }
        });
        return new ListViewHolder(recyclerViewItem);
    }

    // Trouve Image ID correspondant au nom de l'image.
    public int getDrawableResIdByName(String resName)  {
        String pkgName = context.getPackageName();
        // Return 0 if not found.
        int resID = context.getResources().getIdentifier(resName , "drawable", pkgName);
        Log.i("AAA", "Res Name: "+ resName+"==> Res ID = "+ resID);
        return resID;
    }

    //Clique sur le recyclerview
    private void handleRecyclerItemClick(RecyclerView recyclerView, View itemView) {
        int itemPosition = recyclerView.getChildLayoutPosition(itemView);
        MyData mydata  = this.data.get(itemPosition);

        Toast.makeText(this.context, mydata.getTitre(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        MyData datas = this.data.get(position);
        holder.titre.setText(datas.getTitre());
        holder.date.setText(datas.getDate());

        // Convertir le tableau de bytes en bitmap
        byte[] imageData = datas.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

        if (holder.img != null) {
            holder.img.setImageBitmap(bitmap);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Passage à la nouvelle activité pour afficher le détail de la photo

                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(datas.getLatitude(), datas.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String position = addresses.get(0).getLocality();

                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("titre", datas.getTitre());
                intent.putExtra("description", datas.getDescription());
                intent.putExtra("date", datas.getDate());
                intent.putExtra("position", position);
                intent.putExtra("image", datas.getImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
