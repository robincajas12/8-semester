package org.example;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Optional;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static final ObjectMapper miMapper = new ObjectMapper();
    public static void main(String[] args) throws URISyntaxException, IOException {


        PostServiceImpl miImpl = new PostServiceImpl();
        miImpl.findAll().forEach(System.out::println);

        System.out.println(miImpl.findById(1));
        miImpl.create(Post.builder().title("Hola").userId(5).body("xdd").build());
        miImpl.delete(1);
        miImpl.update(Post.builder().title("Hola").id(1).userId(5).body("xddd").build());
    }
}

/*
*         final String URL = "https://jsonplaceholder.typicode.com/posts/1";
        var connection = (HttpURLConnection)new URI(URL).toURL().openConnection();
        connection.connect();

        System.out.println(connection.getResponseCode());
        System.out.println(HttpURLConnection.HTTP_OK);
        System.out.println(new String(connection.getInputStream().readAllBytes(), Charset.defaultCharset()));
        try(var httpClient =  HttpClients.createDefault())
        {
            HttpGet get = new HttpGet(URL);
            get.addHeader("Accept", "application/json");

            var resp = httpClient.execute(get, res -> {
                return res.getCode() == HttpURLConnection.HTTP_OK ?
                        Optional.of(miMapper.readValue(res.getEntity().getContent() ,Post.class)) : Optional.empty();
            });
            System.out.println(resp);
        }
* */