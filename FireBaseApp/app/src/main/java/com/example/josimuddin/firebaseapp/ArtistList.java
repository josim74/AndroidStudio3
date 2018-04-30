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

import java.util.List;

/**
 * Created by JosimUddin on 10/11/2017.
 */

public class ArtistList extends ArrayAdapter<Artist> {

    private Activity context;
    private List<Artist> artistList;

    public ArtistList(@NonNull Activity context, List<Artist> artistList) {
        super(context, R.layout.list_layout);

        this.context = context;
        this.artistList = artistList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // get the layout inflater...
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);

        TextView tvName = listViewItem.findViewById(R.id.tv_name_list_layout);
        TextView tvGenre = listViewItem.findViewById(R.id.tv_geners_list_layout);

        //get the position of the Artist from the list
        Artist artist = artistList.get(position);

        // set the value to the textview...
        tvName.setText(artist.getArtistName());
        tvGenre.setText(artist.getArtistGener());

        return listViewItem;


    }
}
