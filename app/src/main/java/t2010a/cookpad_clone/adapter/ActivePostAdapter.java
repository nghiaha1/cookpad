package t2010a.cookpad_clone.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import t2010a.cookpad_clone.R;
import t2010a.cookpad_clone.event.MessageEvent;
import t2010a.cookpad_clone.local_data.LocalDataManager;
import t2010a.cookpad_clone.model.client_model.Content;
import t2010a.cookpad_clone.model.client_model.HomeModel;
import t2010a.cookpad_clone.repository.Repository;

public class ActivePostAdapter extends RecyclerView.Adapter {
    private Activity activity;
    private List<Content> contentList;
    private Repository repository = Repository.getInstance();
    private static int TYPE_EMPTY = 0;
    private static int TYPE_CONTENT = 1;

    public ActivePostAdapter(Activity activity, List<Content> contentList) {
        this.activity = activity;
        this.contentList = contentList;
    }

    public void reloadData(List<Content> contentList) {
        this.contentList = contentList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View itemView = activity.getLayoutInflater().inflate(R.layout.item_active_post, parent, false);
            return new ActivePostViewHolder(itemView);
        } else if (viewType == 0) {
            View itemView = activity.getLayoutInflater().inflate(R.layout.item_empty_post, parent, false);
            return new ActivePostViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 1) {
            ActivePostViewHolder viewHolder = (ActivePostViewHolder) holder;
            Content model = contentList.get(position);
            viewHolder.tvTitle.setText(model.getName());
            viewHolder.tvDetail.setText(model.getDetail());
            viewHolder.tvLikeNumber.setText(model.getLikes().toString());
            Glide.with(activity).load(model.getThumbnails()).into(viewHolder.ivThumbnail);

            viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Content content = model;
                    EventBus.getDefault().post(new MessageEvent.EditActivePostEvent(content));
                }
            });

            viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    repository.getService().deletePost(model.getId(), "Bearer " + LocalDataManager.getAccessToken()).enqueue(new Callback<Content>() {
                        @Override
                        public void onResponse(Call<Content> call, Response<Content> response) {
                        }

                        @Override
                        public void onFailure(Call<Content> call, Throwable t) {

                        }
                    });
                    contentList.remove(viewHolder.getAdapterPosition());
                    notifyItemRemoved(viewHolder.getAdapterPosition());
                }
            });
        } else if (holder.getItemViewType() == 0) {
            EmptyPostViewHolder viewHolder = (EmptyPostViewHolder) holder;
            viewHolder.tvNotification.setText("Không có bài đăng!");
        }
    }

    @Override
    public int getItemCount() {
        if (contentList != null) {
            return contentList.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (contentList == null) {
            return 0;
        } else return 1;
    }

    public class ActivePostViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDetail, tvLikeNumber;
        ImageView ivThumbnail;
        LinearLayout btnEdit, btnDelete;


        public ActivePostViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDetail = itemView.findViewById(R.id.tvDetail);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            tvLikeNumber = itemView.findViewById(R.id.tvLikeNumber);
        }
    }

    public class EmptyPostViewHolder extends RecyclerView.ViewHolder {
        TextView tvNotification;

        public EmptyPostViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNotification = itemView.findViewById(R.id.tvNotification);
        }
    }
}
