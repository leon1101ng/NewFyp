package net.leon.myfypproject2;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import net.leon.myfypproject2.Model.FanMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FanPost extends Fragment {
    private String i;
    private final Context c = getContext();
    private DatabaseReference FanMessageRef,UserRef;
    private FirebaseAuth mAuth;
    private String Current_User,CurrentDate,CurrentTime,Postrandomname,msg;
    private EditText messageInput;
    private RecyclerView fanmsglist;


    public FanPost() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fan_post, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
             i = bundle.getString("UserID");
        }

        fanmsglist = (RecyclerView)view.findViewById(R.id.fanmeg_post_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        fanmsglist.setLayoutManager(linearLayoutManager);

        FanMessageRef = FirebaseDatabase.getInstance().getReference().child("FanMessage").child(i);
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        Current_User = mAuth.getCurrentUser().getUid();




        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Fan Message");
                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);

                messageInput = new EditText(getContext());
                layout.addView(messageInput);
                builder.setView(layout);

                builder.setPositiveButton("Post", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CheckValidation();

                    }
                });
                Dialog dialog = builder.create();
                dialog.show();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<FanMessage,FanMessageHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FanMessage, FanMessageHolder>(
                FanMessage.class,
                R.layout.fanmessagelayout,
                FanMessageHolder.class,
                FanMessageRef
        ) {
            @Override
            protected void populateViewHolder(FanMessageHolder viewHolder, FanMessage model, int position) {
                viewHolder.setProfilePicture(getContext(), model.getProfilePicture());
                viewHolder.setUsername(model.getUsername());
                viewHolder.setDate(model.getDate());
                viewHolder.setMessage(model.getMessage());

            }
        };
        fanmsglist.setAdapter(firebaseRecyclerAdapter);
    }

    public static class FanMessageHolder extends RecyclerView.ViewHolder{
        View mView;
        public FanMessageHolder(View itemView){
            super(itemView);
            mView = itemView;
        }
        public void setProfilePicture(Context ctx ,String profilePicture) {
            CircleImageView FanPic = mView.findViewById(R.id.FanPic);
            Glide.with(ctx).load(profilePicture).into(FanPic);
        }
        public void setUsername(String username) {
            TextView fanname = mView.findViewById(R.id.FanUsername);
            fanname.setText(username);
        }
        public void setDate(String date) {
            TextView fanmsgdate = mView.findViewById(R.id.FanmegDatetime);
            fanmsgdate.setText(date);
        }
        public void setMessage(String message) {
            TextView fanmesg = mView.findViewById(R.id.FanMsgText);
            fanmesg.setText(message);
        }
    }

    private void CheckValidation() {
        msg = messageInput.getText().toString();
        if(TextUtils.isEmpty(msg)){

        }else {
            Calendar caldate = Calendar.getInstance();
            SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-yyyy");
            CurrentDate = currentdate.format(caldate.getTime());

            Calendar caltime = Calendar.getInstance();
            SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");
            CurrentTime = currenttime.format(caltime.getTime());
            Random rand = new Random();

            int  n = rand.nextInt(50) + 1;
            Postrandomname = CurrentDate + CurrentTime + n;
            UserRef.child(Current_User).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String userfullname = dataSnapshot.child("Fullname").getValue().toString();
                    String userprofilepicture = dataSnapshot.child("ProfilePicture").getValue().toString();
                    String username = dataSnapshot.child("username").getValue().toString();
                    String msg = messageInput.getText().toString();
                    StoretoDB(userfullname, userprofilepicture, username, msg);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void StoretoDB(String userfullname, String userprofilepicture, String username, String msg) {
        HashMap Fanmsg = new HashMap();
        Fanmsg.put("Message", msg);
        Fanmsg.put("User", Current_User);
        Fanmsg.put("Date", CurrentDate);
        Fanmsg.put("Time",CurrentTime);
        Fanmsg.put("Fullname", userfullname);
        Fanmsg.put("ProfilePicture", userprofilepicture);
        Fanmsg.put("Username", username);
        FanMessageRef.child(Current_User + Postrandomname).setValue(Fanmsg)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            FancyToast.makeText(getContext(),"Message Has Been Send !", FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                        }else {
                            String message = task.getException().getMessage();
                            FancyToast.makeText(getContext(),"Failed To Send Message " + message,FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                        }
                    }
                });
    }


}
