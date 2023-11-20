package t2010a.cookpad_clone.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class InactivePostAdapter extends RecyclerView.Adapter {
    private Activity activity;
    private List<Content> contentList;
    private Repository repository = Repository.getInstance();

    public InactivePostAdapter(Activity activity, List<Content> contentList) {
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
        View itemView = activity.getLayoutInflater().inflate(R.layout.item_inactive_post, parent, false);
        InactivePostViewHolder holder = new InactivePostViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        InactivePostViewHolder viewHolder = (InactivePostViewHolder) holder;
        Content model = contentList.get(position);
        viewHolder.tvTitle.setText(model.getName());
        viewHolder.tvDetail.setText(model.getDetail());
        Glide.with(activity).load(model.getThumbnails()).into(viewHolder.ivThumbnail);
        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Content content = model;
                EventBus.getDefault().post(new MessageEvent.EditInactivePostEvent(content));
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
    }

    @Override
    public int getItemCount() {
        if (contentList != null) {
            return contentList.size();
        }
        return 0;
    }

    public class InactivePostViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDetail;
        ImageView ivThumbnail;
        LinearLayout btnEdit, btnDelete;

        public InactivePostViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDetail = itemView.findViewById(R.id.tvDetail);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
