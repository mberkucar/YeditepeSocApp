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
 * {@link OpinionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OpinionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OpinionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private String user_id, page="1";
    private Context context;
    private List<OpinionModel> modelList;
    //private RecyclerView recyclerView;
    private ListView listView;
    private TextView textView;
    private SwipeRefreshLayout refreshLayout;

    private FloatingActionButton fab;
    public View myView;

    private OnFragmentInteractionListener mListener;

    public OpinionFragment() {
        // Required empty public constructor
    }

    public static OpinionFragment newInstance(String param1, String param2) {
        OpinionFragment fragment = new OpinionFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        user_id = bundle.getString("user_id");
        page = bundle.getString("page");

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_opinion, null);
        fab = (FloatingActionButton) myView.findViewById(R.id.floatingActionButtonWriteOpinion);

        Bundle bundle = getArguments();
        user_id = bundle.getString("user_id");
        page = bundle.getString("page");


        fab.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(getActivity(),WriteOpinion.class);
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

        listView = (ListView) ((AppCompatActivity) context).findViewById(R.id.listview);
        textView = (TextView) ((AppCompatActivity) context).findViewById(R.id.textViewOpinion);
        refreshLayout = (SwipeRefreshLayout) ((AppCompatActivity) context).findViewById(R.id.refresh);

        // refreshLayout a 3 tane renk değeri veriyoruz. İşlem uzadıkçe sırayla verilen renk değerlerini alacak
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent), Color.BLUE,Color.GREEN);

        modelList = new ArrayList<>();
        user_id = getArguments().getString("user_id");
        page = getArguments().getString("page");
        Log.i("OpinionFragment user_id", user_id);
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
        Bundle bundle = getArguments();
        user_id = bundle.getString("user_id");
        page = bundle.getString("page");
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        Bundle bundle = getArguments();
        user_id = bundle.getString("user_id");
        page = bundle.getString("page");
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void sendRequest() {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_OPINION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //loading.dismiss();
                Log.d("Json bilgisi Tweetler: ", response);

                String status = null, message = null;
                JSONArray opinions = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    opinions = jsonObject.getJSONArray("opinions");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //everything is ok
                if (status.equals("200")) {
                    if (opinions.length()==0) {
                        textView.setText("There is nothing here.");
                    }
                    else{
                        for (int i = 0; i < opinions.length(); i++){
                            JSONObject opinion;
                            OpinionModel model = new OpinionModel();
                            try {
                                opinion = opinions.getJSONObject(i);
                                String userNameSurname = opinion.getString("user_name")+
                                        " "+
                                        opinion.getString("user_surname");
                                model.setUser_name(userNameSurname);
                                model.setOpinion_body(opinion.getString("opinion_body"));
                                model.setDate(opinion.getString("opinion_date"));
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

        //RecyclerView.Adapter mAdapter = new MyAdapter(modelList,context);
        //recyclerView.setAdapter(mAdapter);
        Adapter adapter = new Adapter(context, modelList,true);
        listView.setAdapter(adapter);


    }
    private void sendRequestRefresh() {

        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_OPINION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                refreshLayout.setRefreshing(false);
                Log.d("Json bilgisi Opinions: ", response);

                String status = null, message = null;
                JSONArray opinions = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    opinions = jsonObject.getJSONArray("opinions");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (status.equals("200")) {

                    if (opinions.length()==0) {
                        textView.setText("There is nothing here.");
                    }else {

                        for (int i = 0; i < opinions.length(); i++) {
                            JSONObject opinion;
                            OpinionModel model = new OpinionModel();
                            try {
                                opinion = opinions.getJSONObject(i);
                                String userNameSurname = opinion.getString("user_name")+
                                        " "+
                                        opinion.getString("user_surname");
                                model.setUser_name(userNameSurname);
                                model.setOpinion_body(opinion.getString("opinion_body"));
                                model.setDate(opinion.getString("opinion_date"));
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
}
