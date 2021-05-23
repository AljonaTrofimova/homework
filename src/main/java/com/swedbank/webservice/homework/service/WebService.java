package com.swedbank.webservice.homework.service;

import com.swedbank.webservice.homework.model.Response;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.util.regex.Pattern.MULTILINE;

public class WebService {

    public Response getContentFromUrl(String spec) throws Exception {
        if (spec.isBlank())
            throw new Exception("Parameter spec should be defined");

        HttpURLConnection connection = createHttpConnectionOrNull(spec);

        if (connection == null)
            throw new Exception("Could not create http connection");

        if (connection.getResponseCode() != HTTP_OK)
            throw new Exception("Response was with status " + connection.getResponseCode());

        InputStream responseStream = connection.getInputStream();


        if (responseStream == null)
            throw new Exception("A problem has occurred with input stream");

        String json = extractContent(connection.getInputStream());
        connection.disconnect();
        return parseJsonAndPrepareResponse(json);
    }

    private Response parseJsonAndPrepareResponse(String json) throws Exception {
        if (json.isBlank()) throw new Exception("Cannot parse empty json");

        Response response = new Response();
        final String regex = "\"([^\"]+)\"\\s*:\\s*\"([^\"]+)\",?";
        final String string = json;

        final Pattern pattern = Pattern.compile(regex, MULTILINE);
        final Matcher matcher = pattern.matcher(string);

        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                if (matcher.group(1).equalsIgnoreCase("copyright") && matcher.group(2) != null)
                    response.setCopyright(matcher.group(2));
                if (matcher.group(1).equalsIgnoreCase("date") && matcher.group(2) != null)
                    response.setDate(matcher.group(2));
                if (matcher.group(1).equalsIgnoreCase("explanation") && matcher.group(2) != null)
                    response.setExplanation(matcher.group(2));
                if (matcher.group(1).equalsIgnoreCase("hdurl") && matcher.group(2) != null)
                    response.setHdurl(matcher.group(2));
                if (matcher.group(1).equalsIgnoreCase("media_type") && matcher.group(2) != null)
                    response.setMediaType(matcher.group(2));
                if (matcher.group(1).equalsIgnoreCase("service_version") && matcher.group(2) != null)
                    response.setServiceVersion(matcher.group(2));
                if (matcher.group(1).equalsIgnoreCase("title") && matcher.group(2) != null)
                    response.setTitle(matcher.group(2));
                if (matcher.group(1).equalsIgnoreCase("url") && matcher.group(2) != null)
                    response.setUrl(matcher.group(2));
            }
        }
        return response;
    }

    private String extractContent(InputStream responseStream) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(responseStream));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
        } catch (Exception ex) {
            System.out.println("Could not extract content due to " + ex);
        }
        return sb.toString();
    }

    private HttpURLConnection createHttpConnectionOrNull(String spec) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(spec);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("accept", "application/json");
        } catch (Exception ex) {
            System.out.println("Cannot create connection due to " + ex);
        }
        return connection;
    }
}
