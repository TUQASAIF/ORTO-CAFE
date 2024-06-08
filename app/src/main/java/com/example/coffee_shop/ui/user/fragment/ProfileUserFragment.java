package com.example.coffee_shop.ui.user.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffee_shop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileUserFragment extends Fragment {



    public ProfileUserFragment() {
        // Required empty public constructor
    }

    View view;
    TextView name,phone,email;
    CircleImageView imageView;
    FirebaseUser firebaseUser;

    Button updatedate, bu_select;

    String name_st,phone_st,email_st,image_st=null;
    EditText name_ed,phone_ed,email_ed;

    StorageReference storageReference;
    FirebaseStorage storage;

    int Pick_Image=100;
    Uri saveUri;
    ProgressDialog mDialog;
    DatabaseReference databaseReferenceUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_profile_user, container, false);


        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();


        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        imageView=view.findViewById(R.id.image_profile);
        name=view.findViewById(R.id.text_name_profile);
        phone=view.findViewById(R.id.text_phone_profile);
        email=view.findViewById(R.id.text_email_profile);
        databaseReferenceUser= FirebaseDatabase.getInstance().getReference().child("User");
        getDataUsre();


        updatedate=view.findViewById(R.id.bu_update_profile);
        updatedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUpladeDiloge();
            }
        });


        return view;
    }

    private void getDataUsre() {

        databaseReferenceUser.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.hasChild("phone")) {
                    final String phoneUser=dataSnapshot.child("phone").getValue(String.class).toString();
                    phone_st=phoneUser;
                    phone_st = (phone_st != null) ? phone_st : "";
                }

                if (dataSnapshot.hasChild("name")) {
                    final String nameUser=dataSnapshot.child("name").getValue(String.class).toString();
                    name_st=nameUser;
                    name_st = (name_st != null) ? name_st : "";
                }

                if (dataSnapshot.hasChild("email")) {
                    final String emailUser=dataSnapshot.child("email").getValue(String.class).toString();
                    email_st=emailUser;
                    email_st = (email_st != null) ? email_st : "";
                }

                String imagel_st = "";
                if (dataSnapshot.hasChild("image")) {
                    final String imagelUser=dataSnapshot.child("image").getValue(String.class).toString();
                    imagel_st=imagelUser;
                    imagel_st = (imagel_st != null) ? imagel_st : "";
                }




                name.setText(""+name_st);
                phone.setText(""+phone_st);
                email.setText(""+email_st);

                if (!imagel_st.equals("")) {
                    image_st=imagel_st;
                    Picasso.get().load(imagel_st).error(R.drawable.user).into(imageView);

                }else {
                    Picasso.get().load("imagel_st").error(R.drawable.ground).into(imageView);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    private void showUpladeDiloge() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("ONE MORE STEP!");
        builder.setMessage("ENTER YOUR UPDATE DATA..");

        LayoutInflater inflater=getActivity().getLayoutInflater();
        View add_menu=inflater.inflate(R.layout.udate_profile,null);

        name_ed=add_menu.findViewById(R.id.ed_nmaefood_home);
        name_ed.setText(name_st);

        phone_ed =add_menu.findViewById(R.id.ed_decriptionfood_home);
        phone_ed.setText(phone_st);


        bu_select=add_menu.findViewById(R.id.bu_selectimage_home);
        bu_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_Image();
            }
        });

        builder.setView(add_menu);
        builder.setIcon(R.drawable.updatepro);
        builder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (name_ed.getText().toString().isEmpty() && phone_ed.getText().toString().isEmpty()){
                    name.setError("Enter Name Product !!");
                    name.setFocusable(true);

                }else {
                    ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {

                        ChangeImageCat(name_ed,phone_ed,dialogInterface);

                    }else{
                        Toast.makeText(getActivity(), "Check Your Network", Toast.LENGTH_SHORT).show();
                    }

                }
                //dialogInterface.dismiss();
            }


        });

        builder.show();

    }


    private void show_Image() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),Pick_Image);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Pick_Image && resultCode == getActivity().RESULT_OK
                && data != null && data.getData() !=null){

            saveUri=data.getData();
            bu_select.setText("Image Select");

        }

    }


    private void ChangeImageCat(final EditText name_ed, final EditText phone_ed, final DialogInterface dialogInterface) {
        mDialog=new ProgressDialog(getActivity());
        mDialog.setMessage("Upload...");
        mDialog.show();
        mDialog.setCanceledOnTouchOutside(false);

        if (saveUri != null){
            String imagename= UUID.randomUUID().toString();
            final StorageReference imgeFolder=storageReference.child("image/"+imagename);
            imgeFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    imgeFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            databaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("User").child(firebaseUser.getUid());
                            HashMap<String, String> usermap = new HashMap<>();
                            usermap.put("phone",phone_ed.getText().toString().trim());
                            usermap.put("name",name_ed.getText().toString().trim());
                            usermap.put("image",uri.toString());

                            usermap.put("email", email_st);
                            usermap.put("id", firebaseUser.getUid());
                            usermap.put("type", "user");
                            usermap.put("states", "Hi there, Iam use Chat App");

                            databaseReferenceUser.setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
//                                        Intent intent = new Intent(getActivity(), ProfileUserFragment.class);
//                                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        startActivity(intent);
//                                        Toast.makeText(getActivity(), "add", Toast.LENGTH_SHORT).show();
                                        mDialog.dismiss();
//                                        getActivity().finish();
                                        dialogInterface.dismiss();
                                        saveUri=null;
                                        getDataUsre();
                                    } else {
                                        mDialog.dismiss();
                                        Toast.makeText(getActivity(), "trey again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });

                }
            });


        }else{
            databaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("User").child(firebaseUser.getUid());
            HashMap<String, String> usermap = new HashMap<>();
            usermap.put("phone",phone_ed.getText().toString().trim());
            usermap.put("name",name_ed.getText().toString().trim());
            if (image_st ==null) {
                usermap.put("image", "https://firebasestorage.googleapis.com/v0/b/diaafood-26421.appspot.com/o/18833568.jpg?alt=media&token=3409ddb1-6193-48e5-80b7-a99c06a223cb");
            }
            else{
                usermap.put("image",image_st);
            }
            usermap.put("email", email_st);
            usermap.put("id", firebaseUser.getUid());
            usermap.put("type", "user");
            usermap.put("states", "Hi there, Iam use Chat App");

            databaseReferenceUser.setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
//                        Intent intent = new Intent(getActivity(), ProfileUserFragment.class);
//                        startActivity(intent);
////                        Toast.makeText(SingelItem.this, "add", Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                        dialogInterface.dismiss();
                        saveUri=null;
//                        getActivity().finish();
                        getDataUsre();
                    } else {
                        mDialog.dismiss();
//                                email.setError("Set your Data must have @ and .com");
//                                password.setError("Must password greater than 6 ");
                        Toast.makeText(getActivity(), "trey again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}