package net.leon.myfypproject2.Event;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.leon.myfypproject2.MainActivity;
import net.leon.myfypproject2.Model.Event;
import net.leon.myfypproject2.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventMenu extends AppCompatActivity {
    private CircleImageView backbtn,tocreateevent;
    private RecyclerView Eventlist;
    private DatabaseReference EventRef;
    private FirebaseAuth mAuth;

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


                viewHolder.setEvent_Location(model.getEvent_Location());
                viewHolder.setEvent_People(model.getEvent_People());
                viewHolder.setEvent_StartDate(model.getEvent_StartDate() + " " +model.getEvent_StartTime());
                viewHolder.setEvent_Title(model.getEvent_Title());

            }
        };
        Eventlist.setAdapter(firebaseRecyclerAdapter);
    }
    public static class EventViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public EventViewHolder(View itemView){
            super(itemView);
            mView = itemView;
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

    }
}
