package com.practicum.playlistmaker.screens.player.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.BottomSheetAlbumCardBinding
import com.practicum.playlistmaker.features.storage.local_db.domain.model.Album
import com.practicum.playlistmaker.utils.Debouncer
import com.practicum.playlistmaker.utils.debounceClickListener

class BottomSheetAdapter(
    private val debouncer: Debouncer,
) : RecyclerView.Adapter<BottomSheetViewHolder>() {

    internal var playList = listOf<Album>()
    internal var action: ((Album) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        return BottomSheetViewHolder(
            BottomSheetAlbumCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = playList.size
    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        val pos = holder.adapterPosition
        val item = playList[pos]
        holder.bind(item)
        holder.itemView.debounceClickListener(debouncer) {
            action!!.invoke(item)
        }
    }
}

class BottomSheetViewHolder(
    private val binding: BottomSheetAlbumCardBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(album: Album) {
        binding.name.text = album.name
        binding.count.text = album.trackCount.toString()
        Glide.with(itemView.context)
            .load(album.uri)
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(4))
            .into(binding.picture)

    }
}