package net.leon.myfypproject2.CelebritySchedule;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.leon.myfypproject2.Model.Plan;
import net.leon.myfypproject2.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ScheduleList extends AppCompatActivity {
    private DatabaseReference CelebrityScheduleRef;
    private FirebaseAuth mAuth;
    private String Current_user;
    private RecyclerView all_celeplan_recyclerview;
    private CircleImageView BackToMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);

        CelebrityScheduleRef = FirebaseDatabase.getInstance().getReference().child("CelebritySchedule");
        mAuth = FirebaseAuth.getInstance();
        Current_user = mAuth.getCurrentUser().getUid();
        BackToMenu = (CircleImageView)findViewById(R.id.BackToMenu);
        BackToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        all_celeplan_recyclerview = (RecyclerView) findViewById(R.id.all_celeplan_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(false);
        all_celeplan_recyclerview.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Plan,CelePlanViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Plan, CelePlanViewHolder>(
                Plan.class,
                R.layout.all_celeplan_layout,
                CelePlanViewHolder.class,
                CelebrityScheduleRef
        ) {
            @Override
            protected void populateViewHolder(CelePlanViewHolder viewHolder, Plan model, int position) {
                viewHolder.setPlanName(model.getPlanName());
                viewHolder.setDate(model.getDate() + " " + model.getTime());
                viewHolder.setUserImage(getApplication(),model.getUserImage());
                viewHolder.setUsername(model.getUsername());

            }
        };
        all_celeplan_recyclerview.setAdapter(firebaseRecyclerAdapter);
    }

    public static class CelePlanViewHolder extends RecyclerView.ViewHolder {
        View mView;
        private CircleImageView planuserimage;
        private TextView planusername,plandatetime,plan_name;


        public CelePlanViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            planuserimage = (CircleImageView)mView.findViewById(R.id.planuserimage);
            planusername = (TextView)mView.findViewById(R.id.planusername);
            plandatetime = (TextView)mView.findViewById(R.id.plandatetime);
            plan_name = (TextView)mView.findViewById(R.id.plan_name);


        }

        public void setPlanName(String planName) {
            plan_name.setText(planName);

        }

        public void setDate(String date) {
            plandatetime.setText(date);

        }

        public void setUsername(String username) {
            planusername.setText(username);

        }

        public void setUserImage(Context ctx, String userImage) {
            Glide.with(ctx).load(userImage).into(planuserimage);

        }


    }
}
