package com.example.coffee_shop.ui.admin.frragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.coffee_shop.R;
import com.example.coffee_shop.database_local.DataOrderLocal;
import com.example.coffee_shop.ui.ViewHolder.OrderViewHolder;
import com.example.coffee_shop.ui.model.DataReqeste;
import com.example.coffee_shop.ui.user.My_Orders;
import com.example.coffee_shop.ui.user.Show_final_OrderNet;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class FragmentOrders extends Fragment  implements SwipeRefreshLayout.OnRefreshListener{

    public FragmentOrders() {
        // Required empty public constructor
    }

    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    RecyclerView recyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_orders, container, false);


        recyclerView = view.findViewById(R.id.recycal_home);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Request");
        //databaseReference.keepSynced(true);//to catch data if net offline

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                ConnectivityManager conMgr = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {

                    mSwipeRefreshLayout.setRefreshing(true);
                    startUI();
                } else {
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

    private FirebaseRecyclerAdapter<DataReqeste, OrderViewHolder> firebaseRecyclerAdapter;

    private void startUI() {
        FirebaseRecyclerOptions<DataReqeste> options =
                new FirebaseRecyclerOptions.Builder<DataReqeste>()
                        .setQuery(databaseReference, DataReqeste.class)
                        .build();

        firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DataReqeste, OrderViewHolder>(options) {
                    @NonNull
                    @Override
                    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orderstatus, parent, false);
                        return new OrderViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull OrderViewHolder viewHolder, @SuppressLint("RecyclerView") int position, DataReqeste model) {

                        final String postke = getRef(position).getKey();
                        databaseReference.child(postke).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                mSwipeRefreshLayout.setRefreshing(false);
                                ;

                                viewHolder.addrese.setText(model.getFood().size()+" Item");
//                                viewHolder.addrese.setText(model.getAddress());
                                viewHolder.orderid.setText(postke);
                                viewHolder.status.setText(convertcode(model.getStatus()));

                                DatabaseReference databaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("User");
                                databaseReferenceUser.child(model.getIduser()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        mSwipeRefreshLayout.setRefreshing(false);
                                        final String phoneuser = dataSnapshot.child("phone").getValue().toString();
                                        viewHolder.phone.setText(phoneuser);



                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        mSwipeRefreshLayout.setRefreshing(false);
                                    }
                                });

                                if (model.getStatus().equals("0") || model.getStatus().equals("1")){
                                    viewHolder.chat.setVisibility(View.GONE);
                                }

                                viewHolder.chat.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                    }
                                });


                                viewHolder.payment.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                    }
                                });

                                ArrayList<String> dataList = new ArrayList<>();
                                for (DataOrderLocal data : model.getFood()) {
                                    // Assuming DataOrderLocal has a method to provide string representation
                                    dataList.add(data.toString()); // Replace toString() with the appropriate method
                                }
                                viewHolder.showorder.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        List<DataOrderLocal> foodList = model.getFood();

// Convert the list to a JSON string
                                        Gson gson = new Gson();
                                        String json = gson.toJson(foodList);

                                        Intent intent=new Intent(getActivity(), Show_final_OrderNet.class);
                                        intent.putExtra("id",getRef(position).getKey());
                                        intent.putExtra("location",model.getAddress());
                                        intent.putExtra("total",model.getTotal());
                                        intent.putExtra("note",model.getNote());
                                        intent.putExtra("phone_frind",model.getPhoneFrind());
                                        intent.putExtra("date", model.getDate());
                                        intent.putExtra("Current_date", model.getCurrent_date());
//                                        intent.putStringArrayListExtra("data",dataList);
                                        intent.putExtra("data", json);
                                        intent.putExtra("iduser", model.getIduser());

                                        startActivity(intent);
                                    }
                                });

                                if (model.getStatus().equals("3")){
                                    viewHolder.deleteorder.setVisibility(View.GONE);
                                    viewHolder.payment.setVisibility(View.GONE);
                                }
                                viewHolder.deleteorder.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                                        builder.setTitle("DO YOU WANT DELETE !");
                                        builder.setMessage("DO YOU WANT DELETE THIS ORDER  ");

                                        builder.setIcon(R.drawable.baseline_drive_file_rename_outline_24);
                                        builder.setPositiveButton("Delete ", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                                                if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
                                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Request");
                                                    databaseReference.child(postke).removeValue();
                                                }else{
                                                    Toast.makeText(getActivity(), "Check Your Network", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                        builder.setNegativeButton("no ", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        });
                                        builder.show();
                                    }
                                });
                            }


                            private String convertcode(String status) {

                                if (status.equals("0"))
                                    return "ORDER";
                                else if (status.equals("1"))
                                    return "Working On";
                                else if (status.equals("2"))
                                    return "On My Way";
                                else
                                    return "Shipped";
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }

//                    @NonNull
//                    @Override
//                    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_singel_food, parent, false);
//                        return new MenuViewHolder(view);
//                    }
                };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}