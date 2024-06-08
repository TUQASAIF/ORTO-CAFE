package com.example.coffee_shop.ui.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.coffee_shop.R;
import com.example.coffee_shop.ui.InterFace.itemOnClickListener;
import com.example.coffee_shop.ui.ViewHolder.SupCategory_ViewHolder;
import com.example.coffee_shop.ui.model.Data_SupCategrory;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.Objects;
import java.util.UUID;

public class SupCategory extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    FirebaseUser firebaseUser;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    SwipeRefreshLayout mSwipeRefreshLayout;

    StorageReference storageReference;
    FirebaseStorage storage;
    FirebaseRecyclerAdapter<Data_SupCategrory, SupCategory_ViewHolder> adabtor;

    String idCat;
    String titleAppBar;
    EditText name, discrption,price,descount;
    Button bu_select;

    Uri saveUri=null;
    ProgressDialog mDialog;
    int Pick_Image=100;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup_category);
        Bundle extras = getIntent().getExtras(); // to get move intent
        if (extras != null) {
            String a = extras.getString("id");
            String titlea = extras.getString("title");
            if (a!=null){
                idCat= a;
                titleAppBar= titlea;
//                toolbar = (Toolbar) findViewById(R.id.toolbar);
//                toolbar.setTitle("My title");
//                setSupportActionBar(toolbar);
            }
        }

        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        recyclerView=findViewById(R.id.recycal_home);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Products");
        //databaseReference.keepSynced(true);//to catch data if net offline

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        }
        else{
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(SupCategory.this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
                    mSwipeRefreshLayout.setRefreshing(true);
                    startUI();
                }else{
                    Toast.makeText(SupCategory.this, "Check Your Network", Toast.LENGTH_SHORT).show();
                }

            }
        });


        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//               startActivity(new Intent(getApplicationContext(),AddItem.class));
               Intent intent=new Intent(getApplicationContext(), AddItem.class);
               intent.putExtra("id",idCat);
               //intent.putExtra("true",);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onRefresh() {
        startUI();
    }


    private void startUI() {
        FirebaseRecyclerOptions<Data_SupCategrory> options =
                new FirebaseRecyclerOptions.Builder<Data_SupCategrory>()
                        .setQuery(databaseReference.orderByChild("idCat").equalTo(idCat), Data_SupCategrory.class)
                        .build();


        adabtor =
                new FirebaseRecyclerAdapter<Data_SupCategrory, SupCategory_ViewHolder>(options) {
                    @NonNull
                    @Override
                    public SupCategory_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sup_categroy, parent, false);
                        return new SupCategory_ViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull SupCategory_ViewHolder viewHolder, int position, Data_SupCategrory model) {
                        final String postKey = getRef(viewHolder.getAdapterPosition()).getKey();

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                mSwipeRefreshLayout.setRefreshing(false);

                                viewHolder.title.setText("TITLE : "+model.getTitle().toString());
                                viewHolder.priceD.setText("PRICE : "+model.getPriceD().toString());
                                viewHolder.priceS.setText("SMALL : "+model.getPriceS().toString());
                                viewHolder.pricem.setText("MEDIUM : "+model.getPricem().toString());
                                viewHolder.priceL.setText("LARGE : "+model.getPriceL().toString());
                                Picasso.get().load(model.getImage()).into(viewHolder.images);

                                viewHolder.setItemOnClickListener(new itemOnClickListener() {
                                    @Override
                                    public void onClick(View view, int position, boolean isLongClick) {
//                                        Intent intent=new Intent(getApplicationContext(), ProductDetails.class);
//                                        intent.putExtra("id",getRef(position).getKey());
//                                        //intent.putExtra("true",);
//                                        startActivity(intent);
                                    }
                                });

                                viewHolder.deleat_banner.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        deletbanner(postKey);
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                mSwipeRefreshLayout.setRefreshing(false);
                                // calling on cancelled method when we receive
                                // any error or we are not able to get the data.
                                Toast.makeText(SupCategory.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                };
        adabtor.startListening();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adabtor);
        recyclerView.setAdapter(adabtor);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void deletbanner(final String postke) {

        final AlertDialog.Builder builder=new AlertDialog.Builder(SupCategory.this);
        builder.setTitle("DO YOU WANT DELETE!");
        builder.setMessage("DO YOU WANT DELETE THIS ITEM ");

        builder.setIcon(R.drawable.ic_shopping);
        builder.setPositiveButton("Delete ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {

                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Products");
                    databaseReference.child(postke).removeValue();

                }else{
                    Toast.makeText(SupCategory.this, "Check Your Network", Toast.LENGTH_SHORT).show();
                }


            }
//                        dialogInterface.dismiss();

        });

        builder.setNegativeButton("no ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });


        builder.show();

    }



    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if (item.getTitle().equals("UpDate")){
            Log.e("---1",adabtor.getItem(item.getOrder()).getPriceD());
            Log.e("---2",adabtor.getItem(item.getOrder()).getPriceS());
            Log.e("---3",adabtor.getItem(item.getOrder()).getPricem());
            Log.e("---4",adabtor.getItem(item.getOrder()).getPriceL());
            showUpladeDiloge(adabtor.getRef(item.getOrder()).getKey(),adabtor.getItem(item.getOrder()));
        }
        return super.onContextItemSelected(item);
    }



    EditText titlea,priseDa,priceSa,pricema,priceLa;
    boolean switchPriceChoce=false;
    boolean switchSugarChoce=true;
    boolean switchMilkChoce=true;
    boolean switchStyleChoce=true;
    private void showUpladeDiloge(final String key, final Data_SupCategrory item) {
        Log.e("---1",item.getPriceD());
        Log.e("---2",item.getPriceS());
        Log.e("---3",item.getPricem());
        Log.e("---4",item.getPriceL());

        switchPriceChoce=item.getSwitchPrice();
        switchSugarChoce=item.getSwitchSugar();
        switchMilkChoce=item.getSwitchMilk();
        switchStyleChoce=item.getSwitchStyle();

        final AlertDialog.Builder builder=new AlertDialog.Builder(SupCategory.this);
        builder.setTitle("ONE MORE STEP!");
        builder.setMessage("UPDATE ITEM");

        LayoutInflater inflater=SupCategory.this.getLayoutInflater();
        View add_menu=inflater.inflate(R.layout.add_product,null);

        Switch switchPrice,switchStyle,switchMilk,switchSugar;

        switchPrice = add_menu.findViewById(R.id.switchPrice);
        switchPrice.setChecked(item.getSwitchPrice());
        switchPrice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("0000",isChecked+"");
                switchPriceChoce=isChecked;
            }
        });

        switchStyle = add_menu.findViewById(R.id.switchStyle);
        switchStyle.setChecked(item.getSwitchStyle());
        switchStyle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("0000",isChecked+"");
                switchStyleChoce=isChecked;
            }
        });

        switchMilk = add_menu.findViewById(R.id.switchMilk);
        switchMilk.setChecked(item.getSwitchMilk());
        switchMilk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("0000",isChecked+"");
                switchMilkChoce=isChecked;
            }
        });

        switchSugar = add_menu.findViewById(R.id.switchSugar);
        switchSugar.setChecked(item.getSwitchSugar());
        switchSugar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("0000",isChecked+"");
                switchSugarChoce=isChecked;
            }
        });
//        ---

        priseDa = add_menu.findViewById(R.id.ed_price_D);
        priseDa.setText("sss");
        priseDa.setText(""+item.getPriceD().toString());

        priceSa = add_menu.findViewById(R.id.ed_price_S);
        priceSa.setText(item.getPriceS().toString());

        pricema = add_menu.findViewById(R.id.ed_price_m);
        pricema.setText(item.getPricem());

        priceLa = add_menu.findViewById(R.id.ed_price_L);
        priceLa.setText(item.getPriceL().toString());

        titlea = add_menu.findViewById(R.id.ed_title_add);
        titlea.setText(item.getTitle().toString());


        bu_select=add_menu.findViewById(R.id.bu_selectimage_home);
        bu_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_Image();
            }
        });



        builder.setView(add_menu);
        builder.setIcon(R.drawable.ic_shopping);
        builder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (titlea.getText().toString().isEmpty()&&priseDa.getText().toString().isEmpty()){
                    titlea.setError("Enter Name Product !!");
                    titlea.setFocusable(true);

                }else {
                    ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
                        item.setTitle(titlea.getText().toString().trim());
                        item.setPriceD(priseDa.getText().toString().trim());
                        item.setPriceS(priceSa.getText().toString().trim());
                        item.setPricem(pricema.getText().toString().trim());
                        item.setPriceL(priceLa.getText().toString().trim());
//                        item.set(price.getText().toString().trim());
                        ChangeImageCat(key,item,dialogInterface);
                    }else{
                        Toast.makeText(SupCategory.this, "Check Your Network", Toast.LENGTH_SHORT).show();
                    }

                }
                //dialogInterface.dismiss();
            }


        });

        builder.show();

    }

    private void ChangeImageCat(final String key, final Data_SupCategrory item, final DialogInterface dialogInterface) {
        mDialog=new ProgressDialog(SupCategory.this);
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
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Products").child(key);
                            HashMap<String, Object> usermap = new HashMap<>();
//                            usermap.put("name", item.getName());
//                            usermap.put("description", item.getDescription());
//                            usermap.put("price", item.getPrice());
//                            usermap.put("idCat", idCat);
//                            usermap.put("discount", item.getDiscount());
//                            usermap.put("image",uri.toString());

                            usermap.put("title", titlea.getText().toString().trim());
                            usermap.put("priceD", priseDa.getText().toString().trim());
                            usermap.put("priceS", priceSa.getText().toString().trim());
                            usermap.put("pricem", pricema.getText().toString().trim());
                            usermap.put("priceL", priceLa.getText().toString().trim());
                            usermap.put("image",uri.toString());
                            usermap.put("switchSugar",switchSugarChoce);
                            usermap.put("switchPrice",switchPriceChoce);
                            usermap.put("switchStyle",switchStyleChoce);
                            usermap.put("switchMilk",switchMilkChoce);
                            usermap.put("idCat",idCat);

                            databaseReference.setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(getApplicationContext(), SupCategory.class);
                                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("id",idCat);
                                        startActivity(intent);
                                        Toast.makeText(SupCategory.this, "add", Toast.LENGTH_SHORT).show();
                                        mDialog.dismiss();
                                        finish();
                                        dialogInterface.dismiss();
                                        saveUri=null;
                                    } else {
                                        mDialog.dismiss();
//                                email.setError("Set your Data must have @ and .com");
//                                password.setError("Must password greater than 6 ");
                                        Toast.makeText(SupCategory.this, "trey again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });

                }
            });


        }
        else{
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Products").child(key);
            HashMap<String, Object> usermap = new HashMap<>();
            usermap.put("title", titlea.getText().toString().trim());
            usermap.put("priceD", priseDa.getText().toString().trim());
            usermap.put("priceS", priceSa.getText().toString().trim());
            usermap.put("pricem", pricema.getText().toString().trim());
            usermap.put("priceL", priceLa.getText().toString().trim());
            usermap.put("image",item.getImage());
            usermap.put("switchSugar",switchSugarChoce);
            usermap.put("switchPrice",switchPriceChoce);
            usermap.put("switchStyle",switchStyleChoce);
            usermap.put("switchMilk",switchMilkChoce);
            usermap.put("idCat",idCat);

            databaseReference.setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(getApplicationContext(), SupCategory.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("id",idCat);
                        startActivity(intent);
//                        Toast.makeText(SingelItem.this, "add", Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                        dialogInterface.dismiss();
                        saveUri=null;
                        finish();
                    } else {
                        mDialog.dismiss();
//                                email.setError("Set your Data must have @ and .com");
//                                password.setError("Must password greater than 6 ");
                        Toast.makeText(SupCategory.this, "trey again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }


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
            bu_select.setText("Image Select");

        }

    }


}
