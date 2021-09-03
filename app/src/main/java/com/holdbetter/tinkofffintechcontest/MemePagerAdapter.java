package com.holdbetter.tinkofffintechcontest;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.holdbetter.tinkofffintechcontest.model.Meme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MemePagerAdapter extends FragmentStateAdapter
{
    List<Meme> memes = new ArrayList<>(Arrays.asList(new Meme(), new Meme(), new Meme()));
//    int[] memes = new int[3];

    public MemePagerAdapter(@NonNull FragmentActivity fragmentActivity)
    {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position)
    {
        return CardFragment.getInstance();
    }

    @Override
    public int getItemCount()
    {
        return memes.size();
    }
}
