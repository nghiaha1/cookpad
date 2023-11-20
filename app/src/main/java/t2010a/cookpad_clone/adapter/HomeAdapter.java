package t2010a.cookpad_clone.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import t2010a.cookpad_clone.R;
import t2010a.cookpad_clone.event.MessageEvent;
import t2010a.cookpad_clone.local_data.LocalDataManager;
import t2010a.cookpad_clone.model.client_model.Content;
import t2010a.cookpad_clone.model.client_model.HomeModel;
import t2010a.cookpad_clone.model.client_model.User;
import t2010a.cookpad_clone.model.client_model.UserLikes;
import t2010a.cookpad_clone.repository.Repository;

public class HomeAdapter extends RecyclerView.Adapter {
    private Activity activity;
    private List<Content> contentList;

    Repository repository;
    User user;

    public HomeAdapter(Activity activity, List<Content> contentList) {
        this.activity = activity;
        this.contentList = contentList;
    }

    public void reloadData(List<Content> list) {
        contentList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = activity.getLayoutInflater().inflate(R.layout.item_home, parent, false);
        HomeViewHolder holder = new HomeViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HomeViewHolder viewHolder = (HomeViewHolder) holder;
        Content model = contentList.get(position);

        Glide.with(activity).load(model.getUser().getAvatar()).into(viewHolder.ivAvatar);

        viewHolder.tvFullName.setText(model.getUser().getFullName());
        viewHolder.tvEmail.setText(model.getUser().getEmail());
        Glide.with(activity).load(model.getThumbnails()).into(viewHolder.ivThumbnail);
        viewHolder.tvDescription.setText(model.getDetail());
        viewHolder.tvLikeNumber.setText(model.getLikes().toString());
        viewHolder.tvTitle.setText(model.getName());

        repository = Repository.getInstance();

        for (int i = 0; i < model.getUserIdLikes().size(); i++) {
            if (model.getUserIdLikes().get(i).getUserId().equals(LocalDataManager.getUserDetail().getId())) {
                model.setLikeStatus(true);
                viewHolder.cbLike.setBackgroundResource(R.drawable.ic_like_colorized);
            }
        }
        viewHolder.cbLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!model.isLikeStatus()) {
                    model.setLikeStatus(true);
                    model.setLikes((model.getLikes() + 1) + model.getUserIdLikes().size());
                    viewHolder.tvLikeNumber.setText(model.getLikes().toString());
                    viewHolder.cbLike.setBackgroundResource(R.drawable.ic_like_colorized);
                    model.setUserIdLikes(Collections.singletonList(new UserLikes(model.getId(), LocalDataManager.getUserDetail().getId())));
                    user = LocalDataManager.getUserDetail();
                    repository.getService().updatePost(model.getId(), model, "Bearer " + LocalDataManager.getAccessToken()).enqueue(new Callback<Content>() {
                        @Override
                        public void onResponse(Call<Content> call, Response<Content> response) {
                            if (response.code() == 200) {
                                Log.d("TAG", "onResponse: likes" + model);
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
    }

    @Override
    public int getItemCount() {
        if (contentList != null) {
            return contentList.size();
        }
        return 0;
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView ivAvatar;
        TextView tvFullName, tvEmail, tvDescription, tvLikeNumber, tvTitle;
        ImageView ivThumbnail;
        Button cbLike;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvEmail = itemView.findViewById(R.id.tvPostDescription);
            tvLikeNumber = itemView.findViewById(R.id.tvLikeNumber);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            cbLike = itemView.findViewById(R.id.cbLike);

            ivThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TAG", "onClick: Home fragment " + getAdapterPosition());
                    Content content = contentList.get(getAdapterPosition());
                    EventBus.getDefault().post(new MessageEvent.PostEvent(content));
                }
            });


        }
    }
}
