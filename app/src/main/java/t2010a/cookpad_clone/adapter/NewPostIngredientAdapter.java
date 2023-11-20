package t2010a.cookpad_clone.adapter;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import t2010a.cookpad_clone.R;
import t2010a.cookpad_clone.model.client_model.Ingredient;

public class NewPostIngredientAdapter extends RecyclerView.Adapter {
    private Activity activity;
    private List<Ingredient> ingredientList;

    public NewPostIngredientAdapter(Activity activity, List<Ingredient> ingredientList) {
        this.activity = activity;
        this.ingredientList = ingredientList;
    }

    public void reloadData(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = activity.getLayoutInflater().inflate(R.layout.item_new_post_gradient, parent, false);
        NewRecipeGradientViewHolder holder = new NewRecipeGradientViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NewRecipeGradientViewHolder viewHolder = (NewRecipeGradientViewHolder) holder;
        Ingredient model = ingredientList.get(position);
        viewHolder.etGradientDetail.setHint("Nguyên liệu " + (position + 1));

        viewHolder.ivRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientList.remove(holder.getAdapterPosition());
                reloadData(ingredientList);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (ingredientList != null) {
            return ingredientList.size();
        }
        return 0;
    }

    public class NewRecipeGradientViewHolder extends RecyclerView.ViewHolder {
        TextInputEditText etGradientDetail;
        ImageView ivRemoveItem;

        public NewRecipeGradientViewHolder(@NonNull View itemView) {
            super(itemView);
            etGradientDetail = itemView.findViewById(R.id.etGradientDetail);
            ivRemoveItem = itemView.findViewById(R.id.ivRemoveItem);

            etGradientDetail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    ingredientList.get(getAdapterPosition()).setName(etGradientDetail.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }
}
