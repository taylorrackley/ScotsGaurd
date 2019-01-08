package edu.covenant.safety.scotsgaurd;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.covenant.safety.scotsgaurd.Adapter.UserAdapter;
import edu.covenant.safety.scotsgaurd.Model.Chat;
import edu.covenant.safety.scotsgaurd.Model.User;

public class ChatsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private List<User> mUsers;

    FirebaseUser firebaseUser;
    DatabaseReference firebaseDB;

    private List<String> usersList;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        recyclerView = findViewById(R.id.list_active_chats);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();
        firebaseDB = FirebaseDatabase.getInstance().getReference().child("OfficeChats");

        firebaseDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);

                    if(chat.getSender().equals(firebaseUser.getUid())) {
                        boolean toggle = true;
                        for(String user : usersList) {
                            if (user == chat.getReceiver()) {
                                toggle = false;
                            }
                        }
                        if(toggle) {
                            usersList.add(chat.getReceiver());
                        }
                    }
                    if(chat.getReceiver().equals(firebaseUser.getUid())) {
                        boolean toggle = true;
                        for(String user : usersList) {
                            if (user == chat.getSender()) {
                                toggle = false;
                            }
                        }
                        if(toggle) {
                            usersList.add(chat.getSender());
                        }
                    }
                }

                displayChats();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void displayChats() {

        //userAdapter = new UserAdapter(this, usersList);

    }
}
