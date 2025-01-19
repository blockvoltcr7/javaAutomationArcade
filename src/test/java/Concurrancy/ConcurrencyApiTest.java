package Concurrancy;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.http.HttpClient;
public class ConcurrencyApiTest {


    public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException {

        ExecutorService executorService = Executors.newFixedThreadPool(1);

        //initialize a list to hold completable future objects
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < 10; i++) {

            //we will use http client

            //create a custom sslcontext that trusts all certificates
            SSLContext sslContext = SSLContext.getInstance("TLS");

            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new java.security.SecureRandom());


            // create an HTTPClient instance with custom sslcontext
            HttpClient client = HttpClient.newBuilder()
                    .sslContext(sslContext)
                    .build();



            //create a completable future object
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                System.out.println("Running task " + Thread.currentThread().getName());
            }, executorService);

            //add the completable future object to the list
            futures.add(future);
        }



    }
}
