package com.example.chunyili.myrecyclerview;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.chunyili.myrecyclerview.Model.Magazine;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MagazineAdapter extends RecyclerView.Adapter<MagazineAdapter.ViewHolder>{

    private List<Magazine> magazineList;
    private Context context;
    private MagazineHeaderViewHolder magazineHeaderViewHolder;
    private MagazineMemberViewHolder magazineMemberViewHolder;
    private boolean isYearMode;
    private OnItemClickListener onItemClickListener;

    private final int VIEW_TYPE_HEADER = 0,VIEW_TYPE_ITEM = 1,VIEW_TYPE_LOADING = 2;
    ILoadMore loadMore;
    boolean isLoading;
    int visibleThreshold = 5;
    int lastVisibleItem,totalItemCount;

    public MagazineAdapter(RecyclerView recyclerView,List<Magazine> magazineList, Context context, boolean isYearMode) {
        this.magazineList = magazineList;
        this.context = context;
        this.isYearMode = isYearMode;

        final LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = manager.getItemCount();
                lastVisibleItem = manager.findLastVisibleItemPosition();
                if(isLoading){
                    return;
                }
                Log.d("Adapter","lastVisibleItem : " + lastVisibleItem + " ,visibleThreshold : " + visibleThreshold);
                if(totalItemCount <= (lastVisibleItem+visibleThreshold)){
                    if(loadMore != null){
                        loadMore.onLoadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    public void setLoadMore(ILoadMore loadMore) {
        this.loadMore = loadMore;
    }

    @Override
    public int getItemViewType(int position) {

        if(position == 0){
            return VIEW_TYPE_HEADER;
        }

        if(position != 0 && magazineList.get(position) != null){
            return VIEW_TYPE_ITEM;
        }

        if(magazineList.get(position) == null){
            return VIEW_TYPE_LOADING;
        }

        return -1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_HEADER){
            View view_group_magazine_header = inflater.inflate(R.layout.magazine_header,parent,false);
            return new MagazineHeaderViewHolder(view_group_magazine_header);
        }else if(viewType == VIEW_TYPE_ITEM){
            View view_group_magazine_member = inflater.inflate(R.layout.magazine_member,parent,false);
            return new MagazineMemberViewHolder(view_group_magazine_member);
        }else if(viewType == VIEW_TYPE_LOADING){
            View view = LayoutInflater.from(context).inflate(R.layout.item_loading,parent,false);
            return new LoadingViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(position == 25){
            return;
        }

        Magazine magazine = magazineList.get(position);

        setOnclickListener(holder);



        if (holder instanceof MagazineHeaderViewHolder){
            String dateStr = setTime(magazine.getPublish_time());
            magazineHeaderViewHolder = (MagazineHeaderViewHolder) holder;
            magazineHeaderViewHolder.title.setText(magazine.getTitle());
            magazineHeaderViewHolder.preface.setText(magazine.getPreface());
            magazineHeaderViewHolder.periods.setText(magazine.getPeriods()+"期");
            magazineHeaderViewHolder.publish_time.setText(dateStr);
            magazineHeaderViewHolder.cover.setImageURI(magazine.getImage_url());
        }

        if (holder instanceof MagazineMemberViewHolder){
            String dateStr = setTime(magazine.getPublish_time());
            magazineMemberViewHolder = (MagazineMemberViewHolder) holder;
            magazineMemberViewHolder.cover.setImageURI(magazine.getImage_url());
            magazineMemberViewHolder.periods.setText(magazine.getPeriods()+"期");
            magazineMemberViewHolder.public_date.setText(dateStr);
        }

        if (holder instanceof LoadingViewHolder){
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }

    }

    private void setOnclickListener(final ViewHolder holder) {
        if (onItemClickListener != null && holder instanceof MagazineHeaderViewHolder || holder instanceof MagazineMemberViewHolder){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView,pos);
                }
            });
        }
    }

    private String setTime(String dateStr){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(dateStr);
            return dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return magazineList.size();
    }


    public void setLoaded(){
        isLoading = false;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }


    // View Holder


    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class MagazineHeaderViewHolder extends ViewHolder{
        public TextView publish_time;
        public TextView periods;
        public TextView title;
        public TextView preface;
        public SimpleDraweeView cover;

        public MagazineHeaderViewHolder(View view) {
            super(view);
            publish_time = (TextView) view.findViewById(R.id.public_date);
            periods = (TextView) view.findViewById(R.id.periods);
            title = (TextView) view.findViewById(R.id.title);
            preface = (TextView) view.findViewById(R.id.preface);
            cover = (SimpleDraweeView) view.findViewById(R.id.magazine_cover);
        }
    }

    public static class MagazineMemberViewHolder extends ViewHolder{
        public TextView public_date;
        public TextView periods;
        public SimpleDraweeView cover;

        public MagazineMemberViewHolder(View view) {
            super(view);
            public_date = (TextView) view.findViewById(R.id.member_publish_date);
            periods = (TextView) view.findViewById(R.id.member_periods);
            cover = (SimpleDraweeView) view.findViewById(R.id.member_magazine_cover);
        }
    }

    public static class LoadingViewHolder extends ViewHolder{
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

}
