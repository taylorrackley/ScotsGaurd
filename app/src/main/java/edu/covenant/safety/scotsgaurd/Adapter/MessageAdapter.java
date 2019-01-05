package edu.covenant.safety.scotsgaurd.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import edu.covenant.safety.scotsgaurd.Model.Chat;
import edu.covenant.safety.scotsgaurd.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 1;
    public static final int MSG_TYPE_RIGHT = 0;

    private Context mContext;
    private List<Chat> mChat;

    FirebaseUser firebaseUser;

    public MessageAdapter(Context mContext, List<Chat> mChat) {
        this.mContext = mContext;
        this.mChat = mChat;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("mChat type", "Type: " + viewType);
        if(viewType == MSG_TYPE_LEFT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new ViewHolder(view);
        }
    }

    // Load data into layout for each chat
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Chat chat = mChat.get(position);
        holder.message.setText(chat.getMessage());

        // Attach default image
        //holder.image.setImageResource(R.drawable.ic_profile_image);
    }

    @Override
    public int getItemCount() {
        Log.i("mChat Size in adapter", "Size: " + mChat.size());
        return mChat.size();
    }

    // Class which holds the specific view for each message
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView message;
        //public ImageView image;

        public ViewHolder(View view) {
            super(view);
            message = view.findViewById(R.id.chat_text);
            //image = view.findViewById(R.id.chat_left_image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(firebaseUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

}
