package com.practicum.playlistmaker.screens.mediaLib.childFragments

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.features.itunes_api.domain.model.Track
import com.practicum.playlistmaker.utils.Debouncer
import com.practicum.playlistmaker.utils.debounceClickListener
import com.practicum.playlistmaker.utils.getTimeFormat
import java.util.Collections

class FavoriteTrackAdapter: RecyclerView.Adapter<TrackViewHolder>() {

    internal val trackList = mutableListOf<Track>()
    internal var action: ((Track) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun getItemCount(): Int = trackList.size
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val pos = holder.adapterPosition
        val track = trackList[pos]
        holder.bind(track)
        holder.itemView.setOnClickListener { action!!.invoke(track) }
    }

}

class TrackViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.artist_card, parent, false)
) {
    private val trackName = itemView.findViewById<TextView>(R.id.song)
    private val artistName = itemView.findViewById<TextView>(R.id.artist)
    private val trackTime = itemView.findViewById<TextView>(R.id.time)
    private val artwork = itemView.findViewById<ImageView>(R.id.picture)

    fun bind(model: Track) {
        trackName.text = model.track
        artistName.text = model.artist
        trackTime.text = model.trackTime.getTimeFormat()
        Glide
            .with(itemView.context)
            .load(model.url)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(10))
            .into(artwork)
    }
}
