package tw.qtlin.a21maptest.maptest_api21;

import android.net.wifi.WifiManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.Random;

import fi.iki.elonen.NanoHTTPD;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button bt1;
    private Button bt2;
    private Button bt3;
    private Button bt4;
    private Button bt5;
    private Button bt6;
    private Button bt7;
    private Button bt8;
    private Button bt9;
    private LatLng mLatLng;
    private TextView iptext;
    private NumberPicker mPicker;
    final static String TAG = MapsActivity.class.getSimpleName();
    AndroidWebServer androidWebServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        bt1 = (Button)findViewById(R.id.btn_b);
        bt2 = (Button)findViewById(R.id.btn_l);
        bt3 = (Button)findViewById(R.id.btn_lb);
        bt4 = (Button)findViewById(R.id.btn_lu);
        bt5 = (Button)findViewById(R.id.btn_rb);
        bt6 = (Button)findViewById(R.id.btn_ru);
        bt7 = (Button)findViewById(R.id.btn_u);
        bt8 = (Button)findViewById(R.id.btn_set);
        bt9 = (Button)findViewById(R.id.btn_r);
        bt1.setOnClickListener(mClkListener);
        bt2.setOnClickListener(mClkListener);
        bt3.setOnClickListener(mClkListener);
        bt4.setOnClickListener(mClkListener);
        bt5.setOnClickListener(mClkListener);
        bt6.setOnClickListener(mClkListener);
        bt7.setOnClickListener(mClkListener);
        bt8.setOnClickListener(mClkListener);
        bt9.setOnClickListener(mClkListener);

        mPicker = (NumberPicker)findViewById(R.id.numberPicker);
        mPicker.setMaxValue(50);
        mPicker.setMinValue(1);
        mPicker.setValue(25);
        iptext = (TextView)findViewById(R.id.textViewIp);
        iptext.setText(getIpAccess()+"8888");
        androidWebServer = new AndroidWebServer(8888);
        try {
            androidWebServer.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        setUpMap();
    }

    private void moveMap(LatLng place) {
        // 建立地圖攝影機的位置物件
        CameraPosition cameraPosition =
                new CameraPosition.Builder()
                        .target(place)
                        .zoom(17)
                        .build();

        // 使用動畫的效果移動地圖
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    private void setUpMap() {
        // 刪除原來預設的內容
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        // 建立位置的座標物件
        mLatLng = new LatLng(25.033408, 121.564099);

        // 移動地圖
        moveMap(mLatLng);

    }
    private View.OnClickListener mClkListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Random rand = new Random();
            int speed = mPicker.getValue();
            double mv_x = ((rand.nextInt(10)+speed)/1000000.0);
            double mv_y = ((rand.nextInt(10)+speed)/1000000.0);
            Log.i(TAG, Double.toString(mv_x));
            Log.i(TAG, Double.toString(mv_y));


            switch(v.getId()){
                case R.id.btn_set:
                    mLatLng = mMap.getCameraPosition().target;
                    break;
                case R.id.btn_u:
                    mLatLng = new LatLng(mLatLng.latitude + mv_y, mLatLng.longitude);
                    break;
                case R.id.btn_b:
                    mLatLng = new LatLng(mLatLng.latitude - mv_y, mLatLng.longitude);
                    break;
                case R.id.btn_l:
                    mLatLng = new LatLng(mLatLng.latitude, mLatLng.longitude - mv_x);
                    break;
                case R.id.btn_r:
                    mLatLng = new LatLng(mLatLng.latitude, mLatLng.longitude + mv_x);
                    break;
                case R.id.btn_lb:
                    mLatLng = new LatLng(mLatLng.latitude - mv_y, mLatLng.longitude - mv_x);
                    break;
                case R.id.btn_lu:
                    mLatLng = new LatLng(mLatLng.latitude + mv_y, mLatLng.longitude - mv_x);
                    break;
                case R.id.btn_rb:
                    mLatLng = new LatLng(mLatLng.latitude - mv_y, mLatLng.longitude + mv_x);
                    break;
                case R.id.btn_ru:
                    mLatLng = new LatLng(mLatLng.latitude + mv_y, mLatLng.longitude + mv_x);
                    break;
            }


            // add marker
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(mLatLng).title("Marker"));
            moveMap(mLatLng);
            upload2Mac(mLatLng);
        }
    };

    void upload2Mac(LatLng place){
        Log.i(TAG, place.toString());
        String JSONstring = "";
        try {
            JSONObject jsonObject= new JSONObject();
            jsonObject.put("lat", Double.toString(place.latitude));
            jsonObject.put("lng", Double.toString(place.longitude));
            androidWebServer.setResponseString(jsonObject.toString());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            androidWebServer.stop();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private String getIpAccess() {
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        final String formatedIpAddress = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        return "http://" + formatedIpAddress + ":";
    }
}


class AndroidWebServer extends NanoHTTPD {
    private String response;
    public AndroidWebServer(int port) {
        super(port);
        response = "";
    }

    @Override
    public Response serve(IHTTPSession session) {



        return newFixedLengthResponse(response);
    }

    void setResponseString(String str){
        response = str;
    }
}