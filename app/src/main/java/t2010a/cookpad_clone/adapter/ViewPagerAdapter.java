package t2010a.cookpad_clone.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import t2010a.cookpad_clone.fragment.ActivePostFragment;
import t2010a.cookpad_clone.fragment.InactivePostFragment;
import t2010a.cookpad_clone.fragment.PendingPostFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {


    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ActivePostFragment();
            case 1:
                return new InactivePostFragment();
            case 2:
                return new PendingPostFragment();
            default:
                return new ActivePostFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Bài đăng";
                break;
            case 1:
                title = "Bản nháp";
                break;
            case 2:
                title = "Đang chờ";
                break;
        }
        return title;
    }
}
