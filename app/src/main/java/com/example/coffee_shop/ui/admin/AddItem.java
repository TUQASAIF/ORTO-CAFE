package com.example.coffee_shop.ui.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.coffee_shop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;

public class AddItem extends AppCompatActivity {

    Button bu_select_photo,bu_upload;

    Uri saveUri=null;

    ImageView photoUploade;

    EditText title,priseD,priceS,pricem,priceL;

    ProgressDialog mDialog;

    StorageReference storageReference;
    FirebaseStorage storage;

    DatabaseReference databaseReference;

    String idCat;
    Switch switchPrice,switchStyle,switchMilk,switchSugar;
    boolean switchPriceChoce=false;
    boolean switchSugarChoce=true;
    boolean switchMilkChoce=true;
    boolean switchStyleChoce=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Bundle extras = getIntent().getExtras(); // to get move intent
        if (extras != null) {
            String a = extras.getString("id");
            if (a!=null){
                idCat= a;
            }
        }

        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        switchPrice = findViewById(R.id.switchPrice);
        switchPrice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("0000",isChecked+"");
                switchPriceChoce=isChecked;
            }
        });

        switchStyle = findViewById(R.id.switchStyle);
        switchStyle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("0000",isChecked+"");
                switchStyleChoce=isChecked;
            }
        });

        switchMilk = findViewById(R.id.switchMilk);
        switchMilk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("0000",isChecked+"");
                switchMilkChoce=isChecked;
            }
        });

        switchSugar = findViewById(R.id.switchSugar);
        switchSugar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("0000",isChecked+"");
                switchSugarChoce=isChecked;
            }
        });
        photoUploade = findViewById(R.id.photoUploade);

        bu_select_photo = findViewById(R.id.bu_selectimage_home);
        bu_select_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_Image();
            }
        });


        priseD = findViewById(R.id.ed_price_D);
        priceS = findViewById(R.id.ed_price_S);
        pricem = findViewById(R.id.ed_price_m);
        priceL = findViewById(R.id.ed_price_L);
        title = findViewById(R.id.ed_title_add);
        bu_upload = findViewById(R.id.bu_upload_event);
        bu_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.getText().toString().isEmpty()&&priseD.getText().toString().isEmpty()){
                    title.setError("Enter title Event !!");
                    title.setFocusable(true);

                }else {
                    upoladeImage();
                }
            }
        });
    }

    int Pick_Image=100;
    private void show_Image() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),Pick_Image);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Pick_Image && resultCode == RESULT_OK
                && data != null && data.getData() !=null){

            saveUri=data.getData();
            bu_select_photo.setText("IMAGE SELECTED");

            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    photoUploade.setImageBitmap(selectedImage);
                    photoUploade.setAdjustViewBounds(true);
                    photoUploade.setScaleType(ImageView.ScaleType.FIT_XY);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
//                    Toast.makeText(PostImage.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }

            }

        }

    }




    private void upoladeImage() {

        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
            if (saveUri != null){

                mDialog=new ProgressDialog(AddItem.this);
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
                                databaseReference = FirebaseDatabase.getInstance().getReference().child("Products");
                                HashMap<String, Object> usermap = new HashMap<>();
                                usermap.put("title", title.getText().toString().trim());
                                usermap.put("priceD", priseD.getText().toString().trim());
                                usermap.put("priceS", priceS.getText().toString().trim());
                                usermap.put("pricem", pricem.getText().toString().trim());
                                usermap.put("priceL", priceL.getText().toString().trim());
                                usermap.put("image",uri.toString());
                                usermap.put("switchSugar",switchSugarChoce);
                                usermap.put("switchPrice",switchPriceChoce);
                                usermap.put("switchStyle",switchStyleChoce);
                                usermap.put("switchMilk",switchMilkChoce);
                                usermap.put("idCat",idCat);

                                databaseReference.push().setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            finish();
                                            Toast.makeText(AddItem.this, "add", Toast.LENGTH_SHORT).show();
                                            mDialog.dismiss();
                                            saveUri=null;

                                        } else {
                                            mDialog.dismiss();
                                            Toast.makeText(AddItem.this, "trey again", Toast.LENGTH_SHORT).show();
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
        }else{
            Toast.makeText(AddItem.this, "Check Your Network", Toast.LENGTH_SHORT).show();
        }

    }
}