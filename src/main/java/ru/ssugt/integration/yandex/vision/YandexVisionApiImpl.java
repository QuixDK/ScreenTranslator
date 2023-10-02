package ru.ssugt.integration.yandex.vision;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

public class YandexVisionApiImpl implements YandexVisionApi{
    private final YandexVisionApiConfig apiConfig;

    public YandexVisionApiImpl(YandexVisionApiConfig apiConfig) {
        this.apiConfig = apiConfig;
    }

    public String recognizeText(String mimeType, List<String> languagesCodes, String model, String content) {
        return callYandexVisionApi(mimeType, languagesCodes, model, content);
    }

    private String callYandexVisionApi(String mimeType, List<String> languagesCodes, String model, String content) {
        try {
            HttpPost postRequest = createPostRequest(mimeType, languagesCodes, model, content);

            HttpClient httpClient = HttpClients.createDefault();

            HttpResponse response = httpClient.execute(postRequest);

            if (response == null) {
                return null;
            }

            if (response.getStatusLine().getStatusCode() != 200) {
                System.err.println("Failed to translate text. HTTP Status Code: " + response.getStatusLine().getStatusCode());

                throw new RuntimeException(String.format("Failed to translate text. HTTP Status Code=%d",
                        response.getStatusLine().getStatusCode()));
            }

            // Parse and show the translated text
//            String responseBody = EntityUtils.toString(response.getEntity());
//
//            Gson gson = new Gson();
//            JsonObject responseEntity = gson.fromJson(responseBody, JsonObject.class);
//
//            JsonArray translationsArr = responseEntity.get("translations").getAsJsonArray();
//
//            JsonObject text = translationsArr.get(0).getAsJsonObject();
//            return text.get("text").getAsString();

        } catch ( IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private HttpPost createPostRequest(String mimeType, List<String> languagesCodes, String model, String content) {
        try {
            HttpPost postRequest = new HttpPost(apiConfig.host());

            postRequest.setHeader("Content-Type", "application/json");
            postRequest.setHeader("Authorization", "Bearer " + apiConfig.IAMToken());
            postRequest.setHeader("x-folder-id", apiConfig.folderID());
            postRequest.setHeader("x-data-logging-enabled", "true");

            JsonObject requestBody = createRequestBody(mimeType, languagesCodes, model, content);

            postRequest.setEntity(new StringEntity(requestBody.toString(), ContentType.APPLICATION_JSON));

            return postRequest;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private JsonObject createRequestBody(String mimeType, List<String> languageCodes, String model, String content) {
        JsonObject requestBody = new JsonObject();

        requestBody.addProperty("mimeType", mimeType);
        requestBody.addProperty("model", model);
        requestBody.addProperty("content", content);

        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < languageCodes.size(); i++) {
            jsonArray.add(languageCodes.get(i));
        }

        requestBody.add("languageCodes", jsonArray);

        return requestBody;
    }
}
