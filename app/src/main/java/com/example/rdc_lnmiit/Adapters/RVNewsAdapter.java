package com.example.rdc_lnmiit.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rdc_lnmiit.Models.NewsArticleModel;
import com.example.rdc_lnmiit.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RVNewsAdapter extends RecyclerView.Adapter<RVNewsAdapter.viewHolder> {

    private Context context;
    private List<NewsArticleModel> articleList;
    Dialog newsDialog;
    Activity activity;
    WebView webView;
    Dialog loading_dialog;
    ImageView loading_gif_imageView;
    ProgressBar progressBar;

    public RVNewsAdapter(Context context, List<NewsArticleModel> articleList) {
        this.context = context;
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rv_news_item, viewGroup, false);

        loading_dialog = new Dialog(context);
        loading_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loading_dialog.setContentView(R.layout.loading_dialog);
        loading_gif_imageView = (ImageView) loading_dialog.findViewById(R.id.loading_gif_imageView);

        Glide.with(context).load(R.drawable.loading).placeholder(R.drawable.loading).into(loading_gif_imageView);
        loading_dialog.setCanceledOnTouchOutside(false);
        loading_dialog.setCancelable(false);

        newsDialog = new Dialog(context);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {
        final NewsArticleModel article = articleList.get(i);
        viewHolder.txtTitle.setText(article.getTitle());
        viewHolder.txtDescription.setText(article.getDescription());

        if (article.getAuthor() == null)
            viewHolder.txtSource.setVisibility(View.GONE);
        else
            viewHolder.txtSource.setVisibility(View.VISIBLE);

        viewHolder.txtSource.setText(article.getAuthor());

        Glide.with(context).load(article.getUrlToImage()).placeholder(R.drawable.placeholder).into(viewHolder.img);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewsDialog(article.getUrl(), context);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle, txtDescription, txtSource;
        ImageView img;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtDescription = (TextView) itemView.findViewById(R.id.txtDescription);
            txtSource = (TextView) itemView.findViewById(R.id.txtSource);
            img = (ImageView) itemView.findViewById(R.id.img);
        }
    }

    void showNewsDialog(String url, Context context) {

        newsDialog.setContentView(R.layout.dialog_news_url);

        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.96);
        int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.96);

        newsDialog.getWindow().setLayout(width, height);

        webView = (WebView) newsDialog.findViewById(R.id.webView);
        progressBar = (ProgressBar) newsDialog.findViewById(R.id.progressBar);

        //webView.setVisibility(View.INVISIBLE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                progressBar.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }
        });
        webView.loadUrl(url);
        newsDialog.show();
    }

}
