package com.example.coffee_shop.ui.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffee_shop.R;


public class SupCategoryUser_ViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener  {


    public TextView title;
    public ImageView images;

    com.example.coffee_shop.ui.InterFace.itemOnClickListener itemOnClickListener;

    public SupCategoryUser_ViewHolder(@NonNull View itemView) {
        super(itemView);


        title =(TextView) itemView.findViewById(R.id.text_name_contestfrind);
        images= (ImageView) itemView.findViewById(R.id.imagefone_contextfrind);


        itemView.setOnClickListener(this);
    }

    public void setItemOnClickListener(com.example.coffee_shop.ui.InterFace.itemOnClickListener itemOnClickListener) {

        this.itemOnClickListener = itemOnClickListener;
    }

    @Override
    public void onClick(View view) {
        itemOnClickListener.onClick(view,getAdapterPosition(),false);

    }

}

