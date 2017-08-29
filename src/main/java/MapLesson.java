import com.google.common.collect.Multimap;
import org.json.simple.JSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by smohanty on 8/28/17.
 */
public class MapLesson {

    public static void main(String[] args) {
        LocationService locationService = new LocationService();

        JSONObject jsonObject = locationService.getRoute("Sunnyvale", "Fremont");

        Multimap<String, Object> mapInfo = locationService.getRouteInfo("Fremont", "Universal+Studios+Hollywood4");

        System.out.println("Origin is : " + mapInfo.get("Origin"));
        System.out.println("destination is : " + mapInfo.get("Destination"));
        System.out.println("Polyline is : " + mapInfo.get("Polyline"));


        Collection<Object> mapPoints = mapInfo.get("points");
        Iterator<Object> itrMapPoints = mapPoints.iterator();


        while (itrMapPoints.hasNext()) {
            HashMap<String, String> mp = (HashMap<String, String>) itrMapPoints.next();
            int i = 0;
            while (i < mp.size()) {
                System.out.println("Point " + i + " Lat/Lng is : " + mp.get("Step" + i));
                i++;
            }
        }

    }
}
