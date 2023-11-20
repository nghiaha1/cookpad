package t2010a.cookpad_clone.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import t2010a.cookpad_clone.R;
import t2010a.cookpad_clone.model.client_model.Making;

public class PostStepAdapter extends RecyclerView.Adapter {
    private Activity activity;
    private List<Making> makingList;

    public PostStepAdapter(Activity activity, List<Making> makingList) {
        this.activity = activity;
        this.makingList = makingList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = activity.getLayoutInflater().inflate(R.layout.item_post_step, parent, false);
        PostStepViewHolder holder = new PostStepViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PostStepViewHolder viewHolder = (PostStepViewHolder) holder;
        Making model = makingList.get(position);
        viewHolder.tvPostStepId.setText(Integer.toString((position + 1)));
        viewHolder.tvPostStepDetail.setText(model.getName());
    }

    @Override
    public int getItemCount() {
        if (makingList != null) {
            return makingList.size();
        }
        return 0;
    }

    public class PostStepViewHolder extends RecyclerView.ViewHolder {
        TextView tvPostStepId, tvPostStepDetail;
        public PostStepViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPostStepId = itemView.findViewById(R.id.tvPostStepId);
            tvPostStepDetail = itemView.findViewById(R.id.tv_post_step_detail);
        }
    }
}
