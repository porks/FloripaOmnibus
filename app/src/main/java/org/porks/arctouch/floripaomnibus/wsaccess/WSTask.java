package org.porks.arctouch.floripaomnibus.wsaccess;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Base64;
import org.json.JSONObject;
import java.io.IOException;
import java.net.SocketException;

/**
 * Validate the param to the WS, Call the Web Servce and return the response of the WS
 *
 * @author Marcelo
 */
public class WSTask extends AsyncTask<WSParam, Integer, TaskResult> {
    /**
     * Simple progress dialog
     */
    private ProgressDialog dialog;

    /**
     * CallBack to update the activity
     */
    private TaskCallBack callBack;

    public WSTask(Activity activity, TaskCallBack callBack) {
        this.dialog = new ProgressDialog(activity);
        this.callBack = callBack;
    }

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("Searching...");
        this.dialog.show();
    }

    @Override
    protected TaskResult doInBackground(WSParam... wsParams) {
        TaskResult result;

        // Validate the params
        if ((result = this.validateWSParams(wsParams)) != null)
            return result;

        // Call and consume de WS
        return this.consumeWS(wsParams[0]);
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
    }

    @Override
    protected void onPostExecute(TaskResult result) {
        if (this.dialog.isShowing()) {
            this.dialog.dismiss();
        }

        if (this.callBack != null)
            this.callBack.onComplete(result);
    }

    /**
     * Validate the params to the WS
     */
    private TaskResult validateWSParams(WSParam... wsParams) {
        // Must exists only one para
        if (wsParams.length != 1) {
            TaskResult result = new TaskResult();
            result.setErrMsg("You should use only one WS Parameter");
            return result;
        }

        // TODO more validations?

        // Success
        return null;
    }

    /**
     * Makes the call to the WS
     */
    private TaskResult consumeWS(WSParam wsParam) {
        try {
            // Connection URL
            java.net.URL url = new java.net.URL(wsParam.getUrl());

            // Open the connection
            javax.net.ssl.HttpsURLConnection httpConn = (javax.net.ssl.HttpsURLConnection) url.openConnection();

            // Timeouts
            httpConn.setConnectTimeout(15000);
            httpConn.setReadTimeout(15000);

            // Always use POST
            httpConn.setRequestMethod("POST");
            httpConn.setRequestProperty("Content-Type", "application/json");

            // Allow Input and Output
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);

            // Do not use cache
            httpConn.setUseCaches(false);

            // HTTP Basic Authentication header
            httpConn.setRequestProperty("Authorization", //
                    "Basic " + //
                            Base64.encodeToString("WKD4N7YMA1uiM8V:DtdTtzMLQlA0hk2C1Yi5pLyVIlAQ68".getBytes(), Base64.NO_WRAP) //
            );

            // Custom Header
            httpConn.setRequestProperty("X-AppGlu-Environment", "staging");

            // Make the connection
            httpConn.connect();

            // Write the HTTP BODY
            byte[] outputInBytes = wsParam.buildBody();
            java.io.OutputStream os = httpConn.getOutputStream();
            os.write(outputInBytes);
            os.flush();
            os.close();

            java.io.BufferedReader bfReader;
            try {
                // Get the response
                int responseCode = httpConn.getResponseCode();
                if (responseCode != 200) {
                    // Error 403, 404, etc.
                    TaskResult result = new TaskResult();
                    result.setErrMsg("Sorry. The Web Service did not respond well");
                    return result;
                }

                // Response Stream
                bfReader = new java.io.BufferedReader(new java.io.InputStreamReader(httpConn.getInputStream()));
            } catch (SocketException ex) {
                TaskResult result = new TaskResult();
                result.setErrMsg("Problem unknown at the Web Service.");
                return result;
            } catch (IOException ex) {
                TaskResult result = new TaskResult();
                result.setErrMsg("Problem unknown at the Web Service.");
                return result;
            } catch (Exception ex) {
                TaskResult result = new TaskResult();
                result.setErrMsg("Problem unknown at the Web Service.");
                return result;
            }

            // JSON response
            JSONObject jsonResponse;

            try {
                // Reads the response
                String bfrLine;
                StringBuilder responseJSON = new StringBuilder();
                while ((bfrLine = bfReader.readLine()) != null)
                    responseJSON.append(bfrLine);

                // Ends the connection
                httpConn.disconnect();

                // Creates the JSON
                jsonResponse = new JSONObject(responseJSON.toString());
            } catch (OutOfMemoryError ex) {
                TaskResult result = new TaskResult();
                result.setErrMsg("Problem unexpected.");
                return result;
            }

            // Sucesso
            TaskResult result = new TaskResult();
            result.setJsonObj(jsonResponse);
            return result;
        } catch (Exception ex) {
            TaskResult result = new TaskResult();
            result.setErrMsg("Problem unexpected.");
            return result;
        }
    }
}
