package com.example.coffee_shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.coffee_shop.R;

public class start_screen extends AppCompatActivity {

    Button signIn,signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);



        signup=findViewById(R.id.bu_sighnup_main);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUp.class));

            }
        });


        signIn=findViewById(R.id.bu_login_main);
        signIn.setBackgroundResource(R.drawable.busin2);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

    }
}