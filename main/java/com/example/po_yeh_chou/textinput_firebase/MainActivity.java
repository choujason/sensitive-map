package com.example.po_yeh_chou.textinput_firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Button SubmitButton;

    EditText NameEditText, ReportEditText, RepoetType;

    // Declaring String variable ( In which we are storing firebase server URL ).
//    public static final String Firebase_Server_URL = "https://insertdata-android-examples.firebaseio.com/";

    // Declaring String variables to store name & phone number get from EditText.
    String NameHolder, ReportHolder, ReportTypeHolder ;


    DatabaseReference databaseReference;

    // Root Database Name for Firebase Database.
    public static final String Database_Path = "UserReport_Details_Database";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

        SubmitButton = (Button) findViewById(R.id.submit);

        NameEditText = (EditText) findViewById(R.id.name);

        ReportEditText = (EditText) findViewById(R.id.Report);

//        type = (RadioGroup) findViewById(R.id.rg_type);

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


                // Getting the ID from firebase database.
                String UserRecordIDFromServer = databaseReference.push().getKey();


                //如果輸入內容是空的，跳出通知並停止
                if( NameHolder.isEmpty()){
                    Toast.makeText(MainActivity.this, "請輸入您的姓名代號", Toast.LENGTH_LONG).show();
                    return ;
                }


                // Adding the both name and number values using student details class object using ID.
                databaseReference.child(UserRecordIDFromServer).setValue(userDetail);



                // Showing Toast message after successfully data submit.
                Toast.makeText(MainActivity.this, "Data Inserted Successfully into Firebase Database", Toast.LENGTH_LONG).show();


                //回報成功後，清空資料
                NameEditText.getText().clear();
                ReportEditText.getText().clear();
                rg.clearCheck();


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

        switch (rg.getCheckedRadioButtonId()) {
            case R.id.visual:
                ReportTypeHolder=("視覺回報");
                break;
            case R.id.audio:
                ReportTypeHolder=("聽覺回報");
                break;
            case R.id.smell:
                ReportTypeHolder=("嗅覺回報");
                break;
            case R.id.other:
                ReportTypeHolder=("其他回報");
                break;
            default :
            Toast.makeText(MainActivity.this, "請輸入回報種類", Toast.LENGTH_LONG).show();


            }

        }


    }




