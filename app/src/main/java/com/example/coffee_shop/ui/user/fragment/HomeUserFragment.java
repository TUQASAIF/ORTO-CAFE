package com.example.coffee_shop.ui.user.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.coffee_shop.R;
import com.example.coffee_shop.ui.ViewHolder.MenuViewHolder;
import com.example.coffee_shop.ui.admin.SupCategory;
import com.example.coffee_shop.ui.admin.frragments.FragmentHome;
import com.example.coffee_shop.ui.model.Data_Category;
import com.example.coffee_shop.ui.user.My_Orders;
import com.example.coffee_shop.ui.user.SupCategoryUser;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class HomeUserFragment extends Fragment
        implements  SwipeRefreshLayout.OnRefreshListener  {


    public HomeUserFragment() {
        // Required empty public constructor
    }
    RecyclerView recyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;

    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;


    FirebaseAuth auth;
    StorageReference storageReference;
    FirebaseStorage storage;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_home, container, false);

        ImageView checkout = view.findViewById(R.id.checkout);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(getActivity(), My_Orders.class);
                 startActivity(intent);
            }
        });

        storage= FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("category");

        recyclerView=view.findViewById(R.id.recycal_home);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(HomeUserFragment.this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
                    mSwipeRefreshLayout.setRefreshing(true);
                    startUI();
                }else{
                    Toast.makeText(getActivity(), "Check Your Network", Toast.LENGTH_SHORT).show();
                }

            }
        });


        return view;
    }

    @Override
    public void onRefresh() {
        startUI();
    }

    private FirebaseRecyclerAdapter<Data_Category, MenuViewHolder> firebaseRecyclerAdapter;

    private void startUI() {
        FirebaseRecyclerOptions<Data_Category> options =
                new FirebaseRecyclerOptions.Builder<Data_Category>()
                        .setQuery(databaseReference, Data_Category.class)
                        .build();


//        FirebaseRecyclerAdapter<Data_Category, MenuViewHolder>
        firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Data_Category, MenuViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull MenuViewHolder viewHolder, @SuppressLint("RecyclerView") int position, Data_Category model) {
                        final String postKey = getRef(viewHolder.getAdapterPosition()).getKey();
//                        Toast.makeText(Home.this, "???"+ postKey, Toast.LENGTH_SHORT).show();

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                Toast.makeText(Home.this, "!!!!"+snapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();

                                mSwipeRefreshLayout.setRefreshing(false);
                                viewHolder.posttext.setText(model.getName());
                                Picasso.get().load(model.getImage()).into(viewHolder.images);

                                viewHolder.setItemOnClickListener((view, position1, isLongClick) -> {
                                    Intent intent=new Intent(getActivity(), SupCategoryUser.class);
                                    intent.putExtra("id",""+getRef(position).getKey());
                                    intent.putExtra("title",""+model.getName());

                                    startActivity(intent);
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                mSwipeRefreshLayout.setRefreshing(false);
                                // calling on cancelled method when we receive
                                // any error or we are not able to get the data.
                                Toast.makeText(getActivity(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
                        return new MenuViewHolder(view);
                    }
                };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        //  mSwipeRefreshLayout.setRefreshing(false);

    }

}