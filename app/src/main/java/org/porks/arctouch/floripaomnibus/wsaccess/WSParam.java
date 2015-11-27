package org.porks.arctouch.floripaomnibus.wsaccess;

import org.json.JSONObject;

/**
 * Represents the access parameters to the WS
 */
public class WSParam {
    /**
     * WS Access URL
     */
    private String url = null;

    /**
     * URL to find the Routes by Stop/Street name
     */
    public static final String URL_FIND_ROUTES_BY_STOPNAME = "https://api.appglu.com/v1/queries/findRoutesByStopName/run";

    /**
     * URL to find Stops by Route ID
     */
    public static final String URL_FIND_STOPS_BY_ROUTEID = "https://api.appglu.com/v1/queries/findStopsByRouteId/run";

    /**
     * URL to find Departures by Route ID
     */
    public static final String URL_FIND_DEPARTURES_BY_ROUTEID = "https://api.appglu.com/v1/queries/findDeparturesByRouteId/run";

    /**
     * Data that will be sent to the WS
     */
    private JSONObject body = new JSONObject();

    /**
     * @param url        The WS Access URL
     * @param paramName  Parameter name
     * @param paramValue Parameter Value as <code>int</code>
     * @throws org.json.JSONException
     */
    public WSParam(String url, String paramName, int paramValue) throws org.json.JSONException {
        {
            this.setUrl(url);
            this.setParam(paramName, paramValue);
        }
    }

    /**
     * @param url        The WS Access URL
     * @param paramName  Parameter name
     * @param paramValue Parameter Value as <code>String</code>
     * @throws org.json.JSONException
     */
    public WSParam(String url, String paramName, String paramValue) throws org.json.JSONException {
        {
            this.setUrl(url);
            this.setParam(paramName, paramValue);
        }
    }

    /**
     * Set the WS call param
     *
     * @param name  Parameter name
     * @param value Parameter value as <code>int</code>
     */

    private void setParam(String name, int value) throws org.json.JSONException {
        this.body.put(name, value);
    }

    /**
     * Set the WS call param
     *
     * @param name  Parameter name
     * @param value Parameter value as <code>String</code>
     */
    private void setParam(String name, String value) throws org.json.JSONException {
        this.body.put(name, value);
    }

    /**
     * Reset the parameters values
     */
    public void resetParams() {
        this.body = new JSONObject();
    }

    /**
     * Returns the HTTP BODY to send to the WS
     */
    public byte[] buildBody() throws Exception {
        JSONObject result = new JSONObject();
        result.put("params", this.body);
        return result.toString().getBytes("UTF-8");
    }

    public String getUrl() {
        return this.url;
    }

    private void setUrl(String url) {
        this.url = url;
    }
}
