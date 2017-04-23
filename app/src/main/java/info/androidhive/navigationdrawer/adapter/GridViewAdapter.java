package info.androidhive.navigationdrawer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import info.androidhive.navigationdrawer.R;
import info.androidhive.navigationdrawer.model.MyDataModel;


public class GridViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w150";

    private ArrayList<MyDataModel> informationArraylist;

    public Context context;

    public GridViewAdapter(ArrayList<MyDataModel> informationArraylist) {

        this.informationArraylist = informationArraylist;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

         context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder vh;
        View productView = inflater.inflate(R.layout.griditem, parent, false);
        vh = new ViewHolder(productView);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        final GridViewAdapter.ViewHolder viewHolder = (GridViewAdapter.ViewHolder) holder;
        viewHolder.titleTextView.setText(informationArraylist.get(position).getName());
        viewHolder.descriptionTextView.setText(informationArraylist.get(position).getEmail());
        Picasso.with(context)
                .load(BASE_URL_IMG + informationArraylist.get(position).getPoster_path())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(viewHolder.profile_image);
    }

    @Override
    public int getItemCount() {
        return informationArraylist.size();
    }

    public void addAll(ArrayList<MyDataModel> movieResults) {
        int size = informationArraylist.size();
        informationArraylist.addAll(movieResults);
        notifyItemInserted(size);

    }

    private static class ViewHolder extends RecyclerView.ViewHolder {


        TextView titleTextView;
        ImageView profile_image;
        TextView descriptionTextView;

        ViewHolder(View itemView) {

            super(itemView);

            titleTextView = (TextView) itemView.findViewById(R.id.tv_tittle);
            profile_image = (ImageView) itemView.findViewById(R.id.profile_image);
            descriptionTextView = (TextView) itemView.findViewById(R.id.tv_description);


        }

    }
}
