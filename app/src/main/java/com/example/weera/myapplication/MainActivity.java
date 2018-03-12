package com.example.weera.myapplication;
        import android.annotation.SuppressLint;
        import android.content.Intent;
        import android.graphics.Color;
        import android.graphics.drawable.ColorDrawable;
        import android.os.Handler;
        import android.support.annotation.NonNull;
        import android.support.constraint.ConstraintLayout;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
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
        import android.widget.ImageView;
        import android.widget.TextView;


        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.ChildEventListener;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.List;
        import java.util.Map;

        import android.os.Bundle;
        import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private View headerView;
    DatabaseReference myRef,mUsersRef;
    private ConstraintLayout layout,layout2;
    private String value;
    private TextView t_status,t_location,t_noti;
    private Button btn_log;
    private RecyclerView recycle;
    private List<log_entrance> result;
    private UserAdapter adapter;
    private TextView email_name;
    private username name;
  android.support.v7.app.ActionBar actionBar;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        mUsersRef = myRef.child("log-entrace");
        result = new ArrayList<>();

        recycle = findViewById(R.id.list);
        recycle.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        recycle.setLayoutManager(llm);
        // createResult();


        adapter = new UserAdapter(result);
        recycle.setAdapter(adapter);
        updateList();
        bindView();


        mAuth = FirebaseAuth.getInstance();



        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    String Users= user.getEmail().toString().trim();
                    name = new username(Users.substring(0,Users.indexOf("@")));

                    email_name.setText(user.getEmail().toString().trim());
                } else {

                }
            }
        };


        myRef.child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                value = String.valueOf(map.get("s_door"));



                btn_log.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        // Code here executes on main thread after user presses button
                        if (value.equals("Unlock")) {
                            myRef.child("status").child("s_door").setValue("Lock");
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @SuppressLint("ResourceAsColor")
                                public void run() {
                                    Toast.makeText(MainActivity.this, "Lock Complete!",
                                            Toast.LENGTH_LONG).show();
                                    // Actions to do after 10 seconds
                                    btn_log.setBackgroundResource(R.drawable.key_press);

                                    layout.setBackgroundResource(R.drawable.main_header_selecter_red);
                                    t_status.setText(value);
                                    actionBar = getSupportActionBar();
                                    actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#D83C70")));

                                }
                            }, 3500);

                        } else {
                            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                            FriendlyMessage data = new FriendlyMessage("Android",name.getName(),timestamp.toString());

                            mUsersRef.push().setValue(data);
                            myRef.child("status").child("s_door").setValue("Unlock");
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    // Actions to do after 10 seconds
                                    Toast.makeText(MainActivity.this, "Unlock Complete!",
                                            Toast.LENGTH_LONG).show();
                                    btn_log.setBackgroundResource(R.drawable.key_unlock);
                                    layout.setBackgroundResource(R.drawable.main_header_selecter);
                                    t_status.setText(value);
                                    actionBar = getSupportActionBar();
                                    actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6AD9BC")));

                                }
                            }, 3500);

                        }
                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }
    private void bindView(){
        t_status = (TextView) findViewById(R.id.textView2);
        btn_log = (Button) findViewById(R.id.push_button);
        t_location = (TextView) findViewById(R.id.t_email);
        t_noti = (TextView) findViewById(R.id.textView3);
        layout = (ConstraintLayout) findViewById(R.id.layout);
        email_name = headerView.findViewById(R.id.t_email);

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
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_log) {


            Intent intent = new Intent(this, List_log.class);
            startActivity(intent);

            // Handle the camera action
        } else if (id == R.id.nav_image) {
            startActivity(new Intent(MainActivity.this, ImagesActivity.class));


        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            finish();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));


        }else if(id == R.id.nav_home){

            return true;


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void createResult() {


        for(int i = 0;i<10;i++){

            result.add(new log_entrance("raspberry","weerachai","12-12-12",""));


        }

    }

    private void updateList(){
        myRef.child("log-entrace").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // log_entrance sh = dataSnapshot.getValue(log_entrance.class);
                // Log.d("data",sh.getName());
                result.add(dataSnapshot.getValue(log_entrance.class));
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //  result.add(dataSnapshot.getValue(log_entrance.class));
                //  adapter.notifyDataSetChanged();
                log_entrance model  = dataSnapshot.getValue(log_entrance.class);

                int index = getItemIndex(model);
                result.set(index,model);
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
    private int getItemIndex(log_entrance log){
        int index=-1;
        for(int i=0;i<result.size();i++){

            if(result.get(i).getTimestamp().equals(log.getTimestamp())) {
                index = 1;
                break;
            }

        }

        return index;

    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


}
