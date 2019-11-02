package com.example.rdc_lnmiit.Fragments.NewsCategoryFragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.bumptech.glide.Glide;
import com.example.rdc_lnmiit.Adapters.RVNewsAdapter;
import com.example.rdc_lnmiit.Models.NewsArticleModel;
import com.example.rdc_lnmiit.Models.NewsFeedModel;
import com.example.rdc_lnmiit.Network.APINewsInterface;
import com.example.rdc_lnmiit.Network.NewsAPIClient;
import com.example.rdc_lnmiit.R;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeneralNewsFragment extends Fragment {

    private static final String API_KEY = "b9f477f95f334b33bc6abb474a228690";
    RecyclerView recyclerView;
    Dialog loading_dialog;
    ImageView loading_gif_imageView;
    PullRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_general_news, container, false);

        swipeRefreshLayout = (PullRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loading_dialog = new Dialog(getContext());
        loading_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loading_dialog.setContentView(R.layout.loading_dialog);
        loading_gif_imageView = (ImageView) loading_dialog.findViewById(R.id.loading_gif_imageView);

        Glide.with(getActivity()).load(R.drawable.loading).placeholder(R.drawable.loading).into(loading_gif_imageView);
        loading_dialog.setCanceledOnTouchOutside(false);
        loading_dialog.setCancelable(false);

        loading_dialog.show();
        getData(API_KEY);

        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(API_KEY);
            }
        });
        return view;
    }

    void getData(String api_key){
        //loading_dialog.show();
        final APINewsInterface apiInterface = NewsAPIClient.getClient().create(APINewsInterface.class);

        Call<NewsFeedModel> call = apiInterface.getGeneralNews(api_key);
        call.enqueue(new Callback<NewsFeedModel>() {
            @Override
            public void onResponse(Call<NewsFeedModel> call, Response<NewsFeedModel> response) {
                List<NewsArticleModel> articleList = response.body().getArticles();
                List<NewsArticleModel> extendedArticle = new ArrayList<>();
                extendedArticle.addAll(response.body().getArticles());
                RVNewsAdapter adapter = new RVNewsAdapter(getActivity(), extendedArticle);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
                loading_dialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<NewsFeedModel> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                loading_dialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
