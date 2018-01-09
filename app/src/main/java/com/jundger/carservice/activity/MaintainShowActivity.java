package com.jundger.carservice.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.jundger.carservice.R;
import com.jundger.carservice.annotation.InjectView;
import com.jundger.carservice.domain.Article;
import com.jundger.carservice.util.InjectUtil;

public class MaintainShowActivity extends AppCompatActivity {

    private Article article;

    @InjectView(R.id.maintain_show_tllt)
    private CollapsingToolbarLayout maintain_show_tllt;

    @InjectView(R.id.maintain_show_tb)
    private Toolbar maintain_show_tb;

    @InjectView(R.id.maintain_show_tv)
    private TextView maintain_show_tv;

    @InjectView(R.id.maintain_show_iv)
    private ImageView maintain_show_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintain_show);

        InjectUtil.InjectView(this);

        article = (Article) getIntent().getSerializableExtra("Article");

        setSupportActionBar(maintain_show_tb);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true); // 返回按钮可点击
        }

        maintain_show_iv.setImageResource(article.getImageId());
        maintain_show_tv.setText(article.getTitle());
//        maintain_show_tllt.setExpandedTitleColor(getResources().getColor(R.color.appThemeColor));
    }

    public static void launchActivity(Context context, Article article) {
        Intent intent = new Intent(context, MaintainShowActivity.class);
        intent.putExtra("Article", article);
        context.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            MaintainShowActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu_toolbar, menu);
        return true;
    }
}