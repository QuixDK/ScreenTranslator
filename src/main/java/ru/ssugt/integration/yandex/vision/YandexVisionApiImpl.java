package ru.ssugt.integration.yandex.vision;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class YandexVisionApiImpl implements YandexVisionApi {
    private final YandexVisionApiConfig apiConfig;

    public YandexVisionApiImpl(YandexVisionApiConfig apiConfig) {
        this.apiConfig = apiConfig;
    }

    public StringBuilder recognizeText(String mimeType, List<String> languagesCodes, String model, byte[] content) {
        return callYandexVisionApi(mimeType, languagesCodes, model, content);
    }

    private StringBuilder callYandexVisionApi(String mimeType, List<String> languagesCodes, String model, byte[] content) {
        try {
            HttpPost postRequest = createPostRequest(mimeType, languagesCodes, model, content);

            HttpClient httpClient = HttpClients.createDefault();

            HttpResponse response = httpClient.execute(postRequest);

            if ( response == null ) {
                return null;
            }

            if ( response.getStatusLine().getStatusCode() != 503 && response.getStatusLine().getStatusCode() != 200 ) {
                System.err.println("Failed to translate text. HTTP Status Code: " + response.getStatusLine().getStatusCode());
                System.out.println("\n" + response.getStatusLine().getReasonPhrase());
                throw new RuntimeException(String.format("Failed to translate text. HTTP Status Code=%d",
                        response.getStatusLine().getStatusCode()));
            }

            if ( response.getStatusLine().getStatusCode() == 200 ) {
                String responseBody = EntityUtils.toString(response.getEntity());

                Gson gson = new Gson();
                JsonObject responseEntity = gson.fromJson(responseBody, JsonObject.class);

                JsonObject result = responseEntity.get("result").getAsJsonObject();
                JsonObject textAnnotation = result.get("text_annotation").getAsJsonObject();
                JsonArray blocks = textAnnotation.get("blocks").getAsJsonArray();
                JsonArray lines;
                JsonArray alternatives;
                JsonElement alternativesElement;
                JsonObject blockElement;
                StringBuilder stringBuilder = new StringBuilder();
                for ( int i = 0; i < blocks.size(); i++ ) {
                    blockElement = blocks.get(i).getAsJsonObject();
                    lines = blockElement.get("lines").getAsJsonArray();
                    for ( int j = 0; j < lines.size(); j++ ) {
                        JsonObject linesElement;
                        linesElement = lines.get(j).getAsJsonObject();
                        alternatives = linesElement.get("alternatives").getAsJsonArray();
                        for ( int k = 0; k < alternatives.size(); k++ ) {
                            alternativesElement = alternatives.get(k).getAsJsonObject();
                            String string = alternativesElement.getAsJsonObject().get("text").getAsString();
                            stringBuilder.append(string);
                            stringBuilder.append("\n");
                        }
                    }
                }
                return stringBuilder;

            }
            //Unmarshall
            System.out.println(response.getStatusLine().getStatusCode());

        } catch ( IOException e ) {
            e.printStackTrace();
        }

        return null;
    }

    private HttpPost createPostRequest(String mimeType, List<String> languagesCodes, String model, byte[] content) {
        try {
            HttpPost postRequest = new HttpPost(apiConfig.host());

            postRequest.setHeader("Content-Type", "application/json");
            postRequest.setHeader("Authorization", "Bearer " + apiConfig.IAMToken());
            postRequest.setHeader("x-folder-id", apiConfig.folderID());
            postRequest.setHeader("x-data-logging-enabled", "true");

            JsonObject requestBody = createRequestBody(mimeType, languagesCodes, model, content);

            postRequest.setEntity(new StringEntity(requestBody.toString(), ContentType.APPLICATION_JSON));

            return postRequest;

        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return null;
    }

    private JsonObject createRequestBody(String mimeType, List<String> languageCodes, String model, byte[] content) {
        JsonObject requestBody = new JsonObject();
        String s = new String(content, StandardCharsets.UTF_8);
        requestBody.addProperty("mimeType", mimeType);
        requestBody.addProperty("model", model);
        requestBody.addProperty("content", s);

        JsonArray jsonArray = new JsonArray();
        for ( int i = 0; i < languageCodes.size(); i++ ) {
            jsonArray.add(languageCodes.get(i));
        }

        requestBody.add("languageCodes", jsonArray);

        return requestBody;
    }
}
