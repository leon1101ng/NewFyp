package net.leon.myfypproject2.Comment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.leon.myfypproject2.Model.LiveStreamComment;
import net.leon.myfypproject2.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class CameraComment extends Fragment {
    private DatabaseReference LiveStreamCommentref, UserRef, LiveStreamCommentref1;
    private FirebaseAuth mAuth;
    private String CurrentUser, i, livestreamcomment;
    private EditText livecomment;
    private TextView PostLiveComment;
    private RecyclerView comment_recycleview1;


    public CameraComment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_camera_comment, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            i = bundle.getString("liveID");
        }

        LiveStreamCommentref = FirebaseDatabase.getInstance().getReference().child("LiveStream").child(i).child("comment");
        LiveStreamCommentref1 = FirebaseDatabase.getInstance().getReference().child("LiveStream").child("comment");
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser().getUid();
        livecomment = (EditText) view.findViewById(R.id.input_livecm1);
        PostLiveComment = (TextView) view.findViewById(R.id.postliveComment1);
        comment_recycleview1 = (RecyclerView) view.findViewById(R.id.comment_recycleview1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(false);
        comment_recycleview1.setLayoutManager(linearLayoutManager);

        PostLiveComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserRef.child(CurrentUser).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String userName = dataSnapshot.child("username").getValue().toString();

                            StoreChatComment(userName);
                            livecomment.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        return view;
    }

    private void StoreChatComment(String userName) {
        livestreamcomment = livecomment.getText().toString();

        Calendar caldate = Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-yyyy");
        final String CurrentDate = currentdate.format(caldate.getTime());

        Calendar caltime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");
        final String CurrentTime = currenttime.format(caltime.getTime());

        final String RandomKey = CurrentUser + CurrentDate + CurrentTime;

        HashMap commentMap = new HashMap();
        commentMap.put("Uid", CurrentUser);
        commentMap.put("Comment", livestreamcomment);
        commentMap.put("Date", CurrentDate);
        commentMap.put("Time", CurrentTime);
        commentMap.put("Username", userName);
        LiveStreamCommentref.child(RandomKey).updateChildren(commentMap)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {

                        } else {

                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<LiveStreamComment, LiveComment.LiveStreamCommentViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<LiveStreamComment, LiveComment.LiveStreamCommentViewHolder>(
                LiveStreamComment.class,
                R.layout.livecommentlayout,
                LiveComment.LiveStreamCommentViewHolder.class,
                LiveStreamCommentref
        ) {
            @Override
            protected void populateViewHolder(LiveComment.LiveStreamCommentViewHolder viewHolder, LiveStreamComment model, int position) {
                viewHolder.setUsername(model.getUsername());
                viewHolder.setComment(model.getComment());

            }
        };
        comment_recycleview1.setAdapter(firebaseRecyclerAdapter);
    }
}