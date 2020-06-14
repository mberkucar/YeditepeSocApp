package com.example.yeditepesocapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateEvent extends AppCompatActivity {

    public EditText selectDate, selectTime;
    private EditText eventName, eventBody, eventLocation;
    private Context context = this;
    private FloatingActionButton fabCreateEvent, fabHome;
    private RequestQueue requestQueue;
    private String user_id, eventDate, page;
    private SharedPreferences preferences;
    private Button selectDateButton, selectTimeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            user_id = bundle.getString("user_id");
            page = bundle.getString("page");
        }
        Log.i("CreateEvent", user_id);
        eventName = (EditText) findViewById(R.id.editTextCreateEventName);
        eventBody = (EditText) findViewById(R.id.editTextCreateEventBody);
        eventLocation = (EditText) findViewById(R.id.editTextCreateEventLocation);


        selectDate = (EditText) findViewById(R.id.editTextselectDate);
        selectTime = (EditText) findViewById(R.id.editTextselectTime);
        selectDateButton = (Button) findViewById(R.id.selectDateButton);
        selectTimeButton = (Button) findViewById(R.id.selectTimeButton);

        fabCreateEvent = (FloatingActionButton) findViewById(R.id.floatingActionButtonCreatedEvent);
        fabHome = (FloatingActionButton) findViewById(R.id.floatingActionButtonEvent);

        preferences = PreferenceManager.getDefaultSharedPreferences(CreateEvent.this);
        requestQueue = Volley.newRequestQueue(CreateEvent.this);

        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Şimdiki zaman bilgilerini alıyoruz. güncel yıl, güncel ay, güncel gün.
                final Calendar takvim = Calendar.getInstance();
                int year = takvim.get(Calendar.YEAR);
                int month = takvim.get(Calendar.MONTH);
                int day = takvim.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month += 1;
                                selectDate.setText(year + "-" + month + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Select", dpd);
                dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancel", dpd);
                dpd.show();
                Log.i("CreateEvent", selectDate.getText().toString());
            }
        });
        selectTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Şimdiki zaman bilgilerini alıyoruz. güncel saat, güncel dakika.
                Format formatter = new SimpleDateFormat("hh:mm");
                final Calendar calendar = Calendar.getInstance();
                formatter.format(calendar.getTime());
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog tpd = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                selectTime.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, true);
                tpd.setButton(TimePickerDialog.BUTTON_POSITIVE, "Select", tpd);
                tpd.setButton(TimePickerDialog.BUTTON_NEGATIVE, "Cancel", tpd);
                tpd.show();
                Log.i("CreateEvent", selectTime.getText().toString());
            }
        });
        fabCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventDate = selectDate.getText().toString() +
                        " " +
                        selectTime.getText().toString();
                Log.i("CreateEvent", eventDate);

                if (eventName.getText().toString().trim().length() != 0 ){
                    if(eventBody.getText().toString().trim().length() != 0 ) {
                        if(eventLocation.getText().toString().trim().length() != 0){
                            SendRequest();
                            Snackbar.make(findViewById(R.id.floatingActionButtonCreatedEvent), "Opinion is saved.", Snackbar.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            intent.putExtra("user_id", user_id);
                            intent.putExtra("page", page);
                            startActivity(intent);
                        }
                        else
                            Snackbar.make(findViewById(R.id.floatingActionButtonCreatedEvent), "Please write your event location.", Snackbar.LENGTH_LONG).show();
                    }
                    else
                        Snackbar.make(findViewById(R.id.floatingActionButtonCreatedEvent), "Please write description of your event.", Snackbar.LENGTH_LONG).show();
                }
                else{
                    Snackbar.make(findViewById(R.id.floatingActionButtonCreatedEvent), "There is nothing here for sending.", Snackbar.LENGTH_LONG).show();
                }


            }
        });

        fabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("page", page);
                startActivity(intent);
            }
        });
    }
    private void SendRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_CREATE_EVENT, new Response.Listener<String>() {
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
                    Snackbar.make(findViewById(R.id.floatingActionButtonCreatedEvent), "", Snackbar.LENGTH_LONG).show();
                    //opinion.setText("");
                }
                else{
                    Snackbar.make(findViewById(R.id.floatingActionButtonCreatedEvent), message, Snackbar.LENGTH_LONG).show();
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
                //String uuid = UUID.randomUUID().toString();
                //parametreler ekleniyor
                Log.i("InMap","Params");
                Log.i("InMap",String.valueOf(user_id));
                params.put("event_name", eventName.getText().toString());
                params.put("event_body", eventBody.getText().toString());
                params.put("event_location", eventLocation.getText().toString());
                params.put("event_date", eventDate);
                params.put("user_id", String.valueOf(user_id));

                return params;
            }
        };

        requestQueue.add(stringRequest);

    }
}
