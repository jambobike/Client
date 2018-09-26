package com.example.alvin.volleytrial;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Users> users;
    private Adapter adapter;
    ProgressBar progressBar;
    private static final String TAG = SearchFragment.class.getSimpleName();
    private static String GET_USERS = "https://sthsth.000webhostapp.com/items/users.php";


    public SearchFragment(){

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        users = new ArrayList<>();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        progressBar = view.findViewById(R.id.progress);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        Adapter adapter = new Adapter(getContext(), users);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        fetchUsers();
//        recyclerView = view.findViewById(R.id.recycler);
//        layoutManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setHasFixedSize(true);
//        adapter = new Adapter();
//        recyclerView.setAdapter(adapter);
//
//        fetchUsers("");//without keyword

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Search");
    }

    public void fetchUsers(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, GET_USERS,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        progressDialog.dismiss();
                        Log.i(TAG, response.toString());

                        try {
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++){

                                JSONObject jsonObject = array.getJSONObject(i);
                                users.add(new Users(
                                        jsonObject.getInt("id"),
                                        jsonObject.getString("name").trim(),
                                        jsonObject.getString("email").trim(),
                                        jsonObject.getString("photo")
                                ));

                            }
                            Adapter adapter = new Adapter(getContext(), users);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast toast = Toast.makeText(
                                    //getActivity(),"Custom Toast From Fragment",Toast.LENGTH_LONG
                                    getActivity().getApplicationContext(), "Error Reading Detail "+e.toString(), Toast.LENGTH_LONG
                            );
                            // Set the Toast display position layout center
                            toast.setGravity(Gravity.CENTER,0,0);
                            // Finally, show the toast
                            toast.show();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast toast = Toast.makeText(
                                //getActivity(),"Custom Toast From Fragment",Toast.LENGTH_LONG
                                getActivity().getApplicationContext(), "Error Reading Detail "+error.toString(), Toast.LENGTH_LONG
                        );
                        // Set the Toast display position layout center
                        toast.setGravity(Gravity.CENTER,0,0);
                        // Finally, show the toast
                        toast.show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}
