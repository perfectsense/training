package bex.training.util;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public final class NetworkUtils {

    private NetworkUtils() {
    }

    public static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
            .cache(null)
            .build();

    public static <T> T get(String url, ResponseBodyCallback<T> callback) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        return request(request, callback);
    }

    public static <T> T request(Request request, ResponseBodyCallback<T> callback) throws IOException {
        return request(request, callback, resp -> {
            throw new IOException("Received status code " + resp.code() + " from " + request.url());
        });
    }

    public static <T> T request(Request request, ResponseBodyCallback<T> callback, ErrorCallback<T> errorCallback) throws IOException {
        try (Response resp = NetworkUtils.HTTP_CLIENT.newCall(request).execute()) {
            if (!resp.isSuccessful()) {
                return errorCallback.apply(resp);
            }

            return callback.apply(resp.body());
        }
    }

    @FunctionalInterface
    public interface ResponseBodyCallback<T> {
        T apply(ResponseBody body) throws IOException;
    }

    @FunctionalInterface
    public interface ErrorCallback<T> {
        T apply(Response response) throws IOException;
    }
}
