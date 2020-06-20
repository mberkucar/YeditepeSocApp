package com.example.yeditepesocapp;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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
 * {@link EventFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private String user_id, page="4";
    private Context context;
    private List<EventModel> modelList;
    private ListView listView;
    private TextView textView;
    private SwipeRefreshLayout refreshLayout;
    private Fragment fragment = new Fragment();

    private FloatingActionButton fab;
    public View myView;

    private OnFragmentInteractionListener mListener;

    public EventFragment() {
        // Required empty public constructor
    }

    public static EventFragment newInstance(String param1, String param2) {
        EventFragment fragment = new EventFragment();

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
        myView = inflater.inflate(R.layout.fragment_event, null);
        fab = (FloatingActionButton) myView.findViewById(R.id.floatingActionButtonCreateEvent);

        Bundle bundle = getArguments();
        user_id = bundle.getString("user_id");

        fab.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(getActivity(),CreateEvent.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("page", page);
                getActivity().startActivity(intent);
                //getActivity().finish();
            }}
        );

        return myView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView = (ListView) ((AppCompatActivity) context).findViewById(R.id.Eventlistview);
        textView = (TextView) ((AppCompatActivity) context).findViewById(R.id.textViewEvent);
        refreshLayout = (SwipeRefreshLayout) ((AppCompatActivity) context).findViewById(R.id.RefreshEvent);

        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent), Color.BLUE,Color.GREEN);

        modelList = new ArrayList<>();
        user_id = getArguments().getString("user_id");
        Log.i("eventFragment user_id", user_id);

        sendRequest();


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                modelList.clear();
                sendRequestRefresh();
            }
        });
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                setAdapter();
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private void sendRequest() {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_EVENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //loading.dismiss();
                Log.d("Json bilgisi Events: ", response);

                String status = null, message = null;
                JSONArray events = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    events = jsonObject.getJSONArray("events");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //everything is ok
                if (status.equals("200")) {
                    if (events.length()==0) {
                        textView.setText("There is nothing here.");
                    }
                    else{
                        for (int i = 0; i < events.length(); i++){
                            JSONObject event;
                            EventModel model = new EventModel();
                            try {
                                event = events.getJSONObject(i);
                                String userNameSurname = event.getString("user_name")+
                                        " "+
                                        event.getString("user_surname");
                                model.setUser_name(userNameSurname);
                                model.setEvent_id(event.getString("event_id"));
                                model.setEvent_name(event.getString("event_name"));
                                model.setEvent_body(event.getString("event_body"));
                                model.setEvent_location(event.getString("event_location"));
                                model.setEvent_date(event.getString("event_date"));
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
                    Snackbar.make(listView, message, Snackbar.LENGTH_LONG).show();
                }
                Log.d("Volley işlemleri testi", "request işlemler tamamlandı.........................");


                /*if (modelList.size() == 0) {
                   textView.setText("Hiçbir tweet bulunamadı...");
                } else {
                    setAdapter();
                }
*/
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

        //RecyclerView.Adapter mAdapter = new EventAdapter(context, modelList,context);
        //recyclerView.setAdapter(mAdapter);
        final EventAdapter eventAdapter = new EventAdapter(context, modelList,true, user_id);
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                //do your modifications here

                // for example
                //eventAdapter.add(new Object());
                eventAdapter.notifyDataSetChanged();
                listView.setAdapter(eventAdapter);
                eventAdapter.notifyDataSetChanged();
            }
        });


    }
    private void sendRequestRefresh() {

        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_EVENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                refreshLayout.setRefreshing(false);
                Log.d("Json bilgisi Events:: ", response);

                String status = null, message = null;
                JSONArray events = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    events = jsonObject.getJSONArray("events");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (status.equals("200")) {

                    if (events.length()==0) {
                        textView.setText("There is nothing here.");
                    }else {

                        for (int i = 0; i < events.length(); i++) {
                            JSONObject event;
                            EventModel model = new EventModel();
                            try {
                                event = events.getJSONObject(i);
                                String userNameSurname = event.getString("user_name")+
                                        " "+
                                        event.getString("user_surname");
                                model.setUser_name(userNameSurname);
                                model.setEvent_id(event.getString("event_id"));
                                model.setEvent_name(event.getString("event_name"));
                                model.setEvent_body(event.getString("event_body"));
                                model.setEvent_location(event.getString("event_location"));
                                model.setEvent_date(event.getString("event_date"));
                            } catch (JSONException e) {
                                Log.e("json parse error;", e.getLocalizedMessage());
                            }

                            modelList.add(model);

                        }
                        setAdapter();
                    }

                } else {
                    Snackbar.make(listView, message, Snackbar.LENGTH_LONG).show();
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
    public void sendRequestwithButton() {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_REGISTER_EVENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //loading.dismiss();
                Log.d("Json bilgisi Events: ", response);

                String status = null, message = null;
                JSONArray events = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    events = jsonObject.getJSONArray("events");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //everything is ok
                if (status.equals("200")) {
                    if (events.length()==0) {
                        textView.setText("There is nothing here.");
                    }
                    else{
                        for (int i = 0; i < events.length(); i++){
                            JSONObject event;
                            EventModel model = new EventModel();
                            try {
                                event = events.getJSONObject(i);
                                String userNameSurname = event.getString("user_name")+
                                        " "+
                                        event.getString("user_surname");
                                model.setUser_name(userNameSurname);
                                model.setEvent_id(event.getString("event_id"));
                                model.setEvent_name(event.getString("event_name"));
                                model.setEvent_body(event.getString("event_body"));
                                model.setEvent_location(event.getString("event_location"));
                                model.setEvent_date(event.getString("event_date"));
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
                    Snackbar.make(listView, message, Snackbar.LENGTH_LONG).show();
                }
                Log.d("Volley işlemleri testi", "request işlemler tamamlandı.........................");


                /*if (modelList.size() == 0) {
                   textView.setText("Hiçbir tweet bulunamadı...");
                } else {
                    setAdapter();
                }
*/
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
                //params.put("event_id", event_id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }


}
