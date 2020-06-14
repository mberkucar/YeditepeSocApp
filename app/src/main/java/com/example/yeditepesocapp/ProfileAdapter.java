package com.example.yeditepesocapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ProfileAdapter extends BaseAdapter {

    private Context context;
    private List<ProfileModel> modelList;

    public ProfileAdapter(Context context, List<ProfileModel> modelList) {
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

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (modelList == null)
            return null;

        final LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.fragment_profile, parent, false);

        TextView nameSurname = (TextView) layout.findViewById(R.id.textViewProfileNameSurname);
        TextView department_name = (TextView) layout.findViewById(R.id.textViewProfileDepartmentName);
        TextView faculty_name = (TextView) layout.findViewById(R.id.textViewProfileFacultyName);


        final ProfileModel profile = modelList.get(position);

        nameSurname.setText(profile.getUser_name());
        department_name.setText(profile.getDepartment_name());
        faculty_name.setText(profile.getFaculty_name());

        return layout;
    }
}
