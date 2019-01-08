package edu.covenant.safety.scotsgaurd.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.covenant.safety.scotsgaurd.Model.User;
import edu.covenant.safety.scotsgaurd.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;

    public UserAdapter(Context mContext, List<User> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_chat_list, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    // Load data into layout for each chat
    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.username.setText(user.getId());

    }

    // Class which holds the specific view for each message
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;

        public ViewHolder(View view) {
            super(view);
            username = view.findViewById(R.id.user_chat_name);
        }
    }

}
