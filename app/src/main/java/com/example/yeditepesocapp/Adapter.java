package com.example.yeditepesocapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Adapter extends BaseAdapter {

    private Context context;
    private List<OpinionModel> modelList;
    private boolean deleteOpinion;


    public Adapter(Context context, List<OpinionModel> modelList,boolean deleteOpinion) {
        this.modelList = modelList;
        this.context = context;
        this.deleteOpinion=deleteOpinion;
    }

    @Override
    public int getCount() {
        if (modelList == null)
            return 0;

        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (modelList == null)
            return null;

        final LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.opinion_list_model, parent, false);

        TextView nameSurname = (TextView) layout.findViewById(R.id.NameSurname);
        TextView opinion_date = (TextView) layout.findViewById(R.id.textViewTime);
        TextView opinion_body = (TextView) layout.findViewById(R.id.textViewBody);

        final OpinionModel opinion = modelList.get(position);

        nameSurname.setText(opinion.getUser_name());
        opinion_body.setText(opinion.getOpinion_body());
        opinion_date.setText(opinion.getDate());

        Date now=new Date();

        DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=null;
        try {
            date=df.parse(opinion.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int difference= (int) (now.getTime()-date.getTime());

        int day=difference/(1000*60*60*24);
        int hour=difference/(1000*60*60);
        int minute=difference/(1000*60);
        int second=difference/(1000);

        if (second==0)
            opinion_date.setText("now");

        if (second>0 && minute==0)
            opinion_date.setText(second+"s");

        if (minute>0 && hour==0)
            opinion_date.setText(minute+"min");

        if (hour>0 && day==0)
            opinion_date.setText(hour+"h");

        if (day>0)
            opinion_date.setText(day+"day");


        return layout;
    }


}
