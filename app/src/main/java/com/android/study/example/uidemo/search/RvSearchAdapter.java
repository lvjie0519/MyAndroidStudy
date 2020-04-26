package com.android.study.example.uidemo.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.study.example.R;

import java.util.List;

public class RvSearchAdapter extends RecyclerView.Adapter<RvSearchAdapter.SearchViewHolder> {

    private Context mContext;
    private List<String> mDatas;

    public RvSearchAdapter(Context mContext, List<String> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {

        return  new SearchViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_rv_search, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder searchViewHolder, int position) {
        searchViewHolder.tvSearchInfo.setText(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public void setDatas(List<String> mDatas) {
        this.mDatas = mDatas;
    }

    class SearchViewHolder extends RecyclerView.ViewHolder{

        TextView tvSearchInfo;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            initView(itemView);
        }

        private void initView(View view){
            tvSearchInfo = view.findViewById(R.id.item_tv_search_info);
        }
    }
}
