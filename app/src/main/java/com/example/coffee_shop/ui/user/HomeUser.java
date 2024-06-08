package com.example.coffee_shop.ui.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.coffee_shop.R;
import com.example.coffee_shop.databinding.ActivityHomeAdminBinding;
import com.example.coffee_shop.databinding.ActivityHomeUserBinding;
import com.example.coffee_shop.ui.admin.HomeAdmin;
import com.example.coffee_shop.ui.admin.frragments.FragmentHome;
import com.example.coffee_shop.ui.admin.frragments.FragmentOrders;
import com.example.coffee_shop.ui.user.fragment.HomeUserFragment;
import com.example.coffee_shop.ui.user.fragment.ProfileUserFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class HomeUser extends AppCompatActivity {

    ActivityHomeUserBinding binding;
    MenuItem home;
    MenuItem profile;

//    DatabaseReference databaseReference;
//    FirebaseDatabase firebaseDatabase;
//    StorageReference storageReference;
//    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        databaseReference = firebaseDatabase.getReference("category");
//
//        storage= FirebaseStorage.getInstance();
//        storageReference=storage.getReference();

        binding = ActivityHomeUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FloatingActionButton fab = binding.getRoot().findViewById(R.id.fab1);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(HomeUser.this, CartActivity.class);
            startActivity(intent);

        });


        home= binding.bottomNavigationView.getMenu().findItem(R.id.homeUser).setChecked(true);
        home.setOnMenuItemClickListener(item -> {
            home= binding.bottomNavigationView.getMenu().findItem(R.id.homeUser).setChecked(true);
            replaceFragment(new HomeUserFragment());
            return  true;
        });

        profile= binding.bottomNavigationView.getMenu().findItem(R.id.profileUser);
        profile.setOnMenuItemClickListener(item -> {
            profile= binding.bottomNavigationView.getMenu().findItem(R.id.profileUser).setChecked(true);
            replaceFragment(new ProfileUserFragment());
            return  true;
        });


        replaceFragment(new HomeUserFragment());
        binding.bottomNavigationView.setBackground(null);
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }



}