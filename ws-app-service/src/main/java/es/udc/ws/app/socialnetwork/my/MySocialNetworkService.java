package es.udc.ws.app.socialnetwork.my;


import es.udc.ws.app.model.show.Show;
import es.udc.ws.app.socialnetwork.SocialNetworkService;
import es.udc.ws.app.socialnetwork.SocialNetworkShowPost;
import es.udc.ws.app.socialnetwork.exceptions.AuthenticationException;
import es.udc.ws.app.socialnetwork.exceptions.DuplicatedPostException;
import es.udc.ws.app.socialnetwork.exceptions.PostNotFountException;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.*;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;


public class MySocialNetworkService implements SocialNetworkService {
    private final static String ENDPOINT_ADDRESS_PARAMETER = "SocialNetworkService.endpointAddress";

    private String endpointAddress = null;
    private String token = null;
    private final Object tokenMutex = new Object();


    private synchronized String getEndpointAddress()
    {
        if (endpointAddress == null)
            endpointAddress = ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);
        return endpointAddress;
    }

    private String getToken()
            throws AuthenticationException
    {
        synchronized (tokenMutex) {
            if (token == null)
                token = login(ConfigurationParametersManager.getParameter("MySocialNetworkService.username"),
                        ConfigurationParametersManager.getParameter("MySocialNetworkService.password"));
        }
        return token;
    }

    private void resetToken()
    {
        synchronized (tokenMutex) {
            token = null;
        }
    }

    private String responseContentToString(HttpResponse response) {
        String charsetName;
        Charset charset = ContentType.getOrDefault(response.getEntity()).getCharset();
        if (charset != null)
            charsetName = charset.name();
        else
            charsetName = Charset.defaultCharset().name();

        try {
            BufferedInputStream bis = new BufferedInputStream(response.getEntity().getContent());
            ByteArrayOutputStream buff = new ByteArrayOutputStream();
            int result = bis.read();
            while(result != -1) {
                buff.write((byte) result);
                result = bis.read();
            }

            String content = buff.toString(charsetName);
            System.out.println("Response code: " + response.getStatusLine().getStatusCode() + ": " + content);
            return content;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getShowTextFormat() {
        /*return  "New Show available!!\n\n" +

                "identifier: {0, number, integer}\n" +
                "name: {1}\n" +
                "description: {2}\n" +
                "starts: {3, date, full}\n" +
                "duration: {4, number, integer} minutes\n" +
                "reservations limit date: {5, date, full}\n" +
                "total tickets: {6, number, integer}\n" +
                "resting tickets: {7, number, integer}\n" +
                "price: {8, number}\n" +
                "discounted price: {9, number}\n\n";
        */
        return  "{0, number, integer}\n" +
                "{1}\n" +
                "{2}\n" +
                "{3, date, full}\n" +
                "{4, number, integer} minutes\n" +
                "{5, date, full}\n" +
                "{6, number, integer}\n" +
                "{7, number, integer}\n" +
                "{8}\n" +
                "{9}";
    }

    private String showToText(Show show) {
        return MessageFormat.format(
                getShowTextFormat(),
                show.getId(),
                show.getName(),
                show.getDescription(),
                show.getStartDate().getTime(),
                show.getDuration(),
                show.getLimitDate().getTime(),
                show.getMaxTickets(),
                show.getTickets(),
                show.getPrice(),
                show.getDiscountedPrice());
    }

    private SocialNetworkShowPost jsonToShowPost(JSONObject json) {

        MessageFormat format = new MessageFormat(getShowTextFormat());
        Object[] objects;
        try {
            objects = format.parse(json.getString("text"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Calendar start = GregorianCalendar.getInstance();
        Calendar limit = GregorianCalendar.getInstance();

        start.setTime((Date)objects[3]);
        limit.setTime((Date)objects[5]);

        SocialNetworkShowPost show = new SocialNetworkShowPost();
        show.setPostId(json.getLong("postId"));
        show.setShowId((Long) objects[0]);
        show.setShowName((String) objects[1]);
        show.setShowDescription((String) objects[2]);
        show.setShowStartDate(start);
        show.setShowDuration((Long) objects[4]);
        show.setShowLimitDate(limit);
        show.setShowMaxTickets((Long) objects[6]);
        show.setShowTickets((Long) objects[7]);
        show.setShowPrice(Float.parseFloat((String)objects[8]));
        show.setShowDiscountedPrice(Float.parseFloat((String)objects[9]));

        show.setLikes(json.getJSONArray("likes").size());

        return show;
    }

    private JSONObject findPostByShowId(Show show)
            throws PostNotFountException
    {
        HttpResponse response;
        try
        {
            response = Request.Get(getEndpointAddress() + "posts")
                    .execute()
                    .returnResponse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JSONArray jsonArray = JSONArray.fromObject(responseContentToString(response));

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            try {
                SocialNetworkShowPost showPost = jsonToShowPost(json);
                if (showPost.getShowId().equals(show.getId()))
                    return json;
            } catch (RuntimeException e) { e.printStackTrace(); }
        }

        throw new PostNotFountException("Post for show: " + show.getId() + " not found");
    }

    private String login(String username, String password)
            throws AuthenticationException
    {
        System.out.println("Logging in social network (" + username + ", " + password + ")");

        try
        {
            Map<String, Object> map = new HashMap<>();
            map.put("username", username);
            map.put("password", password);

            HttpResponse response = Request.Post(getEndpointAddress() + "login")
                    .bodyString(JSONObject.fromObject(map).toString(), ContentType.APPLICATION_JSON)
                    .execute()
                    .returnResponse();

            int statusCode = response.getStatusLine().getStatusCode();
            JSONObject json = JSONObject.fromObject(responseContentToString(response));

            if (statusCode == 200) {
                return json.getString("value");
            }
            else
                throw new AuthenticationException(
                        new MySocialNetworkError(json).getMessage() );

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }


    private SocialNetworkShowPost _publishShow(Show show)
            throws InputValidationException, AuthenticationException, DuplicatedPostException
    {
        String token = getToken();

        try
        {
            Map<String, Object> map = new HashMap<>();
            map.put("text", showToText(show));

            String body = JSONObject.fromObject(map).toString();
            System.out.println("Sending: " + body);

            HttpResponse response = Request.Post(getEndpointAddress() + "posts")
                    .addHeader("Authorization", "Bearer " + token)
                    .bodyString(body, ContentType.APPLICATION_JSON)
                    .execute()
                    .returnResponse();

            int statusCode = response.getStatusLine().getStatusCode();
            JSONObject json = JSONObject.fromObject(responseContentToString(response));

            if (statusCode == 201)
                return jsonToShowPost(json);

            MySocialNetworkError error = new MySocialNetworkError(json);

            switch (statusCode) {
                case 400:
                    throw new InputValidationException(error.getMessage());
                case 401:
                    throw new AuthenticationException(error.getMessage());
                case 404:
                    throw new DuplicatedPostException(error.getMessage());
                default:
                    throw new RuntimeException("HTTP error: " + statusCode + "\n" + error.getMessage());
            }

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private SocialNetworkShowPost _updateShow(Show show)
            throws PostNotFountException, AuthenticationException
    {
        String token = getToken();
        JSONObject json = findPostByShowId(show);
        long postId = json.getLong("postId");

        try
        {
            Map<String, Object> map = new HashMap<>();
            map.put("text", showToText(show));

            String body = JSONObject.fromObject(map).toString();
            System.out.println("Sending: " + body);

            HttpResponse response = Request.Put(getEndpointAddress() + "posts/" + postId)
                    .addHeader("Authorization", "Bearer " + token)
                    .bodyString(body, ContentType.APPLICATION_JSON)
                    .execute()
                    .returnResponse();

            int statusCode = response.getStatusLine().getStatusCode();
            json = JSONObject.fromObject(responseContentToString(response));

            if (statusCode == 200) return jsonToShowPost(json);

            MySocialNetworkError error = new MySocialNetworkError(json);

            switch (statusCode) {
                case 401:
                    throw new AuthenticationException(error.getMessage());
                case 404:
                    throw new PostNotFountException(error.getMessage());
                default:
                    throw new RuntimeException("HTTP error: " + statusCode + "\n" + error.getMessage());
            }
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private SocialNetworkShowPost _getShow(Show show)
            throws PostNotFountException, AuthenticationException
    {
        String token = getToken(); // Do nothing

        JSONObject json = findPostByShowId(show);
        return jsonToShowPost(json);
    }

    private void _commentShow(Show show, String comment)
            throws InputValidationException, AuthenticationException, PostNotFountException
    {
        String token = getToken(); // Do nothing

        JSONObject json = findPostByShowId(show);
        long postId = json.getLong("postId");

        try
        {
            Map<String, Object> map = new HashMap<>();
            map.put("postId", postId);
            map.put("text", comment);

            String body = JSONObject.fromObject(map).toString();
            System.out.println("Sending: " + body);

            HttpResponse response = Request.Post(getEndpointAddress() + "comments")
                    .addHeader("Authorization", "Bearer " + token)
                    .bodyString(body, ContentType.APPLICATION_JSON)
                    .execute()
                    .returnResponse();

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 201) return;

            json = JSONObject.fromObject(responseContentToString(response));

            MySocialNetworkError error = new MySocialNetworkError(json);

            switch (statusCode) {
                case 400:
                    throw new InputValidationException(error.getMessage());
                case 401:
                    throw new AuthenticationException(error.getMessage());
                case 404:
                    throw new PostNotFountException(error.getMessage());
                default:
                    throw new RuntimeException("HTTP error: " + statusCode + "\n" + error.getMessage());
            }
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public SocialNetworkShowPost publishShow(Show show)
            throws InputValidationException, AuthenticationException, DuplicatedPostException
    {
        try {
            return _publishShow(show);
        } catch (AuthenticationException e) {
            resetToken(); // Retry, token can be expired
            return _publishShow(show);
        }
    }

    @Override
    public SocialNetworkShowPost updateShow(Show show)
            throws PostNotFountException, AuthenticationException
    {
        try {
            return _updateShow(show);
        } catch (AuthenticationException e) {
            resetToken(); // Retry, token can be expired
            return _updateShow(show);
        }
    }

    @Override
    public SocialNetworkShowPost getShow(Show show)
            throws PostNotFountException, AuthenticationException
    {
        try {
            return _getShow(show);
        } catch (AuthenticationException e) {
            resetToken(); // Retry, token can be expired
            return _getShow(show);
        }
    }

    @Override
    public void commentShow(Show show, String comment)
            throws InputValidationException, AuthenticationException, PostNotFountException
    {
        try {
            _commentShow(show, comment);
        } catch (AuthenticationException e) {
            resetToken(); // Retry, token can be expired
            _commentShow(show, comment);
        }
    }
}