package com.example.josimuddin.firebaseapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by JosimUddin on 10/11/2017.
 */

public class TrackList extends ArrayAdapter<Track> {

    private Activity context;
    private List<Track> tracks;


    public TrackList(@NonNull Activity context, List<Track> tracks) {
        super(context, R.layout.list_layout, tracks);
        this.context = context;
        this.tracks = tracks;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);

        TextView tvName = listViewItem.findViewById(R.id.tv_name_list_layout);
        TextView tvRating = listViewItem.findViewById(R.id.tv_geners_list_layout);

        Track track = tracks.get(position);
        tvName.setText(track.getTrackName());
        tvRating.setText(track.getRating());



        return listViewItem;
    }
}
