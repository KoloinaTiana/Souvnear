package com.example.projet.ui.list;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.projet.Photo;
import com.example.projet.R;
import com.example.projet.databinding.FragmentListBinding;


public class ListFragment extends Fragment {

    private FragmentListBinding binding;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ListViewModel listViewModel =
                new ViewModelProvider(this).get(ListViewModel.class);

        binding = FragmentListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textList;
        listViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        Button button_new = root.findViewById(R.id.button_new);

        button_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getActivity(), Photo.class);
                startActivity(intent);*/
                /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }*/

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // La permission n'a pas été accordée, demander la permission
                    ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.CAMERA }, REQUEST_CAMERA_PERMISSION);
                } else {
                    // La permission a été accordée, ouvrir l'appareil photo
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (i.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Récupération de la photo capturée
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            // Passage à la nouvelle activité pour afficher la photo dans ImageView
            Intent intent = new Intent(getActivity(), Photo.class);
            intent.putExtra("imageBitmap", imageBitmap);
            startActivity(intent);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}