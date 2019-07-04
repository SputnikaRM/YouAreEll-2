package views;

import controllers.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class YouAreEll {

    private MessageController msgCtrl;
    private IdController idCtrl;
    private String baseUrl = "http://zipcode.rocks:8085";

    public YouAreEll (MessageController m, IdController j) {
        // used j because i seems awkward
        this.msgCtrl = m;
        this.idCtrl = j;
    }

    public static void main(String[] args) {
        // hmm: is this Dependency Injection?
        YouAreEll urlhandler = new YouAreEll(new MessageController(), new IdController());
        System.out.println(urlhandler.MakeURLCall("/ids", "GET", ""));
        System.out.println(urlhandler.MakeURLCall("/messages", "GET", ""));
    }

    public String get_ids() {
        return MakeURLCall("/ids", "GET", "");
    }

    public String post_ids(String name, String github){

        StringBuilder body = new StringBuilder();

        body.append("{")
                .append("\"userid\": \"-\",")
                .append("\"name\": ").append("\"").append(name).append("\",")
                .append("\"github\": ").append("\"").append(github).append("\"")
            .append("}");


        return MakeURLCall("/ids", "POST", body.toString());
    }

    public String get_messages() {
        return MakeURLCall("/messages", "GET", "");
    }

    public String MakeURLCall(String route, String method, String body) {

        String output;

        try {
            URL url = new URL(baseUrl + route);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);

            if(method == "POST"){

                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                osw.write(body);
                osw.flush();
                osw.close();
                os.close();  //don't forget to close the OutputStream
                conn.connect();

            }

            if(conn.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));

                String line;
                output = "";

                while ((line = br.readLine()) != null) {
                    output += line;
                }
            }

            else {
                output = "Error";
            }
        }

        catch (MalformedURLException e) {
            e.printStackTrace ();
            output = "Error";
        }

        catch (IOException e) {
            e.printStackTrace();
            output = "Error";
        }



        return output;
    }
}
