package t2010a.cookpad_clone.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import t2010a.cookpad_clone.R;
import t2010a.cookpad_clone.activity.PostDetailActivity;
import t2010a.cookpad_clone.adapter.SearchAdapter;
import t2010a.cookpad_clone.event.MessageEvent;
import t2010a.cookpad_clone.model.client_model.Content;
import t2010a.cookpad_clone.model.client_model.HomeModel;
import t2010a.cookpad_clone.model.client_model.Ingredient;
import t2010a.cookpad_clone.repository.Repository;

public class SearchFragment extends Fragment{
    private View itemView;
    private RecyclerView rvSearch;
    private List<Content> contentList = new ArrayList<>();
    private List<Content> moreContentList = new ArrayList<>();
    private Repository repository = Repository.getInstance();
    private SearchAdapter adapter;
    private SearchView searchView;
    private int pageLimit = 1000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        itemView = inflater.inflate(R.layout.fragment_search, container, false);
        initView(itemView);
        setSearchView();
        setAnimationResource(R.anim.animation_layout_left_to_right);
        return itemView;
    }

    private void initView(View itemView) {
        searchView = itemView.findViewById(R.id.searchView);
        rvSearch = itemView.findViewById(R.id.rvSearch);

        initData(1, pageLimit, 1 , -1, "desc");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        adapter = new SearchAdapter(getActivity(), contentList);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rvSearch.addItemDecoration(itemDecoration);

        rvSearch.setLayoutManager(layoutManager);
    }

    private List<Content> initData(int page, int limit, int status, long userId, String sortId) {
        repository.getService().getPostList(page, limit, status, userId, sortId).enqueue(new Callback<HomeModel>() {
            @Override
            public void onResponse(Call<HomeModel> call, Response<HomeModel> response) {
                if (response.code() == 200) {
                    contentList = response.body().getContent();
                    adapter.reloadData(contentList);
                    Log.d("TAG", "onResponse: Success");
                }
            }

            @Override
            public void onFailure(Call<HomeModel> call, Throwable t) {

            }
        });
        return contentList;
    }

    private void setSearchView() {
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
    }

    private void filterList(String text) {
        List<Content> filteredList = new ArrayList<>();
        for (Content item : contentList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            } else if (item.getUser().getFullName().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))) {
                filteredList.add(item);
            } else if (item.getCategory().getName().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))) {
                filteredList.add(item);
            } else if (item.getOrigin().getCode().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))) {
                filteredList.add(item);
            }
        }
        Log.d("TAG", "filterList: " + filteredList.size());

        if (filteredList.isEmpty()) {
            Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapter.reloadData(filteredList);
        }
    }

    private void toPostDetail(Content content) {
        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
        Log.d("TAG", "toPostDetail: ");
        intent.putExtra("POST", content);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent.SearchFragmentEvent searchFragmentEvent) {
        Content content = searchFragmentEvent.getPost();
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

    private void setAnimationResource(int animationResource) {
        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(getActivity(), animationResource);
        rvSearch.setLayoutAnimation(layoutAnimationController);
        rvSearch.setAdapter(adapter);
    }
}