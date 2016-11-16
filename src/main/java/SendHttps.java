import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class SendHttps {

    public static void main(String[] args) throws Exception {

        SendHttps https = new SendHttps();

        System.out.println("Testing 1 - Send Http GET request");
        https.sendGet();

    }

    // HTTP GET request
    private void sendGet() throws Exception {

        String url = "https://pddimp.yandex.ru/api2/admin/dkim/status?domain=slovo-bogoslova.ru&secretkey=yes";

        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("PddToken", "IX632H5N4YDUYYV7DNE6SELXA5LGJHNXFOVQIU2IBWLAJ4QHSW7Q");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

    }



}