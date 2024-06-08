package com.example.coffee_shop.ui.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffee_shop.R;
import com.example.coffee_shop.database_local.Database_order_local;
import com.example.coffee_shop.ui.model.Data_SupCategrory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.text.DecimalFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ProductDetails extends AppCompatActivity {


    Button addCard;
    EditText ed_note_login,ed_date_login,ed_phoneFrind;
    Database_order_local database_allnotes;

    ImageView foodimage,remove,add;
    ImageView sizeM,sizeS,sizeL;
    ImageView cup0,cup1;
    ImageView s0,s1,s2,s3; //sougr

    Button bu_MilkMazon0,bu_MilkMara3e1;

    TextView namePro,pricePro,addDone;
    TextView total;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    String idCat;


    Data_SupCategrory dataFood;


    String image_url;
    TextView num;
    int numFinal=1;
    int finalSize=0;
    String finalSizeString="SMALL";

    int cupFinal=0;
    String cupFinalString="PLASTIC CUP";
    int sugarFinal=2;
    String sugarFinalString="TWO SUGAR";
    int milkFinal=1;
    String milkFinalString="ALMARAI MILK";


    double finalPrice=0;
    DecimalFormat df ;

    LinearLayout linSize,linStyle,linSugar,linMilk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        // Define the locale for English
        Locale englishLocale = new Locale("en", "US");
        // Create DecimalFormatSymbols with English locale
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(englishLocale);
        // Set decimal separator to '.'
        symbols.setDecimalSeparator('.');
        df = new DecimalFormat("#.000", symbols);



        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();



        Bundle extras = getIntent().getExtras(); // to get move intent
        if (extras != null) {
            String a = extras.getString("id");
            String aa = extras.getString("true");
            if (a!=null){
                idCat= a;
//                Toast.makeText(this, "???"+aa, Toast.LENGTH_SHORT).show();
            }
        }




        databaseReference= FirebaseDatabase.getInstance().getReference().child("Products");





        addCard=findViewById(R.id.bu_update_profile);
        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database_allnotes=new Database_order_local(ProductDetails.this);
                database_allnotes.open();
                //String name,String id,String amunt,String price,String discription
                database_allnotes.insert(
//                        dataFood.getPrice(),dataFood.getDiscount(),image_url,
                        dataFood.getTitle(), idCat,String.valueOf(numFinal),String.valueOf(finalPrice),
                        ed_note_login.getText().toString(), dataFood.getImage(),
                        ed_phoneFrind.getText().toString(),String.valueOf(cupFinalString),
                        ed_date_login.getText().toString(),String.valueOf(sugarFinalString) ,String.valueOf(milkFinalString),
                        String.valueOf(finalSizeString)
                );

                cek_button();
                Toast.makeText(ProductDetails.this, "Add Cart", Toast.LENGTH_LONG).show();
            }
        });
        ed_note_login=findViewById(R.id.ed_note_login);
        ed_date_login=findViewById(R.id.ed_date_login);
        ed_phoneFrind=findViewById(R.id.ed_phoneFrind);


        sizeM=findViewById(R.id.sizeM);
        sizeL=findViewById(R.id.sizeL);
        sizeS=findViewById(R.id.sizeS);

        cup1=findViewById(R.id.cup1);
        cup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cupFinal=1;
                cupFinalString="PAPER CUP";
                cup1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTop));
                cup0.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.your_color));


            }
        });
        cup0=findViewById(R.id.cup0);
        cup0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cupFinal=0;
                cupFinalString="PLASTIC CUP ";

                cup0.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTop));
                cup1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.your_color));

            }
        });



//        p----------------------
        s0=findViewById(R.id.s0);
        s1=findViewById(R.id.s1);
        s2=findViewById(R.id.s2);
        s3=findViewById(R.id.s3);
        s0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sugarFinal=0;
                sugarFinalString="WITHOUT SUGAR";
                s0.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTop));
                s1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.your_color));
                s2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.your_color));
                s3.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.your_color));
            }
        });
        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sugarFinal=1;
                sugarFinalString="ONE SUGAR";
                s1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTop));
                s0.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.your_color));
                s2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.your_color));
                s3.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.your_color));
            }
        });
        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sugarFinal=2;
                sugarFinalString="TWO SUGAR";
                s2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTop));
                s1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.your_color));
                s0.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.your_color));
                s3.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.your_color));
            }
        });
        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sugarFinal=3;
                sugarFinalString="THREE SUGAR";
                s3.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTop));
                s1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.your_color));
                s2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.your_color));
                s0.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.your_color));
            }
        });


        bu_MilkMazon0=findViewById(R.id.bu_MilkMazon0);
        bu_MilkMara3e1=findViewById(R.id.bu_MilkMara3e1);
        bu_MilkMazon0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                milkFinal=0;
                milkFinalString="MAZOON MILK";
                bu_MilkMazon0.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.br));
                bu_MilkMara3e1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTop));

            }
        });

        bu_MilkMara3e1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                milkFinal=1;
                milkFinalString="ALMARAI MILK ";
                 bu_MilkMara3e1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.br));
                bu_MilkMazon0.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTop));

            }
        });

//        --

        foodimage=findViewById(R.id.photoPrpduct);
        add=findViewById(R.id.add);
        remove=findViewById(R.id.remove);
        num=findViewById(R.id.num);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numFinal=numFinal+1;
                num.setText(""+numFinal);
                total.setText(df.format((numFinal*finalPrice))+"");
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if(numFinal>1){
                    numFinal=numFinal-1;
                    num.setText(""+numFinal);
                    total.setText(df.format((numFinal*finalPrice))+"");
                }

            }
        });


        total=findViewById(R.id.total);

        namePro=findViewById(R.id.namePro);
        pricePro=findViewById(R.id.pricePro);
        addDone=findViewById(R.id.addDone);

//        description=findViewById(R.id.text_descrebtion_productdetelies);
////        elegantNumberButton=findViewById(R.id.ElegantNumberButton_fooddescription);
//
//        discount=findViewById(R.id.text_discount_productdetelies);
//        linearLayout_discount=findViewById(R.id.liner_decound);






        getdetiles();
        cek_button();
    }


    @SuppressLint("RestrictedApi")
    private void cek_button() {
        database_allnotes=new Database_order_local(ProductDetails.this);
        database_allnotes.open();
        if (database_allnotes.cek_isExist_orNot(idCat) == true) {
            addCard.setVisibility(View.GONE);
            addDone.setVisibility(View.VISIBLE);
//            cek_button();

        }else{
            addCard.setVisibility(View.VISIBLE);
            addDone.setVisibility(View.GONE);
        }
    }

    boolean  switchPrices;
    boolean switchSugass;
    boolean switchMilks;
    boolean switchStyles;


    private void getdetiles() {
        databaseReference.child(idCat)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
                            dataFood=dataSnapshot.getValue(Data_SupCategrory.class);

                            Picasso.get().load(dataFood.getImage()).into(foodimage);

                            View viewSize,viewStyle,viewSugre,viewMilk;
                            linSize=findViewById(R.id.linSize);
                            viewSize=findViewById(R.id.viewSize);
                            if(dataFood.getSwitchPrice()==true){
                                linSize.setVisibility(View.VISIBLE);
                                viewSize.setVisibility(View.VISIBLE);
                                switchPrices=dataFood.getSwitchPrice();
                            }else{
                                linSize.setVisibility(View.GONE);
                                viewSize.setVisibility(View.GONE);
                                switchPrices=false;
                            }


                            linStyle=findViewById(R.id.linStyle);
                            viewStyle=findViewById(R.id.viewStyle);
                            if(dataFood.getSwitchStyle()==true){
                                linStyle.setVisibility(View.VISIBLE);
                                viewStyle.setVisibility(View.VISIBLE);
                                switchStyles=true;
                            }else{
                                linStyle.setVisibility(View.GONE);
                                viewStyle.setVisibility(View.GONE);
                                switchStyles=false;
                            }
                            //    boolean switchMilks;
                            linSugar=findViewById(R.id.linSugar);
                            viewSugre=findViewById(R.id.viewSugre);
                            if(dataFood.getSwitchSugar()==true){
                                viewSugre.setVisibility(View.VISIBLE);
                                linSugar.setVisibility(View.VISIBLE);
                                switchSugass=true;
                            }else{
                                viewSugre.setVisibility(View.GONE);
                                linSugar.setVisibility(View.GONE);
                                switchSugass=false;
                            }

                            linMilk=findViewById(R.id.linMilk);
                            viewMilk=findViewById(R.id.viewMilk);
                            if(dataFood.getSwitchMilk()==true){
                                linMilk.setVisibility(View.VISIBLE);
                                viewMilk.setVisibility(View.VISIBLE);
                                switchMilks=true;
                            }else{
                                viewMilk.setVisibility(View.GONE);
                                linMilk.setVisibility(View.GONE);
                                switchMilks=false;
                            }

                            image_url=dataFood.getImage();
                            pricePro.setText(dataFood.getPriceD()+"  OMR");
                            namePro.setText(dataFood.getTitle()+"");

                            finalPrice=Double.parseDouble(dataFood.getPriceD().toString());
                            total.setText(df.format((numFinal*finalPrice))+"");

                            if(dataFood.getPriceS().equals("")){
                                sizeS.setVisibility(View.GONE);
                            }else {
                                sizeS.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        finalSize=0;
                                        finalSizeString="SMALL";
                                        finalPrice=Double.parseDouble(dataFood.getPriceS());
                                        total.setText(df.format((numFinal*finalPrice))+"");
                                    }
                                });;
                            }

                            if(dataFood.getPricem().equals("")){
                                sizeM.setVisibility(View.GONE);
                            }else {
                                sizeM.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        finalSize=1;
                                        finalSizeString="MEDIUM";
                                        finalPrice=Double.parseDouble(dataFood.getPricem());
                                        total.setText(df.format((numFinal*finalPrice))+"");
                                    }
                                });;
                            }

                            if(dataFood.getPriceL().equals("")){
                                sizeL.setVisibility(View.GONE);
                            }else {
                                sizeL.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        finalSize=2;
                                        finalSizeString="LARGE";
                                        finalPrice=Double.parseDouble(dataFood.getPriceL());
                                        total.setText(df.format((numFinal*finalPrice))+"");
                                    }
                                });;
                            }

                            Log.e("diaa",dataFood.getPriceL()+"");
                            Log.e("diaa",dataFood.getPricem()+"");
                            Log.e("diaa",dataFood.getPriceS()+"");

//                            description.setText(dataFood.getDescription());
//                            collapsingToolbarLayout.setTitle(dataFood.getName());
//
//                            if (dataFood.getDescription().equals("")){
//                                linearLayout_discount.setVisibility(View.GONE);
//                            }else{
//                                discount.setText(dataFood.getDiscount());
//                            }

                        }else{
                            Toast.makeText(ProductDetails.this, "Check Your Network", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }
}
