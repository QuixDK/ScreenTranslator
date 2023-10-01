package ru.ssugt.integration.yandex.translate;

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

public class YandexTranslateApiImpl implements YandexTranslateApi {
    private final YandexTranslateApiConfig apiConfig;

    public YandexTranslateApiImpl(YandexTranslateApiConfig apiConfig) {
        this.apiConfig = apiConfig;
    }

    public String getTranslatedText(String sourceText, String sourceLang, String targetLang) {
        return callYandexTranslateApi(sourceText, sourceLang, targetLang);
    }

    private String callYandexTranslateApi(String sourceText, String sourceLang, String targetLang) {
        try {
            HttpPost postRequest = createPostRequest(sourceText, sourceLang, targetLang);

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
            String responseBody = EntityUtils.toString(response.getEntity());

            Gson gson = new Gson();
            JsonObject responseEntity = gson.fromJson(responseBody, JsonObject.class);

            JsonArray translationsArr = responseEntity.get("translations").getAsJsonArray();

            JsonObject text = translationsArr.get(0).getAsJsonObject();
            return text.get("text").getAsString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private HttpPost createPostRequest(String sourceText, String sourceLang, String targetLang) {
        try {
            HttpPost postRequest = new HttpPost(apiConfig.getHost());

            postRequest.setHeader("Content-Type", "application/json");
            postRequest.setHeader("Authorization", "Api-Key " + apiConfig.getApiKey());

            JsonObject requestBody = this.createRequestBody(sourceText, sourceLang, targetLang);

            postRequest.setEntity(new StringEntity(requestBody.toString(), ContentType.APPLICATION_JSON));

            return postRequest;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private JsonObject createRequestBody(String sourceText, String sourceLang, String targetLang) {
        JsonObject requestBody = new JsonObject();

        requestBody.addProperty("sourceLanguageCode", sourceLang);
        requestBody.addProperty("targetLanguageCode", targetLang);

        JsonArray jsonArray = new JsonArray();
        jsonArray.add(sourceText);

        requestBody.add("texts", jsonArray);

        return requestBody;
    }

}
