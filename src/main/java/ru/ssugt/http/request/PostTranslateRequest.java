package ru.ssugt.http.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

public class PostTranslateRequest {

    public HttpPost create(String textForTranslate, String sourceLang, String targetLang) {

        String apiKey = "AQVN3BDYAydEHCgIM-NYR4OPN5UvryzXoMC8EXMI";
        String translateEndpoint = "https://translate.api.cloud.yandex.net/translate/v2/translate";

        try {

            HttpPost postRequest = new HttpPost(translateEndpoint);
            JsonArray jsonArray = new JsonArray();
            jsonArray.add(textForTranslate);
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("sourceLanguageCode", sourceLang);
            requestBody.addProperty("targetLanguageCode", targetLang);
            requestBody.add("texts", jsonArray);
            postRequest.setHeader("Content-Type", "application/json");
            postRequest.setEntity(new StringEntity(requestBody.toString(), ContentType.APPLICATION_JSON));
            postRequest.setHeader("Authorization", "Api-Key " + apiKey);
            return postRequest;

        } catch ( Exception e ) {
            e.printStackTrace();
            return null;
        }
    }
}
