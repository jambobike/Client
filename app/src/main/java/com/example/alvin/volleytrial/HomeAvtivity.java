package com.example.alvin.volleytrial;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

public class HomeAvtivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView name, email, fullname, emailaddr;
    private Button btn_logout;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_avtivity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        name = findViewById(R.id.name);
        fullname = findViewById(R.id.fullname);
        emailaddr = findViewById(R.id.emailaddr);

//        email = findViewById(R.id.email);
//        btn_logout = findViewById(R.id.btn_logout);

        HashMap<String, String> user = sessionManager.getUserDetail();
        String mName = user.get(sessionManager.NAME);
        String mEmail = user.get(sessionManager.EMAIL);

//        name.setText(mName);
//        email.setText(mEmail);
        TextView txtUser = (TextView) navigationView.getHeaderView(0).findViewById(R.id.fullname);
        txtUser.setText(mName);
        TextView txtEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.emailaddr);
        txtEmail.setText(mEmail);

//        Intent intent = getIntent();
//        String extraName = intent.getStringExtra("name");
//        String extraEmail = intent.getStringExtra("email");

//        name.setText(extraName);
//        email.setText(extraEmail);

//        btn_logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                sessionManager.logout();
//            }
//        });

        displaySelectedScreen(R.id.nav_home);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_avtivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displaySelectedScreen(int id){
        Fragment fragment = null;

        switch(id){
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;
            case R.id.nav_search:
                fragment = new SearchFragment();
                break;
            case R.id.nav_slideshow:
                break;
            case R.id.nav_manage:
                fragment = new UpdateFragment();
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_send:
                sessionManager.logout();
                break;
        }
        if(fragment != null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_home, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displaySelectedScreen(id);

        return true;
    }
}
