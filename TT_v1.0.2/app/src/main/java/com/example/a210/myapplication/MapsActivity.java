package com.example.a210.myapplication;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Geocoder geocoder;
    private Button button,finalSelectBtn;
    private EditText editText;
    private TextView resultAddress;
    MarkerOptions  mOptions2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        editText = (EditText) findViewById(R.id.addressEdt);
        button=(Button)findViewById(R.id.addressBtn);
        finalSelectBtn = (Button)findViewById(R.id.finalSelectBtn);
        resultAddress = (TextView)findViewById(R.id.resultAddress);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent getIt = getIntent();
        String searchAddress = getIt.getStringExtra("search address");

        if(!searchAddress.isEmpty())
        {
            finalSelectBtn.setVisibility(View.INVISIBLE);
            editText.setText(searchAddress);
            button.performClick();
        }else
        {
            finalSelectBtn.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
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
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        geocoder = new Geocoder(this);

        // 맵 터치 이벤트 구현 //
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(LatLng point) {
                MarkerOptions mOptions = new MarkerOptions();
                // 마커 타이틀
                mOptions.title("마커 좌표");
                Double latitude = point.latitude; // 위도
                Double longitude = point.longitude; // 경도
                // 마커의 스니펫(간단한 텍스트) 설정
                mOptions.snippet(latitude.toString() + ", " + longitude.toString());
                // LatLng: 위도 경도 쌍을 나타냄
                mOptions.position(new LatLng(latitude, longitude));
            }
        });
        ////////////////////

        // 버튼 이벤트
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                String str=editText.getText().toString();

                List<Address> addressList = null;
                try {
                    // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
                    addressList = geocoder.getFromLocationName(
                            str, // 주소
                            10); // 최대 검색 결과 개수
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                if (addressList.size() == 0){
                    resultAddress.setText("검색 결과가 없습니다.");
                    return;
                }

                System.out.println(addressList.get(0).toString());
                // 콤마를 기준으로 split
                String []splitStr = addressList.get(0).toString().split(",");
                String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1,splitStr[0].length() - 2); // 주소
                System.out.println(address);
                resultAddress.setText(address);

                String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
                String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도
                System.out.println(latitude);
                System.out.println(longitude);
                mapAsync mapasync = new mapAsync();

                try {
                    mOptions2 = mapasync.execute(address, latitude, longitude).get();
                }catch(Exception e) {
                    e.printStackTrace();
                }
                }
        });
        ////////////////////

         //Add a marker in Sydney and move the camera
        /*
        LatLng sichong = new LatLng(37.5662952, 126.9779451);
        mMap.addMarker(new MarkerOptions().position(sichong).title("서울특별시 시청"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sichong));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sichong,15));
*/


        finalSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                if(resultAddress.getText().equals("") || resultAddress.getText().equals("검색 결과가 없습니다.")){
                    Toast.makeText(getApplicationContext(),"검색 결과가 없습니다.",Toast.LENGTH_LONG).show();
                    return;
                }
                intent.putExtra("address", resultAddress.getText());
                intent.putExtra("position", mOptions2.getPosition().toString());

                setResult(100, intent);
                finish();
            }
        });
    }

    public class mapAsync extends AsyncTask<String, String, MarkerOptions> {
        @Override
        protected void onPostExecute(MarkerOptions markerOptions) {
            super.onPostExecute(markerOptions);
            mMap.clear();
            mMap.addMarker(mOptions2);
            // 해당 좌표로 화면 줌

            mMap.moveCamera(CameraUpdateFactory.newLatLng(mOptions2.getPosition()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mOptions2.getPosition(), 15));
        }

        //JS
        @Override
        protected MarkerOptions doInBackground(String... strings) {
            String latitude = strings[1];
            String longitude = strings[2];
            String address = strings[0];

            final LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
            // 마커 생성
            mOptions2 = new MarkerOptions();
            mOptions2.title("search result");
            mOptions2.snippet(address);
            mOptions2.position(point);

            return mOptions2;
        }
    }
}