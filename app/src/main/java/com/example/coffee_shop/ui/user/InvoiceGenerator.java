package com.example.coffee_shop.ui.user;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.coffee_shop.database_local.DataOrderLocal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class InvoiceGenerator {

    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 101;

    public static void generateInvoice(Context context, String idUser, String address, String total,
                                       String status, String totalDiscount, String totalDue, String locationDriver,
                                       List<DataOrderLocal> food, int totalPrice, int count) {

        // Check for permission to write to external storage
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
            return;
        }

        // Create a new PdfDocument
        PdfDocument document = new PdfDocument();

        // Create a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();

        // Start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        // Get the canvas for drawing
        Canvas canvas = page.getCanvas();

        // Create a Paint instance
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(12);

        // Write invoice details
        canvas.drawText("Invoice Details:", 20, 40, paint);
        canvas.drawText("User ID: " + idUser, 20, 60, paint);
        canvas.drawText("Address: " + address, 20, 80, paint);
        canvas.drawText("Total: " + total, 20, 100, paint);
        canvas.drawText("Status: " + status, 20, 120, paint);
        canvas.drawText("Total Discount: " + totalDiscount, 20, 140, paint);
        canvas.drawText("Total Due: " + totalDue, 20, 160, paint);
        canvas.drawText("Location Driver: " + locationDriver, 20, 180, paint);

        // Write food details
        canvas.drawText("Food Details:", 20, 220, paint);
        int y = 240;
        for (DataOrderLocal item : food) {
            canvas.drawText("Product ID: " + item.getId(), 20, y, paint);
            canvas.drawText("Product Name: " + item.getProdectname(), 20, y + 20, paint);
            canvas.drawText("Product Price: " + item.getProdectprice(), 20, y + 40, paint);
            canvas.drawText("Product Amount: " + item.getProdectCount(), 20, y + 60, paint);
            // Add more fields as needed
            y += 80;
        }

        // Write total price and count
        canvas.drawText("Total Price: " + totalPrice, 20, y, paint);
        canvas.drawText("Count: " + count, 20, y + 20, paint);

        // Finish the page
        document.finishPage(page);

        // Save the document to a file
        File file = new File(context.getExternalFilesDir(null), "invoice.pdf");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            fos.close();
            Toast.makeText(context, "Invoice created successfully", Toast.LENGTH_SHORT).show();

            // Open the generated PDF file
            openPdfFile(context, file);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error creating invoice: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // Close the document
        document.close();
    }

    private static void openPdfFile(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No PDF viewer installed", Toast.LENGTH_SHORT).show();
        }
    }
}

