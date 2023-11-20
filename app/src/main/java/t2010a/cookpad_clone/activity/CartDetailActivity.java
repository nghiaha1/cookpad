package t2010a.cookpad_clone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import t2010a.cookpad_clone.R;
import t2010a.cookpad_clone.adapter.CartDetailAdapter;
import t2010a.cookpad_clone.local_data.LocalDataManager;
import t2010a.cookpad_clone.model.shop.Cart;
import t2010a.cookpad_clone.model.shop.CartItem;
import t2010a.cookpad_clone.repository.Repository;

public class CartDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvCartCount, tvTotal;
    private RecyclerView rvCartDetail;
    private Toolbar toolbar;
    private AppBarLayout appBar;
    private Button btnToCheckout;
    public BigDecimal totalPrice;

    private List<CartItem> cartItemList = new ArrayList<>();
    private Repository repository = Repository.getInstance();
    private CartDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_detail);
        initView();
        configRv();
        configToolBar();
        totalPrice = adapter.totalPrice();
    }

    private void initView() {
        rvCartDetail = findViewById(R.id.rvCartDetail);
        toolbar = findViewById(R.id.toolbar);
        appBar = findViewById(R.id.appBar);
        btnToCheckout = findViewById(R.id.btnToCheckout);
        tvTotal = findViewById(R.id.tvTotal);

        btnToCheckout.setOnClickListener(this);
    }

    private void configRv() {
        cartItemList = initData();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new CartDetailAdapter(this, cartItemList);

        rvCartDetail.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));

        rvCartDetail.setAdapter(adapter);
        rvCartDetail.setLayoutManager(layoutManager);
    }

    private List<CartItem> initData() {
        //retrofit get data
        repository.getService().findAllCart("Bearer " + LocalDataManager.getAccessToken()).enqueue(new Callback<List<CartItem>>() {
            @Override
            public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {
                if (response.code() == 200) {
                    cartItemList = response.body();
                    adapter.reloadData(cartItemList);
                    totalPrice();
                }

            }

            @Override
            public void onFailure(Call<List<CartItem>> call, Throwable t) {
                Log.d("TAG", "onFailure: ");
            }
        });
        return cartItemList;
    }

    private void configToolBar() {
        setSupportActionBar(toolbar);
        appBar.setOutlineProvider(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void totalPrice() {
        BigDecimal price = adapter.totalPrice();
        tvTotal.setText(price.toString() + " VND");
        adapter.reloadData(cartItemList);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnToCheckout:
                Intent intent = new Intent(this, CheckoutActivity.class);
                intent.putExtra("CART", totalPrice);
                startActivity(intent);
                break;
        }
    }
}