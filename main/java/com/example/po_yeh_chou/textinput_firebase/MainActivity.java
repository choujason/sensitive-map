package com.example.po_yeh_chou.textinput_firebase;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener {

    Button SubmitButton;

    EditText NameEditText, ReportEditText, RepoetType;

    // Declaring String variable ( In which we are storing firebase server URL ).
//    public static final String Firebase_Server_URL = "https://insertdata-android-examples.firebaseio.com/";

    // Declaring String variables to store name & phone number get from EditText.
    String NameHolder, ReportHolder, ReportTypeHolder ;
    Double Longti_Holder, Lati_Holder;     ;

    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    protected String mLatitudeLabel;
    protected String mLongitudeLabel;
    protected TextView mLatitudeText;
    protected TextView mLongitudeText;
    public double longtitude;
    public double latitude;

    protected static final String TAG = "MainActivity";

    DatabaseReference databaseReference;

    // Root Database Name for Firebase Database.
    public static final String Database_Path = "UserReport_Details_Database";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLatitudeLabel = "緯度";
        mLongitudeLabel = "經度";
        mLatitudeText = (TextView) findViewById((R.id.latitude_text));
        mLongitudeText = (TextView) findViewById((R.id.longitude_text));

        buildGoogleApiClient();

        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

        SubmitButton = (Button) findViewById(R.id.submit);

        NameEditText = (EditText) findViewById(R.id.name);

        ReportEditText = (EditText) findViewById(R.id.Report);

        final RadioGroup rg = (RadioGroup) findViewById(R.id.rg_type);

        SubmitButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                UserDetail userDetail = new UserDetail();

                GetDataFromEditText();

                // Adding name into class function object.
                userDetail.setUserNames(NameHolder);

                // Adding phone number into class function object.
                userDetail.setUserReport(ReportHolder);

                userDetail.setReportType(ReportTypeHolder);
                userDetail.setLatitude(Lati_Holder);
                userDetail.setLongtitude(Longti_Holder);




                // Getting the ID from firebase database.
                String UserRecordIDFromServer = databaseReference.push().getKey();


                //如果輸入內容是空的，跳出通知並停止
                if (NameHolder.isEmpty()) {
                    Toast.makeText(MainActivity.this, "請輸入您的姓名代號", Toast.LENGTH_LONG).show();
                    return;
                }


                // Adding the both name and number values using student details class object using ID.
                databaseReference.child(UserRecordIDFromServer).setValue(userDetail);


                // Showing Toast message after successfully data submit.
                Toast.makeText(MainActivity.this, "Data Inserted Successfully into Firebase Database", Toast.LENGTH_LONG).show();


                //回報成功後，清空資料
                NameEditText.getText().clear();
                ReportEditText.getText().clear();
                rg.clearCheck();


                // Reset
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                //finish();

                /*
                switch (rg.getCheckedRadioButtonId()) {
                    case R.id.visual:
                        databaseReference.child("ReportType").push().setValue(("視覺回報"));
                        break;
                    case R.id.audio:
                        databaseReference.push().setValue("聽覺回報");
                        break;
                    case R.id.smell:
                        databaseReference.push().setValue("嗅覺回報");
                        break;
                    case R.id.other:
                        databaseReference.push().setValue("其他回報");
                        break;


                }
                */

            }
        });

    }

    public void GetDataFromEditText() {

        final RadioGroup rg = (RadioGroup) findViewById(R.id.rg_type);

        NameHolder = NameEditText.getText().toString().trim();
        ReportHolder = ReportEditText.getText().toString().trim();
        Lati_Holder = latitude;
        Longti_Holder =longtitude;

        switch (rg.getCheckedRadioButtonId()) {
            case R.id.visual:
                ReportTypeHolder = ("視覺回報");
                break;
            case R.id.audio:
                ReportTypeHolder = ("聽覺回報");
                break;
            case R.id.smell:
                ReportTypeHolder = ("嗅覺回報");
                break;
            case R.id.other:
                ReportTypeHolder = ("其他回報");
                break;
            default:
                Toast.makeText(MainActivity.this, "請輸入回報種類", Toast.LENGTH_LONG).show();


        }

    }


    protected synchronized void buildGoogleApiClient()


    {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    // These below will run after GoogleApiClient connects Google Play Service
    public void onConnected(Bundle connectionHint) {
        // ignore that this line will be underlined in red in IDE
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null)
        {

            longtitude = mLastLocation.getLongitude();
            latitude = mLastLocation.getLatitude();

            mLatitudeText.setText(String.format("%s: %f", mLatitudeLabel, latitude));
            mLongitudeText.setText(String.format("%s: %f", mLongitudeLabel, longtitude));
        }
        else
        {
            Toast.makeText(this, "偵測不到定位，請確認定位功能已開啟。", Toast.LENGTH_LONG).show();
        }
    }

    public void onConnectionFailed(ConnectionResult result)
    {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    public void onConnectionSuspended(int cause)
    {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }
}







