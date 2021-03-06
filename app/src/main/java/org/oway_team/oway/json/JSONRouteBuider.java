package org.oway_team.oway.json;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.yandex.yandexmapkit.utils.GeoPoint;

public class JSONRouteBuider {
    public static JSONRoute buildRoute(String jString) {
        JSONRoute route = new JSONRoute();
        try {
            JSONObject rootObj = new JSONObject(jString);
            JSONArray jArr = rootObj.getJSONArray("paths");
            for (int i = 0; i < jArr.length(); i++) {
                JSONLineString lineString = new JSONLineString();
                String line = jArr.getString(i);
                line = line.replace("LINESTRING(","");
                line = line.replace(")","");
                String[] coords = line.split(",");
                for (int j = 0; j < coords.length; j++) {
                    String []geo = coords[j].split(" ");
                    double lon = Double.parseDouble(geo[1]);
                    double lat = Double.parseDouble(geo[0]);
                    lineString.points.add(new GeoPoint(lon, lat));
                }
                route.lineStrings.add(lineString);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return route;
    }
}
