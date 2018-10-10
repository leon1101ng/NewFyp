package net.leon.myfypproject2.Comment;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.leon.myfypproject2.Model.ImageCommentClass;
import net.leon.myfypproject2.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ImageComment extends AppCompatActivity {

    private TextView postComment;
    private EditText inputcm;
    private RecyclerView commentviewlist;
    private DatabaseReference userRef,ImagePostRef;
    private FirebaseAuth mAuth;

    String postKey,currentUserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_comment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        postKey = getIntent().getExtras().get("PostKey").toString();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ImagePostRef = FirebaseDatabase.getInstance().getReference().child("Post").child("Image_Posts").child(postKey).child("Comments");

        postComment = (TextView)findViewById(R.id.postComment);
        inputcm = (EditText)findViewById(R.id.input_cm);
        commentviewlist = (RecyclerView)findViewById(R.id.comment_recycleview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        commentviewlist.setLayoutManager(linearLayoutManager);

        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String userName = dataSnapshot.child("username").getValue().toString();

                            ValidateComment(userName);
                            inputcm.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }
    protected void onStart(){
        super.onStart();

        FirebaseRecyclerAdapter<ImageCommentClass,ImageCommentViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<ImageCommentClass, ImageCommentViewHolder>(
                        ImageCommentClass.class,
                        R.layout.image_commentlayout,
                        ImageCommentViewHolder.class,
                        ImagePostRef

                ) {
                    @Override
                    protected void populateViewHolder(ImageCommentViewHolder viewHolder, ImageCommentClass model, int position) {
                        viewHolder.setCommentUsername(model.getUsername());
                        viewHolder.setComment(model.getComment());
                        viewHolder.setDateTime(model.getDate() + " " +model.getTime());
                    }
                };
        commentviewlist.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ImageCommentViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public ImageCommentViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }
        public void setCommentUsername(String username) {
            TextView Cusername =  (TextView)mView.findViewById(R.id.imgcomment_user);
            Cusername.setText(username);
        }

        public void setComment(String comment) {
            TextView Ucomment =  (TextView)mView.findViewById(R.id.imgcomment);
           Ucomment.setText(comment);

        }
        public void setDateTime(String datetime) {
            TextView Datetime =  (TextView)mView.findViewById(R.id.Time_Date);
            Datetime.setText(datetime);
        }
    }


    private void ValidateComment(String userName) {
        String comment = inputcm.getText().toString();


        if(TextUtils.isEmpty(comment)){
            Toast.makeText(this,"Please Enter Text To Comment..." ,Toast.LENGTH_SHORT).show();
        }else {
            Calendar caldate = Calendar.getInstance();
            SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-yyyy");
            final String CurrentDate = currentdate.format(caldate.getTime());

            Calendar caltime = Calendar.getInstance();
            SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm");
            final String CurrentTime = currenttime.format(caltime.getTime());

            final String RandomKey = currentUserID+CurrentDate + CurrentTime;

            HashMap commentMap = new HashMap();
            commentMap.put("Uid",currentUserID);
            commentMap.put("Comment",comment);
            commentMap.put("Date",CurrentDate);
            commentMap.put("Time",CurrentTime);
            commentMap.put("Username",userName);
            ImagePostRef.child(RandomKey).updateChildren(commentMap)
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ImageComment.this,"Done Comment..." ,Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(ImageComment.this,"Failed To Comment" ,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }
}
