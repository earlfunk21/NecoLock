package com.morax.necolock.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.morax.necolock.R;
import com.morax.necolock.database.AppDatabase;
import com.morax.necolock.database.dao.ProfileDao;
import com.morax.necolock.database.entity.Profile;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private Context context;
    private List<Profile> profileList;
    private Handler handler;
    private Runnable runnable;

    public ProfileAdapter(Context context, List<Profile> profileList) {
        this.context = context;
        this.profileList = profileList;
    }

    public void setCartList(List<Profile> profileList) {
        this.profileList = profileList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        Profile profile = profileList.get(position);
        ProfileDao profileDao = AppDatabase.getInstance(context).profileDao();
        String name = profile.name;
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                profile.seconds++; // Increment seconds
                long seconds = profile.seconds;
                long hours = seconds / 3600;
                long minutes = (seconds % 3600) / 60;
                String formatted = hours + "h " + minutes + "m " + (seconds % 60) + "s";
                viewHolder.tvTime.setText(formatted); // Update the time string
                handler.postDelayed(this, 1000); // Schedule the next update after 1 second
            }
        };
        handler.postDelayed(runnable, 1000);
        viewHolder.tvName.setText(name);
        if (profile.block) {
            int color = ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.gray);
            viewHolder.cvBlock.setCardBackgroundColor(color);
            viewHolder.cvBlock.setEnabled(false);
            viewHolder.cvBlock.setClickable(false);
        }
        viewHolder.cvBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile.block = true;
                profileDao.update(profile);
                int color = ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.gray);
                viewHolder.cvBlock.setCardBackgroundColor(color);
                viewHolder.cvBlock.setEnabled(false);
                viewHolder.cvBlock.setClickable(false);
            }
        });
        viewHolder.ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileDao.delete(profile);
                int position = viewHolder.getAdapterPosition();
                profileList.remove(position);
                notifyItemRemoved(position);
            }
        });
    }

    @NonNull
    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.profile_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageButton ibClose;
        public TextView tvName, tvTime;
        public CardView cvBlock;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            ibClose = view.findViewById(R.id.ib_close);
            tvName = view.findViewById(R.id.tv_profile_name);
            tvTime = view.findViewById(R.id.tv_time);
            cvBlock = view.findViewById(R.id.cv_block);
        }

    }

    public Profile getProfileAt(int position) {
        return profileList.get(position);
    }
}