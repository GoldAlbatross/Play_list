package com.practicum.playlistmaker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.model.Track

class TrackAdapter(var trackList: List<Track>): RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent)

    override fun getItemCount(): Int = trackList.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) =
        holder.bind(trackList[position])
}

class TrackViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.artist_card, parent, false)
) {
    private val trackName = itemView.findViewById<TextView>(R.id.song)
    private val artistName = itemView.findViewById<TextView>(R.id.artist)
    private val trackTime = itemView.findViewById<TextView>(R.id.time)
    private val artwork = itemView.findViewById<ImageView>(R.id.picture)

    fun bind(obj: Track) {
        trackName.text = obj.trackName
        artistName.text = obj.artistName
        trackTime.text = obj.trackTime
        Glide
            .with(itemView.context)
            .load(obj.artworkUrl)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(4))
            .into(artwork)
    }
}

