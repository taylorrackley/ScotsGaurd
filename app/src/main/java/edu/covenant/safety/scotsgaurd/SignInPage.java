package edu.covenant.safety.scotsgaurd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignInPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_page);
    }

    public void login(View view) {
        EditText userEditText = findViewById(R.id.signInUsernameInput);
        String user = userEditText.getText().toString();

        EditText passEditText = findViewById(R.id.signInPasswordInput);
        String pass = passEditText.getText().toString();

        if(user.equals("taylor.rackley") && pass.equals("1234")) {
            Intent intent = new Intent(this, Dashboard.class);
            intent.putExtra("USERNAME", user);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Invalid Login", Toast.LENGTH_SHORT).show();
        }
    }

}