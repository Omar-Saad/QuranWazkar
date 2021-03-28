package com.omar.quranwazkar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class TasbehScrollingActivity extends AppCompatActivity {

    CardView card1;
    CardView card2;
    CardView card3;
    CardView card4;
    CardView card5;
    CardView card6;
    CardView card7;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_tasbeh);

        card1 = findViewById(R.id.caed1_tasbeh);
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TasbehScrollingActivity.this , AzkarActivity.class);
                intent.putExtra("cardNo",5);
                startActivity(intent);

            }
        });
        card2 = findViewById(R.id.card2_tasbeh);
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TasbehScrollingActivity.this , AzkarActivity.class);
                intent.putExtra("cardNo",6);
                startActivity(intent);

            }
        });
        card3 = findViewById(R.id.card3_tasbeh);
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TasbehScrollingActivity.this , AzkarActivity.class);
                intent.putExtra("cardNo",7);
                startActivity(intent);

            }
        });
        card4 = findViewById(R.id.card4_tasbeh);
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TasbehScrollingActivity.this , AzkarActivity.class);
                intent.putExtra("cardNo",8);
                startActivity(intent);

            }
        });
        card5 = findViewById(R.id.card5_tasbeh);
        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TasbehScrollingActivity.this , AzkarActivity.class);
                intent.putExtra("cardNo",9);
                startActivity(intent);

            }
        });

        card6 = findViewById(R.id.card6_tasbeh);
        card6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TasbehScrollingActivity.this , AzkarActivity.class);
                intent.putExtra("cardNo",10);
                startActivity(intent);

            }
        });


        card7 = findViewById(R.id.card7_tasbeh);
        card7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TasbehScrollingActivity.this , AzkarActivity.class);
                intent.putExtra("cardNo",11);
                startActivity(intent);

            }
        });



    }
}