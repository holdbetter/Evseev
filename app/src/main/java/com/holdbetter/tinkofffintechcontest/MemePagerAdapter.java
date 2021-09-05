package com.holdbetter.tinkofffintechcontest;

import static com.holdbetter.tinkofffintechcontest.MainActivity.DEFAULT_PAGER_SIZE;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MemePagerAdapter extends FragmentStateAdapter {
    private int fragmentStartSize = DEFAULT_PAGER_SIZE;

    public MemePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return CardFragment.getInstance(position);
    }

    @Override
    public int getItemCount() {
        return fragmentStartSize;
    }

    public void addPage() {
        fragmentStartSize += 1;
        notifyItemInserted(fragmentStartSize - 1);
    }
}
