package t2010a.cookpad_clone.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import t2010a.cookpad_clone.R;
import t2010a.cookpad_clone.event.MessageEvent;
import t2010a.cookpad_clone.model.client_model.Content;

public class SearchAdapter extends RecyclerView.Adapter {
    private Activity activity;
    private List<Content> contentList;

    public SearchAdapter(Activity activity, List<Content> contentList) {
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
        View itemView = activity.getLayoutInflater().inflate(R.layout.item_search, parent, false);
        SearchViewHolder holder = new SearchViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SearchViewHolder viewHolder = (SearchViewHolder) holder;
        Content model = contentList.get(position);
        viewHolder.tvPostTitle.setText(model.getName());
        viewHolder.tvOrigin.setText(model.getOrigin().getCode());
        viewHolder.tvFullName.setText(model.getUser().getFullName());
        viewHolder.tvCategory.setText(model.getCategory().getName());
        Glide.with(activity).load(model.getThumbnails()).into(viewHolder.ivPostThumbnail);
    }

    @Override
    public int getItemCount() {
        if (contentList != null) {
            return contentList.size();
        }
        return 0;
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView ivPostThumbnail;
        TextView tvPostTitle, tvOrigin, tvFullName, tvCategory;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPostThumbnail = itemView.findViewById(R.id.ivPostThumbnail);
            tvPostTitle = itemView.findViewById(R.id.tvName);
            tvOrigin = itemView.findViewById(R.id.tvOrigin);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TAG", "onClick: search adapter");
                    Content content = contentList.get(getAdapterPosition());
                    EventBus.getDefault().post(new MessageEvent.SearchFragmentEvent(content));
                }
            });
        }
    }
}
