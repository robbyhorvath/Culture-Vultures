package robbyhorvath.honorsmobileapps.unsocialmediaapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> mUsers;

    public UserAdapter(List<User> users) {
        mUsers = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View rootView = layoutInflater.inflate(R.layout.user_item, parent, false);
        UserViewHolder newUserViewHolder = new UserViewHolder(rootView);
        return newUserViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bindUser(mUsers.get(position));
    }

    public void setUsers(List<User> users) {
        mUsers = users;
    }

    @Override
    public int getItemCount() {
        if (mUsers != null)
            return mUsers.size();
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Context context;
        private ImageView mProfilePictureImageView;
        private TextView mUsernameTextView;
        private User mUser;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            mProfilePictureImageView = itemView.findViewById(R.id.itemProfilePicture);
            mUsernameTextView = itemView.findViewById(R.id.itemUsername);
        }

        public void bindUser(User user) {
            mUser = user;
            try {
                mUsernameTextView.setText(mUser.getUsername());
                FirebaseStorage.getInstance().getReferenceFromUrl("gs://androidfinalproject-e27f1.appspot.com/profile pictures/").child(mUser.getUid() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        GlideApp.with(context)
                                .load(FirebaseStorage.getInstance().getReferenceFromUrl("gs://androidfinalproject-e27f1.appspot.com/profile pictures/").child(mUser.getUid() + ".jpg"))
                                .into(mProfilePictureImageView);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        GlideApp.with(context)
                                .load(FirebaseStorage.getInstance().getReferenceFromUrl("gs://androidfinalproject-e27f1.appspot.com/profile pictures/default-user.jpg"))
                                .into(mProfilePictureImageView);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View view) {

        }
    }
}