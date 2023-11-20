package t2010a.cookpad_clone.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import t2010a.cookpad_clone.R;
import t2010a.cookpad_clone.activity.EditProfileActivity;
import t2010a.cookpad_clone.activity.LoginActivity;
import t2010a.cookpad_clone.local_data.LocalDataManager;
import t2010a.cookpad_clone.model.client_model.User;


public class ProfileFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private View itemView;
    private TextView tvFullName, tvUsername, tvEmail;
    private ShapeableImageView ivAvatar;
    private LinearLayout layoutEditProfile, btnLogout;
    private SwipeRefreshLayout swipeRefreshLayout;

    private User user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        itemView = inflater.inflate(R.layout.fragment_profile, container, false);
        initView(itemView);
        initData();
        return itemView;
    }

    private void initView(View itemView) {
        tvFullName = itemView.findViewById(R.id.tvFullName);
        tvUsername = itemView.findViewById(R.id.tvUsername);
        tvEmail = itemView.findViewById(R.id.tvPostDescription);
        ivAvatar = itemView.findViewById(R.id.ivAvatar);
        layoutEditProfile = itemView.findViewById(R.id.layoutEditProfile);
        btnLogout = itemView.findViewById(R.id.btnLogout);
        swipeRefreshLayout = itemView.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.main_color));

        btnLogout.setOnClickListener(this);
        layoutEditProfile.setOnClickListener(this);
    }

    private void initData() {
        user = LocalDataManager.getUserDetail();
        if (user != null) {
            tvFullName.setText(user.getFullName());
            tvUsername.setText(user.getUsername());
            if ((user.getEmail() == null)) {
                tvEmail.setText("email@email.com");
            } else {
                tvEmail.setText(user.getEmail());
            }            Glide.with(this).load(user.getAvatar()).into(ivAvatar);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogout:
                LocalDataManager.clearData();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.layoutEditProfile:
                startActivity(new Intent(getActivity(), EditProfileActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                User user = LocalDataManager.getUserDetail();
                tvFullName.setText(user.getFullName());
                tvUsername.setText(user.getUsername());
                if ((user.getEmail() == null)) {
                    tvEmail.setText("email@email.com");
                } else {
                    tvEmail.setText(user.getEmail());
                }
                Glide.with(ProfileFragment.this).load(user.getAvatar()).into(ivAvatar);
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }
}