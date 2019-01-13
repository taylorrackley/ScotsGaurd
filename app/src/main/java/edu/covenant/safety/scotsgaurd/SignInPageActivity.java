package edu.covenant.safety.scotsgaurd;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.covenant.safety.scotsgaurd.Model.User;

public class SignInPageActivity extends AppCompatActivity {

    private EditText username, password;
    private Button login;

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private DatabaseReference firebaseDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_page);

        auth = FirebaseAuth.getInstance();

        firebaseUser = auth.getCurrentUser();
        if(firebaseUser != null) {
            login();
        }

        username = findViewById(R.id.signin_username_input);
        password = findViewById(R.id.signin_password_input);
        login = findViewById(R.id.signin_signin_btn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            // Enforce lowercase username bound to their email
            String email = username.getText().toString().toLowerCase()+"@covenant.edu";
            String pass = password.getText().toString();

            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
                Toast.makeText(SignInPageActivity.this, "Both email and password are required", Toast.LENGTH_LONG).show();
            } else {
                auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            login();
                        } else {
                            Toast.makeText(SignInPageActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                        }
                        }
                });
            }
            }
        });

    }

    private void login() {

        firebaseUser = auth.getCurrentUser();

        firebaseDB = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());;
        firebaseDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                // Default go to student activity
                Intent intent;
                if(user.getAccountType() == User.STUDENT_WORKER) {
                    intent = new Intent(SignInPageActivity.this, AdminDashboardActivity.class);
                } else if(user.getAccountType() == User.ADMIN) {
                    intent = new Intent(SignInPageActivity.this, AdminDashboardActivity.class);
                } else {
                    intent = new Intent(SignInPageActivity.this, StudentDashboardActivity.class);
                }

                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
