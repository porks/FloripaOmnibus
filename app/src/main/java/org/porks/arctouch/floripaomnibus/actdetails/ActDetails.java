package org.porks.arctouch.floripaomnibus.actdetails;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.porks.arctouch.floripaomnibus.R;
import org.porks.arctouch.floripaomnibus.models.Route;
import org.porks.arctouch.floripaomnibus.models.RouteStop;
import org.porks.arctouch.floripaomnibus.wsaccess.TaskCallBack;
import org.porks.arctouch.floripaomnibus.wsaccess.TaskResult;
import org.porks.arctouch.floripaomnibus.wsaccess.WSParam;
import org.porks.arctouch.floripaomnibus.wsaccess.WSTask;

public class ActDetails extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    TabRoute tabRoute = null;

    TabDeparture tabDeparture = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_actdetails);

        // Create the ToolBar (Back Button and route description on Activity Top)
        this.toolbar = (Toolbar) this.findViewById(R.id.ActDetails_Toolbar);
        this.setSupportActionBar(this.toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Create the View Pages for the TabLayout
        this.viewPager = (ViewPager) this.findViewById(R.id.ActDetails_ViewPager);
        this.defineViewPages(this.viewPager);

        // TabLayout and set the ViewPager
        this.tabLayout = (TabLayout) this.findViewById(R.id.ActDetails_Tabs);
        this.tabLayout.setupWithViewPager(this.viewPager);

        // Set route name
        Intent actDetailsIntent = this.getIntent();
        String routeName = actDetailsIntent.getStringExtra("routeName");
        if (routeName != null)
            this.getSupportActionBar().setTitle(routeName);

        // Get the route ID
        if (actDetailsIntent.hasExtra("routeId")) {
            // Has Route ID, Call the WS
            int routeId = actDetailsIntent.getIntExtra("routeId", 0);
            this.getRouteInfo(routeId);

            // Remove the Route ID to not get de RouteInfo again if the Activity be recreated
            actDetailsIntent.removeExtra("routeId");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /**
     * Get the route info (Stops, Departure) at the WS
     */
    private void getRouteInfo(int routeId) {
        try {
        // Execute the ASyncTask to get the Route's Stops names from the WS
        WSParam wsParam = new WSParam(WSParam.URL_FIND_STOPS_BY_ROUTEID, "routeId", routeId);
        WSTask wsTask = new WSTask(ActDetails.this, ActDetails.this.stopsCallBack);
        wsTask.execute(wsParam);
//            TaskResult result = new TaskResult();
//            result.setJsonObj(new JSONObject("{\"rows\":[{\"id\":26,\"name\":\"TICEN\",\"sequence\":1,\"route_id\":32},{\"id\":27,\"name\":\"RUA PROCURADOR ABELARDO GOMES\",\"sequence\":2,\"route_id\":32},{\"id\":28,\"name\":\"AVENIDA PAULO FONTES\",\"sequence\":3,\"route_id\":32},{\"id\":29,\"name\":\"RUA DOUTOR ÁLVARO MILLEN DA SILVEIRA\",\"sequence\":4,\"route_id\":32},{\"id\":30,\"name\":\"RUA DOUTOR JORGE DA LUZ FONTES\",\"sequence\":5,\"route_id\":32},{\"id\":31,\"name\":\"RUA SILVA JARDIM\",\"sequence\":6,\"route_id\":32},{\"id\":32,\"name\":\"AVENIDA MAURO RAMOS\",\"sequence\":7,\"route_id\":32},{\"id\":33,\"name\":\"AVENIDA JORNALISTA RUBENS DE ARRUDA RAMOS\",\"sequence\":8,\"route_id\":32},{\"id\":34,\"name\":\"RUA COMANDANTE CONSTANTINO NICOLAU SPYRIDES\",\"sequence\":9,\"route_id\":32},{\"id\":35,\"name\":\"RUA DELMINDA SILVEIRA\",\"sequence\":10,\"route_id\":32},{\"id\":36,\"name\":\"AVENIDA PROFESSOR HENRIQUE DA SILVA FONTES\",\"sequence\":11,\"route_id\":32},{\"id\":37,\"name\":\"TITRI\",\"sequence\":12,\"route_id\":32},{\"id\":38,\"name\":\"RUA PROFESSOR MILTON ROQUE RAMOS KRIEGER\",\"sequence\":13,\"route_id\":32},{\"id\":39,\"name\":\"RUA LAURO LINHARES\",\"sequence\":14,\"route_id\":32},{\"id\":40,\"name\":\"RUA DELMINDA SILVEIRA\",\"sequence\":15,\"route_id\":32},{\"id\":41,\"name\":\"RUA RUI BARBOSA\",\"sequence\":16,\"route_id\":32},{\"id\":42,\"name\":\"RUA FREI CANECA\",\"sequence\":17,\"route_id\":32},{\"id\":43,\"name\":\"RUA HEITOR LUZ\",\"sequence\":18,\"route_id\":32},{\"id\":44,\"name\":\"AVENIDA MAURO RAMOS\",\"sequence\":19,\"route_id\":32},{\"id\":45,\"name\":\"RUA SILVA JARDIM\",\"sequence\":20,\"route_id\":32},{\"id\":46,\"name\":\"AVENIDA GOVERNADOR GUSTAVO RICHARD\",\"sequence\":21,\"route_id\":32}],\"rowsAffected\":0}"));
//            ActDetails.this.stopsCallBack.onComplete(result);

        // Execute the ASyncTask to get the Route's Departure from the WS
        wsParam = new WSParam(WSParam.URL_FIND_DEPARTURES_BY_ROUTEID, "routeId", routeId);
        wsTask = new WSTask(ActDetails.this, ActDetails.this.departureCallBack);
        wsTask.execute(wsParam);
//            TaskResult result = new TaskResult();
//            result.setJsonObj(new JSONObject("{\"rows\":[{\"id\":570,\"calendar\":\"WEEKDAY\",\"time\":\"05:39\"},{\"id\":571,\"calendar\":\"WEEKDAY\",\"time\":\"05:57\"},{\"id\":548,\"calendar\":\"WEEKDAY\",\"time\":\"06:15\"},{\"id\":572,\"calendar\":\"WEEKDAY\",\"time\":\"06:33\"},{\"id\":209,\"calendar\":\"WEEKDAY\",\"time\":\"06:51\"},{\"id\":210,\"calendar\":\"WEEKDAY\",\"time\":\"07:07\"},{\"id\":475,\"calendar\":\"WEEKDAY\",\"time\":\"07:20\"},{\"id\":432,\"calendar\":\"WEEKDAY\",\"time\":\"07:35\"},{\"id\":288,\"calendar\":\"WEEKDAY\",\"time\":\"07:55\"},{\"id\":573,\"calendar\":\"WEEKDAY\",\"time\":\"08:11\"},{\"id\":574,\"calendar\":\"WEEKDAY\",\"time\":\"08:27\"},{\"id\":575,\"calendar\":\"WEEKDAY\",\"time\":\"08:43\"},{\"id\":576,\"calendar\":\"WEEKDAY\",\"time\":\"08:59\"},{\"id\":577,\"calendar\":\"WEEKDAY\",\"time\":\"09:15\"},{\"id\":578,\"calendar\":\"WEEKDAY\",\"time\":\"09:31\"},{\"id\":217,\"calendar\":\"WEEKDAY\",\"time\":\"09:47\"},{\"id\":579,\"calendar\":\"WEEKDAY\",\"time\":\"10:05\"},{\"id\":219,\"calendar\":\"WEEKDAY\",\"time\":\"10:24\"},{\"id\":220,\"calendar\":\"WEEKDAY\",\"time\":\"10:43\"},{\"id\":580,\"calendar\":\"WEEKDAY\",\"time\":\"11:02\"},{\"id\":581,\"calendar\":\"WEEKDAY\",\"time\":\"11:21\"},{\"id\":222,\"calendar\":\"WEEKDAY\",\"time\":\"11:40\"},{\"id\":223,\"calendar\":\"WEEKDAY\",\"time\":\"12:00\"},{\"id\":582,\"calendar\":\"WEEKDAY\",\"time\":\"12:19\"},{\"id\":441,\"calendar\":\"WEEKDAY\",\"time\":\"12:38\"},{\"id\":583,\"calendar\":\"WEEKDAY\",\"time\":\"12:57\"},{\"id\":483,\"calendar\":\"WEEKDAY\",\"time\":\"13:15\"},{\"id\":584,\"calendar\":\"WEEKDAY\",\"time\":\"13:34\"},{\"id\":585,\"calendar\":\"WEEKDAY\",\"time\":\"13:53\"},{\"id\":586,\"calendar\":\"WEEKDAY\",\"time\":\"14:12\"},{\"id\":587,\"calendar\":\"WEEKDAY\",\"time\":\"14:31\"},{\"id\":313,\"calendar\":\"WEEKDAY\",\"time\":\"14:50\"},{\"id\":588,\"calendar\":\"WEEKDAY\",\"time\":\"15:09\"},{\"id\":589,\"calendar\":\"WEEKDAY\",\"time\":\"15:29\"},{\"id\":590,\"calendar\":\"WEEKDAY\",\"time\":\"15:49\"},{\"id\":591,\"calendar\":\"WEEKDAY\",\"time\":\"16:05\"},{\"id\":592,\"calendar\":\"WEEKDAY\",\"time\":\"16:21\"},{\"id\":593,\"calendar\":\"WEEKDAY\",\"time\":\"16:37\"},{\"id\":594,\"calendar\":\"WEEKDAY\",\"time\":\"16:53\"},{\"id\":595,\"calendar\":\"WEEKDAY\",\"time\":\"17:09\"},{\"id\":491,\"calendar\":\"WEEKDAY\",\"time\":\"17:25\"},{\"id\":596,\"calendar\":\"WEEKDAY\",\"time\":\"17:41\"},{\"id\":597,\"calendar\":\"WEEKDAY\",\"time\":\"17:58\"},{\"id\":598,\"calendar\":\"WEEKDAY\",\"time\":\"18:14\"},{\"id\":167,\"calendar\":\"WEEKDAY\",\"time\":\"18:30\"},{\"id\":599,\"calendar\":\"WEEKDAY\",\"time\":\"18:46\"},{\"id\":600,\"calendar\":\"WEEKDAY\",\"time\":\"19:04\"},{\"id\":601,\"calendar\":\"WEEKDAY\",\"time\":\"19:23\"},{\"id\":602,\"calendar\":\"WEEKDAY\",\"time\":\"19:42\"},{\"id\":603,\"calendar\":\"WEEKDAY\",\"time\":\"20:01\"},{\"id\":340,\"calendar\":\"WEEKDAY\",\"time\":\"20:20\"},{\"id\":342,\"calendar\":\"WEEKDAY\",\"time\":\"20:40\"},{\"id\":343,\"calendar\":\"WEEKDAY\",\"time\":\"21:00\"},{\"id\":604,\"calendar\":\"WEEKDAY\",\"time\":\"21:19\"},{\"id\":605,\"calendar\":\"WEEKDAY\",\"time\":\"21:39\"},{\"id\":243,\"calendar\":\"WEEKDAY\",\"time\":\"22:00\"},{\"id\":606,\"calendar\":\"WEEKDAY\",\"time\":\"22:22\"},{\"id\":607,\"calendar\":\"WEEKDAY\",\"time\":\"22:43\"},{\"id\":608,\"calendar\":\"WEEKDAY\",\"time\":\"23:01\"},{\"id\":609,\"calendar\":\"WEEKDAY\",\"time\":\"23:31\"},{\"id\":354,\"calendar\":\"WEEKDAY\",\"time\":\"00:05\"},{\"id\":610,\"calendar\":\"SATURDAY\",\"time\":\"05:50\"},{\"id\":611,\"calendar\":\"SATURDAY\",\"time\":\"06:15\"},{\"id\":357,\"calendar\":\"SATURDAY\",\"time\":\"06:40\"},{\"id\":612,\"calendar\":\"SATURDAY\",\"time\":\"07:05\"},{\"id\":613,\"calendar\":\"SATURDAY\",\"time\":\"07:30\"},{\"id\":360,\"calendar\":\"SATURDAY\",\"time\":\"07:55\"},{\"id\":614,\"calendar\":\"SATURDAY\",\"time\":\"08:20\"},{\"id\":615,\"calendar\":\"SATURDAY\",\"time\":\"08:45\"},{\"id\":364,\"calendar\":\"SATURDAY\",\"time\":\"09:10\"},{\"id\":616,\"calendar\":\"SATURDAY\",\"time\":\"09:35\"},{\"id\":617,\"calendar\":\"SATURDAY\",\"time\":\"10:00\"},{\"id\":618,\"calendar\":\"SATURDAY\",\"time\":\"10:30\"},{\"id\":619,\"calendar\":\"SATURDAY\",\"time\":\"11:00\"},{\"id\":620,\"calendar\":\"SATURDAY\",\"time\":\"11:30\"},{\"id\":621,\"calendar\":\"SATURDAY\",\"time\":\"12:00\"},{\"id\":373,\"calendar\":\"SATURDAY\",\"time\":\"12:25\"},{\"id\":517,\"calendar\":\"SATURDAY\",\"time\":\"12:50\"},{\"id\":519,\"calendar\":\"SATURDAY\",\"time\":\"13:15\"},{\"id\":377,\"calendar\":\"SATURDAY\",\"time\":\"13:40\"},{\"id\":622,\"calendar\":\"SATURDAY\",\"time\":\"14:05\"},{\"id\":623,\"calendar\":\"SATURDAY\",\"time\":\"14:30\"},{\"id\":260,\"calendar\":\"SATURDAY\",\"time\":\"15:00\"},{\"id\":624,\"calendar\":\"SATURDAY\",\"time\":\"15:30\"},{\"id\":261,\"calendar\":\"SATURDAY\",\"time\":\"16:00\"},{\"id\":625,\"calendar\":\"SATURDAY\",\"time\":\"16:30\"},{\"id\":262,\"calendar\":\"SATURDAY\",\"time\":\"17:00\"},{\"id\":460,\"calendar\":\"SATURDAY\",\"time\":\"17:30\"},{\"id\":462,\"calendar\":\"SATURDAY\",\"time\":\"18:00\"},{\"id\":464,\"calendar\":\"SATURDAY\",\"time\":\"18:30\"},{\"id\":466,\"calendar\":\"SATURDAY\",\"time\":\"19:00\"},{\"id\":626,\"calendar\":\"SATURDAY\",\"time\":\"19:30\"},{\"id\":263,\"calendar\":\"SATURDAY\",\"time\":\"20:00\"},{\"id\":396,\"calendar\":\"SATURDAY\",\"time\":\"20:30\"},{\"id\":264,\"calendar\":\"SATURDAY\",\"time\":\"21:00\"},{\"id\":397,\"calendar\":\"SATURDAY\",\"time\":\"21:30\"},{\"id\":265,\"calendar\":\"SATURDAY\",\"time\":\"22:00\"},{\"id\":398,\"calendar\":\"SATURDAY\",\"time\":\"22:30\"},{\"id\":266,\"calendar\":\"SATURDAY\",\"time\":\"23:00\"},{\"id\":399,\"calendar\":\"SATURDAY\",\"time\":\"23:30\"},{\"id\":504,\"calendar\":\"SATURDAY\",\"time\":\"00:00\"},{\"id\":627,\"calendar\":\"SUNDAY\",\"time\":\"05:53\"},{\"id\":401,\"calendar\":\"SUNDAY\",\"time\":\"06:25\"},{\"id\":563,\"calendar\":\"SUNDAY\",\"time\":\"06:55\"},{\"id\":628,\"calendar\":\"SUNDAY\",\"time\":\"07:25\"},{\"id\":629,\"calendar\":\"SUNDAY\",\"time\":\"07:55\"},{\"id\":630,\"calendar\":\"SUNDAY\",\"time\":\"08:25\"},{\"id\":268,\"calendar\":\"SUNDAY\",\"time\":\"08:55\"},{\"id\":631,\"calendar\":\"SUNDAY\",\"time\":\"09:25\"},{\"id\":269,\"calendar\":\"SUNDAY\",\"time\":\"09:55\"},{\"id\":632,\"calendar\":\"SUNDAY\",\"time\":\"10:25\"},{\"id\":270,\"calendar\":\"SUNDAY\",\"time\":\"10:55\"},{\"id\":409,\"calendar\":\"SUNDAY\",\"time\":\"11:25\"},{\"id\":271,\"calendar\":\"SUNDAY\",\"time\":\"11:55\"},{\"id\":633,\"calendar\":\"SUNDAY\",\"time\":\"12:25\"},{\"id\":272,\"calendar\":\"SUNDAY\",\"time\":\"12:55\"},{\"id\":634,\"calendar\":\"SUNDAY\",\"time\":\"13:25\"},{\"id\":273,\"calendar\":\"SUNDAY\",\"time\":\"13:55\"},{\"id\":635,\"calendar\":\"SUNDAY\",\"time\":\"14:25\"},{\"id\":274,\"calendar\":\"SUNDAY\",\"time\":\"14:55\"},{\"id\":636,\"calendar\":\"SUNDAY\",\"time\":\"15:25\"},{\"id\":637,\"calendar\":\"SUNDAY\",\"time\":\"15:55\"},{\"id\":416,\"calendar\":\"SUNDAY\",\"time\":\"16:25\"},{\"id\":275,\"calendar\":\"SUNDAY\",\"time\":\"16:55\"},{\"id\":638,\"calendar\":\"SUNDAY\",\"time\":\"17:25\"},{\"id\":276,\"calendar\":\"SUNDAY\",\"time\":\"17:55\"},{\"id\":639,\"calendar\":\"SUNDAY\",\"time\":\"18:25\"},{\"id\":420,\"calendar\":\"SUNDAY\",\"time\":\"18:55\"},{\"id\":640,\"calendar\":\"SUNDAY\",\"time\":\"19:25\"},{\"id\":277,\"calendar\":\"SUNDAY\",\"time\":\"19:55\"},{\"id\":641,\"calendar\":\"SUNDAY\",\"time\":\"20:25\"},{\"id\":642,\"calendar\":\"SUNDAY\",\"time\":\"20:55\"},{\"id\":643,\"calendar\":\"SUNDAY\",\"time\":\"21:25\"},{\"id\":644,\"calendar\":\"SUNDAY\",\"time\":\"21:55\"},{\"id\":645,\"calendar\":\"SUNDAY\",\"time\":\"22:25\"},{\"id\":278,\"calendar\":\"SUNDAY\",\"time\":\"22:55\"},{\"id\":279,\"calendar\":\"SUNDAY\",\"time\":\"23:25\"},{\"id\":430,\"calendar\":\"SUNDAY\",\"time\":\"00:00\"}],\"rowsAffected\":0}"));
//            ActDetails.this.departureCallBack.onComplete(result);
        } catch (Exception ex) {
            Toast.makeText(ActDetails.this, "Problem getting the Route Info", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState == null)
            return;

        try {
            // Restore the saved GUI
        } catch (Exception ex) {
            Toast.makeText(ActDetails.this, "Error restoring the route info", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Defines/creates the View Pages at the View Page Adapter
     */
    private void defineViewPages(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this.getSupportFragmentManager());

        this.tabRoute = new TabRoute();
        viewPagerAdapter.addFragment(this.tabRoute, "Route");

        this.tabDeparture = new TabDeparture();
        viewPagerAdapter.addFragment(this.tabDeparture, "Departure");

        this.viewPager.setAdapter(viewPagerAdapter);
    }

    /**
     * CallBack after get the Stops Names for the Route from the WS
     */
    private TaskCallBack stopsCallBack = new TaskCallBack() {
        @Override
        public void onComplete(TaskResult result) {
            try {
                switch (result.getStatus()) {
                    case TaskResult.STS_OK:
                        // Success calling the WS
                        ActDetails.this.tabRoute.updateStops(result.getJsonObj());
                        break;

                    case TaskResult.STS_ERR:
                        // Error calling the WS
                        Toast.makeText(ActDetails.this, result.getErrMsg(), Toast.LENGTH_LONG).show();
                        break;

                    case TaskResult.STS_UNKNOWN:
                    default:
                        // Status unknown
                        Toast.makeText(ActDetails.this, "Status unknown!", Toast.LENGTH_LONG).show();
                        break;
                }
            } catch (Exception ex) {
                Toast.makeText(ActDetails.this, "Problem unexpected", Toast.LENGTH_LONG).show();
            }
        }
    };

    /**
     * CallBack after get the Departures for the Route from the WS
     */
    private TaskCallBack departureCallBack = new TaskCallBack() {
        @Override
        public void onComplete(TaskResult result) {
            try {
                switch (result.getStatus()) {
                    case TaskResult.STS_OK:
                        // Success calling the WS
                        ActDetails.this.tabDeparture.updateDeparture(result.getJsonObj());
                        break;

                    case TaskResult.STS_ERR:
                        // Error calling the WS
                        Toast.makeText(ActDetails.this, result.getErrMsg(), Toast.LENGTH_LONG).show();
                        break;

                    case TaskResult.STS_UNKNOWN:
                    default:
                        // Status unknown
                        Toast.makeText(ActDetails.this, "Status unknown!", Toast.LENGTH_LONG).show();
                        break;
                }
            } catch (Exception ex) {
                Toast.makeText(ActDetails.this, "Problem unexpected", Toast.LENGTH_LONG).show();
            }
        }
    };
}
