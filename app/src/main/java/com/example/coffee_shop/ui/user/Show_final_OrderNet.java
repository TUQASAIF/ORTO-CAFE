package com.example.coffee_shop.ui.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffee_shop.Permeation;
import com.example.coffee_shop.R;
import com.example.coffee_shop.database_local.DataOrderLocal;
import com.example.coffee_shop.ui.InterFace.itemOnClickListener;
import com.example.coffee_shop.ui.ViewHolder.Finailorder_fromnet_ViewHolder;
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
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
public class Show_final_OrderNet extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener  {

    DecimalFormat df ;


    private static final String TAG = "PdfCreatorActivity";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private File pdfFile;
    ImageView imgdownload;
    ArrayList<DataOrderLocal> MyList1;
    DataOrderLocal giftitemPOJO;
    Context context;
    DataOrderLocal name;
    DataOrderLocal price;

    DataOrderLocal date;

    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    RecyclerView recyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;

    String idorder_string,location_string,total_string;
    TextView idorder,location,text_note_finalorder,text_date,txtCurrent_date;

    ImageView print;
    String iduserFinal;
    List<DataOrderLocal> foodList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_final_order_net);

        // Define the locale for English
        Locale englishLocale = new Locale("en", "US");
        // Create DecimalFormatSymbols with English locale
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(englishLocale);
        // Set decimal separator to '.'
        symbols.setDecimalSeparator('.');
        df = new DecimalFormat("#.000", symbols);

        FirebaseUser firebaseUser;
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();


        context = this;
        giftitemPOJO = new DataOrderLocal();

        Bundle extras = getIntent().getExtras(); // to get move intent
        if (extras != null) {
            String a = extras.getString("id");
            String iduser = extras.getString("iduser");
            iduserFinal=iduser;
            String locations = extras.getString("location");
            String total = extras.getString("total");
            String note = extras.getString("note");
            String phone_frind = extras.getString("phone_frind");
            String date = extras.getString("date");
            String current_date = extras.getString("Current_date");
            if (a!=null){
                idorder_string= a;
                location_string=locations;
                total_string=total;
            }
            text_date=findViewById(R.id.text_date);
            if(date!=null && !date.isEmpty()){
                text_date.setText("TIME/DATE ORDER : "+date);
            }else {
                text_date.setVisibility(View.GONE);
            }

            txtCurrent_date=findViewById(R.id.txtCurrent_date);
            if(current_date!=null && !current_date.isEmpty()){
                txtCurrent_date.setText("DATE ORDER : "+current_date);
            }else {
                txtCurrent_date.setVisibility(View.GONE);
            }

            text_note_finalorder=findViewById(R.id.text_note_finalorder);
            if(note!=null && !note.isEmpty()){
                text_note_finalorder.setText("EXTRA NOTE : "+note);
            }else {
                text_note_finalorder.setVisibility(View.GONE);
            }

            TextView text_phone_frind_finalorder=findViewById(R.id.text_phone_frind_finalorder);
            if(phone_frind!=null&& !phone_frind.isEmpty()){
                text_phone_frind_finalorder.setText("FRIEND PHONE NO  (GIFT) : "+phone_frind);
            }else {
                text_phone_frind_finalorder.setVisibility(View.GONE);
            }
            String json = getIntent().getStringExtra("data");
            Gson gson = new Gson();
            Type type = new TypeToken<List<DataOrderLocal>>() {}.getType();
            foodList = gson.fromJson(json, type);
        }

        Permeation permeation=new Permeation();
        permeation.checkAndRequestPermissions(Show_final_OrderNet.this);


        idorder=findViewById(R.id.text_idorder_finalorder);
        idorder.setText("ID ORDER : "+idorder_string);
        location=findViewById(R.id.text_location_finalorder);

        DatabaseReference databaseReferenceUser;
        databaseReferenceUser= FirebaseDatabase.getInstance().getReference().child("User");
        databaseReferenceUser.child(iduserFinal).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("email")) {
                    final String emailUser=dataSnapshot.child("email").getValue(String.class).toString();
                    String email_st=emailUser;
                     email_st = (email_st != null) ? email_st : "";
                    location.setText("EMAIL : "+email_st);
                }

                String imagel_st = "";
                if (dataSnapshot.hasChild("image")) {
                    final String imagelUser=dataSnapshot.child("image").getValue(String.class).toString();
                    imagel_st=imagelUser;
                    imagel_st = (imagel_st != null) ? imagel_st : "";
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });




        recyclerView=findViewById(R.id.recycal_home);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Request");
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
        mSwipeRefreshLayout.setOnRefreshListener(Show_final_OrderNet.this);
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
                    Toast.makeText(Show_final_OrderNet.this, "Check Your Network", Toast.LENGTH_SHORT).show();
                }

            }
        });

        print=findViewById(R.id.print);

    }


    @Override
    public void onRefresh() {
        startUI();
    }

    private FirebaseRecyclerAdapter<DataOrderLocal, Finailorder_fromnet_ViewHolder> firebaseRecyclerAdapter;
    private void startUI() {

        MyList1 = new ArrayList<DataOrderLocal>();

        FirebaseRecyclerOptions<DataOrderLocal> options =
                new FirebaseRecyclerOptions.Builder<DataOrderLocal>()
                        .setQuery(databaseReference.child(idorder_string).child("food"), DataOrderLocal.class)
                        .build();

        firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DataOrderLocal, Finailorder_fromnet_ViewHolder>(options) {
                    @NonNull
                    @Override
                    public Finailorder_fromnet_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_finalorder_fromnet, parent, false);
                        return new Finailorder_fromnet_ViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull Finailorder_fromnet_ViewHolder viewHolder, int position, DataOrderLocal model) {
                        final String postke=getRef(position).getKey();

                        databaseReference.child(postke).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshot) {
//                                Toast.makeText(context, ">>"+dataSnapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
                                //to create pdf to fatora
                                giftitemPOJO = new DataOrderLocal();
                                giftitemPOJO.setProdectCount(model.getProdectCount());

                                giftitemPOJO.setProdectname(model.getProdectname());
                                giftitemPOJO.setProdectprice(model.getProdectprice());

                                MyList1.add(giftitemPOJO);



                                String postkesaa=dataSnapshot.getRef().getKey();
                                Log.e("json","json: _________>" + postkesaa);

                                mSwipeRefreshLayout.setRefreshing(false);

                                viewHolder.price.setText("PRICE : "+df.format(Double.parseDouble(model.getProdectprice())));
                                viewHolder.numfood.setText("COUNT : "+model.getProdectCount());
                                viewHolder.namefood.setText("ORDER : "+model.getProdectname());
                                viewHolder.text_milk_showfinalorderfromnet.setText("TYPE OF MILK : "+model.getMilk());
                                viewHolder.text_Type_of_cup.setText("CUP STYLE : "+model.getStyle());
                                viewHolder.text_sugare_of_cup.setText("NUMBER OF SUGAR: "+model.getSugaure());
                                viewHolder.size.setText("SIZE CUP: "+model.getProdectSize());

                                Picasso.get().load(model.getImageurl()).into(viewHolder.images);


                                viewHolder.setItemOnClickListener(new itemOnClickListener() {
                                    @Override
                                    public void onClick(View view, int position, boolean isLongClick) {

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                mSwipeRefreshLayout.setRefreshing(false);
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        mSwipeRefreshLayout.setRefreshing(false);

//        print.setOnClickListener(v -> {
//            InvoiceGenerator.generateInvoice(
//                    Show_final_OrderNet.this, firebaseUser.getEmail(),  "address",  "total",
//                    "String status", "String totalDiscount", "String totalDue", "String locationDriver",
//                    foodList
////                                            List<DataOrderLocal> food
//                    , 100, 5
//            );
//        });
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    createPdfWrapper();
                } catch (FileNotFoundException | DocumentException e) {
                    Toast.makeText(context, ""+e.toString(), Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(e);
                }
//                InvoiceGenerator.generateInvoice(
//                        Show_final_OrderNet.this, firebaseUser.getEmail(),  "address",  "total",
//                        "String status", "String totalDiscount", "String totalDue", "String locationDriver",
//                        foodList
//                        , 100, 5
//                );
            }
        });
    }




//    private void createPdfWrapper() throws FileNotFoundException, DocumentException {
//
//        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            // Permission is not granted, request it
//            Toast.makeText(context, "----", Toast.LENGTH_SHORT).show();
//            ActivityCompat.requestPermissions((Activity) context,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    1);
//        } else {
//            // Permission already granted, proceed with PDF creation
//            createPdf();
//        }
////        createPdf();
//
////        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
////        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
////            // Check if we should show the rationale dialog
////            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
////                // Show rationale dialog explaining why the permission is needed
////                showMessageOKCancel("You need to allow access to Storage",
////                        new DialogInterface.OnClickListener() {
////                            @Override
////                            public void onClick(DialogInterface dialog, int which) {
////                                // Request the permission
////                                ActivityCompat.requestPermissions((Activity) context,
////                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
////                                        REQUEST_CODE_ASK_PERMISSIONS);
////                            }
////                        });
////            } else {
////                Toast.makeText(context, "needed", Toast.LENGTH_SHORT).show();
////                // No rationale needed, request the permission directly
////                ActivityCompat.requestPermissions((Activity) context,
////                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
////                        REQUEST_CODE_ASK_PERMISSIONS);
////            }
////        } else {
////            // Permission already granted, proceed with PDF creation
////            Toast.makeText(context, "PDF", Toast.LENGTH_SHORT).show();
////            createPdf();
////        }
//    }

    private void createPdfWrapper() throws FileNotFoundException, DocumentException  {
//        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            // Permission is not granted, request it
//            ActivityCompat.requestPermissions((Activity) context,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    1);
//        } else {
//            // Permission already granted, proceed with PDF creation
//            createPdf();
//        }

        if (checkSelfPermission()) {
            createPdf();
        } else {
            requestPermission();
        }
    }

    private boolean checkSelfPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with PDF creation
//                createPdf();
                Toast.makeText(this, "-----------------", Toast.LENGTH_SHORT).show();

            } else {
                // Permission denied, show a message
                Toast.makeText(this, "???Permission denied. Cannot create PDF.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 101;


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void createPdf() throws FileNotFoundException, DocumentException {

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Bill");
//        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i(TAG, "Created a new directory for PDF");
        }

        Toast.makeText(context, "This Folder Saved in file Bill on device ", Toast.LENGTH_LONG).show();
        String pdfname = "ORTO CAFE"+idorder_string+".pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document(PageSize.A4);
        PdfPTable table = new PdfPTable(new float[]{3, 3, 3, 3});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(50);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell("NAME");
        table.addCell("PRICE");
        table.addCell("COUNT");
        table.addCell("TOTAL");
        table.setHeaderRows(1);
        PdfPCell[] cells = table.getRow(0).getCells();
        for (int j = 0; j < cells.length; j++) {
            cells[j].setBackgroundColor(BaseColor.GRAY);
        }
//        Toast.makeText(context, ""+MyList1.size(), Toast.LENGTH_SHORT).show();
        for (int i = 0; i < MyList1.size(); i++) {
            name = MyList1.get(i);
            date = MyList1.get(i);
            price = MyList1.get(i);

            String namen = name.getProdectname();
            String pricen = df.format(Double.parseDouble(price.getProdectprice()))+"";
            String amounten = date.getProdectCount();

            double totalen = Double.parseDouble(pricen) * Double.parseDouble(amounten);

            table.addCell(namen);
            table.addCell(pricen);
            table.addCell(amounten);
            table.addCell(""+totalen);

        }

//        System.out.println("Done");

        PdfWriter.getInstance(document, output);
        document.open();
        Font f = new Font(Font.FontFamily.TIMES_ROMAN, 30.0f, Font.BOLD, BaseColor.BLACK);
        Font g = new Font(Font.FontFamily.TIMES_ROMAN, 20.0f, Font.NORMAL, BaseColor.BLACK);
        document.add(new Paragraph("ORTO CAFE \n\n", f));
        document.add(new Paragraph("ID ORDER : "+idorder_string, g));
        document.add(new Paragraph("TOTAL ORDER : "+total_string+"\n\n", g));
        document.add(table);

//        for (int i = 0; i < MyList1.size(); i++) {
//            document.add(new Paragraph(String.valueOf(MyList1.get(i))));
//        }
        document.close();
        previewPdf();
    }

    private void previewPdf() {

//        PackageManager packageManager = context.getPackageManager();
//        Intent testIntent = new Intent(Intent.ACTION_VIEW);
//        testIntent.setType("application/pdf");
//        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
//        if (list.size() > 0) {
////            Intent intent = new Intent();
////            intent.setAction(Intent.ACTION_VIEW);
////            Uri uri = Uri.fromFile(pdfFile);
////            intent.setDataAndType(uri, "application/pdf");
////            startActivity(intent);
//        } else {
//            Toast.makeText(context, "Download a PDF Viewer to see the generated PDF", Toast.LENGTH_SHORT).show();
//        }
    }
}

