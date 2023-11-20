package t2010a.cookpad_clone.adapter;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import t2010a.cookpad_clone.R;
import t2010a.cookpad_clone.model.client_model.Making;

public class EditPostStepAdapter extends RecyclerView.Adapter {
    private Activity activity;
    private List<Making> makingList;

    public EditPostStepAdapter(Activity activity, List<Making> makingList) {
        this.activity = activity;
        this.makingList = makingList;
    }

    public void reloadData(List<Making> makingList) {
        this.makingList = makingList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = activity.getLayoutInflater().inflate(R.layout.item_new_post_step, parent, false);
        NewRecipeStepViewHolder holder = new NewRecipeStepViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NewRecipeStepViewHolder viewHolder = (NewRecipeStepViewHolder) holder;
        Making model = makingList.get(position);
        model.setId(null);
        viewHolder.etStepDetail.setText(model.getName());
        viewHolder.etStepDetail.setHint("Bước " + (position + 1));
        viewHolder.tvPostStepId.setText("" + (position + 1));

        viewHolder.ivRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                makingList.remove(holder.getAdapterPosition());
                reloadData(makingList);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (makingList != null) {
            return makingList.size();
        }
        return 0;
    }

    public class NewRecipeStepViewHolder extends RecyclerView.ViewHolder {
        TextInputEditText etStepDetail;
        TextView tvPostStepId;
        ImageView ivRemoveItem;
        public NewRecipeStepViewHolder(@NonNull View itemView) {
            super(itemView);
            etStepDetail = itemView.findViewById(R.id.etStepDetail);
            tvPostStepId = itemView.findViewById(R.id.tvPostStepId);
            ivRemoveItem = itemView.findViewById(R.id.ivRemoveItem);

            etStepDetail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    makingList.get(getAdapterPosition()).setName(etStepDetail.getText().toString().trim());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }
}
