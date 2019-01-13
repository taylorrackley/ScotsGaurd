package edu.covenant.safety.scotsgaurd;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.GridLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.covenant.safety.scotsgaurd.Model.User;

public class AdminDashboardActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference firebaseDB;

    Button shuttle;
    Button activeChats;

    TextView toolbarUsername;
    GridLayout dashboardMenu;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firebaseDB = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        shuttle = findViewById(R.id.menuBtnShuttle);
        activeChats = findViewById(R.id.menuBtnViewChats);

        toolbarUsername = findViewById(R.id.toolbar_username);
        dashboardMenu = findViewById(R.id.menu_grid);

        firebaseDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

                toolbarUsername.setText(user.getUsername());

                activeChats.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(AdminDashboardActivity.this, ChatsActivity.class);
                        //intent.putExtra("officeId", "rdWxxLygmagtHkj3DRzCcyU4Tak1");
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        // Check for previous login
        if(firebaseUser == null) {
            Intent intent = new Intent(AdminDashboardActivity.this, SignInPageActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AdminDashboardActivity.this, SignInPageActivity.class));
                finish();
                return true;
        }

        return false;
    }
}
