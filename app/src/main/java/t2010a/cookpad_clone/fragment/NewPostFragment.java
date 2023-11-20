package t2010a.cookpad_clone.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import t2010a.cookpad_clone.R;
import t2010a.cookpad_clone.activity.NewPostActivity;
import t2010a.cookpad_clone.adapter.ViewPagerAdapter;

public class NewPostFragment extends Fragment {
    private View itemView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;
    private Toolbar toolbar;
    private AppBarLayout appBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        itemView = inflater.inflate(R.layout.fragment_new_post, container, false);

        initView(itemView);
        configViewPagerAdapter();
        return itemView;
    }

    private void initView(View itemView) {
        viewPager = itemView.findViewById(R.id.viewPager);
        tabLayout = itemView.findViewById(R.id.tabLayout);
        toolbar = itemView.findViewById(R.id.toolbar);
        appBar = itemView.findViewById(R.id.appBar);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        appBar.setOutlineProvider(null);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");
        setHasOptionsMenu(true);
    }

    private void configViewPagerAdapter() {
        viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setLayout_new_recipe() {
        Intent intent = new Intent(getActivity(), NewPostActivity.class);
        startActivity(intent);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_post, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miAdd:
                setLayout_new_recipe();
                break;
            default:
                break;
        }
        return true;
    }
}