package net.bytecore.aiprofanityfilter.utils;

import com.google.gson.JsonObject;
import net.bytecore.aiprofanityfilter.config.Config;
import net.bytecore.aiprofanityfilter.managers.ProfanityManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class ProfanityAIChecker {

    public static CompletableFuture<Boolean> checkProfanityAsync(String message) {
        return CompletableFuture.supplyAsync(() -> checkProfanity(message));
    }

    public static Boolean checkProfanity(String message) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL("https://vector.profanity.dev");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            String jsonInputString = "{\"message\":\"" + message + "\"}";
            System.out.println("Sending JSON: " + jsonInputString);  // Debugging line

            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonInputString.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);  // Debugging line

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println("Response Body: " + response.toString());  // Debugging line
                    return parseProfanityResponse(response.toString(), message);
                }
            } else {
                System.err.println("[AIProfanityFilter] HTTP request failed with response code: " + responseCode);
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.err.println("[AIProfanityFilter] Response message: " + response.toString());
                }
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static Boolean parseProfanityResponse(String responseBody, String message) {
        boolean isProfanity = false;
        double score = 0;

        try {
            String isProfanityString = extractJsonValue(responseBody, "isProfanity");
            String scoreString = extractJsonValue(responseBody, "score");

            isProfanity = Boolean.parseBoolean(isProfanityString);
            score = Double.parseDouble(scoreString);

            // If isProfanity is true and score > 0.82, add to profanity list
            if (isProfanity && score > 0.82) {
                ProfanityManager.getInstance().addWord(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isProfanity;
    }

    public static String extractJsonValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int startIndex = json.indexOf(searchKey) + searchKey.length();
        if (startIndex == searchKey.length() - 1) {
            return "";
        }
        int endIndex = json.indexOf(",", startIndex);
        if (endIndex == -1) {
            endIndex = json.indexOf("}", startIndex);
        }
        return json.substring(startIndex, endIndex).replace("\"", "").trim();
    }

}
