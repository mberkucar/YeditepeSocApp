package com.example.yeditepesocapp;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
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
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private String user_id, page="5";
    public View myView;
    private FragmentActivity myContext;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView textViewNameSurname, textViewDepartment, textViewFaculty;
    private String NameSurname, Department, Faculty;
    private Context context;
    private List<ProfileModel> modelList;
    private RequestQueue requestQueue;
    ProfileOpinionList profileOpinionList;
    ProfileEntryList profileEntryList;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        user_id = bundle.getString("user_id");

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        user_id = bundle.getString("user_id");
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_profile, null);
        final ViewPager viewPager = myView.findViewById(R.id.ViewPager);
        setupViewPager(viewPager);
        final TabLayout tabLayout = myView.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        //final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getFragmentManager(), myContext, bundle);
        //viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());//setting current selected item over viewpager
                switch (tab.getPosition()) {
                    case 0:
                        Log.e("ProfileFragment","Entries");
                        break;
                    case 1:
                        Log.e("ProfileFragment","Opinions");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        Log.i("ProfileFragment", user_id);

        textViewNameSurname = (TextView)  myView.findViewById(R.id.textViewProfileNameSurname);
        textViewFaculty = (TextView)  myView.findViewById(R.id.textViewProfileFacultyName);
        textViewDepartment = (TextView)  myView.findViewById(R.id.textViewProfileDepartmentName);
        requestQueue = (RequestQueue) Volley.newRequestQueue(getActivity().getApplicationContext());

        sendRequest();

        Log.d("Volley işlemleri testi", ".............................................");
        //Log.i("ProfileFragment", NameSurname);



        return myView;
    }

    private void setupViewPager(ViewPager viewPager) {
        Bundle bundle = new Bundle();
        bundle.putString("user_id", user_id);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(), bundle);
        adapter.addFrag(new ProfileEntryList(), "ENTRIES");
        adapter.addFrag(new ProfileOpinionList(), "OPINIONS");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //sendRequest();

    }

    @Override
    public void onAttach(Context context) {
        myContext=(FragmentActivity) context;
        super.onAttach(context);

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
        StringRequest stringRequest  = new StringRequest(Request.Method.POST, Constants.URL_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //loading.dismiss();
                Log.d("Json bilgisi User: ", response);

                String status = null, message = null;
                JSONArray user = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    user = jsonObject.getJSONArray("userInfo");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //everything is ok
                if (status.equals("200")) {
                    if (user.length()==0) {
                    }
                    else{
                            JSONObject userInfo;
                            try {
                                userInfo = user.getJSONObject(0);
                                NameSurname = userInfo.getString("user_name")+
                                        " "+
                                        userInfo.getString("user_surname");
                                Faculty = userInfo.getString("faculty_name");
                                Department = userInfo.getString("department_name");
                                textViewNameSurname.setText(NameSurname);
                                textViewFaculty.setText(Faculty);
                                textViewDepartment.setText(Department);
                            } catch (JSONException e) {
                                Log.e("json parse error;", e.getLocalizedMessage());
                            }

                        //setAdapter();
                    }



                }

                else {
                    //request başarısız ise
                    Log.w("ProfileFragment", "RequestFail");
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

        requestQueue.add(stringRequest);
    }

}
