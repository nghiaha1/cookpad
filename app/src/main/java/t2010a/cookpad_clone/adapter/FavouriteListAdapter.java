package t2010a.cookpad_clone.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import t2010a.cookpad_clone.R;
import t2010a.cookpad_clone.model.shop.Favourite;
import t2010a.cookpad_clone.model.shop.Product;

public class FavouriteListAdapter extends RecyclerView.Adapter {
    private Activity activity;
    private List<Product> productList;

    public FavouriteListAdapter(Activity activity, List<Product> productList) {
        this.activity = activity;
        this.productList = productList;
    }

    public void reloadData(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = activity.getLayoutInflater().inflate(R.layout.item_favourite_list, parent, false);
        return new FavouriteListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FavouriteListViewHolder viewHolder = (FavouriteListViewHolder) holder;
        Product model = productList.get(position);
        viewHolder.tvName.setText(model.getName());
        viewHolder.tvPrice.setText(model.getPrice().toString());
        viewHolder.tvDescription.setText(model.getDescription());
        Glide.with(activity).load(model.getThumbnails()).into(viewHolder.ivThumbnail);
    }

    @Override
    public int getItemCount() {
        if (productList != null) {
            return productList.size();
        }
        return 0;
    }

    public class FavouriteListViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumbnail;
        TextView tvName, tvPrice, tvDescription;
        public FavouriteListViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }
}
