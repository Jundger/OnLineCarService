package com.jundger.carservice.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jundger.carservice.R;
import com.jundger.carservice.activity.MaintainShowActivity;
import com.jundger.carservice.base.MyApplication;
import com.jundger.carservice.pojo.Article;

import java.util.List;

/**
 * Created by 14246 on 2018/1/5.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    private Context mContext;

    private List<Article> mArticleList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView articleImage;
        TextView title;
        TextView from;
        TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            articleImage = itemView.findViewById(R.id.article_image_view);
            title = itemView.findViewById(R.id.article_title_tv);
            from = itemView.findViewById(R.id.article_from_tv);
            time = itemView.findViewById(R.id.release_time_tv);
        }
    }

    public ArticleAdapter(List<Article> mArticleList) {
        this.mArticleList = mArticleList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_maintain_article, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Article article = mArticleList.get(position);
//                Toast.makeText(view.getContext(), "子项：" + article.getTitle(), Toast.LENGTH_SHORT).show();
                MaintainShowActivity.launchActivity(view.getContext(), article);
            }
        });
        holder.articleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Article article = mArticleList.get(position);
//                Toast.makeText(view.getContext(), "图片：" + article.getTitle(), Toast.LENGTH_SHORT).show();
                MaintainShowActivity.launchActivity(view.getContext(), article);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article article = mArticleList.get(position);
//        holder.articleImage.setImageResource(article.getImageId());
        holder.title.setText(article.getTitle());
        holder.from.setText(article.getFrom());
        holder.time.setText(article.getTime());
        Glide.with(MyApplication.getContext())
                .load(article.getImage())
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.load_fail)
                .thumbnail(0.2f)
                .into(holder.articleImage);
    }

    @Override
    public int getItemCount() {
        return mArticleList.size();
    }

}
