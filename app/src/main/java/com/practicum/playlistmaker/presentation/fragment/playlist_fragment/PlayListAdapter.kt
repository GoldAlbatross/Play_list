package com.practicum.playlistmaker.presentation.fragment.playlist_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.CreateItemBinding
import com.practicum.playlistmaker.domain.local_db.model.Album
import com.practicum.playlistmaker.utils.Debouncer
import com.practicum.playlistmaker.utils.debounceClickListener

class PlayListAdapter(
    private val debouncer: Debouncer,
) : RecyclerView.Adapter<PlayListViewHolder>() {

    internal var playList = listOf<Album>()
    internal var action: ((Album) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        return PlayListViewHolder(
            CreateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = playList.size
    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        val pos = holder.adapterPosition
        val item = playList[pos]
        holder.bind(item)
        holder.itemView.debounceClickListener(debouncer) {
            action!!.invoke(item)
        }
    }
}

class PlayListViewHolder(
    private val binding: CreateItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(album: Album) {
        binding.name.text = album.name
        binding.count.text = album.trackCount.toString()
        Glide.with(itemView.context)
            .load(album.uri)
            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .into(binding.photo)

    }
}