package com.example.coffee_shop.ui.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffee_shop.R;


public class SupCategory_ViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener , View.OnCreateContextMenuListener {


    public TextView title,priceD,priceS,pricem,priceL;
    public ImageView images;
    public ImageButton deleat_banner;

    com.example.coffee_shop.ui.InterFace.itemOnClickListener itemOnClickListener;

    public SupCategory_ViewHolder(@NonNull View itemView) {
        super(itemView);


        title =(TextView) itemView.findViewById(R.id.text_name_supcategry);
        priceD =(TextView) itemView.findViewById(R.id.text_priceD_supcategry);
        priceS =(TextView) itemView.findViewById(R.id.text_prices_supcategry);
        pricem =(TextView) itemView.findViewById(R.id.text_pricem_supcategry);
        priceL =(TextView) itemView.findViewById(R.id.text_pricel_supcategry);

        images= (ImageView) itemView.findViewById(R.id.image_supcategry);
        deleat_banner= (ImageButton) itemView.findViewById(R.id.imagebu_deleet_supcategry);

        itemView.setOnCreateContextMenuListener(this);

        itemView.setOnClickListener(this);
    }

    public void setItemOnClickListener(com.example.coffee_shop.ui.InterFace.itemOnClickListener itemOnClickListener) {

        this.itemOnClickListener = itemOnClickListener;
    }

    @Override
    public void onClick(View view) {
        itemOnClickListener.onClick(view,getAdapterPosition(),false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

        contextMenu.add(0,0,getAdapterPosition(),"UpDate");
    }


}

