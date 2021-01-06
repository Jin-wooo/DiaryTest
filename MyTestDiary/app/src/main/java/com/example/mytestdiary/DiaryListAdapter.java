package com.example.mytestdiary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DiaryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<DiaryInfo> mList;
    private OnItemClickListener mListener;

    public DiaryListAdapter() { mList = new ArrayList<DiaryInfo>(); }
    public DiaryListAdapter(ArrayList<DiaryInfo> mList) { this.mList = mList; }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getNumTypeCode();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0: // Diary_item
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.diary_item, parent, false);
                return new DiaryItemViewHolder(view);
            case 1: // Day Separate Line
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.diary_dayline, parent, false);
                return new DaySepLineViewHolder(view);

        }
        // Case에 안 걸렸으면 그냥 이게 튀어나가면 됨
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.diary_item, parent, false);
        return new DiaryItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final DiaryInfo diaryInfo = mList.get(position);
        switch (diaryInfo.getNumTypeCode()) {
            case 0: //item
                DiaryItemViewHolder itemViewHolder = (DiaryItemViewHolder) holder;
                itemViewHolder.tvTitle.setText(diaryInfo.getStrDiaryTitle());
                itemViewHolder.tvContent.setText(diaryInfo.getStrDiaryContent());
                itemViewHolder.tvMonth.setText(diaryInfo.getNumDiaryWriteMonth());
                itemViewHolder.tvDay.setText(diaryInfo.getNumDiaryWriteDay());
            case 1: //Day Sep Line
                DaySepLineViewHolder lineViewHolder = (DaySepLineViewHolder) holder;
                lineViewHolder.tvSepMonth.setText(diaryInfo.getNumDiaryWriteMonth() + "월");
                lineViewHolder.tvSepDay.setText(diaryInfo.getNumDiaryWriteDay() + "일");
        }
    }
    // idx에 해당하는 PlaceListProduct를 리턴합니다.
    public DiaryInfo getItem(int idx) { return mList.get(idx); };

    // 해당 데이터를 Adapter의 리스트에 추가합니다.
    public void setItem(DiaryInfo diaryInfo) { mList.add(diaryInfo); }

    // idx에 해당하는 데이터를 삭제합니다.
    public void removeItem(int idx) { mList.remove(idx); }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class DiaryItemViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvMonth;
        protected TextView tvDay;
        protected TextView tvTitle;
        protected TextView tvContent;

        public DiaryItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvMonth = (TextView) itemView.findViewById(R.id.tvMonth);
            this.tvDay = (TextView) itemView.findViewById(R.id.tvDay);
            this.tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            this.tvContent = (TextView) itemView.findViewById(R.id.tvContent);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (mListener != null)
                        mListener.onItemClick(view, pos);
                }
            });
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

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }
}
