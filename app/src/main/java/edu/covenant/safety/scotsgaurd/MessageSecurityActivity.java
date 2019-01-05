package edu.covenant.safety.scotsgaurd;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.covenant.safety.scotsgaurd.Adapter.MessageAdapter;
import edu.covenant.safety.scotsgaurd.Model.Chat;

public class MessageSecurityActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    DatabaseReference firebaseDB;

    ImageButton btn_send;
    EditText message_text;

    MessageAdapter messageAdapter;
    List<Chat> mChat;

    RecyclerView recyclerView;

    Intent intent;

    @Override
    protected void onStart() {
        super.onStart();

        // TODO kick user out if no login
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_security);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ScotsGaurd");

        btn_send = findViewById(R.id.btn_send);
        message_text = findViewById(R.id.message_text);
        recyclerView = findViewById(R.id.list_messages);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // Get the id of the other user
        intent = getIntent();
        //final String officeID = intent.getStringExtra("officeId");
        final String officeID = "rdWxxLygmagtHkj3DRzCcyU4Tak1";

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = message_text.getText().toString();
                if(!message.equals("")) {
                    sendMessage(firebaseUser.getUid(), officeID, message);
                    message_text.setText("");
                }
            }
        });

        firebaseDB = FirebaseDatabase.getInstance().getReference();
        firebaseDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                readMessages(firebaseUser.getUid(), officeID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
                startActivity(new Intent(MessageSecurityActivity.this, SignInPage.class));
                finish();
                return true;
        }

        return false;
    }

    private void sendMessage(String sender, String receiver, String message) {
        firebaseDB = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        firebaseDB.child("OfficeChats").push().setValue(hashMap);
    }

    private void readMessages(final String myID, final String officeID) {

        mChat = new ArrayList<>();

        firebaseDB = FirebaseDatabase.getInstance().getReference().child("OfficeChats");
        firebaseDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(myID) && chat.getSender().equals(officeID) ||
                            chat.getReceiver().equals(officeID) && chat.getSender().equals(myID)) {
                        mChat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(MessageSecurityActivity.this, mChat);
                    recyclerView.setAdapter(messageAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
