package com.example.coffee_shop.ui.user;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.daimajia.swipe.util.Attributes;
import com.example.coffee_shop.R;
import com.example.coffee_shop.database_local.DataOrderLocal;
import com.example.coffee_shop.database_local.Database_order_local;
import com.example.coffee_shop.ui.model.DataReqeste;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class CartActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener  {

    FirebaseUser firebaseUser;
    RecyclerView recyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<DataOrderLocal> mDataSet;

    TextView totalprice ,total_due;

    Button addtofirebase;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    Database_order_local database_allnotes;

    String location_name;
    private final static int PLACE_PIKER_RESSULT=100;

    /**
     * Code used in requesting runtime permissions.
     */
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;


    private boolean mAlreadyStartedService = false;

    DecimalFormat df ;
    // dilog
    EditText my_location,ed_note_login,ed_date_login,ed_phoneFrind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        Locale englishLocale = new Locale("en", "US");
        // Create DecimalFormatSymbols with English locale
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(englishLocale);
        // Set decimal separator to '.'
        symbols.setDecimalSeparator('.');
        df = new DecimalFormat("#.000", symbols);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Request");

        totalprice = findViewById(R.id.text_totalprice_cartitem);
        total_due = findViewById(R.id.text_total_due_cartitem);


        recyclerView = findViewById(R.id.recycal_home);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(CartActivity.this);
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
                    mSwipeRefreshLayout.setRefreshing(false);
                    startUI();
                } else {
                    Toast.makeText(CartActivity.this, "Check Your Network", Toast.LENGTH_SHORT).show();
                }

            }
        });




        addtofirebase = findViewById(R.id.bu_addtofirebase_cartitem);
        addtofirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deiloglocation();

            }
        });
    }

    private void deiloglocation() {

        final AlertDialog.Builder builder1=new AlertDialog.Builder(CartActivity.this);
        builder1.setTitle("ONE MORE STEP!");
//        builder1.setMessage("Enter Your location");
        builder1.setCancelable(false);

        LayoutInflater inflater=CartActivity.this.getLayoutInflater();
        View add_menu=inflater.inflate(R.layout.add_order,null);

        my_location=add_menu.findViewById(R.id.ed_me_location_addorder);
        ed_note_login=add_menu.findViewById(R.id.ed_note_login);
        ed_phoneFrind=add_menu.findViewById(R.id.ed_phoneFrind);
        ed_date_login=add_menu.findViewById(R.id.ed_date_login);




        builder1.setView(add_menu);
//        builder1.setIcon(R.drawable.ic_shopping);
        builder1.setPositiveButton("UPLOAD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                if (my_location.getText().toString().isEmpty()&&my_location.getText().toString().isEmpty()){
//                    my_location.setError("Enter Your location !!");
//                    my_location.setFocusable(true);
//
//                }else {

                database_allnotes = new Database_order_local(CartActivity.this);
                database_allnotes.open();
                mDataSet = database_allnotes.getallarrylit_check();
                //String iduser, String address, String total, List<DataOrderLocal> food

//                Date d = new Date();
//                CharSequence s  = DateFormat.format("MMMM d, yyyy ", d.getTime());
                Date currentDate = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
                String s = dateFormat.format(currentDate);
                DataReqeste dataReqeste = new DataReqeste(
                        total_due.getText().toString().trim()
                        ,"0"
                        ,firebaseUser.getUid()
                        , my_location.getText().toString().trim()
                        , totalprice.getText().toString().trim()
                        , mDataSet,
                        ed_note_login.getText().toString(),
                        ed_phoneFrind.getText().toString(),
                        ed_date_login.getText().toString(),
                        s.toString()
                );

                databaseReference.child(String.valueOf(System.currentTimeMillis())).setValue(dataReqeste);
                database_allnotes.remove();
                Toast.makeText(CartActivity.this, "Done Add Request ", Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();
                finish();
//                }
            }


        });
        builder1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }


        });

        builder1.show();

    }



    private void startUI() {
        database_allnotes = new Database_order_local(CartActivity.this);
        database_allnotes.open();
        mDataSet = database_allnotes.getallarrylit_check();

        totalprice.setText("");
        if (mDataSet.size() < 0) {
            totalprice.setText("");
            //Toast.makeText(this, "ddd"+mDataSet.size(), Toast.LENGTH_SHORT).show();
        } else {
            double x = 0.0;
            double dis = 0.0;
            for (int i = 0; i < mDataSet.size(); i++) {
                DataOrderLocal dataOrderLocal = new DataOrderLocal();
                double price = Double.parseDouble(mDataSet.get(i).getProdectprice()) *
                        Double.parseDouble(mDataSet.get(i).getProdectCount());
                x += price;
                totalprice.setText("" + df.format(x));


                total_due.setText(""+(df.format(x+dis)));
            }
        }

        Adabtor_databaseLocal mAdapter = new Adabtor_databaseLocal(this, mDataSet);

//        mAdapter.setMode(Attributes.Mode.Single);
//        ((Adabtor_databaseLocal) mAdapter).setMode(Attributes.Mode.Single);

        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e("RecyclerView", "onScrollStateChanged");
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        mDataSet = new ArrayList<>();

    }


    @Override
    public void onRefresh() {
        startUI();
    }


    public class Adabtor_databaseLocal extends RecyclerView.Adapter<Adabtor_databaseLocal.SimpleViewHolder> {

        private Context mContext;
        private ArrayList<DataOrderLocal> studentList;

        public Adabtor_databaseLocal(Context context, ArrayList<DataOrderLocal> objects) {
            this.mContext = context;
            this.studentList = objects;
        }

        @Override
        public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
            return new SimpleViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
            final DataOrderLocal item = studentList.get(position);

            viewHolder.name.setText(item.getProdectname());
            Picasso.get().load(item.getImageurl()).into(viewHolder.image);

            double x = Double.parseDouble(item.getProdectCount()) * Double.parseDouble(item.getProdectprice());
            viewHolder.price.setText(df.format(Double.parseDouble(item.getProdectprice()))+"");
            viewHolder.price.setVisibility(View.GONE);
            viewHolder.amount.setText("TOTAL : " + df.format(x));
            viewHolder.num.setText(item.getProdectCount());

            viewHolder.deleate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handle delete button click
                    database_allnotes = new Database_order_local(mContext);
                    database_allnotes.open();
                    database_allnotes.remove_item(item.getId());
                    startUI();
                }
            });
            viewHolder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int x= Integer.parseInt(item.getProdectCount())+1;
                    viewHolder.num.setText(String.valueOf(x));
                    database_allnotes = new Database_order_local(mContext);
                    database_allnotes.open();

                    database_allnotes.up_data(""+viewHolder.num.getText(),item.getId());
                    startUI();
                }
            });
            viewHolder.minimize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int xx= Integer.parseInt(item.getProdectCount());
                    if(xx > 1){
                        int x= Integer.parseInt(item.getProdectCount())-1;
                        viewHolder.num.setText(String.valueOf(x));
                        database_allnotes = new Database_order_local(mContext);
                        database_allnotes.open();

                        database_allnotes.up_data(""+viewHolder.num.getText(),item.getId());
                        startUI();
                    }

                }
            });

        }

        @Override
        public int getItemCount() {
            return studentList.size();
        }

        public class SimpleViewHolder extends RecyclerView.ViewHolder {

            public TextView name,num;
            public TextView price;
            public TextView amount;
            public ImageView image, deleate;
            public ImageButton add,minimize;

            public SimpleViewHolder(View itemView) {
                super(itemView);
                add = itemView.findViewById(R.id.add);
                minimize = itemView.findViewById(R.id.minimize);
                name = itemView.findViewById(R.id.text_namefood_itemcart);
                num = itemView.findViewById(R.id.num);
                price = itemView.findViewById(R.id.text_price_itemcart);
                amount = itemView.findViewById(R.id.text_amunt_itemcart);
                image = itemView.findViewById(R.id.image_food_cart);
                deleate = itemView.findViewById(R.id.image_deleatfrom_cart);
            }
        }
    }
}
