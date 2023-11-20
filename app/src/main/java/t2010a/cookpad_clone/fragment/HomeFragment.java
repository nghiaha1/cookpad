package t2010a.cookpad_clone.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import t2010a.cookpad_clone.R;
import t2010a.cookpad_clone.activity.PostDetailActivity;
import t2010a.cookpad_clone.adapter.HomeAdapter;
import t2010a.cookpad_clone.event.MessageEvent;
import t2010a.cookpad_clone.model.client_model.Content;
import t2010a.cookpad_clone.model.client_model.HomeModel;
import t2010a.cookpad_clone.repository.Repository;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private View itemView;
    private List<Content> contentList = new ArrayList<>();
    private List<Content> moreContentList = new ArrayList<>();
    private Repository repository = Repository.getInstance();
    private CarouselView carouselView;
    private Button btnLoadMore;
    private ProgressBar progressBar;
    private LinearLayoutManager layoutManager;
    int[] sampleImages = {R.drawable.banner1,
            R.drawable.banner2,
            R.drawable.banner4,
            R.drawable.banner5,
            R.drawable.banner6,
            R.drawable.banner3};
    private HomeAdapter adapter;
    private RecyclerView rvHome;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int contentListSize;
    private int moreContentListSize;

    private int pageLimit = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        itemView = inflater.inflate(R.layout.fragment_home, container, false);
        initView(itemView);
        configRvAdapter();
        setAnimationResource(R.anim.animation_layout_left_to_right);
        initBanner();
        return itemView;
    }

    private void initView(View itemView) {
        carouselView = itemView.findViewById(R.id.homeCarouselView);
        rvHome = itemView.findViewById(R.id.rvHome);
        swipeRefreshLayout = itemView.findViewById(R.id.swipeRefreshLayout);
        btnLoadMore = itemView.findViewById(R.id.btnLoadMore);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.main_color));
        btnLoadMore.setOnClickListener(this);
    }

    private void configRvAdapter() {
        initData(1, pageLimit, 1, -1, "desc");

        layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        adapter = new HomeAdapter(getActivity(), contentList);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rvHome.addItemDecoration(itemDecoration);

        rvHome.setLayoutManager(layoutManager);
    }

    private void initBanner() {
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(sampleImages[position]);
            }
        });
        carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                Log.d("TAG", "onClick: " + position);
            }
        });
    }

    private List<Content> initData(int page, int limit, int status, long userId, String sortId) {
//        retrofit get data
        repository.getService().getPostList(page, limit, status, userId, sortId).enqueue(new Callback<HomeModel>() {
            @Override
            public void onResponse(Call<HomeModel> call, Response<HomeModel> response) {
                if (response.code() == 200) {
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
        contentList = initData(1, pageLimit, 1, -1, "desc");
//        adapter.reloadData(contentList);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLoadMore:
                pageLimit += 5;
//                setAnimationResource(R.anim.animation_layout_left_to_right);
                Log.d("TAG", "onClick: " + contentList);
                contentListSize = contentList.size();
                moreContentListSize = moreContentList.size();
                moreContentList = initData(1, pageLimit, 1, -1, "desc");
                contentList = moreContentList;
                if (contentListSize != moreContentListSize) {
                    adapter.reloadData(moreContentList);
                } else btnLoadMore.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    //    start eventbus
    private void toPostDetail(Content post) {
        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
        intent.putExtra("POST", post);
        Log.d("TAG", "toPostDetail search: ");
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent.PostEvent postEvent) {
        Content content = postEvent.getPost();
        toPostDetail(content);
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
//    end eventbus

    private void setAnimationResource(int animationResource) {
        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(getActivity(), animationResource);
        rvHome.setLayoutAnimation(layoutAnimationController);
        rvHome.setAdapter(adapter);
    }
}