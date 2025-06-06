package it.nathanub.rewardads.Velocity.Tools.Api;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Api {
    private static final String apiPath = "https://global.rewardads.it/";

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static Future<String> handle(String table) {
        return executor.submit(() -> {
            URL url = new URL(table.startsWith("http") ? table : ("https://global.rewardads.it/" + table));
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null)
                return null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                buffer.append(line).append("\n");
            return (buffer.length() == 0) ? null : buffer.toString();
        });
    }
}
