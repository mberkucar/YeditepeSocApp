package com.example.yeditepesocapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class CourseAdapter extends BaseAdapter {
    private Context context;
    private List<CourseModel> modelList;

    public CourseAdapter(Context context, List<CourseModel> modelList) {
        this.context = context;
        this.modelList = modelList;
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

        final LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.course_list_model, parent, false);

        TextView courseName = (TextView) layout.findViewById(R.id.CourseName);


        final CourseModel course = modelList.get(position);

        courseName.setText(course.getCourse_name());

        return layout;
    }
}
