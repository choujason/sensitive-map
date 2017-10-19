package com.example.po_yeh_chou.textinput_firebase;

/**
 * Created by Po_Yeh_Chou on 18/10/2017.
 */

public class UserDetail {


    private String name;
    private String number;
    private String ReportType;
    private double longtitude;
    private double latitude;


    public UserDetail() {

        // default constructor.

    }


    // user Name

    public String getUserName(){

        return name;
    }


    public void setUserNames(String name){

        this.name = name;

    }

    //user Report

    public String getUserReport(){

        return number;
    }


    public void setUserReport(String number){

        this.number = number;
    }




    //Report type

    public String getReportType(){

        return ReportType;

    }

    public void setReportType(String ReportType){

       this.ReportType = ReportType;

    }


    // gps

    public double getLongtitude(){


        return longtitude;

    }

    public  void setLongtitude(double longtitude){


        this.longtitude = longtitude;

    }

    public double getLatitude(){


        return latitude;

    }

    public  void setLatitude(double latitude){


        this.latitude = latitude;

    }


    }

