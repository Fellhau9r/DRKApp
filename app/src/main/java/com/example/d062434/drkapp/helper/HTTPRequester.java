package com.example.d062434.drkapp.helper;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.net.CookieManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpCookie;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by D062434 on 28.10.2015.
 */
public class HTTPRequester extends AsyncTask<String, Void, String>{
    static final String COOKIES_HEADER = "Set-Cookie";
    static CookieManager cookieManager = new CookieManager();

    private String response;

    private String[][] post;
    private String link;

    public HTTPRequester(String[][] postParam, String link){
        this.post = postParam;
        this.link = link;
    }

    public String getResponse(){
        return this.response;
    }

    @Override
    protected String doInBackground(String... params) {
        try{


            String data = "";
            if(this.post != null){
                for(int i = 0; i < this.post.length; i++){
                    if(i > 0){
                        data += "&";
                    }
                    data += URLEncoder.encode(this.post[i][0], "UTF-8") + "=" + URLEncoder.encode(this.post[i][1], "UTF-8");
                }
            }

            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            if(cookieManager.getCookieStore().getCookies().size() > 0)
            {
                //While joining the Cookies, use ',' or ';' as needed. Most of the server are using ';'
                conn.setRequestProperty("Cookie",
                        TextUtils.join(";",  cookieManager.getCookieStore().getCookies()));
            }

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("UTF-8")));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                sb.append(line);
                break;
            }
            this.response = sb.toString();

            Map<String, List<String>> headerFields = conn.getHeaderFields();
            List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);
            if(cookiesHeader != null){
                for(String cookie : cookiesHeader){
                    cookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                }
            }

            return sb.toString();
        }
        catch(Exception e){
            //Fill with empty Content if no Internet Connection is aviable
            return new String("");
        }
    }

    @Override
    protected void onPostExecute(String result){
        this.response = result;
    }
}
