package com.holdbetter.tinkofffintechcontest.viewmodel;

import static com.holdbetter.tinkofffintechcontest.MainActivity.DEFAULT_PAGER_SIZE;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.holdbetter.tinkofffintechcontest.model.Meme;
import com.holdbetter.tinkofffintechcontest.model.MemeNotUniqueException;
import com.holdbetter.tinkofffintechcontest.model.NotAGifException;
import com.holdbetter.tinkofffintechcontest.services.ExceptionStore;
import com.holdbetter.tinkofffintechcontest.services.ServiceProvider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import retrofit2.Response;

public class MemeViewModel extends ViewModel {
    private final List<Meme> memes = new ArrayList<>();
    private final MutableLiveData<Integer> currentPage = new MutableLiveData<>(DEFAULT_PAGER_SIZE);

    public List<Meme> getCachedMemes() {
        return memes;
    }

    public LiveData<Integer> getCurrentPage() {
        return currentPage;
    }

    public Maybe<Meme> getMeme(int position, View progressIndicator) {
        return Observable.concat(getCachedMeme(position), getFreshMeme(progressIndicator))
                .firstElement();
    }

    private Observable<Meme> getCachedMeme(int position) {
        return Observable.create((ObservableOnSubscribe<Meme>) emitter -> {
            if (position < memes.size()) {
                emitter.onNext(memes.get(position));
            }
            emitter.onComplete();
        }).doOnNext(meme -> Log.d("GET_MEME", "from cache"));
    }

    private Observable<Meme> getFreshMeme(View progressIndicator) {
        return ServiceProvider.getApi()
                .getMeme()
                .map(this::handleResponse)
                .retryWhen(errors -> errors.flatMap(e -> {
                    if (e instanceof NotAGifException || e instanceof MemeNotUniqueException) {
                        Log.d("REQUEST_ERROR", e.getMessage() + "...retrying");
                        return Flowable.just("retry");
                    }
                    return Flowable.error(e);
                }))
                .flatMapObservable(memeResponse -> Observable.just(memeResponse.body()))
                .doOnNext(this::cache)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> {
                    if (progressIndicator != null) {
                        progressIndicator.setVisibility(View.VISIBLE);
                    }
                })
                .doFinally(() -> {
                    if (progressIndicator != null) {
                        progressIndicator.setVisibility(View.GONE);
                    }
                });
    }

    private void cache(Meme meme) throws CloneNotSupportedException {
        Log.d("GET_MEME", "from network");
        Meme cloneMeme = (Meme) meme.clone();
        cloneMeme.isCache = true;
        getCachedMemes().add(cloneMeme);
    }

    @NonNull
    private Response<Meme> handleResponse(Response<Meme> memeResponse) throws Exception {
        if (memeResponse.isSuccessful()) {
            Meme meme = memeResponse.body();
            if (!meme.type.equals("gif")) {
                throw new NotAGifException(meme);
            } else if (getCachedMemes().stream().anyMatch(m -> m.id == meme.id)) {
                throw new MemeNotUniqueException();
            }
            return memeResponse;
        } else {
            throw ExceptionStore.provideSuitableException(memeResponse);
        }
    }

    public void setCurrentPage(Integer pagePosition) {
        currentPage.postValue(pagePosition);
    }
}
