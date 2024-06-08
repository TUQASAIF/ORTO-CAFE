package com.example.coffee_shop.ui.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.coffee_shop.ui.admin.frragments.FragmentHome;
import com.example.coffee_shop.ui.admin.frragments.FragmentOrders;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.HashMap;
import java.util.UUID;


public class HomeAdmin extends AppCompatActivity {

    ActivityHomeAdminBinding binding;
    MenuItem home123;
    MenuItem orders123;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    StorageReference storageReference;
    FirebaseStorage storage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home_admin);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("category");

        storage= FirebaseStorage.getInstance();
        storageReference=storage.getReference();


        binding = ActivityHomeAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FloatingActionButton fab = binding.getRoot().findViewById(R.id.fab1);
        fab.setOnClickListener(view -> {
            createCat();
        });


        home123= binding.bottomNavigationView.getMenu().findItem(R.id.home123).setChecked(true);
        home123.setOnMenuItemClickListener(item -> {
            home123= binding.bottomNavigationView.getMenu().findItem(R.id.home123).setChecked(true);
            replaceFragment(new FragmentHome());
            return  true;
        });

        orders123= binding.bottomNavigationView.getMenu().findItem(R.id.orders123);
        orders123.setOnMenuItemClickListener(item -> {
            orders123= binding.bottomNavigationView.getMenu().findItem(R.id.orders123).setChecked(true);
            replaceFragment(new FragmentOrders());
            return  true;
        });
       

        replaceFragment(new FragmentHome());
        binding.bottomNavigationView.setBackground(null);
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    void createCat(){

        final AlertDialog.Builder builder=new AlertDialog.Builder(HomeAdmin.this);
//                builder.setTitle("One More Step!");
        builder.setMessage("ENTER NEW SECTION");

        LayoutInflater inflater=HomeAdmin.this.getLayoutInflater();
        View add_menu=inflater.inflate(R.layout.add_new_menu,null);

        final EditText edtName=add_menu.findViewById(R.id.ed_nmaemenu_home);
        bu_select=add_menu.findViewById(R.id.bu_selectimage_home);
        bu_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_Image();
            }
        });


        builder.setView(add_menu);
//                builder.setIcon(R.drawable.ic_shopping);
        builder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (edtName.getText().toString().isEmpty()){
                    edtName.setError("ENTER NEW ITEM !!");
                    edtName.setFocusable(true);

                }else {
                    upoladeImage(edtName.getText().toString().trim(),dialogInterface);
                }
//                        dialogInterface.dismiss();
            }


        });

        builder.show();
    }




    private void upoladeImage(final String name, final DialogInterface dialogInterface) {

        if (saveUri != null){

            final ProgressDialog mDialog=new ProgressDialog(HomeAdmin.this);
            mDialog.setMessage("Upload...");
            mDialog.show();
            mDialog.setCanceledOnTouchOutside(false);

            String imagename= UUID.randomUUID().toString();
            final StorageReference imgeFolder=storageReference.child("image/"+imagename);
            imgeFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    imgeFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("category");
                            HashMap<String, String> usermap = new HashMap<>();
                            usermap.put("name", name);
                            usermap.put("image",uri.toString());

                            databaseReference.push().setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
//                                        Intent intent = new Intent(getApplicationContext(), Home.class);
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        startActivity(intent);
                                        Toast.makeText(HomeAdmin.this, "add", Toast.LENGTH_SHORT).show();
                                        mDialog.dismiss();
                                        dialogInterface.dismiss();
                                        saveUri=null;
//                                        finish();
                                    } else {
                                        mDialog.dismiss();
//                                email.setError("Set your Data must have @ and .com");
//                                password.setError("Must password greater than 6 ");
                                        Toast.makeText(HomeAdmin.this, "trey again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });

                }
            });


        }else{
            Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show();
        }
    }

    private void show_Image() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image"),Pick_Image);
    }
    Uri saveUri;
    Button bu_select;
    final int Pick_Image=71;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Pick_Image && resultCode == RESULT_OK
                && data != null && data.getData() !=null){

            saveUri=data.getData();
            bu_select.setText("IMAGE SELECT");

        }

    }
}