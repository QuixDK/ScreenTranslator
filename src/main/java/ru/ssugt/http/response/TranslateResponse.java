package ru.ssugt.http.response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class TranslateResponse {

    public String get(HttpPost postRequest) {
        if (postRequest == null) {
            return null;
        }
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpResponse response = httpClient.execute(postRequest);
            if ( response != null && response.getStatusLine().getStatusCode() == 200 ) {
                // Parse and show the translated text
                String responseBody = EntityUtils.toString(response.getEntity());
                Gson gson = new Gson();
                JsonObject responseEntity = gson.fromJson(responseBody, JsonObject.class);
                JsonArray translationsArr = responseEntity.get("translations").getAsJsonArray();
                JsonObject text = translationsArr.get(0).getAsJsonObject();
                return text.get("text").getAsString();

            } else {
                System.err.println("Failed to translate text. HTTP Status Code: " + response.getStatusLine().getStatusCode());
            }
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }
        return null;
    }
}
