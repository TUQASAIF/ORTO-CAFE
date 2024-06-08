package com.example.coffee_shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {
    TextView text_login;

    private FirebaseAuth FAuth= FirebaseAuth.getInstance();
    EditText full_name,mobile_number,email,password,confirm_password;

    DatabaseReference databaseReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        text_login=findViewById(R.id.text_login);
        text_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });



        full_name = findViewById(R.id.ed_name_login);
        mobile_number = findViewById(R.id.ed_phone_login);

        email = findViewById(R.id.ed_email_login);
        password = findViewById(R.id.ed_password_login);
        confirm_password = findViewById(R.id.confirm_passwword_textbox_reg);

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateEmail();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePassword();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        Button create_user_button = findViewById(R.id.signup_button);

        create_user_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {

                    String fullname_value = full_name.getText().toString().trim();
                    String mobile_number_value = mobile_number.getText().toString().trim();
                    String email_value = email.getText().toString().trim();
                    String password_value = password.getText().toString().trim();
                    String confirm_password_value = confirm_password.getText().toString().trim();
                    if (!validateEmail() |  !validatePassword()) {
                        Toast.makeText(getApplicationContext(), "Error: Your Password And Email", Toast.LENGTH_LONG).show();
                    }else if(password_value.compareTo(confirm_password_value) == 0){
                        progressDialog = new ProgressDialog(SignUp.this);
                        progressDialog.setTitle("Register User");
                        progressDialog.setMessage("Please Waite ........");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                        FAuth.createUserWithEmailAndPassword(email_value, password_value).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    String userID= FirebaseAuth.getInstance().getCurrentUser().getUid();

                                    saveUsersData(userID, fullname_value, email_value, String.valueOf(mobile_number_value));


                                    Toast.makeText(getApplicationContext(), "The account is created successfully!", Toast.LENGTH_LONG).show();


                                }
                                else {
                                    progressDialog.dismiss();
                                    Exception exception = task.getException();
                                    Toast.makeText(getApplicationContext(), "Can\'t Create Account Now Please try again later! " + exception.getMessage(), Toast.LENGTH_LONG).show();

                                }

                            }
                        });

                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Error: The passwords are Not matched! please check", Toast.LENGTH_LONG).show();

                    }
                }else{
                    Toast.makeText(SignUp.this, "Check Your Network", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }


    private void saveUsersData(String userID, String fullname, String emailT, String mobile_no){
        // saving user's Data

        try{
//            User user = new User(email, fullname, mobile_no);
//            CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Users");
//            collectionReference.document(userID).set(user);

            databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(userID);
            HashMap<String, String> usermap = new HashMap<>();
            usermap.put("email", emailT.trim());
            usermap.put("phone",mobile_no);
            usermap.put("name",fullname);
            usermap.put("id", userID);
            usermap.put("stop", "false");
            usermap.put("type", "user");
            databaseReference.setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Toast.makeText(SignUp.this, "add database", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        finish();
                    } else {
                        progressDialog.dismiss();
                        email.setError("Set your Data must have @ and .com");
                        password.setError("Must password greater than 6 ");
                        Toast.makeText(SignUp.this, "trey again", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        catch (Exception e){
            progressDialog.dismiss();
            System.out.println("The Error -> " + e.getMessage());
        }


    }



    private boolean validatePassword() {
        String passwordInput = password.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            password.setError("Field can't be empty");
            password.setFocusable(true);
            return false;
        }
        else if (passwordInput.length()<6) {
            password.setError("Password length must be more 6 string");
            return false;
        }
        else {
            password.setError(null);
            return true;
        }
    }
    private boolean validateEmail() {
        String emailInput = email.getText().toString().trim();

        if (emailInput.isEmpty()) {
            email.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError("Please enter a valid email address Like ex@gmail.com");
            return false;
        } else {
            email.setError(null);
            checkEmail_Is_Esxist(email.getText().toString().trim());
            return true;
        }
    }

    private void checkEmail_Is_Esxist(String emails) {
        FAuth.fetchSignInMethodsForEmail(emails)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                        boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                        if (isNewUser) {
                            Log.e("TAG", "Is New User!");
                        } else {
                            Log.e("TAG", "Is Old User!");
                        }

                    }
                });
    }

}