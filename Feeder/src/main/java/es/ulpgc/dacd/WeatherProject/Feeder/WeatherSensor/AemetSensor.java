package es.ulpgc.dacd.WeatherProject.DataLakeFeeder.WeatherSensor;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import es.ulpgc.dacd.WeatherProject.DataLakeFeeder.model.Weather;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.util.List;

public class AemetSensor implements WeatherSensor {
    private final Gson gson;

    public AemetSensor() {
        this.gson = new Gson();
    }

    @Override
    public List<Weather> read(Double minLat, Double maxLat, Double minLon, Double maxLon) throws IOException {
        JsonObject responseJsonObject = gson.fromJson(getAemetInformation(), JsonObject.class);
        String data = responseJsonObject.getAsJsonPrimitive("datos").getAsString();
        String jsonEventsFromAemet = getSensorsData(data);
        JsonArray jsonElements = gson.fromJson(jsonEventsFromAemet, JsonArray.class);
        return jsonElements.asList().stream()
                .filter(this::hasTemperature)
                .filter(o -> minLat < o.getAsJsonObject().get("lat").getAsDouble() && o.getAsJsonObject().get("lat").getAsDouble() < maxLat)
                .filter(o -> minLon < o.getAsJsonObject().get("lon").getAsDouble() && o.getAsJsonObject().get("lon").getAsDouble() < maxLon)
                .map(this::toWeather).toList();
    }

    private boolean hasTemperature(JsonElement jsonElement) {
        return jsonElement.getAsJsonObject().get("ta") != null;
    }

    private Weather toWeather(JsonElement jsonElement) {
        String id = String.valueOf(jsonElement.getAsJsonObject().get("idema"));
        String time = jsonElement.getAsJsonObject().getAsJsonPrimitive("fint").getAsString();
        LocalDateTime times = LocalDateTime.parse(time);
        String location = String.valueOf(jsonElement.getAsJsonObject().get("ubi"));
        Double temperature = Double.valueOf(String.valueOf(jsonElement.getAsJsonObject().get("ta")));
        Double minTemperature = Double.valueOf(String.valueOf(jsonElement.getAsJsonObject().get("tamin")));
        Double maxTemperature = Double.valueOf(String.valueOf(jsonElement.getAsJsonObject().get("tamax")));
        return new Weather(times, location, id, temperature, minTemperature, maxTemperature);
    }

    private String getSensorsData(String data) throws IOException {
        return SSLHelper.getConnection(data)
                .ignoreContentType(true)
                .maxBodySize(0)
                .execute()
                .body();
    }

    private static String getAemetInformation() throws IOException {
        String url = "https://opendata.aemet.es/opendata/api/observacion/convencional/todas";
        String apiKey = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJHYWJyaWVsLlJleWVzOTc1M0BnbWFpbC5jb20iLCJqdGkiOiIxNTdjMjM2OC1hOWM2LTRiZDktYTU1Yi05NTQ4ZGFjMGQ5MjIiLCJpc3MiOiJBRU1FVCIsImlhdCI6MTY3MDk1MTkxNywidXNlcklkIjoiMTU3YzIzNjgtYTljNi00YmQ5LWE1NWItOTU0OGRhYzBkOTIyIiwicm9sZSI6IiJ9.XUeqK71kE53F7Jz4IEW8EztANv7HspApRww6ZS0Ith8";
        return SSLHelper.getConnection(url)
                .ignoreContentType(true)
                .maxBodySize(0)
                .header("accept", "application/json")
                .header("api_key", apiKey)
                .method(Connection.Method.GET)
                .execute()
                .body();
    }

    private static class SSLHelper {
        static public Connection getConnection(String url) {
            return Jsoup.connect(url).sslSocketFactory(SSLHelper.socketFactory());
        }

        static private SSLSocketFactory socketFactory() {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            try {
                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                return sslContext.getSocketFactory();
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                throw new RuntimeException("Failed to create a SSL socket factory", e);
            }
        }
    }
}
