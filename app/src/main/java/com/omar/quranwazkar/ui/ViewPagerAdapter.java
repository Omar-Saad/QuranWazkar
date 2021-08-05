package com.omar.quranwazkar.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.omar.quranwazkar.R;

import java.util.ArrayList;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

    ArrayList<ViewPgaerModel> pgaerModels;

    public ViewPagerAdapter( ArrayList<ViewPgaerModel> pgaerModels) {
        this.pgaerModels = pgaerModels;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pager_item , parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.zkr_tv.setText(pgaerModels.get(position).getZkr_tv());
        holder.zkr_dec_tv.setText(pgaerModels.get(position).getZkr_desc_tv());

    }

    @Override
    public int getItemCount() {
        return pgaerModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView zkr_tv;
        TextView zkr_dec_tv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            zkr_tv = itemView.findViewById(R.id.zkrtv);
            zkr_dec_tv = itemView.findViewById(R.id.zkr_desc_tv);

        }
    }
}
