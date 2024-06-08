package com.example.coffee_shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffee_shop.ui.admin.HomeAdmin;
import com.example.coffee_shop.ui.user.HomeUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    EditText email,password;
    Button login;
    TextView text_forget_password,text_signUp;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth=FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("User");

        email=findViewById(R.id.ed_phone_login);
        password=findViewById(R.id.ed_password_login);

        login=findViewById(R.id.ed_login_login);
        text_forget_password=findViewById(R.id.text_forget_password);

        text_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgetPassword.class));
            }
        });
        text_signUp=findViewById(R.id.text_signUp);

        text_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUp.class));
            }
        });



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {

                    loginwithFirebase();
                }else{
                    Toast.makeText(Login.this, "Check Your Network", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    private void loginwithFirebase() {

        final ProgressDialog progressDialog = new ProgressDialog(Login.this);
        progressDialog.setTitle("Login User");
        progressDialog.setMessage("Please Waite ........");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String emales=email.getText().toString();
        String passswords=password.getText().toString();

        if (!TextUtils.isEmpty(emales)&&!TextUtils.isEmpty(passswords)) {
            auth.signInWithEmailAndPassword(emales, passswords).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
//                        Toast.makeText(Login.this, "Done", Toast.LENGTH_LONG).show();

//                        Intent intent = new Intent(getApplicationContext(), Home.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        progressDialog.dismiss();
//                        finish();

                        DatabaseReference databaseReferenceUser;
                        databaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("User");
                        databaseReferenceUser.child(auth.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                progressDialog.dismiss();
                                if (dataSnapshot.hasChild("type")) {
                                    final String typeUser = dataSnapshot.child("type").getValue(String.class).toString();
//                                    Toast.makeText(LoginActivity.this, ""+typeUser, Toast.LENGTH_SHORT).show();
                                    if(typeUser.equals("admin")){
//                                        Intent intent = new Intent(Login.this, HomeUser.class);
                                        Intent intent = new Intent(Login.this, HomeAdmin.class);
                                        startActivity(intent);
                                        finish();
                                    }else{

                                        Intent intent = new Intent(Login.this, HomeUser.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                progressDialog.dismiss();
                            }
                        });
                    } else {
                        email.setError("Check your email");
                        password.setError("Check your password ");
                        progressDialog.hide();
                    }
                }
            });
        }
        else {
            progressDialog.hide();
            //lenthpass.setTextColor(R.color.read);
            email.setError("Set your email");
            password.setError("Set your password ");
        }
    }
}
