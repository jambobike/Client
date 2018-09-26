package com.example.alvin.volleytrial;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.alvin.volleytrial.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.support.v4.media.MediaBrowserServiceCompat.RESULT_OK;

public class UpdateFragment extends Fragment {
    private EditText name, email;
    CircleImageView profile_image;
    private static final String TAG = UpdateFragment.class.getSimpleName();
    Button btn_update, btn_photo;
    SessionManager sessionManager;
    String getId;
    private Bitmap bitmap;
    private static String URL_READ = "https://sthsth.000webhostapp.com/ANDROID_REGISTER_LOGIN/read_detail.php";
    private static String URL_UPDATE = "https://sthsth.000webhostapp.com/ANDROID_REGISTER_LOGIN/update_detail.php";
    private static String URL_UPLOAD = "https://sthsth.000webhostapp.com/ANDROID_REGISTER_LOGIN/upload.php";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_update, container, false);


        sessionManager = new SessionManager(getContext());
        sessionManager.checkLogin();

        email = view.findViewById(R.id.textPersonEmail);
        name = view.findViewById(R.id.textPersonName);
        btn_photo = view.findViewById(R.id.btn_photo);
        btn_update = view.findViewById(R.id.btn_update);
        profile_image = view.findViewById(R.id.profile_image);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveDetail();
            }
        });

        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFile();
            }
        });

        // Inflate the layout for this fragment
        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("My Account");
    }

    private void getUserDetail(){

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG, response.toString());

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (success.equals("1")){
                                for (int i =0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String strName = object.getString("name").trim();
                                    String strEmail = object.getString("email").trim();

                                    name.setText(strName);
                                    email.setText(strEmail);
                                }
                            }
                        }catch (JSONException e){
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
                new Response.ErrorListener() {
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
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", getId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserDetail();
    }

    private void SaveDetail(){

        final String name = this.name.getText().toString().trim();
        final String email = this.email.getText().toString().trim();
        final String id = getId;

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Saving..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast toast = Toast.makeText(
                                        //getActivity(),"Custom Toast From Fragment",Toast.LENGTH_LONG
                                        getActivity().getApplicationContext(), "Success! ".toString(), Toast.LENGTH_LONG
                                );
                                // Set the Toast display position layout center
                                toast.setGravity(Gravity.CENTER,0,0);
                                // Finally, show the toast
                                toast.show();
                                sessionManager.createSession(name,email,id);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast toast = Toast.makeText(
                                    //getActivity(),"Custom Toast From Fragment",Toast.LENGTH_LONG
                                    getActivity().getApplicationContext(), "Error Updating Data "+e.toString(), Toast.LENGTH_LONG
                            );
                            // Set the Toast display position layout center
                            toast.setGravity(Gravity.CENTER,0,0);
                            // Finally, show the toast
                            toast.show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast toast = Toast.makeText(
                                //getActivity(),"Custom Toast From Fragment",Toast.LENGTH_LONG
                                getActivity().getApplicationContext(), "Error Updating Data "+error.toString(), Toast.LENGTH_LONG
                        );
                        // Set the Toast display position layout center
                        toast.setGravity(Gravity.CENTER,0,0);
                        // Finally, show the toast
                        toast.show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", getId);
                params.put("name", name);
                params.put("email", email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void chooseFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri filePath = data.getData();
            try{
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                profile_image.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }

            UploadPicture(getId, getStringImage(bitmap));
        }
    }

    private void UploadPicture(final String id, final String photo){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG, response.toString());

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast toast = Toast.makeText(
                                        //getActivity(),"Custom Toast From Fragment",Toast.LENGTH_LONG
                                        getActivity().getApplicationContext(), "Success!", Toast.LENGTH_LONG
                                );
                                // Set the Toast display position layout center
                                toast.setGravity(Gravity.CENTER,0,0);
                                // Finally, show the toast
                                toast.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast toast = Toast.makeText(
                                    //getActivity(),"Custom Toast From Fragment",Toast.LENGTH_LONG
                                    getActivity().getApplicationContext(), "Try Again! "+e.toString(), Toast.LENGTH_LONG
                            );
                            // Set the Toast display position layout center
                            toast.setGravity(Gravity.CENTER,0,0);
                            // Finally, show the toast
                            toast.show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast toast = Toast.makeText(
                                //getActivity(),"Custom Toast From Fragment",Toast.LENGTH_LONG
                                getActivity().getApplicationContext(), "Try Again! "+error.toString(), Toast.LENGTH_LONG
                        );
                        // Set the Toast display position layout center
                        toast.setGravity(Gravity.CENTER,0,0);
                        // Finally, show the toast
                        toast.show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("photo", photo);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray =  byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodedImage;
    }


}
