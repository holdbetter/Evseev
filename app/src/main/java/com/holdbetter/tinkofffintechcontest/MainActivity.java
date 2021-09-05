package com.holdbetter.tinkofffintechcontest;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.holdbetter.tinkofffintechcontest.databinding.ActivityMainBinding;
import com.holdbetter.tinkofffintechcontest.viewmodel.MemeViewModel;

public class MainActivity extends AppCompatActivity {
    public static final int DEFAULT_PAGER_SIZE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        // initialize
        MemeViewModel viewModel = new ViewModelProvider(this).get(MemeViewModel.class);
        viewModel.getCurrentPage().observe(this, i -> binding.previousMeme.setEnabled(i > 0));

        MemePagerAdapter adapter = setupViewPager(binding.memePager, viewModel);

        binding.nextMeme.setOnClickListener(new NextMemeClickListener(binding,  adapter, viewModel));
        binding.previousMeme.setOnClickListener(new PreviousMemeClickListener(binding));
    }

    private MemePagerAdapter setupViewPager(ViewPager2 memePager, MemeViewModel viewModel) {
        MemePagerAdapter adapter = new MemePagerAdapter(this);
        memePager.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
        float px = convertDpToPx(24);
        memePager.setPageTransformer(new MarginPageTransformer((int) px));
        memePager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                viewModel.setCurrentPage(position);
            }
        });
        memePager.setAdapter(adapter);
        return adapter;
    }

    private int convertDpToPx(int dp) {
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                this.getResources().getDisplayMetrics()
        );
        return (int) px;
    }

    private static class NextMemeClickListener implements View.OnClickListener {
        private final ActivityMainBinding binding;
        private final MemePagerAdapter adapter;
        private final MemeViewModel viewModel;

        public NextMemeClickListener(ActivityMainBinding binding, MemePagerAdapter adapter, MemeViewModel viewModel) {
            this.binding = binding;
            this.adapter = adapter;
            this.viewModel = viewModel;
        }

        @Override
        public void onClick(View v) {
            if (binding.memePager.getCurrentItem() == adapter.getItemCount() - 1) {
                adapter.addPage();
                v.setEnabled(false);
            } else if (binding.memePager.getCurrentItem() == viewModel.getCachedMemes().size() - 1) {
                v.setEnabled(false);
            }
            binding.memePager.setCurrentItem(binding.memePager.getCurrentItem() + 1);
        }
    }

    private static class PreviousMemeClickListener implements View.OnClickListener {
        private final ActivityMainBinding binding;

        public PreviousMemeClickListener(ActivityMainBinding binding) {
            this.binding = binding;
        }

        @Override
        public void onClick(View v) {
            binding.memePager.setCurrentItem(binding.memePager.getCurrentItem() - 1);
            binding.nextMeme.setEnabled(true);
        }
    }
}