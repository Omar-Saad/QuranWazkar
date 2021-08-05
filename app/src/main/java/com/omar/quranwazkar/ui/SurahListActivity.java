package com.omar.quranwazkar.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.omar.quranwazkar.util.JsonReader;
import com.omar.quranwazkar.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SurahListActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> surahNames;
    ArrayList<Integer> pageNos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listView = findViewById(R.id.listView);
        final int cardNo = getIntent().getIntExtra("cardNo",0);
      //  final ArrayList<String> title =new ArrayList<>();
       // final ArrayList<Integer> pageCount =new ArrayList<>();
      surahNames = new ArrayList<>();
      pageNos = new ArrayList<>();
        String json = JsonReader.loadJSONFromAsset(SurahListActivity.this , "surah_list.json");
        try {
            JSONObject all_json = new JSONObject(json);
            JSONArray jsonArray = all_json.getJSONArray("chapters");
            for(int i=0 ;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
              //  JSONObject jsonObject_name = jsonObject.getJSONObject("translated_name");
                JSONArray jsonArray1_pageNo = jsonObject.getJSONArray("pages");
                pageNos.add(jsonArray1_pageNo.getInt(0));
                surahNames.add("سورة "+jsonObject.getString("name_arabic"));
                listView.setAdapter(new MyListAdapter(SurahListActivity.this, surahNames));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(cardNo==0){
                            Intent intent = new Intent(SurahListActivity.this , QuranActivity.class);
                            intent.putExtra("pageNo",(pageNos.get(position)));

                            startActivity(intent);
                        }

                        else if(cardNo==1){
                            Intent intent = new Intent(SurahListActivity.this , QuranListenActivity.class);
                            intent.putExtra("surahNo",position);
                            intent.putExtra("surahName",surahNames.get(position));
                            intent.putExtra("page_type",0);

                            // Log.e("om","p "+pageCount.get(position));
                            startActivity(intent);
                        }
                    }
                });

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }




    }
}

