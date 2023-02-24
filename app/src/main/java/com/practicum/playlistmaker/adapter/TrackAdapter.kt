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
import java.text.SimpleDateFormat
import java.util.Locale

class TrackAdapter: RecyclerView.Adapter<TrackViewHolder>() {
    internal val trackList = mutableListOf<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent)
    override fun getItemCount(): Int = trackList.size
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = trackList[holder.adapterPosition]
        holder.bind(track)
    }

}

class TrackViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.artist_card, parent, false)
) {
    private val trackName = itemView.findViewById<TextView>(R.id.song)
    private val artistName = itemView.findViewById<TextView>(R.id.artist)
    private val trackTime = itemView.findViewById<TextView>(R.id.time)
    private val artwork = itemView.findViewById<ImageView>(R.id.picture)

    fun bind(model: Track) {
        trackName.text = model.track
        artistName.text = model.artist
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTime).toString()
        Glide
            .with(itemView.context)
            .load(model.url)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(8))
            .into(artwork)
    }
}

