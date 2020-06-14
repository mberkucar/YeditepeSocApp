package com.example.yeditepesocapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends BaseAdapter implements Filterable{

    private Context context;
    private List<SearchUserModel> modelList;
    private List<String> UserList, copyList;




    public SearchAdapter(Context context, List<SearchUserModel> modelList) {
        this.modelList = modelList;
        this.context = context;
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

        final LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.search_user_model, parent, false);

        TextView nameSurname = (TextView) layout.findViewById(R.id.SearchNameSurname);


        final SearchUserModel entry = modelList.get(position);

        nameSurname.setText(entry.getUser_name());

        UserList = new ArrayList<>();

        for (int i=0; i<modelList.size(); i++){

            UserList.add(modelList.get(i).user_name);

        }

        copyList = new ArrayList<String>(UserList);



        return layout;
    }

    @Override
    public Filter getFilter() {
        return searchFilter;
    }

    private Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<String> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(copyList);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (String item : copyList) {
                    if (item.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            UserList.clear();
            UserList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

}
