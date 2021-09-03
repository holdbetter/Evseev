package com.holdbetter.tinkofffintechcontest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.MarginPageTransformer;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import com.holdbetter.tinkofffintechcontest.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        MemePagerAdapter adapter = new MemePagerAdapter(this);
        float px = convertDpToPx(24);

        binding.memePager.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
        binding.memePager.setPageTransformer(new MarginPageTransformer((int) px));
        binding.memePager.setAdapter(adapter);
    }

    private int convertDpToPx(int dp)
    {
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                this.getResources().getDisplayMetrics()
        );
        return (int) px;
    }
}