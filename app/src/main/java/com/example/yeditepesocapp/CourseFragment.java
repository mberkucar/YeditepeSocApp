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
public class CourseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private String user_id, course_id, course_name, page="2";
    private Context context;
    private List<CourseModel> modelList;
    private ListView listViewCourse;
    private TextView textViewCourse;
    private SwipeRefreshLayout refreshLayout;

    private FloatingActionButton fabCreateCourse;
    public View myView;

    private OnFragmentInteractionListener mListener;

    public CourseFragment() {
        // Required empty public constructor
    }

    public static CourseFragment newInstance(String param1, String param2) {
        CourseFragment fragment = new CourseFragment();

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
        myView = inflater.inflate(R.layout.fragment_course, null);
        fabCreateCourse = (FloatingActionButton) myView.findViewById(R.id.floatingActionButtonCreateCourse);

        Bundle bundle = getArguments();
        user_id = bundle.getString("user_id");
        //page = bundle.getString("page");

        fabCreateCourse.setOnClickListener(new View.OnClickListener(){
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

        listViewCourse = (ListView) ((AppCompatActivity) context).findViewById(R.id.listviewCourse);
        textViewCourse = (TextView) ((AppCompatActivity) context).findViewById(R.id.textViewCourse);
        refreshLayout = (SwipeRefreshLayout) ((AppCompatActivity) context).findViewById(R.id.refreshCourse);

        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent), Color.BLUE,Color.GREEN);

        modelList = new ArrayList<>();
        user_id = getArguments().getString("user_id");
        Log.i("CourseFragment user_id", user_id);

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
        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_COURSE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //loading.dismiss();
                Log.d("Json bilgisi Courses: ", response);

                String status = null, message = null;
                JSONArray courses = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    courses = jsonObject.getJSONArray("courses");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //everything is ok
                if (status.equals("200")) {
                    if (courses.length()==0) {
                        textViewCourse.setText("There is nothing here.");
                    }
                    else{
                        for (int i = 0; i < courses.length(); i++){
                            JSONObject course;
                            CourseModel model = new CourseModel();
                            try {
                                course = courses.getJSONObject(i);
                                String userNameSurname = course.getString("user_name")+
                                        " "+
                                        course.getString("user_surname");
                                model.setUser_name(userNameSurname);
                                model.setCourse_name(course.getString("course_name"));
                                model.setCourse_date(course.getString("course_date"));
                                model.setCourse_id(course.getString("course_id"));
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
                    Snackbar.make(listViewCourse, message, Snackbar.LENGTH_LONG).show();
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

        listViewCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CourseModel info = (CourseModel)listViewCourse.getItemAtPosition(position);
                Log.i("onClickItem", info.getCourse_id());
                course_id = info.getCourse_id();
                course_name = info.getCourse_name();
                Intent intent = new Intent(getActivity(),CourseEntryActivity.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("course_id", course_id);
                intent.putExtra("course_name", course_name);
                getActivity().startActivity(intent);
            }
        });


    }
    private void setAdapter() {

        CourseAdapter adapter = new CourseAdapter(context, modelList);
        listViewCourse.setAdapter(adapter);

    }
    private void sendRequestRefresh() {

        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_COURSE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                refreshLayout.setRefreshing(false);
                Log.d("Json bilgisi Courses: ", response);


                String status = null, message = null;
                JSONArray courses = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    courses = jsonObject.getJSONArray("courses");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //everything is ok
                if (status.equals("200")) {
                    if (courses.length()==0) {
                        textViewCourse.setText("There is nothing here.");
                    }
                    else{
                        for (int i = 0; i < courses.length(); i++){
                            JSONObject course;
                            CourseModel model = new CourseModel();
                            try {
                                course = courses.getJSONObject(i);
                                String userNameSurname = course.getString("user_name")+
                                        " "+
                                        course.getString("user_surname");
                                model.setUser_name(userNameSurname);
                                model.setCourse_name(course.getString("course_name"));
                                model.setCourse_date(course.getString("course_date"));
                                model.setCourse_id(course.getString("course_id"));
                            } catch (JSONException e) {
                                Log.e("json parse error;", e.getLocalizedMessage());
                            }

                            modelList.add(model);

                        }

                        setAdapter();
                    }

                } else {
                    Snackbar.make(listViewCourse, message, Snackbar.LENGTH_LONG).show();
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
