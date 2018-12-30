package edu.covenant.safety.scotsgaurd;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

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

import edu.covenant.safety.scotsgaurd.Model.Chat;

public class MessageSecurityActivity extends AppCompatActivity {

    List<Chat> mChat;

    FirebaseUser firebaseUser;
    DatabaseReference firebaseDB;

    ImageButton btn_send;
    EditText message_text;
    RecyclerView recyclerView;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_security);

        btn_send = findViewById(R.id.btn_send);
        message_text = findViewById(R.id.message_text);
        recyclerView = findViewById(R.id.list_messages);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        intent = getIntent();
        final String officeId = intent.getStringExtra("officeId");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = message_text.getText().toString();
                if(!message.equals("")) {
                    sendMessage(firebaseUser.getUid(), officeId, message);
                    message_text.setText("");
                }
            }
        });

//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        firebaseDB = FirebaseDatabase.getInstance().getReference("Users").child(officeId);

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

        firebaseDB = FirebaseDatabase.getInstance().getReference().child("Chats");
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

}
