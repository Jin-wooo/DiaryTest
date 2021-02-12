package com.example.mytestdiary.DiaryList;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytestdiary.R;

import java.util.ArrayList;

public class DiaryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<DiaryInfo> mList;
    private OnItemClickListener mClickListener;
    private onItemLongClickListener mLongClickListener;

    private final static int DIARY_ITEM = 0;
    private final static int DAY_SEP_LINE = 1;

    public DiaryListAdapter() { mList = new ArrayList<DiaryInfo>(); }
    public DiaryListAdapter(ArrayList<DiaryInfo> mList) { this.mList = mList; }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case DIARY_ITEM: // Diary_item
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.diary_item, parent, false);
                return new DiaryItemViewHolder(view);
            case DAY_SEP_LINE: // Day Separate Line
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
            case DIARY_ITEM: //item
                DiaryItemViewHolder itemViewHolder = (DiaryItemViewHolder) holder;
                itemViewHolder.tvTitle.setText(diaryInfo.getStrDiaryTitle());
                itemViewHolder.tvContent.setText(diaryInfo.getStrDiaryContent());
//                itemViewHolder.tvMonth.setText(diaryInfo.getDbDateCode().getStrDiaryMonth());
//                itemViewHolder.tvDay.setText(diaryInfo.getDbDateCode().getStrDiaryDay());

                if (mList.get(position).isChecked()) {
                    ((CheckBox) itemViewHolder.cbDelCheck).setChecked(true);
                }
                else {
                    ((CheckBox) itemViewHolder.cbDelCheck).setChecked(false);
                }
                break;
            case DAY_SEP_LINE: //Day Sep Line
                DaySepLineViewHolder lineViewHolder = (DaySepLineViewHolder) holder;
                lineViewHolder.tvSepMonth.setText(diaryInfo.getDbDateCode().getStrDiaryMonth());
                lineViewHolder.tvSepDay.setText(diaryInfo.getDbDateCode().getStrDiaryDay());
                lineViewHolder.tvSepWeekDay.setText(diaryInfo.getDbDateCode().getStrDayName());
                break;

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
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getNumTypeCode();
    }

    public ArrayList<DiaryInfo> getDiaryList() {
        return mList;
    }

//    public void setCheckBoxVisibility(boolean isShow) {
//        for (DiaryInfo info : mList) {
//            if (info.getNumTypeCode() == 0) {
//                info.
//            }
//        }
//    }

    public void setAllBoxValue(boolean val) {

    }

    public class DiaryItemViewHolder extends RecyclerView.ViewHolder {
//        protected TextView tvMonth;
//        protected TextView tvDay;
        protected TextView tvTitle;
        protected TextView tvContent;
        protected TextView cbDelCheck;

        public DiaryItemViewHolder(@NonNull View itemView) {
            super(itemView);
//            this.tvMonth = (TextView) itemView.findViewById(R.id.tvMonth);
//            this.tvDay = (TextView) itemView.findViewById(R.id.tvDay);
            this.tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            this.tvContent = (TextView) itemView.findViewById(R.id.tvContent);
            this.cbDelCheck = (CheckBox) itemView.findViewById(R.id.cbDelCheck);

            cbDelCheck.setVisibility(View.VISIBLE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (mClickListener != null)
                        mClickListener.onItemClick(view, pos);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Log.d("DiaryListAdapter", "INSIDE ADAPTER");
                    int pos = getAdapterPosition();
                    if (mLongClickListener != null) {
                        mLongClickListener.onItemLongClick(view, pos);
                        return true;
                    }
                    return false;
                }
            });

            cbDelCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean itemChecked = ((CheckBox) view).isChecked();
                    mList.get(getAdapterPosition()).setChecked(itemChecked);
                }
            });
        }

        public void setCheckBoxVisibility(boolean isVisible) {
            if (isVisible) {
                cbDelCheck.setVisibility(View.VISIBLE);
            }
            else {
                cbDelCheck.setVisibility(View.GONE);
            }

        }
    }

    public class DaySepLineViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvSepMonth;
        protected TextView tvSepDay;
        protected TextView tvSepWeekDay;

        public DaySepLineViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvSepMonth = (TextView) itemView.findViewById(R.id.tvSepMonth);
            this.tvSepDay = (TextView) itemView.findViewById(R.id.tvSepDay);
            this.tvSepWeekDay = (TextView) itemView.findViewById(R.id.tvSepWeekday);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mClickListener = listener;
    }

    public interface onItemLongClickListener {
        void onItemLongClick(View view, int pos);
    }

    public void setOnItemLongClickListener(onItemLongClickListener listener) {
        this.mLongClickListener = listener;
    }
}
