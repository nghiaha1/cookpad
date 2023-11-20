package t2010a.cookpad_clone.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.math.BigDecimal;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import t2010a.cookpad_clone.R;
import t2010a.cookpad_clone.activity.CartDetailActivity;
import t2010a.cookpad_clone.interfaces.ItemClickListener;
import t2010a.cookpad_clone.local_data.LocalDataManager;
import t2010a.cookpad_clone.model.shop.CartItem;
import t2010a.cookpad_clone.repository.Repository;

public class CartDetailAdapter extends RecyclerView.Adapter {
    private Activity activity;
    private List<CartItem> cartItemList;
    private Repository repository = Repository.getInstance();


    public CartDetailAdapter(Activity activity, List<CartItem> cartItemList) {
        this.activity = activity;
        this.cartItemList = cartItemList;
    }

    public void reloadData(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = activity.getLayoutInflater().inflate(R.layout.item_cart_detail, parent, false);
        CartDetailViewHolder holder = new CartDetailViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CartDetailViewHolder viewHolder = (CartDetailViewHolder) holder;
        CartItem model = cartItemList.get(position);
        Glide.with(activity).load(model.getProduct().getThumbnails()).into(viewHolder.ivThumbnail);
        viewHolder.tvPrice.setText(model.getProduct().getPrice().toString());
        viewHolder.tvName.setText(model.getProduct().getName());

        viewHolder.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "tang: ");
                Integer soLuongCu = Integer.parseInt(viewHolder.tvItemCount.getText().toString());
                if (soLuongCu < model.getProduct().getQuantity()) {
                    Integer soLuongMoi = soLuongCu + 1;
                    viewHolder.tvItemCount.setText(String.valueOf(soLuongMoi));
                    updateCart(model.getId().getShoppingCartId(), model.getProduct().getId(), soLuongMoi);
                    cartItemList.get(holder.getAdapterPosition()).setQuantity(soLuongMoi);
                    notifyItemRangeChanged(holder.getAdapterPosition(), getItemCount());
                    reloadData(cartItemList);
                    totalPrice();

                } else {
                    Toast.makeText(activity.getApplication(), "You have reached your product limit!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewHolder.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repository.getService().deleteCart("Bearer " + LocalDataManager.getAccessToken(), model.getId().getShoppingCartId(), model.getProduct().getId()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        cartItemList.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        notifyItemRangeChanged(holder.getAdapterPosition(), getItemCount());
                        reloadData(cartItemList);

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(activity.getApplication(), "Error!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        if (cartItemList != null) {
            return cartItemList.size();
        }
        return 0;
    }

    public class CartDetailViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumbnail, ivMinus, ivAdd;
        TextView tvName, tvPrice, tvItemCount, tvTotalPrice;

        public CartDetailViewHolder(@NonNull View itemView) {
            super(itemView);

            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            ivMinus = itemView.findViewById(R.id.ivMinus);
            ivAdd = itemView.findViewById(R.id.ivAdd);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvItemCount = itemView.findViewById(R.id.tvItemCount);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
        }
    }

    public BigDecimal totalPrice() {
        BigDecimal price = BigDecimal.valueOf(0);
        for (int i = 0; i < cartItemList.size(); i++) {
            price = price.add(cartItemList.get(i).getProduct().getPrice().multiply(BigDecimal.valueOf(cartItemList.get(i).getQuantity())));
        }
        return price;
    }

    private void updateCart(long cartId, long productId, int quantity) {
        repository.getService().updateCart("Bearer " + LocalDataManager.getAccessToken(), cartId, productId, quantity).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}
