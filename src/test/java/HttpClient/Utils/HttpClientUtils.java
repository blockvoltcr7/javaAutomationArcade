package HttpClient.Utils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class HttpClientUtils {

    private static HttpClient createHttpClient() throws Exception {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }}, new java.security.SecureRandom());

        return HttpClient.newBuilder()
                .sslContext(sslContext)
                .build();
    }

    public static CompletableFuture<HttpResponse<String>> sendGetRequest(String url, Map<String, String> headers) throws Exception {
        HttpClient client = createHttpClient();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(30))
                .GET();

        headers.forEach(requestBuilder::header);

        HttpRequest request = requestBuilder.build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public static CompletableFuture<HttpResponse<String>> sendPostRequest(String url, Map<String, String> headers, String jsonBody) throws Exception {
        HttpClient client = createHttpClient();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(30))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody));

        headers.forEach(requestBuilder::header);

        HttpRequest request = requestBuilder.build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public static CompletableFuture<HttpResponse<String>> sendPutRequest(String url, Map<String, String> headers, String jsonBody) throws Exception {
        HttpClient client = createHttpClient();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(30))
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody));

        headers.forEach(requestBuilder::header);

        HttpRequest request = requestBuilder.build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public static CompletableFuture<HttpResponse<String>> sendDeleteRequest(String url, Map<String, String> headers) throws Exception {
        HttpClient client = createHttpClient();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(30))
                .DELETE();

        headers.forEach(requestBuilder::header);

        HttpRequest request = requestBuilder.build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public static CompletableFuture<HttpResponse<String>> sendPostRequestWithQueryParams(String url, Map<String, String> headers, String jsonBody, Map<String, String> queryParams) throws Exception {
        StringBuilder urlWithParams = new StringBuilder(url);
        if (!queryParams.isEmpty()) {
            urlWithParams.append("?");
            queryParams.forEach((key, value) -> urlWithParams.append(key).append("=").append(value).append("&"));
            urlWithParams.setLength(urlWithParams.length() - 1); // Remove the trailing '&'
        }

        return sendPostRequest(urlWithParams.toString(), headers, jsonBody);
    }
}