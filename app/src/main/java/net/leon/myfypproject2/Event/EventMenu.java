package net.leon.myfypproject2.Event;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.leon.myfypproject2.MainActivity;
import net.leon.myfypproject2.Model.Event;
import net.leon.myfypproject2.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventMenu extends AppCompatActivity {
    private CircleImageView backbtn,tocreateevent;
    private RecyclerView Eventlist;
    private DatabaseReference EventRef, EventPlayerRef;
    private FirebaseAuth mAuth;
    private String currentUser;
    private Boolean CheckLike = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_menu);

        backbtn = (CircleImageView)findViewById(R.id.BackToMenu);
        Eventlist = (RecyclerView)findViewById(R.id.EventList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        Eventlist.setLayoutManager(linearLayoutManager);
        EventRef = FirebaseDatabase.getInstance().getReference().child("Event");
        EventPlayerRef = FirebaseDatabase.getInstance().getReference().child("EventPlayer");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EventMenu.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        tocreateevent = (CircleImageView)findViewById(R.id.ToCreateEvent);
        tocreateevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EventMenu.this, CreateEvent.class);
                startActivity(i);
            }
        });
        DisplayAllEvent();
    }

    private void DisplayAllEvent() {
        FirebaseRecyclerAdapter<Event,EventViewHolder>firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Event, EventViewHolder>(
                Event.class,
                R.layout.alleventlayout,
                EventViewHolder.class,
                EventRef
        ) {
            @Override
            protected void populateViewHolder(EventViewHolder viewHolder, Event model, int position) {
                viewHolder.setEvent_Description(model.getEvent_Description());

                final String postkey = getRef(position).getKey();

                viewHolder.setEvent_Location(model.getEvent_Location());
                viewHolder.setEvent_People(model.getEvent_People());
                viewHolder.setEvent_StartDate(model.getEvent_StartDate() + " " +model.getEvent_StartTime());
                viewHolder.setEvent_Title(model.getEvent_Title());

                viewHolder.setLikeButtonStatus(postkey);
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder;
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1){
                            builder = new AlertDialog.Builder(EventMenu.this,android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(EventMenu.this);
                        }

                        builder.setTitle("Join Event")
                                .setMessage("Do you want To Join This Event ? ")
                                .setIcon(android.R.drawable.ic_menu_help)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        CheckLike = true;

                                        EventPlayerRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if(CheckLike.equals(true)){
                                                    if(dataSnapshot.child(postkey).hasChild(currentUser)){
                                                        EventPlayerRef.child(postkey).child(currentUser).removeValue();
                                                        CheckLike = false;
                                                    }else {
                                                        EventPlayerRef.child(postkey).child(currentUser).setValue(true);
                                                        CheckLike = false;
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });


                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {


                                    }
                                })
                                .show();
                    }
                });

            }
        };
        Eventlist.setAdapter(firebaseRecyclerAdapter);
    }
    public static class EventViewHolder extends RecyclerView.ViewHolder{
        View mView;
        private TextView totalppljoin;
        private DatabaseReference EventRef, EventPlayerRef;
        private String currentuserid;
        int countuser,count_postcm;
        public EventViewHolder(View itemView){
            super(itemView);
            mView = itemView;
            totalppljoin = (TextView)mView.findViewById(R.id.totalppljoin);
            EventPlayerRef = FirebaseDatabase.getInstance().getReference().child("EventPlayer");
            currentuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        public void setEvent_Description(String event_Description) {
            TextView locationview= (TextView)mView.findViewById(R.id.EventDescView);
            locationview.setText(event_Description);
        }

        public void setEvent_Title(String event_Title) {
            TextView titleview = (TextView)mView.findViewById(R.id.EventTitleView);
            titleview.setText(event_Title);
        }


        public void setEvent_Location(String event_Location) {
            TextView locationview = (TextView)mView.findViewById(R.id.EventLocationView);
            locationview.setText(event_Location);
        }

        public void setEvent_StartDate(String event_StartDate) {
            TextView timeview = (TextView)mView.findViewById(R.id.EventTimeView);
            timeview.setText(event_StartDate);
        }


        public void setEvent_People(String event_People) {
            TextView nopplview= (TextView)mView.findViewById(R.id.EventNoPplView);
            nopplview.setText(event_People);
        }

        public void setLikeButtonStatus(final String postkey) {
            EventPlayerRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(postkey).hasChild(currentuserid)){
                        countuser= (int) dataSnapshot.child(postkey).getChildrenCount();
                        totalppljoin.setText(Integer.toString(countuser) + "/");


                    }else {
                        countuser = (int) dataSnapshot.child(postkey).getChildrenCount();
                        totalppljoin.setText(Integer.toString(countuser) + "/");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });
        }
    }
}
