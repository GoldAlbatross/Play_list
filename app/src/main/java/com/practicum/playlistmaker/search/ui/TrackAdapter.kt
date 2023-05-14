package com.practicum.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.utils.Debouncer
import com.practicum.playlistmaker.utils.debounceClickListener
import java.text.SimpleDateFormat
import java.util.Collections
import java.util.Locale

class TrackAdapter : RecyclerView.Adapter<TrackViewHolder>() {
    internal val trackList = mutableListOf<Track>()
    internal var listener: ((Track) -> Unit)? = null
    private val debouncer = Debouncer()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun getItemCount(): Int = trackList.size
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = trackList[holder.adapterPosition]
        holder.bind(track)
        holder.itemView.debounceClickListener(debouncer) { listener!!.invoke(track) }
    }

    fun replaceItem(sourcePosition: Int, targetPosition: Int) {
        Collections.swap(trackList, sourcePosition, targetPosition)
        notifyItemMoved(sourcePosition, targetPosition)
    }

    fun removeAt(position: Int) {
        trackList.removeAt(position)
        notifyItemRemoved(position)
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
        trackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTime).toString()
        Glide
            .with(itemView.context)
            .load(model.url)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(10))
            .into(artwork)
    }
}

