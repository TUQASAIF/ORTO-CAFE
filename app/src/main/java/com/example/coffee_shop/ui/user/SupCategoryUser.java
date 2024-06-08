package com.example.coffee_shop.ui.user;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffee_shop.R;
import com.example.coffee_shop.ui.InterFace.itemOnClickListener;
import com.example.coffee_shop.ui.ViewHolder.SupCategoryUser_ViewHolder;
import com.example.coffee_shop.ui.admin.AddItem;
import com.example.coffee_shop.ui.admin.SupCategory;
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
import java.util.UUID;

public class SupCategoryUser extends AppCompatActivity  implements SwipeRefreshLayout.OnRefreshListener {

    String idCat;
    String titleAppBar;
    TextView textSup;

    FirebaseUser firebaseUser;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    SwipeRefreshLayout mSwipeRefreshLayout;

    StorageReference storageReference;
    FirebaseStorage storage;
    FirebaseRecyclerAdapter<Data_SupCategrory, SupCategoryUser_ViewHolder> adabtor;

    Button bu_select;

    Uri saveUri=null;
    ProgressDialog mDialog;
    int Pick_Image=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup_category_user);

        Bundle extras = getIntent().getExtras(); // to get move intent
        if (extras != null) {
            String a = extras.getString("id");
            String titlea = extras.getString("title");
            if (a!=null){
                idCat= a;
                titleAppBar= titlea;
                textSup=findViewById(R.id.textSup);
                textSup.setText(titlea);
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
        mSwipeRefreshLayout.setOnRefreshListener(SupCategoryUser.this);
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
                    Toast.makeText(SupCategoryUser.this, "Check Your Network", Toast.LENGTH_SHORT).show();
                }

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
                new FirebaseRecyclerAdapter<Data_SupCategrory, SupCategoryUser_ViewHolder>(options) {
                    @NonNull
                    @Override
                    public SupCategoryUser_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sup_cat_user, parent, false);
                        return new SupCategoryUser_ViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull SupCategoryUser_ViewHolder viewHolder, int position, Data_SupCategrory model) {
                        final String postKey = getRef(viewHolder.getAdapterPosition()).getKey();

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                mSwipeRefreshLayout.setRefreshing(false);

                                viewHolder.title.setText(""+model.getTitle().toString());
                                Picasso.get().load(model.getImage()).into(viewHolder.images);

                                viewHolder.setItemOnClickListener(new itemOnClickListener() {
                                    @Override
                                    public void onClick(View view, int position, boolean isLongClick) {
                                        Intent intent=new Intent(getApplicationContext(), ProductDetails.class);
                                        intent.putExtra("id",getRef(position).getKey());
                                        //intent.putExtra("true",);
                                        startActivity(intent);
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                mSwipeRefreshLayout.setRefreshing(false);
                                // calling on cancelled method when we receive
                                // any error or we are not able to get the data.
                                Toast.makeText(SupCategoryUser.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
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





}
