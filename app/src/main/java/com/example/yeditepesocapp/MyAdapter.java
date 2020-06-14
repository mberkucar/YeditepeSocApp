package com.example.yeditepesocapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<OpinionModel> modelList;
    private Context context;

    public MyAdapter(List<OpinionModel> modelList,Context c) {
        this.modelList = modelList;
        this.context=c;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LinearLayout rootView = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.opinion_list_model, parent, false);

        ViewHolder vh = new ViewHolder(rootView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        OpinionModel opinion = modelList.get(position);
        holder.opinion_date.setText(opinion.getDate());
        holder.opinion_body.setText(opinion.getOpinion_body());
        holder.user_name.setText(opinion.getUser_name());


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
            holder.opinion_date.setText("now");

        if (second>0 && minute==0)
            holder.opinion_date.setText(second+"s");

        if (minute>0 && hour==0)
            holder.opinion_date.setText(minute+"min");

        if (hour>0 && day==0)
            holder.opinion_date.setText(hour+"h");

        if (day>0)
            holder.opinion_date.setText(day+"day");


        Log.d("pozisyon:::::::: ",String.valueOf(position));


    }

    @Override
    public int getItemCount() {

        //Listedeki item ların sayısını belirtiyoruz
        if (modelList == null)
            return 0;
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView user_name, user_surname, opinion_date, opinion_body;

        public ViewHolder(View itemView) {
            super(itemView);

            user_name = (TextView) itemView.findViewById(R.id.NameSurname);
            opinion_date = (TextView) itemView.findViewById(R.id.textViewTime);
            opinion_body = (TextView) itemView.findViewById(R.id.textViewBody);
        }
    }
}