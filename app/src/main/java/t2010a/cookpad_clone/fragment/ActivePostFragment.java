package t2010a.cookpad_clone.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import t2010a.cookpad_clone.R;
import t2010a.cookpad_clone.activity.EditActivePostActivity;
import t2010a.cookpad_clone.adapter.ActivePostAdapter;
import t2010a.cookpad_clone.adapter.InactivePostAdapter;
import t2010a.cookpad_clone.event.MessageEvent;
import t2010a.cookpad_clone.local_data.LocalDataManager;
import t2010a.cookpad_clone.model.client_model.Content;
import t2010a.cookpad_clone.model.client_model.HomeModel;
import t2010a.cookpad_clone.model.client_model.User;
import t2010a.cookpad_clone.repository.Repository;


public class ActivePostFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private View itemView;
    private RecyclerView rvActivePost;
    private Button btnLoadMore;
    private ActivePostAdapter adapter;
    private List<Content> contentList = new ArrayList<>();
    private Repository repository = Repository.getInstance();
    private SwipeRefreshLayout swipeRefreshLayout;
    private User user = LocalDataManager.getUserDetail();
    private int pageLimit = 20;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        itemView = inflater.inflate(R.layout.fragment_active_post, container, false);
        initView(itemView);
        configRvAdapter();
        return itemView;
    }

    private void initView(View itemView) {
        rvActivePost = itemView.findViewById(R.id.rvActivePost);
        swipeRefreshLayout = itemView.findViewById(R.id.swipeRefreshLayout);
        btnLoadMore = itemView.findViewById(R.id.btnLoadMore);
        btnLoadMore.setOnClickListener(this);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.main_color));
    }

    private void configRvAdapter() {
        contentList = initData(1, pageLimit, 1, user.getId(), "desc");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        adapter = new ActivePostAdapter(getActivity(), contentList);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rvActivePost.addItemDecoration(itemDecoration);

        rvActivePost.setLayoutManager(layoutManager);
        rvActivePost.setAdapter(adapter);

    }

    private List<Content> initData(int page, int limit, int status, long userId, String sortId) {
        repository.getService().getPostList(page, limit, status, userId, sortId).enqueue(new Callback<HomeModel>() {
            @Override
            public void onResponse(Call<HomeModel> call, Response<HomeModel> response) {
                if (response.code() != 200) {
                } else {
                    contentList = response.body().getContent();
                    adapter.reloadData(contentList);
                }
            }

            @Override
            public void onFailure(Call<HomeModel> call, Throwable t) {
                Log.d("TAG", "onFailure: ");
            }
        });
        return contentList;
    }

    @Override
    public void onRefresh() {
        setAnimationResource(R.anim.animation_layout_left_to_right);
        adapter.reloadData(initData(1, pageLimit, 1, LocalDataManager.getUserDetail().getId(), "desc"));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

    private void setAnimationResource(int animationResource) {
        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(getActivity(), animationResource);
        rvActivePost.setLayoutAnimation(layoutAnimationController);
        rvActivePost.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLoadMore:
                pageLimit += 5;
                adapter.reloadData(initData(1, pageLimit, 1, LocalDataManager.getUserDetail().getId(), "desc"));
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent.EditActivePostEvent editActivePostEvent) {
        Content content = editActivePostEvent.getPost();
        toUpdatePost(content);
    }

    private void toUpdatePost(Content content) {
        Intent intent = new Intent(getActivity(), EditActivePostActivity.class);
        intent.putExtra("EDIT ACTIVE POST", content);
        Log.d("TAG", "to edit from new post");
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}