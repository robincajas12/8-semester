package org.example;

import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.BasicHttpEntity;
import org.apache.hc.core5.http.io.entity.HttpEntityWrapper;
import org.apache.hc.core5.http.io.entity.StringEntity;
import tools.jackson.core.JsonEncoding;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Optional;

public class PostServiceImpl implements PostService {
    final ObjectMapper miMapper = new ObjectMapper();
    final String URL = "https://jsonplaceholder.typicode.com/posts/";

    @Override
    public Optional<Post> findById(int id) {
        String getUrl = URL+ id;
        System.out.println(getUrl);
        try (var httpClient = HttpClients.createDefault()) {
            HttpGet get = new HttpGet(getUrl);
            var res = httpClient.execute(get, classicHttpResponse -> classicHttpResponse.getCode() == HttpURLConnection.HTTP_OK ?
                    Optional.of(miMapper.readValue(classicHttpResponse.getEntity().getContent(), Post.class)) : Optional.empty());
            return (Optional<Post>) res;
        } catch (Exception e) {

        }
        return Optional.empty();
    }

    @Override
    public List<Post> findAll() {
        try (var httpClient = HttpClients.createDefault()) {
            HttpGet get = new HttpGet(URL);
            var res = httpClient.execute(get, classicHttpResponse -> classicHttpResponse.getCode() == HttpURLConnection.HTTP_OK ?
                    Optional.of(miMapper.readValue(classicHttpResponse.getEntity().getContent(), new TypeReference<List<Post>>() {
                    })) : Optional.empty());
            return res.isPresent() ? (List<Post>) res.get() : List.of();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public void create(Post obj) {
        try (var httpClient = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(URL);
            post.setEntity(new StringEntity(miMapper.writeValueAsString(obj)));
            httpClient.execute(post, classicHttpResponse -> {
                System.out.println(classicHttpResponse.getCode());
                return null;
            });
        } catch (Exception e) {

        }
    }

    @Override
    public void update(Post post) {
        String putURL =  URL+ post.getId();

        try (var httpClient = HttpClients.createDefault()) {
            HttpPut put = new HttpPut(putURL);
            put.setEntity(new StringEntity(miMapper.writeValueAsString(put)));
            httpClient.execute(put, classicHttpResponse -> {
                System.out.println(classicHttpResponse.getCode());
                return null;
            });
        } catch (Exception e) {

        }
    }

    @Override
    public void delete(int id) {
        String deleteURL =  URL+ id;

        try (var httpClient = HttpClients.createDefault()) {
            HttpDelete miDelete = new HttpDelete(deleteURL);
            httpClient.execute(miDelete, classicHttpResponse -> {
                System.out.println(classicHttpResponse.getCode());
                return null;
            });
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


}



