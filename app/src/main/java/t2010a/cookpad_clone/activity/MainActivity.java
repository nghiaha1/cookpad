package t2010a.cookpad_clone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import t2010a.cookpad_clone.R;
import t2010a.cookpad_clone.fragment.HomeFragment;
import t2010a.cookpad_clone.fragment.NewPostFragment;
import t2010a.cookpad_clone.fragment.ProfileFragment;
import t2010a.cookpad_clone.fragment.SearchFragment;
import t2010a.cookpad_clone.fragment.ShoppingFragment;
import t2010a.cookpad_clone.local_data.LocalDataManager;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navigationView;
    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private NewPostFragment newPostFragment;
    private ShoppingFragment shoppingFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();

        addFragments(homeFragment);
    }

    private void initView() {
        navigationView = findViewById(R.id.bottom_nav);

        searchFragment = new SearchFragment();
        newPostFragment = new NewPostFragment();
        shoppingFragment = new ShoppingFragment();
        profileFragment = new ProfileFragment();
        homeFragment = new HomeFragment();
    }

    private void addFragments(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    private void onActionHome() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.show(homeFragment);
        transaction.hide(searchFragment);
        transaction.hide(newPostFragment);
        transaction.hide(shoppingFragment);
        transaction.hide(profileFragment);
        transaction.commit();
    }

    private void onActionSearch() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!searchFragment.isAdded()) addFragments(searchFragment);

        transaction.show(searchFragment);
        transaction.hide(homeFragment);
        transaction.hide(newPostFragment);
        transaction.hide(shoppingFragment);
        transaction.hide(profileFragment);
        transaction.commit();
    }

    private void onActionNewRecipe() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!newPostFragment.isAdded()) addFragments(newPostFragment);

        transaction.show(newPostFragment);
        transaction.hide(homeFragment);
        transaction.hide(searchFragment);
        transaction.hide(shoppingFragment);
        transaction.hide(profileFragment);
        transaction.commit();
    }

    private void onActionShopping() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!shoppingFragment.isAdded()) addFragments(shoppingFragment);

        transaction.show(shoppingFragment);
        transaction.hide(homeFragment);
        transaction.hide(newPostFragment);
        transaction.hide(searchFragment);
        transaction.hide(profileFragment);
        transaction.commit();
    }

    private void onActionProfile() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!profileFragment.isAdded()) addFragments(profileFragment);

        transaction.show(profileFragment);
        transaction.hide(homeFragment);
        transaction.hide(newPostFragment);
        transaction.hide(searchFragment);
        transaction.hide(shoppingFragment);
        transaction.commit();
    }

    private void initListener() {
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        onActionHome();
                        break;
                    case R.id.action_search:
                        onActionSearch();
                        break;
                    case R.id.action_new_recipe:
                        onActionNewRecipe();
                        break;
                    case R.id.action_shopping:
                        onActionShopping();
                        break;
                    case R.id.action_profile:
                        onActionProfile();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }
}