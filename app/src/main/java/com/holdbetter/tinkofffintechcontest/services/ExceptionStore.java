package com.holdbetter.tinkofffintechcontest.services;

import com.holdbetter.tinkofffintechcontest.model.ClientException;
import com.holdbetter.tinkofffintechcontest.model.HostException;
import com.holdbetter.tinkofffintechcontest.model.UnknownException;

import java.net.HttpURLConnection;

import retrofit2.Response;

public class ExceptionStore {
    private static final int HTTP_SERVICE_UNAVAILABLE = 503;

    public static Exception provideSuitableException(Response<?> response) {
        switch (response.code()) {
            case HttpURLConnection.HTTP_BAD_GATEWAY:
            case HttpURLConnection.HTTP_INTERNAL_ERROR:
            case HTTP_SERVICE_UNAVAILABLE:
                return new HostException();
            case HttpURLConnection.HTTP_BAD_REQUEST:
            case HttpURLConnection.HTTP_NOT_FOUND:
            case HttpURLConnection.HTTP_FORBIDDEN:
                return new ClientException();
            default:
                return new UnknownException();
        }
    }
}
