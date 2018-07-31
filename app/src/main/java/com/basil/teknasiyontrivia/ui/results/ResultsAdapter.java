package com.basil.teknasiyontrivia.ui.results;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.basil.teknasiyontrivia.R;
import com.basil.teknasiyontrivia.model.Standing;

import java.util.List;

/**
 * Created by Basil on 7/31/2018.
 */

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder>{

    List<Standing> mData;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_standing_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Standing standing = mData.get(position);
        holder.standing.setText(standing.getStanding()+". ");
        holder.standingText.setText(standing.getName());
    }

    @Override
    public int getItemCount() {
        if (mData==null)
            return 0;
        else return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView standingText;
        TextView standing;
        public ViewHolder(View itemView) {
            super(itemView);
            standingText = itemView.findViewById(R.id.player_name);
            standing = itemView.findViewById(R.id.standing);
        }
    }

    public void setmData(List<Standing> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }
}
