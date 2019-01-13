package edu.covenant.safety.scotsgaurd;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import edu.covenant.safety.scotsgaurd.Model.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText inputBannerID;
    private EditText inputUsername;
    private EditText inputPassword;

    private Button registerBtn;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference firebaseDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputBannerID = findViewById(R.id.register_bannerid_input);
        inputUsername = findViewById(R.id.register_username_input);
        inputPassword = findViewById(R.id.register_password_input);

        registerBtn = findViewById(R.id.register_register_btn);

        auth = FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            String bannerID = inputBannerID.getText().toString();
            // username must be lowercase
            String username = inputUsername.getText().toString().toLowerCase();
            String password = inputPassword.getText().toString();

            if(validateUser(bannerID, username)) {
                registerUser(new User(username, password, bannerID, User.STUDENT));
            } else {
                Toast.makeText(RegisterActivity.this, "Banner ID or username were invalid", Toast.LENGTH_LONG).show();
            }
            }
        });

    }

    private boolean validateUser(String bannerID, String username) {

        boolean valid = true;

        if(bannerID.length() != 8 || bannerID.contains("@"))
            valid = false;
        if(!username.contains("."))
            valid = false;

        return valid;
    }

    private void registerUser(final User newUser) {
        auth.createUserWithEmailAndPassword(newUser.getUsername()+"@covenant.edu", newUser.getPassword())
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()) {
                firebaseUser = auth.getCurrentUser();
                newUser.setID(firebaseUser.getUid());

                firebaseDB = FirebaseDatabase.getInstance().getReference("Users").child(newUser.getID());

                HashMap<String, String> userHashMap = new HashMap<>();
                userHashMap.put("id", newUser.getID());
                userHashMap.put("banner_id", newUser.getBannerID());
                userHashMap.put("username", newUser.getUsername());
                userHashMap.put("account_type", String.valueOf(newUser.getAccountType()));

                firebaseDB.setValue(userHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Intent intent = new Intent(RegisterActivity.this, StudentDashboardActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Unable to register user", Toast.LENGTH_SHORT).show();
                    }
                    }
                });
            }
            }
        });
    }

}
