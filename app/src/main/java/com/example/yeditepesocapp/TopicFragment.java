package com.example.yeditepesocapp;

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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
 * {@link TopicFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TopicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopicFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private String user_id, topic_id, page="2";
    private Context context;
    private List<TopicModel> modelList;
    //private RecyclerView recyclerView;
    private ListView listViewTopic;
    private TextView textViewTopic;
    private SwipeRefreshLayout refreshLayout;

    private FloatingActionButton fabCreateTopic;
    public View myView;

    private OnFragmentInteractionListener mListener;

    public TopicFragment() {
        // Required empty public constructor
    }

    public static TopicFragment newInstance(String param1, String param2) {
        TopicFragment fragment = new TopicFragment();

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
        myView = inflater.inflate(R.layout.fragment_topic, null);
        fabCreateTopic = (FloatingActionButton) myView.findViewById(R.id.floatingActionButtonCreateTopic);

        Bundle bundle = getArguments();
        user_id = bundle.getString("user_id");
        //page = bundle.getString("page");

        fabCreateTopic.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(getActivity(),CreateTopic.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("page", page);
                getActivity().startActivity(intent);
            }}
        );

        return myView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listViewTopic = (ListView) ((AppCompatActivity) context).findViewById(R.id.listviewTopic);
        textViewTopic = (TextView) ((AppCompatActivity) context).findViewById(R.id.textViewTopic);
        refreshLayout = (SwipeRefreshLayout) ((AppCompatActivity) context).findViewById(R.id.refreshTopic);

        // refreshLayout a 3 tane renk değeri veriyoruz. İşlem uzadıkçe sırayla verilen renk değerlerini alacak
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent), Color.BLUE,Color.GREEN);

        modelList = new ArrayList<>();
        user_id = getArguments().getString("user_id");
        Log.i("TopicFragment user_id", user_id);
        //recyclerView.setHasFixedSize(true);
        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        //recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());

        sendRequest();


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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private void sendRequest() {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_TOPIC, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //loading.dismiss();
                Log.d("Json bilgisi Topics: ", response);

                String status = null, message = null;
                JSONArray topics = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    topics = jsonObject.getJSONArray("topics");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //everything is ok
                if (status.equals("200")) {
                    if (topics.length()==0) {
                        textViewTopic.setText("There is nothing here.");
                    }
                    else{
                        for (int i = 0; i < topics.length(); i++){
                            JSONObject topic;
                            TopicModel model = new TopicModel();
                            try {
                                topic = topics.getJSONObject(i);
                                String userNameSurname = topic.getString("user_name")+
                                        " "+
                                        topic.getString("user_surname");
                                model.setUser_name(userNameSurname);
                                model.setTopic_name(topic.getString("topic_name"));
                                model.setDate(topic.getString("topic_date"));
                                model.setTopic_id(topic.getString("topic_id"));
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
                    Snackbar.make(listViewTopic, message, Snackbar.LENGTH_LONG).show();
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

        listViewTopic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TopicModel info = (TopicModel)listViewTopic.getItemAtPosition(position);
                Log.i("onClickItem", info.getTopic_id());
                topic_id = info.getTopic_id();
                Intent intent = new Intent(getActivity(),EntryActivity.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("topic_id", topic_id);
                getActivity().startActivity(intent);
            }
        });


    }
    private void setAdapter() {

        //RecyclerView.Adapter mAdapter = new MyAdapter(modelList,context);
        //recyclerView.setAdapter(mAdapter);
        TopicAdapter adapter = new TopicAdapter(context, modelList);
        listViewTopic.setAdapter(adapter);


    }
    private void sendRequestRefresh() {

        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_TOPIC, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                refreshLayout.setRefreshing(false);
                Log.d("Json bilgisi Topics: ", response);

                String status = null, message = null;
                JSONArray topics = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    topics = jsonObject.getJSONArray("topics");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (status.equals("200")) {

                    if (topics.length()==0) {
                        textViewTopic.setText("There is nothing here.");
                    }else {

                        for (int i = 0; i < topics.length(); i++) {
                            JSONObject topic;
                            TopicModel model = new TopicModel();
                            try {
                                topic = topics.getJSONObject(i);
                                String userNameSurname = topic.getString("user_name")+
                                        " "+
                                        topic.getString("user_surname");
                                model.setUser_name(userNameSurname);
                                model.setTopic_id("topic_id");
                                model.setTopic_name(topic.getString("topic_name"));
                                model.setDate(topic.getString("topic_date"));
                            } catch (JSONException e) {
                                Log.e("json parse error;", e.getLocalizedMessage());
                            }

                            modelList.add(model);

                        }
                        setAdapter();
                    }

                } else {
                    Snackbar.make(listViewTopic, message, Snackbar.LENGTH_LONG).show();
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
