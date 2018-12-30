package edu.covenant.safety.scotsgaurd;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Dashboard extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference firebaseDB;

    Button shuttle;
    Button messageSecurity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firebaseDB = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        shuttle = findViewById(R.id.menuBtnShuttle);
        messageSecurity = findViewById(R.id.menuBtnMessageSecurity);

        firebaseDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        messageSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, MessageSecurityActivity.class);
                intent.putExtra("officeId", "rdWxxLygmagtHkj3DRzCcyU4Tak1");
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        // Check for previous login
        if(firebaseUser == null) {
            Intent intent = new Intent(Dashboard.this, SignInPage.class);
            startActivity(intent);
            finish();
        }
    }
}
