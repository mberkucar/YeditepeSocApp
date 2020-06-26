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



public class CourseEntryAdapter extends BaseAdapter {

    private Context context;
    private List<CourseEntryModel> modelList;
    private boolean deleteEvent;


    public CourseEntryAdapter(Context context, List<CourseEntryModel> modelList,boolean deleteEntry) {
        this.modelList = modelList;
        this.context = context;
        this.deleteEvent=deleteEntry;
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

        final LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.entry_list_model, parent, false);

        TextView nameSurname = (TextView) layout.findViewById(R.id.textViewEntryNameSurname);
        TextView entry_content = (TextView) layout.findViewById(R.id.textViewModelEntryContent);
        TextView entry_date = (TextView) layout.findViewById(R.id.textViewEntryTime);


        final CourseEntryModel entry = modelList.get(position);

        nameSurname.setText(entry.getUser_name());
        entry_content.setText(entry.getCourse_entry_content());
        entry_date.setText(entry.getCourse_entry_date());


        return layout;
    }

}
