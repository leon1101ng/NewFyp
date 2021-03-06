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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import net.leon.myfypproject2.Model.LiveStreamComment;
import net.leon.myfypproject2.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class LiveComment extends Fragment {
    private DatabaseReference LiveStreamCommentref, UserRef,LiveStreamCommentref1;
    private FirebaseAuth mAuth;
    private String CurrentUser, i, livestreamcomment,userName,CurrentDate,CurrentTime,RandomKey;
    private EditText livecomment;
    private TextView PostLiveComment;
    private RecyclerView comment_recycleview;
    private long countPosts = 0;



    public LiveComment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_live_comment, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            i = bundle.getString("liveID");
        }

        LiveStreamCommentref = FirebaseDatabase.getInstance().getReference().child("LiveStream").child(i).child("comment");




        LiveStreamCommentref1 = FirebaseDatabase.getInstance().getReference().child("LiveStream").child("comment");
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser().getUid();
        livecomment = (EditText)view.findViewById(R.id.input_livecm);
        PostLiveComment = (TextView)view.findViewById(R.id.postliveComment);
        comment_recycleview = (RecyclerView)view.findViewById(R.id.comment_recycleview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(false);
        comment_recycleview.setLayoutManager(linearLayoutManager);

        Calendar caldate = Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-yyyy");
        CurrentDate = currentdate.format(caldate.getTime());

        Calendar caltime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");
        CurrentTime = currenttime.format(caltime.getTime());

        UserRef.child(CurrentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    userName = dataSnapshot.child("username").getValue().toString();

                }
            } @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        PostLiveComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StoreChatComment();

            }
        });


        RandomKey = CurrentTime + CurrentDate;



        return view;
    }

    private void StoreChatComment() {
        livestreamcomment = livecomment.getText().toString();

        LiveStreamCommentref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    countPosts = dataSnapshot.getChildrenCount();

                }else {
                    countPosts = 0;

                }
                HashMap commentMap = new HashMap();
                commentMap.put("Uid",CurrentUser);
                commentMap.put("Comment",livestreamcomment);
                commentMap.put("Date",CurrentDate);
                commentMap.put("Time",CurrentTime);
                commentMap.put("Username",userName);
                commentMap.put("counter", countPosts);
                LiveStreamCommentref.child(RandomKey).updateChildren(commentMap)
                        .addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if(task.isSuccessful()){

                                }else {

                                }
                            }
                        });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    public void onStart() {
        super.onStart();
        Query Descendsort = LiveStreamCommentref.orderByChild("counter");
        FirebaseRecyclerAdapter<LiveStreamComment,LiveStreamCommentViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<LiveStreamComment, LiveStreamCommentViewHolder>(
                LiveStreamComment.class,
                R.layout.livecommentlayout,
                LiveStreamCommentViewHolder.class,
                Descendsort
        ) {
            @Override
            protected void populateViewHolder(LiveStreamCommentViewHolder viewHolder, LiveStreamComment model, int position) {
                viewHolder.setUsername(model.getUsername());
                viewHolder.setComment(model.getComment());

            }
        };
        comment_recycleview.setAdapter(firebaseRecyclerAdapter);
    }

    public static class LiveStreamCommentViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public LiveStreamCommentViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setUsername(String username) {
            TextView livescnametv = (TextView)mView.findViewById(R.id.StreamCommentUsername);
            livescnametv.setText(username);
        }

        public void setComment(String comment) {
            TextView livesctexttv = (TextView)mView.findViewById(R.id.StreamCommentUserText);
            livesctexttv.setText(comment);

        }
    }
}
