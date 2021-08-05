package com.omar.quranwazkar.ui;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.omar.quranwazkar.model.Audio;
import com.omar.quranwazkar.util.JsonReader;
import com.omar.quranwazkar.model.MessageEvent;
import com.omar.quranwazkar.R;
import com.omar.quranwazkar.util.StorageUtil;
import com.omar.quranwazkar.service.MediaPlayerService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class  QuranListenActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvSong  , tvTime , tvDuration,tvRecName;
    Button btnPlay,btnnext,btnPrev;
    SeekBar seekBarTime , seekBarVolume;
    //MediaPlayer mediaPlayer;
    int pos;
    ArrayList<String>surahNames;
    int page_type=0;
    private String recId;
    private int server;

    private MediaPlayerService player;
    boolean serviceBound = false;
    public static final String Broadcast_PLAY_NEW_AUDIO = "com.omar.quranwazkar.PlayNewAudio";

    private MessageEvent message;
    ArrayList<Audio>audios = new ArrayList<>();


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

                    //mediaPlayer.stop();
                    //mediaPlayer.reset();

                    pos = getIntent().getIntExtra("surahNo", 1);
                   // playAudio(pos);
                    audios.clear();
                //    Log.e("autt","aaaddd = "+audios.size());

                    int tempPos = pos;
                    for (int i=0 ;i<surahNames.size();i++) {
                        tempPos = tempPos+1;
                        String temp = "00"+tempPos;
                        if((tempPos>9)&&(tempPos<100)){
                            temp = "0"+tempPos;
                        }
                        else if(tempPos>99){
                            temp=""+tempPos;
                        }

                        String data = "https://server"+server+".mp3quran.net/"+recId+"/" + temp + ".mp3";

                        audios.add(new Audio(data, "قراّن", surahNames.get(i), ""+tvRecName.getText()
                                , 0));}


                    dialog.dismiss();


                    playAudio(pos);



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
        //    mediaPlayer = new MediaPlayer();


            if(server!=-1) {
           //     playMedia(pos);
                ///TODO TEMP
                int tempPos = 0;
                for (int i=0 ;i<surahNames.size();i++) {
                    tempPos = tempPos+1;
                    String temp = "00"+tempPos;
                    if((tempPos>9)&&(tempPos<100)){
                        temp = "0"+tempPos;
                    }
                    else if(tempPos>99){
                        temp=""+tempPos;
                    }

                    String data = "https://server"+server+".mp3quran.net/"+recId+"/" + temp + ".mp3";

                    audios.add(new Audio(data, "قراّن", surahNames.get(i), ""+tvRecName.getText()
                            , 0));
                    //Log.e("o","a = "+audios.size()+"  "+audios.get(i).getData());
                }

                playAudio(pos);

            }

            btnnext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MessageEvent m = new MessageEvent("activity");
                    m.setPlayStatus(MessageEvent.PLAY_NEW_AUDIO_Next);
                    EventBus.getDefault().postSticky(m);
                    int index =new StorageUtil(getApplicationContext()).loadAudioIndex();
                    String name = new StorageUtil(getApplicationContext()).loadAudio().get(index).getAlbum();
                    tvSong.setText("" + name);

                }
            });

            btnPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    MessageEvent m = new MessageEvent("activity");
                    m.setPlayStatus(MessageEvent.PLAY_NEW_AUDIO_PREVIOUS);
                    EventBus.getDefault().postSticky(m);
                    int index =new StorageUtil(getApplicationContext()).loadAudioIndex();
                    String name = new StorageUtil(getApplicationContext()).loadAudio().get(index).getAlbum();
                    tvSong.setText("" + name);


                    }

            });

        }

        else if((page_type==1) || (page_type==2)){
           // mediaPlayer = new MediaPlayer();
            btnPrev.setVisibility(View.GONE);
            btnnext.setVisibility(View.GONE);


            if(page_type==1) {

                String data =Uri.parse("android.resource://" + getPackageName() + "/raw"
                         + "/azkar_sabah").toString();


                audios.clear();
                audios.add(new Audio(data,
                        "قراّن و أذكار ", "أذكار الصباح", " ياسر ابو عمار",0));
               playAudio(0);


            }
            else   if(page_type==2){
                String data =Uri.parse("android.resource://" + getPackageName() + "/raw"
                        + "/azkar_masaa").toString();

                audios.clear();
                audios.add(new Audio(data,
                        "قراّن و أذكار ", "أذكار المساء", " ياسر ابو عمار",0));
                playAudio(0);

            }
        }


    }

    //Binding this Client to the mediaplayer Service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;


            //Toast.makeText(QuranListenActivity.this, "Service Bound", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    private void playAudio(int audioIndex) {
        //Check is service is active
        if (!serviceBound) {
            //Store Serializable audioList to SharedPreferences

            new StorageUtil(QuranListenActivity.this).storeAudio(audios);
            new StorageUtil(QuranListenActivity.this).storeAudioIndex(audioIndex);

            Intent playerIntent = new Intent(this, MediaPlayerService.class);
            startService(playerIntent);
            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            //Store the new audioIndex to SharedPreferences
        //    Log.e("servvv","SERVICE IS ACTIVE");
            new StorageUtil(QuranListenActivity.this).storeAudio(audios);
            new StorageUtil(QuranListenActivity.this).storeAudioIndex(audioIndex);

            //Service is active
            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
            sendBroadcast(broadcastIntent);
        }
    }

   /* void playMedia(int position){
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
                AssetFileDescriptor afd = getAssets().openFd("raw/azkar_sabah.mp3");
                mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());

            }
            else   if(page_type==2){
               // mediaPlayer.setDataSource("https://www.mboxdrive.com/yt1s.com - اذكار المساء للمنشد ياسر فاروق ابو عمار.mp3");
                AssetFileDescriptor afd = getAssets().openFd("raw/azkar_masaa.mp3");
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

//                    btnPlay.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (v.getId() == R.id.btnPlay)
//                            {
//                                if (mediaPlayer.isPlaying()){
//                                    mediaPlayer.pause();
//                                    btnPlay.setBackgroundResource(R.drawable.play);
//                                }
//                                else {
//                                    mediaPlayer.start();
//                                    btnPlay.setBackgroundResource(R.drawable.pause);
//
//                                }
//                            }
//                        }
//                    });
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
    }*/


    String milliSecToString(int time){
        String elsapedTime= "";
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

        tvSong.setText("جاري التحميل...");


        message = new MessageEvent("activity");

        btnPlay.setOnClickListener(this::onClick);


        seekBarVolume.setProgress(100);
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                float volume  = progress/100f;
              //  mediaPlayer.setVolume(volume , volume);
                MessageEvent event = new MessageEvent("activity");
                event.setPlayStatus(MessageEvent.RESUME);
                event.setVolumeStatus(MessageEvent.VOLUME_CHANGED);
                event.setVolume(volume);
                EventBus.getDefault().postSticky(event);}


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        seekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    // mediaPlayer.seekTo(progress);
                    MessageEvent messageEvent = new MessageEvent("activity");
                    messageEvent.setPlayStatus(MessageEvent.RESUME);
                    messageEvent.setSeekBarStatus(MessageEvent.SEEKBAR_CHANGED);
                    messageEvent.setSeekToProgress(progress);
                    EventBus.getDefault().postSticky(messageEvent);
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




    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnPlay)
        {

            switch (message.getPlayStatus())
            {
                case MessageEvent.PLAYING:
                case MessageEvent.RESUME:
                  //  MessageEvent m = new MessageEvent("activity");
                    btnPlay.setBackgroundResource(R.drawable.play);
                    message.setTag("activity");
                    message.setPlayStatus(MessageEvent.PAUSE);
                    EventBus.getDefault().postSticky(message);
                    break;
                case MessageEvent.STOP:
                case MessageEvent.PAUSE :
                    btnPlay.setBackgroundResource(R.drawable.pause);

                  //  MessageEvent mm = new MessageEvent("activity");
                    message.setTag("activity");
                    message.setPlayStatus(MessageEvent.RESUME);
                    EventBus.getDefault().postSticky(message);
                    break;

                default:
                    btnPlay.setBackgroundResource(R.drawable.pause);
break;

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


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("ServiceState", serviceBound);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        serviceBound = savedInstanceState.getBoolean("ServiceState");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceBound) {
            unbindService(serviceConnection);
            //service is active
            player.stopSelf();
            EventBus.getDefault().unregister(this);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this))
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.e("omae0","is reg = "+EventBus.getDefault().isRegistered(this));
    }

    @Subscribe(threadMode = ThreadMode.MAIN , sticky = true)
    public void onMessageEvent(MessageEvent event) {
        message=event;
         if(event.getTag().equals("service")){
       //    message = event;
             //Log.e("ACTIVITY","EVENT  = "+event.getPlayStatus());

             switch (event.getPlayStatus())
        {

            case MessageEvent.PLAYING:
            case MessageEvent.RESUME:
                btnPlay.setBackgroundResource(R.drawable.pause);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (player != null){
                            if(player.isPlaying()){

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String currentTime = milliSecToString(player.getCurrentMediaPosition());
                                        tvTime.setText(currentTime);
                                        seekBarTime.setProgress(player.getCurrentMediaPosition());
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

                break;


            case MessageEvent.PAUSE :
            case MessageEvent.STOP:
                btnPlay.setBackgroundResource(R.drawable.play);
                break;
            case MessageEvent.PLAY_NEW_AUDIO_Next:
            case MessageEvent.PLAY_NEW_AUDIO_PREVIOUS:
//                    if (pos != 113) {
//                        pos = pos + 1;
//                        }
//                    else pos=0;
//
                int index =new StorageUtil(getApplicationContext()).loadAudioIndex();
                String name = new StorageUtil(getApplicationContext()).loadAudio().get(index).getAlbum();
                tvSong.setText("" + name);
//                    event.setPlayStatus(MessageEvent.RESUME);
//                    EventBus.getDefault().postSticky(event);

                    break;

        }
        if(event.getMetaDataStatus()==MessageEvent.METADATA_CHANGED)
        {
            if(event.getAudio()!=null) {
                updateMetaDta(event.getAudio());
//                MessageEvent  messageEvent = new MessageEvent("a");
//                messageEvent.setMetaDataStatus(0);
//                EventBus.getDefault().postSticky(messageEvent);
                //EventBus.getDefault().postSticky("");

            }
        }

        }

    }

    private void updateMetaDta(Audio audio) {
        if(audio==null)
            audio = new Audio("","جاري التحميل" , "جاري التحميل" ,"",0);

        tvSong.setText(audio.getAlbum());

        String duration = milliSecToString(audio.getDuration());
        tvDuration.setText(duration);


        seekBarTime.setMax(audio.getDuration());


    }

    ;



}