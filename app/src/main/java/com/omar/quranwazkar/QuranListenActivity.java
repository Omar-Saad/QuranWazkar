package com.omar.quranwazkar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class QuranListenActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvSong  , tvTime , tvDuration,tvRecName;
    Button btnPlay,btnnext,btnPrev;
    SeekBar seekBarTime , seekBarVolume;
    MediaPlayer mediaPlayer;
    int pos;
    ArrayList<String>surahNames;
    int page_type=0;
    private String recId;
    private int server;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran_listen);
        declaration();

        surahNames = new ArrayList<>();
        String json = JsonReader.loadJSONFromAsset(QuranListenActivity.this , "surah_list.json");
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("chapters");
            for(int i=0 ;i<jsonArray.length();i++){
                JSONObject object = jsonArray.getJSONObject(i);
                surahNames.add("سورة "+object.getString("name_arabic"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


         page_type = getIntent().getIntExtra("page_type",0);
        if(page_type==0) {
             final String [] listNames  = getResources().getStringArray(R.array.rec_id);


            SharedPreferences preferences = getSharedPreferences("rec",MODE_PRIVATE);
            recId = preferences.getString("recId" , "empty");
            server = preferences.getInt("server" , -1);
            int recNo = preferences.getInt("recNo" , -1);
            final AlertDialog.Builder builder = new AlertDialog.Builder(QuranListenActivity.this);
            builder.setTitle(R.string.choose_recuiter);
            builder.setIcon(R.drawable.mic);
          //  builder.setMessage(R.string.dialog_message);
            builder.setSingleChoiceItems(listNames, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //recId =which;
                    tvRecName.setText(listNames[which]);
                    if(which==0){
                        recId="balilah";
                        server=6;
                    }

                    else if(which==1) {
                        recId = "hatem";
                        server=11;
                    }
                    else if(which==2){
                        recId = "maher";
                        server = 12;
                    }
                    else if(which==3) {
                        recId = "jbrl";
                        server = 8;
                    }
                    else if(which==4) {
                        recId = "husr";
                        server = 13;
                    }
                    else if(which==5) {
                        recId = "afs";
                        server = 8;
                    }
                    else if(which==6) {
                        recId = "qtm";
                        server = 6;
                    }
                    else if(which==7) {
                        recId = "hazza";
                        server = 11;
                    }
                    else if(which==8) {
                        recId = "yasser";
                        server = 11;
                    }
                    SharedPreferences.Editor editor = getSharedPreferences("rec",MODE_PRIVATE).edit();
                    editor.putString("recId",recId);
                    editor.putInt("server",server);
                    editor.putInt("recNo",which);
                    editor.apply();
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    pos = getIntent().getIntExtra("surahNo", 1);
                    playMedia(pos);
                    dialog.dismiss();

                }
            });
            //builder.show();
            if(server==-1){
                tvRecName.setText(R.string.choose_recuiter);
                builder.show();
            }
            else
            tvRecName.setText(listNames[recNo]);


            tvRecName.setVisibility(View.VISIBLE);
            tvRecName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // mediaPlayer.pause();
                    builder.show();

                }
            });
            pos = getIntent().getIntExtra("surahNo", 1);
            String s = getIntent().getStringExtra("surahName");
            tvSong.setText("" + s);
            mediaPlayer = new MediaPlayer();
            if(server!=-1) {
                playMedia(pos);
            }

            btnnext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (pos != 113) {
                        pos = pos + 1;
                        tvSong.setText("" + surahNames.get(pos));
                        if (mediaPlayer.isPlaying())
                            btnPlay.setBackgroundResource(R.drawable.play);
                        else
                            btnPlay.setBackgroundResource(R.drawable.pause);
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        playMedia(pos);

                    }
                }
            });

            btnPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pos != 0) {
                        pos = pos - 1;
                        tvSong.setText("" + surahNames.get(pos));
                        if (mediaPlayer.isPlaying())
                            btnPlay.setBackgroundResource(R.drawable.play);
                        else
                            btnPlay.setBackgroundResource(R.drawable.pause);
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        playMedia(pos);
                    }
                }
            });

        }

        else if((page_type==1) || (page_type==2)){
            mediaPlayer = new MediaPlayer();
            btnPrev.setVisibility(View.GONE);
            btnnext.setVisibility(View.GONE);
           // tvSong.setText(getResources().getString(R.string.azkar_sabah));
            playMedia(0);
        }


    }

    void playMedia(int position){
        try {
            position = position+1;
            String temp = "00"+position;
            if((position>9)&&(position<100)){
                temp = "0"+position;
            }
            else if(position>99){
                temp=""+position;
            }

            //     Log.e("tag"," m= "+temp);
            if(page_type==0) {


                mediaPlayer.setDataSource("https://server"+server+".mp3quran.net/"+recId+"/" + temp + ".mp3");


            }
          else   if(page_type==1) {
             //   mediaPlayer.setDataSource("https://www.mboxdrive.com/yt1s.com - اذكار الصباح للمنشد ياسر فاروق ابو عمار.mp3");
                AssetFileDescriptor afd = getAssets().openFd("azkar_sabah.mp3");
                mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());

            }
            else   if(page_type==2){
               // mediaPlayer.setDataSource("https://www.mboxdrive.com/yt1s.com - اذكار المساء للمنشد ياسر فاروق ابو عمار.mp3");
                AssetFileDescriptor afd = getAssets().openFd("azkar_masaa.mp3");
            mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());

            }

            //     mediaPlayer.setDataSource("https://alafasy/mp3/+"+temp+".mp3");
            mediaPlayer.prepareAsync();
            tvSong.setText("جاري التحميل...");
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    if(page_type==0)
                    tvSong.setText(""+surahNames.get(pos));
                    else if(page_type==1)
                        tvSong.setText(getResources().getText(R.string.azkar_sabah));
                    else if(page_type==2)
                        tvSong.setText(getResources().getText(R.string.azkar_masaa));

                    btnPlay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (v.getId() == R.id.btnPlay)
                            {
                                if (mediaPlayer.isPlaying()){
                                    mediaPlayer.pause();
                                    btnPlay.setBackgroundResource(R.drawable.play);
                                }
                                else {
                                    mediaPlayer.start();
                                    btnPlay.setBackgroundResource(R.drawable.pause);

                                }
                            }
                        }
                    });
                    String duration = milliSecToString(mediaPlayer.getDuration());
                    tvDuration.setText(duration);

                    seekBarVolume.setProgress(100);
                    seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            float volume  = progress/100f;
                            mediaPlayer.setVolume(volume , volume);
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });

                    seekBarTime.setMax(mediaPlayer.getDuration());
                    seekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            if(fromUser){
                                mediaPlayer.seekTo(progress);
                                seekBar.setProgress(progress);

                            }
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });

                    mediaPlayer.start();


                    btnPlay.setBackgroundResource(R.drawable.pause);

                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setLooping(true);
        mediaPlayer.seekTo(0);
        mediaPlayer.setVolume(1.0f , 1.0f);


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null){
                    if(mediaPlayer.isPlaying()){

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String currentTime = milliSecToString(mediaPlayer.getCurrentPosition());
                                tvTime.setText(currentTime);
                                seekBarTime.setProgress(mediaPlayer.getCurrentPosition());
                            }
                        });

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }).start();
    }
    String milliSecToString(int time){
        String elsapedTime= "";
     //   int minutes = time / 1000/60;
       // int seconds = time / 1000  ;
        //elsapedTime = minutes+":";
        //if(seconds<10)
         //   elsapedTime+="0";
       // elsapedTime+=seconds;

     //   long millis = time % 1000;
        long second = (time / 1000) % 60;
        long minute = (time / (1000 * 60)) % 60;
        long hour = (time / (1000 * 60 * 60)) % 24;
        elsapedTime=""+hour+":"+minute+":"+second;
        return elsapedTime;


    }

    void declaration(){
        tvSong = findViewById(R.id.tv_surah_name);
        tvTime = findViewById(R.id.tvTime);
        tvDuration= findViewById(R.id.tvDuration);
        btnPlay = findViewById(R.id.btnPlay);
        seekBarTime = findViewById(R.id.seekBarTime);
        seekBarVolume = findViewById(R.id.seekBarVolume);
        btnnext=findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.playPrev);
        tvRecName = findViewById(R.id.tv_rec_name);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnPlay)
        {
            if (mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                btnPlay.setBackgroundResource(R.drawable.play);
            }
            else {
                mediaPlayer.start();
                btnPlay.setBackgroundResource(R.drawable.pause);

            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

       // mediaPlayer.stop();
     //   mediaPlayer = null;
       // mediaPlayer.release();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

            //it is not locked
        mediaPlayer.pause();
        mediaPlayer.reset();


       //     mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
           // mediaPlayer.reset();
        //    btnPlay.setBackgroundResource(R.drawable.play);



    }
}