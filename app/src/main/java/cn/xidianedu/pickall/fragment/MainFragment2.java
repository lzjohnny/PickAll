package cn.xidianedu.pickall.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudPoiInfo;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import java.util.ArrayList;
import java.util.List;

import cn.xidianedu.pickall.R;
import cn.xidianedu.pickall.activity.CloudSearchActivity;
import cn.xidianedu.pickall.activity.MainActivity;
import cn.xidianedu.pickall.mapapi.BikingRouteOverlay;
import cn.xidianedu.pickall.mapapi.OverlayManager;

public class MainFragment2 extends Fragment implements OnGetRoutePlanResultListener, BaiduMap.OnMapClickListener {
    private Context mContext;
    private LocationClient mLocationClient;
    private MapView mapView;
    private BaiduMap baiduMap;
    private Toolbar toolbar;
    RouteLine route = null;
    OverlayManager routeOverlay = null;

    private boolean isFirstLocate = true;
    BDLocation currentLocation = null;
    RoutePlanSearch mSearch = null;
    CloudSearchResult currentResult = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);

        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(getActivity(), permissions, 1);
        } else {
            requestLocation();
        }
    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_2, container, false);
        mapView = (MapView) view.findViewById(R.id.main_frag_2_mapview);
        toolbar = (Toolbar) view.findViewById(R.id.main_frag_2_map_toolbar);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.main_frag_2_btn_locate);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                if (currentLocation != null) {
                    isFirstLocate = true;
                    navigateTo(currentLocation);
                } else {
                    Toast.makeText(mContext, "定位中", Toast.LENGTH_SHORT).show();
                }
            }
        });

        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        baiduMap.setOnMapClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mLocationClient.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

    public void searchRoute(LatLng endLatLng) {
        // 重置浏览节点的路线数据
        route = null;
        baiduMap.clear();

//        String startNodeStr = "航天城地铁站";
//        String endNodeStr = "西安电子科技大学南校区";
//        PlanNode stNode = PlanNode.withCityNameAndPlaceName("西安", startNodeStr);
//        PlanNode enNode = PlanNode.withCityNameAndPlaceName("西安", endNodeStr);
        if (currentLocation != null) {
            LatLng staLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            PlanNode staNode = PlanNode.withLocation(staLatLng);
            PlanNode endNode = PlanNode.withLocation(endLatLng);

            mSearch.bikingSearch((new BikingRoutePlanOption()).from(staNode).to(endNode));
        } else {
            Toast.makeText(mContext, "定位中", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
        Log.d("MainFragment2==", "onGetWalkingRouteResult");
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
        Log.d("MainFragment2==", "onGetTransitRouteResult");
    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {
        Log.d("MainFragment2==", "onGetMassTransitRouteResult");
    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
        Log.d("MainFragment2==", "onGetDrivingRouteResult");
    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {
        Log.d("MainFragment2==", "onGetIndoorRouteResult");
    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult result) {
        Log.d("MainFragment2==", "onGetBikingRouteResult");
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(mContext, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        } else if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            Toast.makeText(mContext, "检索地址有歧义", Toast.LENGTH_SHORT).show();
        } else if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            //返回正确状态码

            if (result.getRouteLines().size() > 1) {
                Toast.makeText(mContext, "路线不唯一", Toast.LENGTH_SHORT).show();
            } else if (result.getRouteLines().size() == 1) {
                route = result.getRouteLines().get(0);
                BikingRouteOverlay overlay = new MyBikingRouteOverlay(baiduMap);
                routeOverlay = overlay;
//                baiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            } else {
                Log.d("route result", "结果数<0");
            }
        }
    }

    //地图被点击，去掉导航路线，展示Marker点
    @Override
    public void onMapClick(LatLng latLng) {
        baiduMap.clear();
        onGetSearchResult(currentResult, 0);
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    private class MyBikingRouteOverlay extends BikingRouteOverlay {
        MyBikingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
        }
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
//            Log.d("===纬度", bdLocation.getLatitude() + "");
//            Log.d("===经度", bdLocation.getLongitude() + "");
//            Log.d("===定位方式", bdLocation.getLocType() + "");
//            Log.d("===省", bdLocation.getProvince());
//            Log.d("===市", bdLocation.getCity());
//            Log.d("===区", bdLocation.getDistrict());
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                MainFragment2.this.currentLocation = bdLocation;
                navigateTo(bdLocation);
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }
    }

    private void navigateTo(BDLocation location) {
        if (isFirstLocate) {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            //地图缩放有问题
            isFirstLocate = false;
        }
        MyLocationData.Builder builder = new MyLocationData.Builder();
        builder.latitude(location.getLatitude());
        builder.longitude(location.getLongitude());
        MyLocationData data = builder.build();
        baiduMap.setMyLocationData(data);
    }

    //权限回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(mContext, "权限拒绝：" + permissions[i], Toast.LENGTH_SHORT).show();
                            Log.d("权限拒绝", permissions[i]);
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(mContext, "未知错误", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            default:
                Toast.makeText(mContext, "未知错误", Toast.LENGTH_SHORT).show();
        }
    }

    //展示Marker点
    public void onGetSearchResult(CloudSearchResult result, int error) {
        if (result != null && result.poiList != null
                && result.poiList.size() > 0) {
//            Log.d("=========", "onGetSearchResult, result length: " + result.poiList.size());
            //缓存采摘园marker信息，无法单独对路线clear，全部信息clear后重新画在地图上。
            this.currentResult = result;
            baiduMap.clear();

            BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
            LatLng ll;
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (CloudPoiInfo info : result.poiList) {
                ll = new LatLng(info.latitude, info.longitude);
                OverlayOptions oo = new MarkerOptions().icon(bd).position(ll).title(info.title);
                baiduMap.addOverlay(oo);
                builder.include(ll);
            }
            baiduMap.setOnMarkerClickListener(listener);
            LatLngBounds bounds = builder.build();
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(bounds);
            baiduMap.animateMapStatus(u);
        }
    }

    BaiduMap.OnMarkerClickListener listener = new BaiduMap.OnMarkerClickListener() {
        /**
         * 地图 Marker 覆盖物点击事件监听函数
         * @param marker 被点击的 marker
         */
        public boolean onMarkerClick(Marker marker) {
//            Log.d("===onMarkerClick", (marker.getTitle() == null) + "");
//            Log.d("===onMarkerClick", marker.getPosition() + "");
//            Log.d("===onMarkerClick", marker.toString());
            toolbar.setTitle(marker.getTitle());
            searchRoute(marker.getPosition());
            return true;
        }
    };
}
