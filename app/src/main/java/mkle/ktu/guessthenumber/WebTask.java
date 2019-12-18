package mkle.ktu.guessthenumber;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebTask extends AsyncTask<String, Void, String> {

    WebTaskCompleteListener mCompleteListener;

    public void setCompleteListener(WebTaskCompleteListener inListener) {
        mCompleteListener = inListener;
    }

    protected String doInBackground(String... params) {
        String result = "";

        if (params.length > 0) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
                InputStream input = httpConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(input);
                BufferedReader bufferedReader = new BufferedReader(reader);
                result = bufferedReader.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public void onPostExecute(String inString) {
        mCompleteListener.onWebTaskComplete(inString);
    }

    public interface WebTaskCompleteListener {
        public void onWebTaskComplete(String string);
    }
}
