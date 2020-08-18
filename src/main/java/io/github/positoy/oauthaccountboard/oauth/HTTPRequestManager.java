package io.github.positoy.oauthaccountboard.oauth;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static io.github.positoy.oauthaccountboard.oauth.HTTPRequestManager.METHOD.GET;


public class HTTPRequestManager {

    final static Logger logger = LoggerFactory.getLogger(HTTPRequestManager.class);

    public static enum METHOD {
        POST, GET, PUT, DELETE
    };

    String apiURL = "";
    METHOD method = GET;
    Map<String, String> queryParams = new HashMap<>();
    Map<String, String> requestHeaders = new HashMap<>();
    String requestBody = "";

    public HTTPRequestManager() {
    }

    public HTTPRequestManager(String apiURL, METHOD method) {
        this.apiURL = apiURL;
        this.method = method;
    }

    public void setApiURL(String apiURL) {
        this.apiURL = apiURL;
    }

    public void setMethod(METHOD method) {
        this.method = method;
    }

    public void setQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    public void setRequestHeaders(Map<String, String> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public void setQueryParam(String key, String value) {
        this.queryParams.put(key, value);
    }

    public void setRequestHeader(String key, String value) {
        this.requestHeaders.put(key, value);
    }

    public JSONObject getJsonResponse() throws Exception {
        String response = response = getResponse();
        JSONObject json = (JSONObject) new JSONParser().parse(response);

        if (response == null || json == null) {
            throw new Exception("failed to getReseponse or parseResponse");
        }

        return json;
    }

    public String getResponse() throws Exception {
        if (apiURL.isEmpty())
            throw new Exception("apiURL is empty");

        // url
        if (queryParams.size() != 0) {
            apiURL += "?";
            int remainQueries = queryParams.size();
            for(Map.Entry<String, String> e :queryParams.entrySet()) {
                apiURL += (e.getKey() + "=" + e.getValue());
                if (--remainQueries != 0) {
                    apiURL += "&";
                }
            }
        }

        logger.info("request --> " + apiURL);
        HttpURLConnection con = connect(apiURL);

        try {
            // method
            switch (method) {
                case POST: con.setRequestMethod("POST"); break;
                case GET: con.setRequestMethod("GET"); break;
                case PUT: con.setRequestMethod("PUT"); break;
                case DELETE: con.setRequestMethod("DELETE"); break;
            }

            // headers
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            // body
            if (!requestBody.isEmpty()) {
                // TODO consider using JSONObject for body if it's really in use
                // conn.setRequestProperty("Content-Type","application/json");
                OutputStream os = con.getOutputStream();
                os.write(requestBody.getBytes());
                os.close();
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API requset&resposne failed", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiURL){
        try {
            URL url = new URL(apiURL);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("apiURL wrong : " + apiURL, e);
        } catch (IOException e) {
            throw new RuntimeException("connection failed : " + apiURL, e);
        }
    }

    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("failed to read response ", e);
        }
    }
}
