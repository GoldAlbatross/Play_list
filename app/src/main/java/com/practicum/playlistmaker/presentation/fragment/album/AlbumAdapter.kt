package com.practicum.playlistmaker.presentation.fragment.album

import com.practicum.playlistmaker.presentation.fragment.favorite.FavoriteTrackAdapter
import com.practicum.playlistmaker.presentation.fragment.favorite.TrackViewHolder
import com.practicum.playlistmaker.utils.Debouncer
import com.practicum.playlistmaker.utils.debounceClickListener

class AlbumAdapter(
    private val debouncer: Debouncer
): FavoriteTrackAdapter() {
    var longPress: ((Int) -> Unit)? = null

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val pos = holder.adapterPosition
        val track = trackList[pos]
        holder.bind(track)
        holder.itemView.debounceClickListener(debouncer) { action?.invoke(track) }
        holder.itemView.setOnLongClickListener { longPress!!.invoke(track.trackId); true }
    }
}