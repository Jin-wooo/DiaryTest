package com.example.mytestdiary;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DiaryListAdapter {

    public class DiaryLineViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvMonth;
        protected TextView tvDay;
        protected TextView tvTitle;
        protected TextView tvContent;

        public DiaryLineViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvMonth = (TextView) itemView.findViewById(R.id.tvMonth);
            this.tvDay = (TextView) itemView.findViewById(R.id.tvDay);
            this.tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            this.tvContent = (TextView) itemView.findViewById(R.id.tvContent);
        }
    }

    public class DaySepLineViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvSepMonth;
        protected TextView tvSepDay;

        public DaySepLineViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvSepMonth = (TextView) itemView.findViewById(R.id.tvSepMonth);
            this.tvSepDay = (TextView) itemView.findViewById(R.id.tvSepDay);
        }
    }
}
