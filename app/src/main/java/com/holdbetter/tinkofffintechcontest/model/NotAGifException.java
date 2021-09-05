package com.holdbetter.tinkofffintechcontest.model;

public class NotAGifException extends Exception {
    private Meme meme;

    public NotAGifException(Meme meme) {
        super(String.format("That's not a gif it's %s", meme.type));
    }

    public Meme getMeme() {
        return meme;
    }
}
