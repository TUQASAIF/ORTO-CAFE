package com.example.coffee_shop.ui.ViewHolder;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffee_shop.R;


public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView titeltext,posttext;
    public ImageView images;

    com.example.coffee_shop.ui.InterFace.itemOnClickListener itemOnClickListener;

    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);


        posttext=(TextView) itemView.findViewById(R.id.text_name_contestfrind);
        images= (ImageView) itemView.findViewById(R.id.imagefone_contextfrind);

        itemView.setOnClickListener(this);
    }

    public void setItemOnClickListener( com.example.coffee_shop.ui.InterFace.itemOnClickListener itemOnClickListener) {

        this.itemOnClickListener = itemOnClickListener;
    }

    @Override
    public void onClick(View view) {
        itemOnClickListener.onClick(view,getAdapterPosition(),false);

    }
}
