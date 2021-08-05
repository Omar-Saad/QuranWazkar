package com.omar.quranwazkar.model;

import com.omar.quranwazkar.model.Audio;

public  class MessageEvent {
    public final static int STOP = 0;
    public final static int PLAYING = 1;
    public final static int RESUME = 2;
    public final static int PAUSE = 3;
    public final static int PLAY_NEW_AUDIO_Next=4;
    public final static int PLAY_NEW_AUDIO_PREVIOUS=5;
    public final static int RESET=6;
    public final static int VOLUME_CHANGED = 1;
    public final static int METADATA_CHANGED=1;
    public final static int SEEKBAR_CHANGED=1;



    private int playStatus , volumeStatus , metaDataStatus , seekBarStatus , seekToProgress;
    private float volume;
    private String audioName;
    private String tag;
    private Audio audio;
    /* Additional fields if needed */

    public MessageEvent(String tag) {
        this.tag = tag;
    }

    public int getPlayStatus() {
        return playStatus;
    }

    public void setPlayStatus(int playStatus) {
        this.playStatus = playStatus;
    }

    public String getAudioName() {
        return audioName;
    }

    public void setAudioName(String audioName) {
        this.audioName = audioName;
    }

    public String getTag() {
        return tag;
    }

    public int getVolumeStatus() {
        return volumeStatus;
    }

    public void setVolumeStatus(int volumeStatus) {
        this.volumeStatus = volumeStatus;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public int getMetaDataStatus() {
        return metaDataStatus;
    }

    public void setMetaDataStatus(int metaDataStatus) {
        this.metaDataStatus = metaDataStatus;
    }

    public Audio getAudio() {
        return audio;
    }

    public void setAudio(Audio audio) {
        this.audio = audio;
    }


    public int getSeekBarStatus() {
        return seekBarStatus;
    }

    public void setSeekBarStatus(int seekBarStatus) {
        this.seekBarStatus = seekBarStatus;
    }

    public int getSeekToProgress() {
        return seekToProgress;
    }

    public void setSeekToProgress(int seekToProgress) {
        this.seekToProgress = seekToProgress;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
