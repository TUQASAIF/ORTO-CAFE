package com.example.coffee_shop.database_local;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;

public class Database_order_local {

        private final static String DATABASE_NAME = "ORDERPRODUCT";
        private final static String DATABASE_Table = "orderProduct";
        private final int DATABASE_VERSION = 2;
        private final String KEY_ID = "_id";

        private final String KEY_prodectID = "_prodectID";
        private final String KEY_imageurl = "_imageurl";
        private final String KEY_prodectname = "_prodectname";
        private final String KEY_prodectCount = "_prodectCount";
        private final String KEY_prodectprice = "_prodectprice";
        private final String KEY_prodectNotes = "_prodectdiscription";
        private final String KEY_prodectSize = "_prodectSize";
        private final String KEY_Milk = "_prodectMilk";
        private final String KEY_Sugaure = "KEY_Sugaure";
        private final String KEY_Time = "KEY_Time";
        private final String KEY_Style = "_KEY_Style";
        private final String KEY_PhoneFrind = "_KEY_PhoneFrind";

        // prodectID ,imageurl , prodectname , prodectCount , prodectprice, prodectNotes,
    //   prodectSize ,milk ,sugaure , style ,phoneFrind

        // KEY_PhoneFrind , KEY_Style ,KEY_Time , KEY_Sugaure , KEY_Milk , KEY_prodectSize

        private final Context context;

        SQLiteDatabase sqLiteDatabase;
        DBhelber dBhelber;

        public Database_order_local(Context context) {
            this.context = context;
        }

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private class DBhelber extends SQLiteOpenHelper {
            public DBhelber(Context context) {
                super(context, DATABASE_NAME, null, DATABASE_VERSION);
            }

            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL("CREATE TABLE " + DATABASE_Table + "(" + KEY_ID
                        + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_prodectID
                        + " TEXT NOT NULL,"+ KEY_prodectCount
                        + " TEXT NOT NULL,"+ KEY_prodectNotes
                        + " TEXT NOT NULL,"+ KEY_prodectprice

                        + " TEXT NOT NULL," + KEY_PhoneFrind
                        + " TEXT NOT NULL," + KEY_Style
                        + " TEXT NOT NULL," + KEY_Time
                        + " TEXT NOT NULL," + KEY_Sugaure
                        + " TEXT NOT NULL," + KEY_Milk
                        + " TEXT NOT NULL," + KEY_prodectSize

                        + " TEXT NOT NULL," + KEY_prodectname
                        + " TEXT NOT NULL," + KEY_imageurl
                        + " TEXT NOT NULL);"
                );

            }


            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL("Drop Table iF Exists " + DATABASE_Table);
                onCreate(db);

            }
        }
        public void open() {
            //use to open class Dbhebel and cinect with databasehelber
            dBhelber = new DBhelber(context);
            sqLiteDatabase = dBhelber.getWritableDatabase();
            sqLiteDatabase=dBhelber.getReadableDatabase();
        }


        public void insert(String name,String id,String count,String price,String discription,String imageurl,
                           String  phoneFrind,String  style,String  time,String  sugaure ,String  milk,
                           String  size
                           ) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(KEY_PhoneFrind,phoneFrind);
            contentValues.put(KEY_Style,style);
            contentValues.put(KEY_Time,time);
            contentValues.put(KEY_Sugaure,sugaure);
            contentValues.put(KEY_Milk,milk);
            contentValues.put(KEY_prodectSize,size);

            contentValues.put(KEY_prodectname,name);
            contentValues.put(KEY_prodectID,id);
            contentValues.put(KEY_imageurl,imageurl);
            contentValues.put(KEY_prodectCount, count);
            contentValues.put(KEY_prodectprice, price);
            contentValues.put(KEY_prodectNotes, discription);

            sqLiteDatabase.insert(DATABASE_Table, null, contentValues);
             //Log.e("zdiaax>>>>>>>>>>>>",discription);
        }


        public boolean chkDB(){
            boolean chk = false;
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + DATABASE_Table, null);

            if (cursor!=null) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    chk = true;
                }
            }
            else{
                chk = false;
            }
            return chk;
        }

        @SuppressLint("Range")
        public ArrayList getallarrylit_check() {//to get last row
            ArrayList<DataOrderLocal> products = new ArrayList<>();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM orderProduct", null);
            if (cursor!=null) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    DataOrderLocal product = new DataOrderLocal();
                    product.setId(cursor.getString(cursor.getColumnIndex(KEY_ID)));;


                    product.setProdectname(cursor.getString(cursor.getColumnIndex(KEY_prodectname)));
                    product.setProdectID(cursor.getString(cursor.getColumnIndex(KEY_prodectID)));
                    product.setImageurl(cursor.getString(cursor.getColumnIndex(KEY_imageurl)));


                    product.setProdectname(cursor.getString(cursor.getColumnIndex(KEY_prodectname)));
                    product.setProdectCount(cursor.getString(cursor.getColumnIndex(KEY_prodectCount)));
                    product.setProdectprice(cursor.getString(cursor.getColumnIndex(KEY_prodectprice)));

                    product.setMilk(cursor.getString(cursor.getColumnIndex(KEY_Milk)));
                    product.setProdectNotes(cursor.getString(cursor.getColumnIndex(KEY_prodectNotes)));
                    product.setProdectSize(cursor.getString(cursor.getColumnIndex(KEY_prodectSize)));

                    product.setSugaure(cursor.getString(cursor.getColumnIndex(KEY_Sugaure)));
                    product.setStyle(cursor.getString(cursor.getColumnIndex(KEY_Style)));
                    product.setPhoneFrind(cursor.getString(cursor.getColumnIndex(KEY_PhoneFrind)));


                    products.add(product);
                }
            }else {
                DataOrderLocal product = new DataOrderLocal();
                product.setId("");
                products.add(product);
            }
            return products;

        }

//    DELETE FROM customers

        public void remove() {
        // String deleat=String.format("DELETE FROM Data_orderFood");
        //sqLiteDatabase.execSQL(deleat);
        context.deleteDatabase(DATABASE_NAME);
    }


    public boolean remove_item(String id_item) {
        boolean chk = false;
        Cursor cursor = sqLiteDatabase.rawQuery(" DELETE  FROM orderProduct"+ " WHERE "
                + KEY_ID + " = '"+ id_item + "'" , null);

        if (cursor!=null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                chk = true;
            }
        }
        else{
            chk = false;
        }
        return chk;

    }



        public boolean cek_isExist_orNot(String id) {//to get last row
            ArrayList<DataOrderLocal> products = new ArrayList<>();
            //Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM importnote"+ " WHERE " + KEY_phone + " = '"+ phone + "'" + "  ORDER BY _id DESC LIMIT 1", null);
            //Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM orderFood", null);

            boolean chk = false;
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM orderProduct"+ " WHERE " + KEY_prodectID + " = '"+ id + "'" + "  ORDER BY _id DESC LIMIT 1", null);

            if (cursor!=null) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    chk = true;
                }
            }
            else{
                chk = false;
            }
            return chk;

        }

//        public ArrayList getallarrylit_singelrow(String phone) {
//            phone="uploded";
//            ArrayList<Data_notes> products = new ArrayList<>();
//            // Cursor cursornum = sqLiteDatabase.rawQuery("SELECT * FROM importnote"+ " WHERE " + KEY_phone + " = '"+ phone + "'" + "", null);
//            Cursor cursor = sqLiteDatabase.rawQuery( "SELECT * FROM " + DATABASE_Table + " WHERE " + KEY_satesnote + " = '"+ "uploded" + "'" + " ORDER BY _id  ", null);
//
//            //Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM azllemails where" + KEY_phone + " = '"+ phone + "'" + " ORDER BY _id DESC LIMIT 1", null);
//            if (cursor!=null) {
//                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
//                    Data_notes product = new Data_notes();
//                    product.setLastrow(cursor.getString(cursor.getColumnIndex(KEY_IDnet)));
//                    product.setIdnetsingel(cursor.getString(cursor.getColumnIndex(KEY_IDnet)));
//                    product.setNotesingel(cursor.getString(cursor.getColumnIndex(KEY_note)));
//                    product.setNamecustmersingel(cursor.getString(cursor.getColumnIndex(KEY_namecustmer)));
//                    product.setSatesnotesingel(cursor.getString(cursor.getColumnIndex(KEY_satesnote)));
//                    product.setPhonesingel(cursor.getString(cursor.getColumnIndex(KEY_phone)));
//                    product.setCountdatabase_allnotes(""+cursor.getCount());
////                Log.v("zdiaacu",""+cursornum.getCount());
//                    products.add(product);
//                }
//            }else {
//                Data_notes product = new Data_notes();
//                product.setLastrow("");
//                products.add(product);
//            }
//            return products;
//
//        }
//
//
//
//
        public boolean up_data(String amount ,String id ){
            //SQLiteDatabase db=this.getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put(KEY_prodectCount,amount);

            // db.update("mytable",contentValues,"id=?",new String[]{id});
            sqLiteDatabase.update(DATABASE_Table, contentValues, KEY_ID + "=" + id, null);
            // Cursor cursor = sqLiteDatabase.rawQuery("UPDATE importnote "+ " set  " + KEY_satesnote + " = '"+ sates + "'" + " WHERE  " + KEY_ID + " = '"+ id + "'" + "", null);
//        Toast.makeText(context, ""+id, Toast.LENGTH_SHORT).show();
//        Toast.makeText(context, ""+sates, Toast.LENGTH_SHORT).show();
            return  true;
        }
//
//
}
