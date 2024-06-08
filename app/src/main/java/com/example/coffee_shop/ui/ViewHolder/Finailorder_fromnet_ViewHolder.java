package com.example.coffee_shop.ui.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffee_shop.R;

public class Finailorder_fromnet_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView namefood,numfood ,size,price,text_milk_showfinalorderfromnet,text_Type_of_cup,text_sugare_of_cup;
    public ImageView images;

    com.example.coffee_shop.ui.InterFace.itemOnClickListener itemOnClickListener;

    public Finailorder_fromnet_ViewHolder(@NonNull View itemView) {
        super(itemView);


        namefood=(TextView) itemView.findViewById(R.id.text_namefood_showfinalorderfromnet);
        numfood=(TextView) itemView.findViewById(R.id.text_numfood_showfinalorderfromnet);
        price=(TextView) itemView.findViewById(R.id.text_price_showfinalorderfromnet);
        text_milk_showfinalorderfromnet=(TextView) itemView.findViewById(R.id.text_milk_showfinalorderfromnet);
        text_Type_of_cup=(TextView) itemView.findViewById(R.id.text_Type_of_cup);
        text_sugare_of_cup=(TextView) itemView.findViewById(R.id.text_sugare_of_cup);
        size=(TextView) itemView.findViewById(R.id.text_price_showfinalorderfromnet);

        images= (ImageView) itemView.findViewById(R.id.image_showfinalorderfromnet);



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
