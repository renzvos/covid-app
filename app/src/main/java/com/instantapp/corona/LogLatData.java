package com.instantapp.corona;

import android.util.Log;

import java.util.ArrayList;

public class LogLatData {
    ArrayList<String> pid;
    ArrayList<String> longlat;
    ArrayList<String> date;
    ArrayList<String> address;
    ArrayList<String> info;
    double plat;
    double plog;
    double lowestt;
    int lowestindex;

    LogLatData(double curlat,double curlog){pid = new ArrayList<String>();
    longlat = new ArrayList<String>();
    date = new ArrayList<String>();
    address = new ArrayList<String>();
    info = new ArrayList<String>();

        Log.i("Distance", "distance: " + curlat + " " +curlog);
        plat = curlat;
        plog = curlog;
    }

    public void addrow (String id,String cord,String date, String address, String info){
        pid.add(id);
        longlat.add(cord);
        this.date.add(date);
        this.address.add(address);
        this.info.add(info);


    }



    public boolean near()
    {
        int radius = 50;
        double lowest = 1000000000;
        for (int i = 0 ;i < longlat.size() ; i++)
        {   String combined = longlat.get(i);
            try {
                String splittd[] = combined.split(",");
                Log.i("Data", "near: " + splittd[0] + " and " + splittd [1]);
                double lat = Double.parseDouble(splittd[0]+"d");
                double log = Double.parseDouble(splittd[1]+"d");
                if (distance(plat,plog,lat,log) < lowest)
                {lowest = distance(plat,plog,lat,log);
                lowestindex = i;
                    Log.i("DataLow", "near: " + lowest);}

            }catch (Exception e){}

        }

        if (lowest <= radius)
        {    lowestt = lowest;
            return true;

        }
        else {return false;}

    }



    private static double distance(double lat1, double lon1, double lat2, double lon2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = 0;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        double dist =  Math.sqrt(distance);
        dist = dist/1000;




            Log.i("Distance", "distance: Distance is" + dist);

            return (dist);
        }



}
