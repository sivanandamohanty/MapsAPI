import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by smohanty on 8/28/17.
 */
public class LocationService {


    public JSONObject getRoute(String strOrigin, String strDestination) {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject =  ;

        try {
        URL url = new URL("https://maps.googleapis.com/maps/api/directions/json?origin=" + strOrigin + "&destination=" + strDestination + "&key=AIzaSyBOifYHXd8otXfFDV9ZrzPEVj3Vmq_F1-E");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }

            try {
                Object obj = parser.parse(new InputStreamReader(
                        (conn.getInputStream())));
                jsonObject = (JSONObject) obj;

            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }

        conn.disconnect();

    } catch (MalformedURLException e) {

        e.printStackTrace();

    } catch (IOException e) {

        e.printStackTrace();

    }

        return jsonObject;
    }



    public Multimap getRouteInfo(String strOrigin, String strDestination) {
        JSONParser parser = new JSONParser();

        Multimap<String,Object> mapDetails = ArrayListMultimap.create();

        HashMap<String, String> mapInfo = new HashMap<String, String>();


        JSONObject jsonObject =  null;

        try {
            URL url = new URL("https://maps.googleapis.com/maps/api/directions/json?origin=" + strOrigin + "&destination=" + strDestination + "&key=AIzaSyBOifYHXd8otXfFDV9ZrzPEVj3Vmq_F1-E");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            try {
                Object obj = parser.parse(new InputStreamReader(
                        (conn.getInputStream())));
                jsonObject = (JSONObject) obj;

                JSONArray routes = (JSONArray) jsonObject.get("routes");

                Iterator<JSONObject> iterator = routes.iterator();
                while (iterator.hasNext()) {
                    JSONObject objRoutes = iterator.next();
                    JSONArray legs = (JSONArray) objRoutes.get("legs");
                    Iterator<JSONObject> iteratorLegs = legs.iterator();
                    while (iteratorLegs.hasNext()) {
                        JSONObject legsData = iteratorLegs.next();
                        //For getting destination and Origin
                        mapDetails.put("Origin", legsData.get("start_address").toString());
                        mapDetails.put("Destination", legsData.get("end_address").toString());

                        //Get the Steps
                        JSONArray steps = (JSONArray) legsData.get("steps");
                        Iterator<JSONObject> iteratorSteps = steps.iterator();
                        int counter = 0;
                        while (iteratorSteps.hasNext()) {
                            JSONObject StepsData = iteratorSteps.next();
                            //For getting destination and Origin
                            mapInfo.put("Step" + counter, ((JSONObject) StepsData.get("start_location")).get("lat").toString() + " " + ((JSONObject) StepsData.get("start_location")).get("lng").toString());

                            //Increment Counter
                            counter ++;
                        }
                        mapDetails.put("points", mapInfo);
                    }

                    //Overview Polyline
                    JSONObject objOverviewPolyline = (JSONObject) objRoutes.get("overview_polyline");
                    mapDetails.put("Polyline", objOverviewPolyline.get("points").toString());
                }

            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }







        return mapDetails;
    }

}
