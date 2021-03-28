package com.omar.quranwazkar;

public class ViewPgaerModel {
    String zkr_tv;
    String zkr_desc_tv;
    int count;

    public ViewPgaerModel(String zkr_tv, String zkr_desc_tv, int count) {
        this.zkr_tv = zkr_tv;
        this.zkr_desc_tv = zkr_desc_tv;
        this.count = count;
    }

    public String getZkr_tv() {
        return zkr_tv;
    }

    public void setZkr_tv(String zkr_tv) {
        this.zkr_tv = zkr_tv;
    }

    public String getZkr_desc_tv() {
        return zkr_desc_tv;
    }

    public void setZkr_desc_tv(String zkr_desc_tv) {
        this.zkr_desc_tv = zkr_desc_tv;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
