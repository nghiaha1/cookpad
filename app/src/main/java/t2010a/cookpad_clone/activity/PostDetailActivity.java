package t2010a.cookpad_clone.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import t2010a.cookpad_clone.R;
import t2010a.cookpad_clone.adapter.PostGradientAdapter;
import t2010a.cookpad_clone.adapter.PostStepAdapter;
import t2010a.cookpad_clone.local_data.LocalDataManager;
import t2010a.cookpad_clone.model.client_model.Content;
import t2010a.cookpad_clone.model.client_model.Ingredient;
import t2010a.cookpad_clone.model.client_model.Making;
import t2010a.cookpad_clone.model.client_model.UserLikes;
import t2010a.cookpad_clone.repository.Repository;

public class PostDetailActivity extends AppCompatActivity {
    private TextView tvUserFullName, tvUserUsername, tvUserAddress, tvPostTimer, tvPostTitle, tvLikeNumber, tvOrigin, tvCategory, tvDetail;
    private RecyclerView rvPostGradient, rvPostStep;
    private Toolbar toolbar;
    private AppBarLayout appBar;
    private ImageView ivThumbnail;
    private ShapeableImageView ivUserAvatar;
    private Button cbLike;

    private Content post;
    private List<Ingredient> ingredientList = new ArrayList<>();
    private List<Making> makingList = new ArrayList<>();
    private Repository repository = Repository.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        initView();

        post = (Content) getIntent().getSerializableExtra("POST");
        tvPostTitle.setText(post.getName());
        tvUserFullName.setText(post.getUser().getFullName());
        tvUserUsername.setText(post.getUser().getUsername());
        tvUserAddress.setText(post.getUser().getAddress());
        tvPostTimer.setText(post.getCookingTime().toString());
        tvLikeNumber.setText(post.getLikes().toString());
        tvOrigin.setText(post.getOrigin().getCode());
        tvCategory.setText(post.getCategory().getName());
        tvDetail.setText(post.getDetail());

        for (int i = 0; i < post.getUserIdLikes().size(); i++) {
            if (post.getUserIdLikes().get(i).getUserId().equals(LocalDataManager.getUserDetail().getId())) {
                post.setLikeStatus(true);
                cbLike.setBackgroundResource(R.drawable.ic_like_colorized);
            }
        }

        cbLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!post.isLikeStatus()) {
                    post.setLikeStatus(true);
                    post.setLikes(post.getLikes() + 1);
                    tvLikeNumber.setText(post.getLikes().toString());
                    cbLike.setBackgroundResource(R.drawable.ic_like_colorized);
                    post.setUserIdLikes(Collections.singletonList(new UserLikes(post.getId(), LocalDataManager.getUserDetail().getId())));
                    repository.getService().updatePost(post.getId(), post, "Bearer " + LocalDataManager.getAccessToken()).enqueue(new Callback<Content>() {
                        @Override
                        public void onResponse(Call<Content> call, Response<Content> response) {
                            if (response.code() == 200) {
                                Log.d("TAG", "onResponse: likes" + post);
                            }
                        }

                        @Override
                        public void onFailure(Call<Content> call, Throwable t) {
                            Log.d("TAG", "onFailure: unlikes");
                        }
                    });
                }
            }
        });

        Glide.with(this).load(post.getThumbnails()).into(ivThumbnail);
        Glide.with(this).load(post.getUser().getAvatar()).into(ivUserAvatar);

        setRecycleView();
    }

    private void initView() {
        ivUserAvatar = findViewById(R.id.ivUserAvatar);
        tvDetail = findViewById(R.id.tvDetail);
        tvUserFullName = findViewById(R.id.tvUserFullName);
        tvOrigin = findViewById(R.id.tvOrigin);
        tvUserUsername = findViewById(R.id.tvUserUsername);
        tvUserAddress = findViewById(R.id.tvUserAddress);
        tvPostTimer = findViewById(R.id.tvPostTimer);
        tvPostTitle = findViewById(R.id.tvTitle);
        tvCategory = findViewById(R.id.tvCategory);
        toolbar = findViewById(R.id.toolbar);
        appBar = findViewById(R.id.appBar);
        ivThumbnail = findViewById(R.id.ivThumbnail);
        rvPostGradient = findViewById(R.id.rvPostGradient);
        rvPostStep = findViewById(R.id.rvPostStep);
        tvLikeNumber = findViewById(R.id.tvLikeNumber);
        cbLike = findViewById(R.id.cbLike);
    }

    private void setRecycleView() {
        initData();

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        PostGradientAdapter adapter1 = new PostGradientAdapter(this, ingredientList);
        PostStepAdapter adapter2 = new PostStepAdapter(this, makingList);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvPostGradient.addItemDecoration(itemDecoration);
        rvPostStep.addItemDecoration(itemDecoration);

        rvPostGradient.setLayoutManager(layoutManager1);
        rvPostGradient.setAdapter(adapter1);

        rvPostStep.setLayoutManager(layoutManager2);
        rvPostStep.setAdapter(adapter2);
    }

    private void initData() {
        ingredientList = post.getIngredient();
        makingList = post.getMaking();

        setSupportActionBar(toolbar);
        appBar.setOutlineProvider(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}