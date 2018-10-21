package net.leon.myfypproject2.ProfileFragment;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.leon.myfypproject2.FanPost;
import net.leon.myfypproject2.Model.FanMessage;
import net.leon.myfypproject2.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FanMessageFragment extends Fragment {
    private String CurrentUser;
    private final Context c = getContext();
    private DatabaseReference FanMessageRef,UserRef;
    private FirebaseAuth mAuth;
    private RecyclerView fanmsglist1;

    public FanMessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fan_message, container, false);

        fanmsglist1 = (RecyclerView)view.findViewById(R.id.fanmeg_post_list1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        fanmsglist1.setLayoutManager(linearLayoutManager);
        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser().getUid();

        FanMessageRef = FirebaseDatabase.getInstance().getReference().child("FanMessage").child(CurrentUser);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<FanMessage,FanMessage1Holder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FanMessage, FanMessage1Holder>(
                FanMessage.class,
                R.layout.fanmessagelayout,
                FanMessage1Holder.class,
                FanMessageRef
        ) {
            @Override
            protected void populateViewHolder(FanMessage1Holder viewHolder, FanMessage model, int position) {

                final String postkey = getRef(position).getKey();
                viewHolder.setProfilePicture(getContext(), model.getProfilePicture());
                viewHolder.setUsername(model.getUsername());
                viewHolder.setDate(model.getDate());
                viewHolder.setMessage(model.getMessage());

                viewHolder.deletebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder;
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1){
                            builder = new AlertDialog.Builder(getContext(),android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(getContext());
                        }

                        builder.setTitle("Fan Message")
                                .setMessage("Do you want delete this message ? ")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        FanMessageRef.child(postkey).removeValue();

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
        fanmsglist1.setAdapter(firebaseRecyclerAdapter);
    }



    public static class FanMessage1Holder extends RecyclerView.ViewHolder{
        View mView;
        private CircleImageView deletebtn;
        public FanMessage1Holder(View itemView){
            super(itemView);
            mView = itemView;
            deletebtn = (CircleImageView)mView.findViewById(R.id.deletepost);
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
}
