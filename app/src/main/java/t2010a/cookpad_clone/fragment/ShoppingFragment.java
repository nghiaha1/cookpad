package t2010a.cookpad_clone.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.bloco.faker.Faker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import t2010a.cookpad_clone.R;
import t2010a.cookpad_clone.activity.ProductDetailActivity;
import t2010a.cookpad_clone.adapter.ShopProductAdapter;
import t2010a.cookpad_clone.event.MessageEvent;
import t2010a.cookpad_clone.model.client_model.Content;
import t2010a.cookpad_clone.model.shop.Product;
import t2010a.cookpad_clone.model.shop.ShopModel;
import t2010a.cookpad_clone.repository.Repository;

public class ShoppingFragment extends Fragment {
    private View itemView;
    private SearchView searchView;
    private RecyclerView rvShop;
    private List<Product> productList = new ArrayList<>();
    private ShopProductAdapter adapter;
    private Faker faker = new Faker();
    private Repository repository = Repository.getInstance();
    private int pageLimit = 1000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        itemView = inflater.inflate(R.layout.fragment_shopping, container, false);
        initView(itemView);
        setAnimationResource(R.anim.animation_layout_left_to_right);
        setSearchView();
        return itemView;
    }

    private void initView(View itemView) {
        searchView = itemView.findViewById(R.id.searchView);
        rvShop = itemView.findViewById(R.id.rvShop);


        initData(1, pageLimit, 1, -1, "desc", "asc");

        adapter = new ShopProductAdapter(getActivity(), productList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);

        rvShop.setLayoutManager(layoutManager);
    }

    private List<Product> initData(int page, int limit, int status, long userId, String sortId, String sortPrice) {
        repository.getService().getProduct(page, limit, status, userId, sortId, sortPrice).enqueue(new Callback<ShopModel>() {
            @Override
            public void onResponse(Call<ShopModel> call, Response<ShopModel> response) {
                productList = response.body().getProductList();
                adapter.reloadData(productList);
//                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ShopModel> call, Throwable t) {
                Log.d("TAG", "onFailure: ");
            }
        });
        return productList;
    }

    private void setSearchView() {
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
    }

    private void filterList(String text) {
        List<Product> filteredList = new ArrayList<>();
        for (Product item : productList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
//            else if (item.getEmail().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))) {
//                filteredList.add(item);
//            }
        }
        Log.d("TAG", "filterList: " + filteredList.size());
        if (filteredList.isEmpty()) {
            Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapter.reloadData(filteredList);
        }
    }

    //    start eventbus
    private void toProductDetail(Product product) {
        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
        intent.putExtra("PRODUCT", product);
        Log.d("TAG", "toProductDetail search: ");
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent.ProductEvent productEvent) {
        Product content = productEvent.getProduct();
        toProductDetail(content);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
    //    end eventbus

    private void setAnimationResource(int animationResource) {
        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(getActivity(), animationResource);
        rvShop.setLayoutAnimation(layoutAnimationController);
        rvShop.setAdapter(adapter);
    }
}