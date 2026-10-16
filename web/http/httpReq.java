package org.example;

import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.*;
import java.nio.charset.Charset;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        String url = "https://jsonplaceholder.typicode.com/posts/2";
        var myUrl = new URI(url).toURL();
        HttpURLConnection connection = (HttpURLConnection) new URI(url).toURL().openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        connection.connect();

        var httpCode = connection.getResponseCode();

        if(httpCode == HttpURLConnection.HTTP_OK)
        {
            var res = connection.getInputStream();

            ObjectMapper mapper = new ObjectMapper();
            System.out.println(mapper.readValue(connection.getInputStream(), Post.class));
        }

        System.out.println(connection.getHeaderFields());



    }
}
