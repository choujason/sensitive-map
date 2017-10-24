package com.example.po_yeh_chou.textinput_firebase;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener {

    Button SubmitButton;
    EditText NameEditText, ReportEditText, RepoetType;


//    // camera
//
//    Button btnTakePhoto;
//    ImageView imgTakenPhoto;
//    TextView textTargetUri;
//    ImageView targetImage;
//
//    //end_camera

    // Declaring String variable ( In which we are storing firebase server URL ).
//    public static final String Firebase_Server_URL = "https://insertdata-android-examples.firebaseio.com/";

    // Declaring String variables to store name & phone number get from EditText.
    String NameHolder, ReportHolder, ReportTypeHolder, UrlHolder;
    Double Longti_Holder, Lati_Holder;

    //gps
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    protected String mLatitudeLabel;
    protected String mLongitudeLabel;
    protected TextView mLatitudeText;
    protected TextView mLongitudeText;
    public double longtitude;
    public double latitude;
    //end_gps

    protected static final String TAG = "MainActivity";

    DatabaseReference databaseReference;

    // Root Database Name for Firebase Database.
    public static final String Database_Path = "UserReport_Details_Database";
//    private static final int CAM_REQUEST = 1313;



    /*  ===============================


                 image Upload


        ===============================*/


    // Folder path for Firebase Storage.
    String Storage_Path = "All_Image_Uploads/";

    // Root Database Name for Firebase Database.
    // Creating button.
    Button ChooseButton, UploadButton;

    // Creating EditText.
    EditText ImageName;
    // Creating ImageView.
    ImageView SelectImage;

    // Creating URI.
    Uri FilePathUri;

    // Creating StorageReference and DatabaseReference object.
    StorageReference storageReference;

    // Image request code for onActivityResult() .
    int Image_Request_Code = 7;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //gps
        mLatitudeLabel = "緯度";
        mLongitudeLabel = "經度";
        mLatitudeText = (TextView) findViewById((R.id.latitude_text));
        mLongitudeText = (TextView) findViewById((R.id.longitude_text));
        buildGoogleApiClient();
        //end_gps
        //camera
//        btnTakePhoto = (Button) findViewById(R.id.button1);
//        imgTakenPhoto = (ImageView) findViewById(R.id.imageview1);
//
////
//
//        btnTakePhoto.setOnClickListener(new btnTakePhotoClicker());
////        Button buttonLoadImage = (Button)findViewById(R.id.loadimage);
//        textTargetUri = (TextView)findViewById(R.id.targeturi);
//        targetImage = (ImageView)findViewById(R.id.targetimage);
//        buttonLoadImage.setOnClickListener(new btnAccessPhotoClicker());
        //end_camera

        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

        SubmitButton = (Button) findViewById(R.id.submit);

        NameEditText = (EditText) findViewById(R.id.name);

        ReportEditText = (EditText) findViewById(R.id.Report);

        final RadioGroup rg = (RadioGroup) findViewById(R.id.rg_type);


        /* Image Upload */


        // Assign FirebaseStorage instance to storageReference.
        storageReference = FirebaseStorage.getInstance().getReference();

        // Assign FirebaseDatabase instance with root database name.
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

        //Assign ID'S to button.
        ChooseButton = (Button) findViewById(R.id.ButtonChooseImage);

        UploadButton = (Button) findViewById(R.id.ButtonUploadImage);

        // Assign ID's to EditText.
        ImageName = (EditText) findViewById(R.id.ImageNameEditText);

        // Assign ID'S to image view.
        SelectImage = (ImageView) findViewById(R.id.ShowImageView);

        // Assigning Id to ProgressDialog.
        progressDialog = new ProgressDialog(MainActivity.this);

        // Adding click listener to Choose image button.



        /* ED_Image Upload */


        SubmitButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                        // 上傳回報內容

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

                         UploadImageFileToFirebaseStorage();


                //end of 上傳回報內容

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

                }

            }
        );


        ChooseButton.setOnClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                // Creating intent.
                                                Intent intent = new Intent();

                                                // Setting intent type as image to select image from phone storage.
                                                intent.setType("image/*");
                                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);

                                            }
                                        });

//        UploadButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                // Calling method to upload selected image on Firebase storage.
//                UploadImageFileToFirebaseStorage();
//
//            }
//        });


    } //end of onCreate


    // get the data from edittext and radiogroup and put it to the Holder.
    public void GetDataFromEditText() {

        final RadioGroup rg = (RadioGroup) findViewById(R.id.rg_type);

        NameHolder = NameEditText.getText().toString().trim();
        ReportHolder = ReportEditText.getText().toString().trim();
        Lati_Holder = latitude;
        Longti_Holder = longtitude;

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

    // end of GetDarafromEdittext


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
        if (mLastLocation != null) {

            longtitude = mLastLocation.getLongitude();
            latitude = mLastLocation.getLatitude();

            mLatitudeText.setText(String.format("%s: %f", mLatitudeLabel, latitude));
            mLongitudeText.setText(String.format("%s: %f", mLongitudeLabel, longtitude));
        } else {
            Toast.makeText(this, "偵測不到定位，請確認定位功能已開啟。", Toast.LENGTH_LONG).show();
        }
    }

    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {

                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);

                // Setting up bitmap selected image into ImageView.
                SelectImage.setImageBitmap(bitmap);

                // After selecting image change choose button above text.
                ChooseButton.setText("Image Selected");

            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    // Creating UploadImageFileToFirebaseStorage method to upload image on storage.
    public void UploadImageFileToFirebaseStorage() {

        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {

            // Setting progressDialog Title.
            progressDialog.setTitle("Image is Uploading...");

            // Showing progressDialog.
            progressDialog.show();

            // Creating second StorageReference.
            StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Getting image name from EditText and store into string variable.
                            String TempImageName = ImageName.getText().toString().trim();

                            // Hiding the progressDialog after done uploading.
                            progressDialog.dismiss();

                            // Showing toast message after done uploading.
                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();

                            @SuppressWarnings("VisibleForTests")
                            imageUploadInfo imageUploadInfo = new imageUploadInfo(TempImageName, taskSnapshot.getDownloadUrl().toString());

                            // Getting image upload ID.
                            String ImageUploadId = databaseReference.push().getKey();

                            // Adding image upload id s child element into databaseReference.
                            databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
                            progressDialog.dismiss();

                            // Showing exception erro message.
                            Toast.makeText(MainActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            // Setting progressDialog Title.
                            progressDialog.setTitle("Image is Uploading...");

                        }
                    });
        }
        else {

            Toast.makeText(MainActivity.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }


} // end of project




//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // TODO Auto-generated method stub
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == CAM_REQUEST)
//        {
//            Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/image.jpg");
//            imgTakenPhoto.setImageBitmap(bitmap);
//        }
//
//    }


//    class btnTakePhotoClicker implements Button.OnClickListener
//    {
//
//        @Override
//        public void onClick(View v) {
//            // TODO Auto-generated method stub
//            Intent cameraintent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//            File tmpFile = new File(Environment.getExternalStorageDirectory(),"image.jpg");
//            Uri outputFileUri = Uri.fromFile(tmpFile);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//            startActivityForResult(cameraintent, CAM_REQUEST);
//        }
//
//    }


// upload image to firebase method












