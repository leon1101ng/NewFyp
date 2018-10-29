package net.leon.myfypproject2.ProfileFragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.shashank.sony.fancytoastlib.FancyToast;

import net.leon.myfypproject2.Model.Status;
import net.leon.myfypproject2.R;
import net.leon.myfypproject2.UserInterface.StatusPost;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {
    private RecyclerView statuspostlist;
    private DatabaseReference StatusPostRef;
    private FirebaseAuth mAuth;
    private String CurrentUser;


    public PostFragment() {
        // Required empty public constructor
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        StatusPostRef = FirebaseDatabase.getInstance().getReference().child("Post").child("Status_Posts");



        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser().getUid();
        View view =  inflater.inflate(R.layout.fragment_post, container, false);
        statuspostlist = (RecyclerView)view.findViewById(R.id.Post_post_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        statuspostlist.setLayoutManager(linearLayoutManager);

        return view;


    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle("");
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.statuspostfragmenu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        Drawable drawable = menu.findItem(R.id.add_status).getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.add_status:
                Intent addimge = new Intent(getContext(), StatusPost.class);
                startActivity(addimge);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        Query  userstatuspost = StatusPostRef.orderByChild("UserID").startAt(CurrentUser).endAt(CurrentUser + "\uf8ff");
        FirebaseRecyclerAdapter<Status,StatusPostHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Status, StatusPostHolder>(
                Status.class,
                R.layout.user_allstatuspost_layout,
                StatusPostHolder.class,
                userstatuspost) {
            @Override
            protected void populateViewHolder(StatusPostHolder viewHolder, Status model, int position) {
                final String postkey = getRef(position).getKey();
                viewHolder.setStatus(model.getStatus());
                viewHolder.setTimeDate(model.getTimeDate());

                viewHolder.deletestatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder;
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1){
                            builder = new AlertDialog.Builder(getContext(),android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(getContext());
                        }

                        builder.setTitle("Your Status Posted")
                                .setMessage("Do you want delete this Status ? ")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        StatusPostRef.child(postkey).removeValue();
                                        FancyToast.makeText(getContext(),"This Status Has Been deleted !", FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();

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
        statuspostlist.setAdapter(firebaseRecyclerAdapter);


    }
    public static class StatusPostHolder extends RecyclerView.ViewHolder{
        View mView;
        private ImageView deletestatus;
        public StatusPostHolder(View itemView){
            super(itemView);

            mView = itemView;
            deletestatus = (ImageView)mView.findViewById(R.id.deletestatus);
        }

        public void setStatus(String status){
            TextView Status = (TextView)mView.findViewById(R.id.UserStatusText);
            Status.setText(status);

        }
        public void setTimeDate(String timeDate) {
            TextView timed = (TextView)mView.findViewById(R.id.UserStatusTimeDate);
            timed.setText(timeDate);
        }
    }
}
