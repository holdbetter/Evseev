package com.holdbetter.tinkofffintechcontest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.holdbetter.tinkofffintechcontest.databinding.CardFragmentBinding;

public class CardFragment extends Fragment
{
    private CardFragment() {}

    public static CardFragment getInstance() {
        return new CardFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return CardFragmentBinding.inflate(inflater, container, false).getRoot();
    }
}
