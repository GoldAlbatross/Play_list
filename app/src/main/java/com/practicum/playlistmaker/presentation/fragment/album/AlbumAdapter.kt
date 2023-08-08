package com.practicum.playlistmaker.presentation.fragment.album

import com.practicum.playlistmaker.presentation.fragment.favorite.FavoriteTrackAdapter
import com.practicum.playlistmaker.presentation.fragment.favorite.TrackViewHolder

class AlbumAdapter: FavoriteTrackAdapter() {
    var longPress: ((Int) -> Unit)? = null

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val pos = holder.adapterPosition
        val track = trackList[pos]
        holder.bind(track)
        holder.itemView.setOnLongClickListener { longPress!!.invoke(track.trackId); true }
    }
}