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



public class EventAdapter extends BaseAdapter {

    private Context context;
    private List<EventModel> modelList;
    private boolean deleteEvent;


    public EventAdapter(Context context, List<EventModel> modelList,boolean deleteEvent) {
        this.modelList = modelList;
        this.context = context;
        this.deleteEvent=deleteEvent;
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

        final LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.event_list_model, parent, false);

        TextView nameSurname = (TextView) layout.findViewById(R.id.textViewNameSurname);
        TextView event_name = (TextView) layout.findViewById(R.id.textViewEventName);
        TextView event_date = (TextView) layout.findViewById(R.id.textViewEventTime);
        TextView event_body = (TextView) layout.findViewById(R.id.textViewModelEventBody);
        TextView event_location = (TextView) layout.findViewById(R.id.textViewEventLocation);


        final EventModel event = modelList.get(position);

        nameSurname.setText(event.getUser_name());
        event_name.setText(event.getEvent_name());
        event_body.setText(event.getEvent_body());
        event_date.setText(event.getEvent_date());
        event_location.setText(event.getEvent_location());


        return layout;
    }

}
