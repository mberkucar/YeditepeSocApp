package com.example.yeditepesocapp;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileEntryList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileEntryList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileEntryList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    String TAG = "ProfileEntryList";
    private String user_id, topic_id, topic_name;
    private View myView;
    private Context context;
    private List<ProfileEntryModel> modelList;
    private ListView listViewEntry;
    private TextView textViewEntry;
    private SwipeRefreshLayout refreshLayout;
    private RequestQueue requestQueue;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileEntryList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileEntryList.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileEntryList newInstance(String param1, String param2) {
        ProfileEntryList fragment = new ProfileEntryList();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_profile_entry_list, null);
        Bundle bundle = getArguments();
        user_id = bundle.getString("user_id");




        return myView;
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listViewEntry = ((AppCompatActivity) context).findViewById(R.id.ProfilelistviewEntry);
        textViewEntry = ((AppCompatActivity) context).findViewById(R.id.textViewProfileEntry);
        refreshLayout = ((AppCompatActivity) context).findViewById(R.id.ProfilerefreshEntry);

        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent), Color.BLUE,Color.GREEN);

        modelList = new ArrayList<>();
        user_id = getArguments().getString("user_id");

        Log.i(TAG, "sendRequestÖncesi");
        sendRequest();
        Log.i(TAG, "sendRequestSonrası");

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                modelList.clear();
                sendRequestRefresh();
            }
        });




        Log.d("Volley işlemleri testi", ".............................................");


    }


        // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void sendRequest() {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_ENTRYUSER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                refreshLayout.setRefreshing(false);
                Log.i("onResponse:", response);
                Log.d("Json bilgisi Entries: ", response);

                String status = null, message = null;
                JSONArray entries = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    entries = jsonObject.getJSONArray("entries");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //everything is ok
                if (status.equals("200")) {
                    if (entries.length()==0) {
                        textViewEntry.setText("There is nothing here.");
                    }
                    else{
                        for (int i = 0; i < entries.length(); i++){
                            JSONObject entry;
                            ProfileEntryModel model = new ProfileEntryModel();
                            try {
                                entry = entries.getJSONObject(i);
                                //topic_name = entry.getString("topic_name");
                                model.setTopic_name(entry.getString("topic_name"));
                                model.setEntry_content(entry.getString("entry_content"));
                                model.setEntry_date(entry.getString("entry_date"));
                            } catch (JSONException e) {
                                Log.e("json parse error;", e.getLocalizedMessage());
                            }

                            modelList.add(model);

                        }

                        setAdapter();
                    }



                }

                else {
                    //request başarısız ise
                    Snackbar.make(listViewEntry, message, Snackbar.LENGTH_LONG).show();
                }
                Log.d("Volley işlemleri testi", "request işlemler tamamlandı.........................");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //loading.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);


    }

    private void setAdapter() {

        ProfileEntryAdapter adapter = new ProfileEntryAdapter(getActivity().getApplicationContext(), modelList);
        listViewEntry.setAdapter(adapter);


    }


    private void sendRequestRefresh() {

        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_ENTRYUSER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                refreshLayout.setRefreshing(false);
                Log.d("Json bilgisi Entries: ", response);

                String status = null, message = null;
                JSONArray entries = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    entries = jsonObject.getJSONArray("entries");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (status.equals("200")) {

                    if (entries.length()==0) {
                        textViewEntry.setText("There is nothing here.");
                    }else {

                        for (int i = 0; i < entries.length(); i++) {
                            JSONObject entry;
                            ProfileEntryModel model = new ProfileEntryModel();
                            try {
                                entry = entries.getJSONObject(i);
                                //topic_name = entry.getString("topic_name");
                                model.setTopic_name(entry.getString("topic_name"));
                                model.setEntry_content(entry.getString("entry_content"));
                                model.setEntry_date(entry.getString("entry_date"));
                            } catch (JSONException e) {
                                Log.e("json parse error;", e.getLocalizedMessage());
                            }

                            modelList.add(model);

                        }
                        setAdapter();
                    }

                } else {
                    Snackbar.make(listViewEntry, message, Snackbar.LENGTH_LONG).show();
                }
                Log.d("Volley işlemleri testi", "request işlemler tamamlandı.........................");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                refreshLayout.setRefreshing(false);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }


}
