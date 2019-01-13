package edu.covenant.safety.scotsgaurd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {

    private Button goToSignIn;
    private Button goToRegister;

    private Intent intent;

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        auth = FirebaseAuth.getInstance();

        firebaseUser = auth.getCurrentUser();
        if(firebaseUser != null) {
            intent = new Intent(StartActivity.this, SignInPageActivity.class);
            startActivity(intent);
            finish();
        }

        goToSignIn = findViewById(R.id.start_signin_btn);
        goToRegister = findViewById(R.id.start_register_btn);

        goToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(StartActivity.this, SignInPageActivity.class);
                startActivity(intent);
            }
        });

        goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}
