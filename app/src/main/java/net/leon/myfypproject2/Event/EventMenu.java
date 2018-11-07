package net.leon.myfypproject2.Event;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.shashank.sony.fancytoastlib.FancyToast;

import net.leon.myfypproject2.MainActivity;
import net.leon.myfypproject2.Model.Event;
import net.leon.myfypproject2.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventMenu extends AppCompatActivity {
    private CircleImageView backbtn,tocreateevent;
    private RecyclerView Eventlist;
    private DatabaseReference EventRef, EventPlayerRef, UserRef;
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
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
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
                viewHolder.setEvent_People(String.valueOf(model.getEvent_People()));
                viewHolder.setEvent_StartDate(model.getEvent_StartDate() + " " +model.getEvent_StartTime());
                viewHolder.setEvent_Title(model.getEvent_Title());

                EventRef.child(postkey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String event_payment = dataSnapshot.child("Event_Payment").getValue().toString();

                            if(event_payment.equals("Free")){
                                viewHolder.eventpayment.setTextColor(Color.GREEN);
                            }else {
                                viewHolder.eventpayment.setTextColor(Color.RED);
                                viewHolder.eventpayment.setText("Paid");
                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                viewHolder.setLikeButtonStatus(postkey);
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EventPlayerRef.child(postkey).child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    FancyToast.makeText(EventMenu.this,"You Already Join This Event",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();

                                }else {
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

                                                    EventRef.child(postkey).addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if(dataSnapshot.exists()){
                                                                String freepayment = "Free";
                                                                String payment = dataSnapshot.child("Event_Payment").getValue().toString();
                                                                if(payment.equals(freepayment)){

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

                                                                }else {
                                                                    String price = dataSnapshot.child("Event_Price").getValue().toString();
                                                                    int price1 = Integer.parseInt(price);

                                                                    AlertDialog.Builder builder;
                                                                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1){
                                                                        builder = new AlertDialog.Builder(EventMenu.this,android.R.style.Theme_Material_Dialog_Alert);
                                                                    } else {
                                                                        builder = new AlertDialog.Builder(EventMenu.this);
                                                                    }

                                                                    builder.setTitle("Event")
                                                                            .setMessage("You Need To Pay To Join This Event" + price1)
                                                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                                    UserRef.child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                            if(dataSnapshot.exists()){
                                                                                                int usercredit = dataSnapshot.child("InAppCredit").getValue(Integer.class);

                                                                                                EventRef.child(postkey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                    @Override
                                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                        String credit = dataSnapshot.child("Event_Price").getValue().toString();
                                                                                                        int usercredit1 = Integer.parseInt(credit);

                                                                                                        if(usercredit < usercredit1){
                                                                                                            FancyToast.makeText(EventMenu.this,"Insufficient Credit To Join Event",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();

                                                                                                        }else {
                                                                                                            int total;
                                                                                                            total = usercredit - usercredit1;
                                                                                                            CheckLike = true;

                                                                                                            UserRef.child(currentUser).child("InAppCredit").setValue(total);

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
                                                                                                    }

                                                                                                    @Override
                                                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                                                    }
                                                                                                });



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
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });

            }
        };
        Eventlist.setAdapter(firebaseRecyclerAdapter);
    }
    public static class EventViewHolder extends RecyclerView.ViewHolder{
        View mView;
        private TextView totalppljoin,eventpayment;
        private DatabaseReference EventRef, EventPlayerRef;
        private String currentuserid;
        int countuser,count_postcm;
        public EventViewHolder(View itemView){
            super(itemView);
            mView = itemView;
            totalppljoin = (TextView)mView.findViewById(R.id.totalppljoin);
            EventPlayerRef = FirebaseDatabase.getInstance().getReference().child("EventPlayer");
            eventpayment = (TextView)mView.findViewById(R.id.eventlui);
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
