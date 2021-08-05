package com.omar.quranwazkar.ui;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.omar.quranwazkar.util.JsonReader;
import com.omar.quranwazkar.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AzkarActivity extends AppCompatActivity {

    ViewPager2 viewPager;
    TextView count_tv;
    TextView remain_tv;
    RelativeLayout relativeLayout;
    private  int count=0;
    ProgressBar progressBar;
    private int countTotal=0;
    ArrayList<ViewPgaerModel> viewPgaerModels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_azkar);
        relativeLayout = findViewById(R.id.relative_layout_azkar);
        count_tv = findViewById(R.id.count_tv);
        remain_tv=findViewById(R.id.remain_tv);
        viewPager =  findViewById(R.id.view_pager);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        int cardNo = getIntent().getIntExtra("cardNo",2);
        if(cardNo==2)
      viewPgaerModels= getAzkarSbah();
        else if(cardNo==3)
            viewPgaerModels= getAzkarMsaa();
        else if(cardNo==4)
            viewPgaerModels= getAzkarNoom();
        else if(cardNo==5){
            viewPgaerModels = new ArrayList<>();
            viewPgaerModels.add(new ViewPgaerModel(getString(R.string.desc2),getString(R.string.desc3) , 100));

        }
        else if(cardNo==7){
            viewPgaerModels = new ArrayList<>();
            viewPgaerModels.add(new ViewPgaerModel(getResources().getString(R.string.allahakbr),"" , 100));

        }
        else if(cardNo==6){
            viewPgaerModels = new ArrayList<>();
            viewPgaerModels.add(new ViewPgaerModel(getResources().getString(R.string.sobhanallah),getString(R.string.sobhanalla_description) , 100));

        }
        else if(cardNo==8){
            viewPgaerModels = new ArrayList<>();
            viewPgaerModels.add(new ViewPgaerModel(getString(R.string.salaalnabyextend),getString(R.string.salaalnabt_description) , 100));

        }
        else if(cardNo==9){
            viewPgaerModels = new ArrayList<>();
            viewPgaerModels.add(new ViewPgaerModel(getString(R.string.hamd),"" , 100));

        }
        else if(cardNo==10){
            viewPgaerModels = new ArrayList<>();
            viewPgaerModels.add(new ViewPgaerModel(getString(R.string.astgfar),"" , 100));

        }
        else if(cardNo==11){
            viewPgaerModels = new ArrayList<>();
            viewPgaerModels.add(new ViewPgaerModel(getString(R.string.lahawl),"" , 100));

        }

        count_tv.setText(""+viewPgaerModels.get(0).getCount());
        remain_tv.setText(""+(0)+"/"+(viewPgaerModels.size())+1);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(viewPgaerModels);
        viewPager.setAdapter(pagerAdapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                count=0;
                progressBar.setProgress(0);
                remain_tv.setText(""+(position+1)+"/"+(viewPgaerModels.size()));


                countTotal = viewPgaerModels.get(position).getCount();
                if(countTotal==1){
                    progressBar.setVisibility(View.INVISIBLE);
                    count_tv.setVisibility(View.INVISIBLE);
                }else {

                        progressBar.setVisibility(View.VISIBLE);
                        count_tv.setVisibility(View.VISIBLE);

                }
               count_tv.setText(""+countTotal);
            }
        });










    }

    ArrayList<ViewPgaerModel> getAzkarSbah(){
        ArrayList<ViewPgaerModel> azkarSbah = new ArrayList<>();

        String json = JsonReader.loadJSONFromAsset(AzkarActivity.this , "azkar.json");
        try {
            JSONArray jsonArray = new JSONArray(json);
            for(int i=0 ;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject.getString("category").equalsIgnoreCase("أذكار الصباح")){
                    azkarSbah.add(new ViewPgaerModel(jsonObject.getString("zekr") , jsonObject.getString("description") ,
                            jsonObject.getInt("count") ) );
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  azkarSbah;
    }

    ArrayList<ViewPgaerModel> getAzkarMsaa(){
        ArrayList<ViewPgaerModel> azkarMsaa = new ArrayList<>();

        String json = JsonReader.loadJSONFromAsset(AzkarActivity.this , "azkar.json");
        try {
            JSONArray jsonArray = new JSONArray(json);
            for(int i=0 ;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject.getString("category").equalsIgnoreCase("أذكار المساء")){
                    azkarMsaa.add(new ViewPgaerModel(jsonObject.getString("zekr") , jsonObject.getString("description") ,
                            jsonObject.getInt("count") ) );
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  azkarMsaa;
    }

    ArrayList<ViewPgaerModel> getAzkarNoom(){
        ArrayList<ViewPgaerModel> azkarNoom = new ArrayList<>();

        String json = JsonReader.loadJSONFromAsset(AzkarActivity.this,"azkar.json");
        try {
            JSONArray jsonArray = new JSONArray(json);
            for(int i=0 ;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int c=0;
                try{
                jsonObject.getInt("count");
                    c=jsonObject.getInt("count") ;
                }
                catch (Exception e){
                    c=1;
                }

                if(jsonObject.getString("category").equalsIgnoreCase("أذكار النوم")){
                    azkarNoom.add(new ViewPgaerModel(jsonObject.getString("zekr") , jsonObject.getString("description") ,
                            c ) );
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  azkarNoom;
    }

    public void viewPagerElementsOnClick(View view){
        if(count<countTotal){
            count++;
            float p = ( (float)count/countTotal)*100;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                progressBar.setProgress((int) p , true);
            }else {
                progressBar.setProgress((int) p );

            }

            count_tv.setText(""+count);}
    }


}