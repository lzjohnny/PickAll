package cn.xidianedu.pickall.activity;

import com.baidu.mapapi.cloud.BoundSearchInfo;
import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudPoiInfo;
import com.baidu.mapapi.cloud.CloudRgcInfo;
import com.baidu.mapapi.cloud.CloudRgcResult;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchInfo;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.cloud.LocalSearchInfo;
import com.baidu.mapapi.cloud.NearbySearchInfo;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.model.LatLngBounds.Builder;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import cn.xidianedu.pickall.R;

/**
 * 从百度地图数据平台加载采摘园坐标数据，并画在地图上
 * 这里面乱七八糟的方法都是百度地图SDK提供的
 * API参考百度地图官网示例代码：http://lbsyun.baidu.com/index.php?title=androidsdk/sdkandev-download
 */

public class CloudSearchActivity extends Activity implements CloudListener {

    private static final String LTAG = CloudSearchActivity.class.getSimpleName();
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_lbssearch);
        CloudManager.getInstance().init(CloudSearchActivity.this);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        findViewById(R.id.regionSearch).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LocalSearchInfo info = new LocalSearchInfo();
                        info.ak = "1fjoP27Ie6xmqCSelUxRj4cAW4WD2GWY";
                        info.geoTableId = 169103;
                        info.tags = "";
//                        info.q = "天安门";
                        info.region = "西安市";
                        CloudManager.getInstance().localSearch(info);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        CloudManager.getInstance().destroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    public void onGetDetailSearchResult(DetailSearchResult result, int error) {
        if (result != null) {
            if (result.poiInfo != null) {
                Toast.makeText(CloudSearchActivity.this, result.poiInfo.title,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CloudSearchActivity.this,
                        "status:" + result.status, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onGetSearchResult(CloudSearchResult result, int error) {
        if (result != null && result.poiList != null
                && result.poiList.size() > 0) {
            Log.d(LTAG, "onGetSearchResult, result length: " + result.poiList.size());
            mBaiduMap.clear();
            BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
            LatLng ll;
            LatLngBounds.Builder builder = new Builder();
            for (CloudPoiInfo info : result.poiList) {
                ll = new LatLng(info.latitude, info.longitude);
                OverlayOptions oo = new MarkerOptions().icon(bd).position(ll);
                mBaiduMap.addOverlay(oo);
                builder.include(ll);
            }
            LatLngBounds bounds = builder.build();
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(bounds);
            mBaiduMap.animateMapStatus(u);
        }
    }

    @Override
    public void onGetCloudRgcResult(CloudRgcResult result, int error) {
        if (result != null && error == 0) {
            if (result.status == 0) {
//                Log.d("---size", result.pois.size() + "");

                BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
                mBaiduMap.clear();
                LatLng ll;
                LatLngBounds.Builder builder = new Builder();
                if (result.customPois != null && result.customPois.size() > 0) {
                    for (int i = 0; i < result.customPois.size(); i++) {
                        CloudPoiInfo info = result.customPois.get(i);
                        ll = new LatLng(info.latitude, info.longitude);
                        OverlayOptions oo = new MarkerOptions().icon(bd).position(ll);
                        mBaiduMap.addOverlay(oo);
                        TextOptions too = new TextOptions().text(info.title).position(ll).bgColor(0xffff0000)
                                .fontSize(30);
                        mBaiduMap.addOverlay(too);
                        builder.include(ll);
                    }
                }

                try {
                    OverlayOptions inOverlay = new MarkerOptions().icon(bd).position(result.location);
                    Marker marker = (Marker) mBaiduMap.addOverlay(inOverlay);

                    builder.include(result.location);
                    TextOptions txo = new TextOptions().text(result.customLocationDescription).position(result
                            .location).bgColor(0xffff0000).fontSize(30);
                    mBaiduMap.addOverlay(txo);

                    LatLngBounds bounds = builder.build();
                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(bounds);

                    mBaiduMap.animateMapStatus(u);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(CloudSearchActivity.this,
                        "status:" + result.status, Toast.LENGTH_SHORT).show();
            }
        }
    }
}

