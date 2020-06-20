package com.example.yeditepesocapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.nfc.Tag;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class EventAdapter extends BaseAdapter {

    private Context context;
    private List<EventModel> modelList;
    private boolean deleteEvent;
    private String user_id;
    private String TAG = "EventAdapter";
    private ArrayList<String> userEventList = new ArrayList<>();
    //private Button RegisterButton, UnregisterButton;


    public EventAdapter(Context context, List<EventModel> modelList,boolean deleteEvent, String user_id) {
        super();
        this.modelList = modelList;
        this.context = context;
        this.deleteEvent=deleteEvent;
        this.user_id=user_id;
    }
    /*
    class viewHolder{
        TextView nameSurname, event_name, event_date, event_body, event_location;
        Button registerButton, unregisterButton;

        viewHolder(View v){
            nameSurname=(TextView)v.findViewById(R.id.textViewNameSurname);
            event_name=(TextView)v.findViewById(R.id.textViewEventName);
            event_date=(TextView)v.findViewById(R.id.textViewEventTime);
            event_body=(TextView)v.findViewById(R.id.textViewModelEventBody);
            event_location=(TextView)v.findViewById(R.id.textViewEventLocation);
            registerButton = (Button) v.findViewById(R.id.RegisterButton);
            unregisterButton = (Button) v.findViewById(R.id.unregisterButton);

        }


    }*/

    @Override
    public int getCount() {
        if (modelList == null)
            return 0;

        return modelList.size();
    }

    @Override
    public Object getItem(int position){
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
        sendRequestGetEvent();


        convertView = LayoutInflater.from(context).inflate(R.layout.event_list_model, parent, false);
        TextView nameSurname = (TextView) convertView.findViewById(R.id.textViewNameSurname);
        TextView event_name = (TextView) convertView.findViewById(R.id.textViewEventName);
        TextView event_date = (TextView) convertView.findViewById(R.id.textViewEventTime);
        TextView event_body = (TextView) convertView.findViewById(R.id.textViewModelEventBody);
        TextView event_location = (TextView) convertView.findViewById(R.id.textViewEventLocation);
        final TextView event_id = (TextView) convertView.findViewById(R.id.textViewEventId);
        final Button RegisterButton = (Button) convertView.findViewById(R.id.RegisterButton);
        final Button UnregisterButton = (Button) convertView.findViewById(R.id.unregisterButton);


        EventModel event = modelList.get(position);
        notifyDataSetChanged();

        for(int i=0; i<userEventList.size(); i++){
            Log.i(TAG+"userEventList event_id", userEventList.get(i) );
            if (userEventList.get(i)==event.getEvent_id()){
                Log.i(TAG+" in if event_id", event_id.getText().toString() );
                RegisterButton.setVisibility(View.INVISIBLE);
                UnregisterButton.setVisibility(View.VISIBLE);
            }
            else{
                RegisterButton.setVisibility(View.VISIBLE);
                UnregisterButton.setVisibility(View.INVISIBLE);
            }
        }
        notifyDataSetChanged();

        nameSurname.setText(event.getUser_name());
        event_name.setText(event.getEvent_name());
        event_body.setText(event.getEvent_body());
        event_date.setText(event.getEvent_date());
        event_location.setText(event.getEvent_location());
        event_id.setText(event.getEvent_id());
        notifyDataSetChanged();

        Log.i(TAG + "for öncesi event_id", event_id.getText().toString());

        /*for(int i=0; i<userEventList.size(); i++){
            Log.i(TAG+"userEventList event_id", userEventList.get(i) );
            if (userEventList.get(i)==event_id.getText().toString()){
                Log.i(TAG+"in if event_id", event_id.getText().toString() );
                RegisterButton.setVisibility(View.INVISIBLE);
                UnregisterButton.setVisibility(View.VISIBLE);
            }
        }*/


        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer eventNumber = (Integer)v.getTag();
                Log.i(TAG+"position", String.valueOf(position));
                Log.i(TAG+"event_id", event_id.getText().toString());
                //sendRequestwithButton(modelList.get(eventNumber).getEvent_id());
                sendRequestwithButton(event_id.getText().toString(), RegisterButton, UnregisterButton);
            }
        });
        notifyDataSetChanged();

        UnregisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer eventNumber = (Integer)v.getTag();
                Log.i(TAG+"position", String.valueOf(position));
                Log.i(TAG+"event_id", event_id.getText().toString());
                //sendRequestwithButton(modelList.get(eventNumber).getEvent_id());
                sendRequestUnregisrerwithButton(event_id.getText().toString(), RegisterButton, UnregisterButton);
            }
        });
        notifyDataSetChanged();


        /*
        final LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.event_list_model, parent, false);

        TextView nameSurname = (TextView) layout.findViewById(R.id.textViewNameSurname);
        TextView event_name = (TextView) layout.findViewById(R.id.textViewEventName);
        TextView event_date = (TextView) layout.findViewById(R.id.textViewEventTime);
        TextView event_body = (TextView) layout.findViewById(R.id.textViewModelEventBody);
        TextView event_location = (TextView) layout.findViewById(R.id.textViewEventLocation);
        RegisterButton = (Button) layout.findViewById(R.id.RegisterButton);
        UnregisterButton = (Button) layout.findViewById(R.id.unregisterButton);



        final EventModel event = modelList.get(position);

        nameSurname.setText(event.getUser_name());
        event_name.setText(event.getEvent_name());
        event_body.setText(event.getEvent_body());
        event_date.setText(event.getEvent_date());
        event_location.setText(event.getEvent_location());

        event_id = event.getEvent_id();


        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestwithButton();
            }
        });


        return layout;*/
        notifyDataSetChanged();
        return convertView;
    }

    private void sendRequestwithButton(final String event_id, final Button register, final Button unregister) {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_REGISTER_EVENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Json info: ", response);

                String status=null, message=null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (status.equals("200")){
                    register.setVisibility(View.INVISIBLE);
                    unregister.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("event_id", String.valueOf(event_id));
                params.put("user_id", String.valueOf(user_id));

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);

    }

    private void sendRequestUnregisrerwithButton(final String event_id, final Button register, final Button unregister) {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_UNREGISTER_EVENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Json info: ", response);

                String status=null, message=null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (status.equals("200")){
                    unregister.setVisibility(View.INVISIBLE);
                    register.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("event_id", String.valueOf(event_id));
                params.put("user_id", String.valueOf(user_id));

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);

    }

    private void sendRequestGetEvent() {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_GETEVENT_USERID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Json info: ", response);

                String status=null, message=null;
                JSONArray eventids = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    eventids = jsonObject.getJSONArray("eventid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (status.equals("200")){
                    if (eventids.length()==0) {
                        Log.d(TAG, "JsonObject is empty.");
                    }
                    else{
                        for (int i = 0; i < eventids.length(); i++){
                            JSONObject eventid;
                            try {
                                eventid = eventids.getJSONObject(i);
                                String event_id = eventid.getString("event_id");
                                userEventList.add(event_id);

                            }catch (JSONException e) {
                                Log.e("json parse error;", e.getLocalizedMessage());
                            }
                        }


                    }

                }
                else{
                    Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(user_id));

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);

    }


}
