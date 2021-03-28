package com.omar.quranwazkar;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class MyListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> maintitle;
 //   private final Integer[] imags;

    public MyListAdapter(Activity context, ArrayList<String> maintitle ) {
        super(context , R.layout.quran_list , maintitle);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.maintitle=maintitle;
     //   this.imags = imags;


    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.quran_list, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title_quran);
      //  ImageView imageView = rowView.findViewById(R.id.list_img);
        titleText.setText(maintitle.get(position));
       // imageView.setImageResource(imags[position]);


        return rowView;

    };

}
