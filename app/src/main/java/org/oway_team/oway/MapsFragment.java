package org.oway_team.oway;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.oway_team.oway.json.JSONLineString;
import org.oway_team.oway.json.JSONNavigationItem;
import org.oway_team.oway.json.JSONRequestBuilder;
import org.oway_team.oway.json.JSONRoute;
import org.oway_team.oway.json.JSONRouterProxy;
import org.oway_team.oway.json.JSONRouterProxyListener;
import org.oway_team.oway.maps.OverlayRect;

import java.util.List;

import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.OverlayManager;
import ru.yandex.yandexmapkit.map.MapEvent;
import ru.yandex.yandexmapkit.map.OnMapListener;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;
import ru.yandex.yandexmapkit.utils.GeoPoint;
import ru.yandex.yandexmapkit.utils.ScreenPoint;

public class MapsFragment extends Fragment implements JSONRouterProxyListener {
    public static final String TAG = "OWay-Map";
    MapController mMapController;
    LinearLayout mView;
    JSONRouterProxy mJSONRouterProxy;
    OverlayManager mOverlayManager;
    String mRouteId;
    JSONRoute mCurrentRoute;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.maps_layout, container, false);
        final MapView mapView = (MapView) view.findViewById(R.id.map);
        mapView.showBuiltInScreenButtons(true);
        mJSONRouterProxy = new JSONRouterProxy(this);
        mMapController = mapView.getMapController();
        //Start map on Novosibirsk
        mMapController.setPositionAnimationTo(new GeoPoint(55.018803,82.933952));
        mOverlayManager = mMapController.getOverlayManager();

        return view;

    }
    public void postPoints(List<JSONNavigationItem> items) {
        Log.d(TAG, "Build route");
        mRouteId = "";
        String jQuery = JSONRequestBuilder.buildPointsList(items);
        mJSONRouterProxy.postPoints(jQuery);
        for (JSONNavigationItem item: items) {
            Drawable overlayItemDrawable;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                overlayItemDrawable = getResources().getDrawable(R.drawable.item, getActivity().getTheme());
            } else {
                overlayItemDrawable = getResources().getDrawable(R.drawable.item);
            }
            final OverlayItem kremlin = new OverlayItem(item.location, overlayItemDrawable);
            // Create a balloon model for the object
            BalloonItem balloonKremlin = new BalloonItem(getActivity(),kremlin.getGeoPoint());
            balloonKremlin.setText(item.title);
//        // Add the balloon model to the object
            kremlin.setBalloonItem(balloonKremlin);
            // Add the object to the layer
            Overlay overlay = new Overlay(mMapController);
            overlay.addOverlayItem(kremlin);
            mOverlayManager.addOverlay(overlay);
        }
    }

    //List post finished
    @Override
    public void onRoutePostReady(String routeId) {
        mRouteId = routeId;
        mJSONRouterProxy.getRoute(routeId);
    }
    //Route building finished
    @Override
    public void onRouteGetReady(JSONRoute jRoute) {
        Log.d(TAG, "Route ready; Linestrings cnt: " + jRoute.lineStrings.size());
        drawRoute(jRoute);
    }
    public void drawRoute(JSONRoute route) {
        mCurrentRoute = route;
        for (JSONLineString lineString:route.lineStrings) {
            OverlayRect overlayRect = new OverlayRect(mMapController, lineString);
            mMapController.getOverlayManager().addOverlay(overlayRect);
        }
        mMapController.notifyRepaint();
    }

    @Override
    public void onRouteLoadingError() {

    }
}
