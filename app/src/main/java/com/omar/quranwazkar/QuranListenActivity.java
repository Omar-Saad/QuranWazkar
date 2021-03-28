package com.omar.quranwazkar;

import android.annotation.SuppressLint;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class QuranListenActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvSong  , tvTime , tvDuration;
    Button btnPlay,btnnext,btnPrev;
    SeekBar seekBarTime , seekBarVolume;
    MediaPlayer mediaPlayer;
    int pos;
    ArrayList<String>surahNames;
    int page_type=0;

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
            pos = getIntent().getIntExtra("surahNo", 1);
            String s = getIntent().getStringExtra("surahName");
            tvSong.setText("" + s);

            mediaPlayer = new MediaPlayer();
            playMedia(pos);


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
            if(page_type==0)
            mediaPlayer.setDataSource("https://server8.mp3quran.net/afs/"+temp+".mp3");
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