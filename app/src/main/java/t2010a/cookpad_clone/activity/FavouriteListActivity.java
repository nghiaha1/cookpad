package t2010a.cookpad_clone.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

import t2010a.cookpad_clone.R;
import t2010a.cookpad_clone.adapter.FavouriteListAdapter;
import t2010a.cookpad_clone.model.shop.Product;

public class FavouriteListActivity extends AppCompatActivity {
    private RecyclerView rvFavouriteList;
    private FavouriteListAdapter adapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_list);
        initView();
        configRv();
    }

    private void initView() {
        rvFavouriteList = findViewById(R.id.rvFavouriteList);
    }

    private List<Product> initData() {
        return productList;
    }

    private void configRv() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvFavouriteList.setLayoutManager(layoutManager);
        adapter = new FavouriteListAdapter(this, productList);
        rvFavouriteList.setAdapter(adapter);
    }
}