package t2010a.cookpad_clone.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import t2010a.cookpad_clone.R;
import t2010a.cookpad_clone.activity.PostDetailActivity;
import t2010a.cookpad_clone.model.client_model.Ingredient;

public class PostGradientAdapter extends RecyclerView.Adapter {
    private Activity activity;
    private List<Ingredient> ingredientList;

    public PostGradientAdapter(PostDetailActivity activity, List<Ingredient> ingredientList) {
        this.activity = activity;
        this.ingredientList = ingredientList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = activity.getLayoutInflater().inflate(R.layout.item_post_gradient, parent, false);
        PostGradientViewHolder holder = new PostGradientViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PostGradientViewHolder viewHolder = (PostGradientViewHolder) holder;
        Ingredient model = ingredientList.get(position);
        viewHolder.tvPostIngredient.setText(model.getName());
    }

    @Override
    public int getItemCount() {
        if (ingredientList != null) {
            return ingredientList.size();
        }
        return 0;
    }

    public class PostGradientViewHolder extends RecyclerView.ViewHolder {
        TextView tvPostIngredient;
        public PostGradientViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPostIngredient = itemView.findViewById(R.id.tvPostIngredient);
        }
    }
}
