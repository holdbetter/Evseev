package com.holdbetter.tinkofffintechcontest;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.button.MaterialButton;
import com.holdbetter.tinkofffintechcontest.model.ClientException;
import com.holdbetter.tinkofffintechcontest.model.HostException;
import com.holdbetter.tinkofffintechcontest.model.ImageLoadFailedException;
import com.holdbetter.tinkofffintechcontest.model.Meme;
import com.holdbetter.tinkofffintechcontest.viewmodel.MemeViewModel;

import java.io.IOException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class CardFragment extends Fragment {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private MemeViewModel memeViewModel;
    private int position;

    public CardFragment() {
    }

    public static CardFragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);

        CardFragment cardFragment = new CardFragment();
        cardFragment.setArguments(bundle);

        return cardFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.card_holder, container, false);
        memeViewModel = new ViewModelProvider(requireActivity()).get(MemeViewModel.class);
        position = getArguments().getInt("position");
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        disposables.add(getMeme(position));
    }

    @NonNull
    private Disposable getMeme(int position) {
        // provide progress
        FrameLayout holder = (FrameLayout) getView();
        View cardProgress = null;
        if (holder != null) {
            holder.removeAllViews();
            cardProgress = getLayoutInflater().inflate(R.layout.card_progress, holder, false);
            holder.addView(cardProgress);
        }

        return memeViewModel.getMeme(position, cardProgress)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateUIOnMemeReceived, this::handleError);
    }

    private void handleError(Throwable error) {
        FrameLayout holder = (FrameLayout) getView();
        if (holder != null) {
            holder.removeAllViews();

            View cardError = getLayoutInflater().inflate(R.layout.card_error, holder, false);
            ImageView errorImage = cardError.findViewById(R.id.error_image);
            TextView errorText = cardError.findViewById(R.id.error_text);
            MaterialButton again = cardError.findViewById(R.id.again_button);
            again.setOnClickListener(v -> disposables.add(getMeme(position)));

            ImageView imageSecure = getActivity().findViewById(R.id.secure_image);
            imageSecure.setEnabled(false);

            if (error instanceof IOException || error instanceof ImageLoadFailedException) {
                setupErrorViews(errorImage, errorText, again, R.drawable.internet_problem, R.string.no_internet_desc, R.string.no_internet_btn);
            } else if (error instanceof HostException) {
                setupErrorViews(errorImage, errorText, again, R.drawable.host_error, R.string.host_error_desc, R.string.error_refresh_btn);
            } else if (error instanceof ClientException) {
                setupErrorViews(errorImage, errorText, again, R.drawable.my_bad, R.string.client_error_desc, R.string.error_refresh_btn);
            } else {
                setupErrorViews(errorImage, errorText, again, R.drawable.my_bad, R.string.unknown_error_desc, R.string.error_refresh_btn);
            }

            holder.addView(cardError);
        }
    }

    private void setupErrorViews(ImageView errorImage, TextView errorText, MaterialButton again, @DrawableRes int errorDrawableId, @StringRes int errorDescriptionId, @StringRes int errorButtonText) {
        Glide.with(this)
                .load(errorDrawableId)
                .apply(new RequestOptions().centerCrop())
                .into(errorImage);

        errorText.setText(errorDescriptionId);
        again.setText(errorButtonText);
    }

    private void updateUIOnMemeReceived(Meme meme) {
        nextMemeButtonEnable();

        FrameLayout holder = (FrameLayout) getView();
        if (holder != null) {
            View cardContent = holder.findViewById(R.id.card_content);
            if (cardContent == null) {
                holder.removeAllViews();

                if (!meme.isCache) {
                    updateMainUIOnUserOnline();
                }

                cardContent = getLayoutInflater().inflate(R.layout.card_content, holder, false);
                cardContent.setClipToOutline(true);
                holder.addView(cardContent);
            }

            TextView memeDescription = cardContent.findViewById(R.id.meme_description);
            memeDescription.setMovementMethod(new ScrollingMovementMethod());
            ImageView memeImage = cardContent.findViewById(R.id.meme_image);

            memeDescription.setText(meme.description);

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL);

            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(getContext());
            circularProgressDrawable.setStrokeWidth(MainActivity.convertDpToPx(4, getContext()));
            circularProgressDrawable.setCenterRadius(MainActivity.convertDpToPx(20, getContext()));
            circularProgressDrawable.start();

            Glide.with(this)
                    .asGif()
                    .apply(options)
                    .load(meme.url)
                    .placeholder(circularProgressDrawable)
                    .listener(new OnLoadFailedListener())
                    .into(memeImage);
        }
    }

    private void nextMemeButtonEnable() {
        MaterialButton nextMeme = getActivity().findViewById(R.id.next_meme);
        nextMeme.setEnabled(true);
    }

    private void updateMainUIOnUserOnline() {
        ImageView imageSecure = getActivity().findViewById(R.id.secure_image);
        imageSecure.setEnabled(true);
    }

    private class OnLoadFailedListener implements RequestListener<GifDrawable> {
        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
            handleError(new ImageLoadFailedException());
            return false;
        }

        @Override
        public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
            return false;
        }
    }
}
