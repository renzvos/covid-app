package com.instantapp.corona;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Map;

public class Casetimedata {
    int count;
    ArrayList<Integer> dc;
    ArrayList<Integer> tc;
    ArrayList<Integer> dr;
    ArrayList<Integer> tr;
    ArrayList<Integer> dd;
    ArrayList<Integer> td;

    Casetimedata(String response)
    {
        dc = new ArrayList<>();
        tc = new ArrayList<>();
        dr = new ArrayList<>();
        tr = new ArrayList<>();
        dd = new ArrayList<>();
        td = new ArrayList<>();

        try {
            JSONArray result = new JSONArray(response);
            count = result.length();
            for (int i = 0; i < count ;i++)
            {
                dc.add(Integer.parseInt(result.getJSONObject(i).getString("dc")));
                tc.add(Integer.parseInt(result.getJSONObject(i).getString("tc")));
                dr.add(Integer.parseInt(result.getJSONObject(i).getString("dr")));
                tr.add(Integer.parseInt(result.getJSONObject(i).getString("tr")));
                dd.add(Integer.parseInt(result.getJSONObject(i).getString("dd")));
                td.add(Integer.parseInt(result.getJSONObject(i).getString("td")));
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public ArrayList<Entry> dc() {
        ArrayList<Entry> result = new ArrayList<>();

        for (int i = 1 ; i < count + 1 ; i++)
        {
            result.add(new Entry(i,dc.get(i-1)));
            Log.i("Chart", "dc: " + i + " " + dc.get(i -1));
        }

    return result;}

    public ArrayList<Entry> dr() {
        ArrayList<Entry> result = new ArrayList<>();

        for (int i = 1 ; i < count + 1 ; i++)
        {
            result.add(new Entry(i,dr.get(i-1)));
            Log.i("Chart", "dr: " + i + " " + dr.get(i -1));
        }

        return result;}

    public ArrayList<Entry> dd() {
        ArrayList<Entry> result = new ArrayList<>();

        for (int i = 1 ; i < count + 1 ; i++)
        {
            result.add(new Entry(i,dd.get(i-1)));
            Log.i("Chart", "dd: " + i + " " + dd.get(i -1));
        }

        return result;}

    public ArrayList<Entry> tc() {
        ArrayList<Entry> result = new ArrayList<>();

        for (int i = 1 ; i < count + 1 ; i++)
        {
            result.add(new Entry(i,tc.get(i-1)));
            Log.i("Chart", "tc: " + i + " " + tc.get(i -1));
        }

        return result;}

    public ArrayList<Entry> tr() {
        ArrayList<Entry> result = new ArrayList<>();

        for (int i = 1 ; i < count + 1 ; i++)
        {
            result.add(new Entry(i,tr.get(i-1)));
            Log.i("Chart", "tr: " + i + " " + tr.get(i -1));
        }

        return result;}

    public ArrayList<Entry> td() {
        ArrayList<Entry> result = new ArrayList<>();

        for (int i = 1 ; i < count + 1 ; i++)
        {
            result.add(new Entry(i,td.get(i-1)));
            Log.i("Chart", "td: " + i + " " + td.get(i -1));
        }

        return result;}





    public float dailybiggest()
    { float biggest  = 0;
    for (int i = 0; i < dc.size() ; i++)
    {
        if (dc.get(i) > biggest){biggest = dc.get(i);}
    }
        for (int i = 0; i < dr.size() ; i++)
        {
            if (dr.get(i) > biggest){biggest = dr.get(i);}
        }

        for (int i = 0; i < dd.size() ; i++)
        {
            if (dd.get(i) > biggest){biggest = dd.get(i);}
        }



    return biggest;}

    public float totalbiggest()
    { float biggest  = 0;
        for (int i = 0; i < tc.size() ; i++)
        {
            if (tc.get(i) > biggest){biggest = tc.get(i);}
        }
        for (int i = 0; i < dr.size() ; i++)
        {
            if (tr.get(i) > biggest){biggest = tr.get(i);}
        }

        for (int i = 0; i < td.size() ; i++)
        {
            if (td.get(i) > biggest){biggest = td.get(i);}
        }



        return biggest;}




}
