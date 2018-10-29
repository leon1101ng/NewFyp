package net.leon.myfypproject2.CelebritySchedule;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.sch.calendar.CalendarView;
import com.sch.calendar.adapter.SampleVagueAdapter;
import com.sch.calendar.annotation.DayOfMonth;
import com.sch.calendar.annotation.Month;
import com.sch.calendar.entity.Date;
import com.sch.calendar.listener.OnDateClickedListener;
import com.sch.calendar.listener.OnMonthChangedListener;

import net.leon.myfypproject2.Model.Plan;
import net.leon.myfypproject2.R;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ManageSchedule extends AppCompatActivity {

    @BindView(R.id.calendar_view)
    CalendarView calendarView;
    private DatabaseReference CelebrityScheduleRef;
    private FirebaseAuth mAuth;
    private String Current_User;
    private RecyclerView allplan_recyclerview;
    private CircleImageView backToMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_schedule);

        ButterKnife.bind(this);

        initCalendarView();
        mAuth = FirebaseAuth.getInstance();
        Current_User = mAuth.getCurrentUser().getUid();
        CelebrityScheduleRef = FirebaseDatabase.getInstance().getReference().child("CelebritySchedule");
        allplan_recyclerview = (RecyclerView) findViewById(R.id.allplan_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(false);
        allplan_recyclerview.setLayoutManager(linearLayoutManager);

        backToMenu = (CircleImageView)findViewById(R.id.BackToMenu);
        backToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void initCalendarView() {
        calendarView.setCanDrag(true); // can't change month by slide
        calendarView.setScaleEnable(false); // can't auto scale calendar when month changed.
        calendarView.setShowOverflowDate(true); // hide overflow date of showing month.
        calendarView.setCanFling(true);
        calendarView.setTitleFormat("yyyy-MM", Locale.CHINA);
        // Set a listenerï¼Œcallback when month changed.
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(Date date) {
            }
        });
        // Set a listenerï¼Œcallback when one of date be clicked.

        calendarView.setOnDateClickedListener(new OnDateClickedListener() {
            @Override
            public void onDateClicked(View itemView, int year, @Month int month, @DayOfMonth int dayOfMonth) {
                String i = String.format("%s-%s", dayOfMonth, month);
                Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
                String date;
                int reslut = calendar.get(Calendar.DAY_OF_WEEK);
                switch (reslut) {
                    case Calendar.MONDAY:
                        date = i + " Monday";
                        Intent AddActivity = new Intent(ManageSchedule.this,AddActivitySchedule.class);
                        AddActivity.putExtra("date", date);
                        startActivity(AddActivity);
                        break;
                    case Calendar.TUESDAY:
                        date = i + " Tuesday";
                        Intent AddActivity1 = new Intent(ManageSchedule.this,AddActivitySchedule.class);
                        AddActivity1.putExtra("date", date);
                        startActivity(AddActivity1);
                        break;
                    case Calendar.WEDNESDAY:
                        date = i + " Wednesday";
                        Intent AddActivity2 = new Intent(ManageSchedule.this,AddActivitySchedule.class);
                        AddActivity2.putExtra("date", date);
                        startActivity(AddActivity2);
                        break;
                    case Calendar.THURSDAY:
                        date = i + " Thursday";
                        Intent AddActivity3 = new Intent(ManageSchedule.this,AddActivitySchedule.class);
                        AddActivity3.putExtra("date", date);
                        startActivity(AddActivity3);
                        break;
                    case Calendar.FRIDAY:
                        date = i + " Friday";
                        Intent AddActivity4 = new Intent(ManageSchedule.this,AddActivitySchedule.class);
                        AddActivity4.putExtra("date", date);
                        startActivity(AddActivity4);
                        break;
                    case Calendar.SATURDAY:
                        date = i + " Saturday";
                        Intent AddActivity5 = new Intent(ManageSchedule.this,AddActivitySchedule.class);
                        AddActivity5.putExtra("date", date);
                        startActivity(AddActivity5);
                        break;
                    case Calendar.SUNDAY:
                        date = i + " Sunday";
                        Intent AddActivity6 = new Intent(ManageSchedule.this,AddActivitySchedule.class);
                        AddActivity6.putExtra("date", date);
                        startActivity(AddActivity6);
                        break;


                }





            }
        });
        // using SampleVagueAdapter
        calendarView.setVagueAdapter(new SampleVagueAdapter());
    }

    @Override
    protected void onStart() {
        super.onStart();


        Query  userplan = CelebrityScheduleRef.orderByChild("Uid").startAt(Current_User).endAt(Current_User + "\uf8ff");

        FirebaseRecyclerAdapter<Plan,PlanViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Plan, PlanViewHolder>(
                Plan.class,
                R.layout.all_user_plan_layout,
                PlanViewHolder.class,
                userplan

        ) {
            @Override
            protected void populateViewHolder(PlanViewHolder viewHolder, Plan model, int position) {
                final String postkey = getRef(position).getKey();
                viewHolder.setPlanName("Plan Name :" + model.getPlanName());
                viewHolder.setDate(model.getDate() + " " + model.getTime());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder;
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1){
                            builder = new AlertDialog.Builder(ManageSchedule.this,android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(ManageSchedule.this);
                        }

                        builder.setTitle("Plan")
                                .setMessage("Do you want delete this plan ? ")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        CelebrityScheduleRef.child(postkey).removeValue();

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
        allplan_recyclerview.setAdapter(firebaseRecyclerAdapter);
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        View mView;
        private TextView PlanNameDetail,PlanTimeDateDetail;

        public PlanViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            PlanNameDetail = (TextView)mView.findViewById(R.id.PlanNameDetail);
            PlanTimeDateDetail = (TextView)mView.findViewById(R.id.PlanTimeDateDetail);

        }

        public void setPlanName(String planName) {
            PlanNameDetail.setText(planName);
        }

        public void setDate(String date) {
            PlanTimeDateDetail.setText(date);
        }


    }
}
