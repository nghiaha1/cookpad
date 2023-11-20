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

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import t2010a.cookpad_clone.R;
import t2010a.cookpad_clone.event.MessageEvent;
import t2010a.cookpad_clone.local_data.LocalDataManager;
import t2010a.cookpad_clone.model.shop.Favourite;
import t2010a.cookpad_clone.model.shop.FavouriteModel;
import t2010a.cookpad_clone.model.shop.Product;
import t2010a.cookpad_clone.repository.Repository;

public class ShopProductAdapter extends RecyclerView.Adapter {
    private Activity activity;
    private List<Product> productList;
    private Repository repository = Repository.getInstance();

    public ShopProductAdapter(Activity activity, List<Product> productList) {
        this.activity = activity;
        this.productList = productList;
    }

    public void reloadData(List<Product> list) {
        productList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = activity.getLayoutInflater().inflate(R.layout.item_shop_product, parent, false);
        ShopViewHolder holder = new ShopViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ShopViewHolder viewHolder = (ShopViewHolder) holder;
        Product model = productList.get(position);
        Glide.with(activity).load(model.getThumbnails()).into(viewHolder.ivThumbnail);
        viewHolder.tvName.setText(model.getName());
        viewHolder.tvPrice.setText(model.getPrice().toString() + " ₫");

        viewHolder.cbFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!model.isFavStatus()) {
                    model.setFavStatus(true);
                    viewHolder.cbFavourite.setBackgroundResource(R.drawable.ic_heart_red);

                    Favourite favourite = new Favourite();

                    List<Product> favProduct = new ArrayList<>();

                    favProduct.add(model);

                    Log.d("TAG", "onClick: " + favProduct);

                    favProduct.add(model);

                    favourite.setProducts(favProduct);

                    repository.getService().postFavourite("Bearer " + LocalDataManager.getAccessToken(), model.getId()).enqueue(new Callback<FavouriteModel>() {
                        @Override
                        public void onResponse(Call<FavouriteModel> call, Response<FavouriteModel> response) {
                            if (response.code() == 200) {
                                Log.d("TAG", "onResponse: Add to fav ");
                            }
                        }

                        @Override
                        public void onFailure(Call<FavouriteModel> call, Throwable t) {
                            Log.d("TAG", "onFailure: " + t);
                        }
                    });
                } else {
                    model.setFavStatus(false);
                    viewHolder.cbFavourite.setBackgroundResource(R.drawable.ic_heart);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (productList != null) {
            return productList.size();
        }
        return 0;
    }

    public class ShopViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumbnail;
        Button cbFavourite;
        TextView tvName, tvPrice;

        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            cbFavourite = itemView.findViewById(R.id.cbFavourite);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);

            ivThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("TAG", "onClick: Shop fragment " + getAdapterPosition());
                    Product content = productList.get(getAdapterPosition());
                    EventBus.getDefault().post(new MessageEvent.ProductEvent(content));
                }
            });
        }


    }
}
