package com.practicum.playlistmaker.adapter

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.PlayerActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.SearchActivity
import com.practicum.playlistmaker.model.Track


class SwipeHandlerCallback(
    private val context: Context,
    private val trackAdapter: TrackAdapter
) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
    ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
) {
    //private lateinit var icon: Drawable
    private var iconWidth = 0
    private var iconHeight = 0
    private val background = ColorDrawable()
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isCanceled = dX == 0f && !isCurrentlyActive

        if (isCanceled) {
            clearCanvas(
                c,
                itemView.right + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()
            )
            clearCanvas(
                c,
                itemView.left + dX,
                itemView.top.toFloat(),
                itemView.left.toFloat(),
                itemView.bottom.toFloat()
            )
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        if (dX > 20f) { // to the right

            // Draw the blue background
            background.color = context.getColor(R.color.blue_background)
            background.setBounds(
                itemView.left + dX.toInt(),
                itemView.top,
                itemView.left,
                itemView.bottom)
            background.draw(c)

            // Draw the icon
            if (dX > 240f) {
                drawIcon(R.drawable.player_notes1, 44, itemView, itemHeight, c)
                if (dX > 550f) {
                    drawIcon(R.drawable.player_notes3, 160, itemView, itemHeight, c)
                    if (dX > 700f) {
                        drawIcon(R.drawable.player_notes2, 300, itemView, itemHeight, c)
                    }
                }
            }
        }

        // swipe to the left
        if (dX < -20f) {

            // Draw the gray background
            background.color = context.getColor(R.color.text_gray)
            background.setBounds(
                itemView.right + dX.toInt(),
                itemView.top,
                itemView.right,
                itemView.bottom)
            background.draw(c)

            // Draw the icon
            if (dX < -130f) {
                drawIcon(R.drawable.recycler_swipe_delete,40,itemView,itemHeight, c)
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }

    private fun drawIcon(id: Int, margin: Int, view: View, viewHeight: Int, c: Canvas) {

        // Calculate position of play icon
        val icon = ContextCompat.getDrawable(context, id)!!
        val iconWidth = icon.intrinsicWidth
        val iconHeight = icon.intrinsicHeight
        val top = view.top + (viewHeight - iconHeight) / 2
        val right = view.left + margin + iconWidth
        val left = view.left + margin
        val bottom = top + iconHeight

        // Draw the icon
        icon.setBounds(left, top, right, bottom)
        icon.draw(c)
    }

    override fun onMove(
        recycler: RecyclerView,
        source: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        trackAdapter.replaceItem(source.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val track = trackAdapter.trackList[position]

        // play item
        if (direction == 8) {
            val intent = Intent(viewHolder.itemView.context, PlayerActivity::class.java)
            intent.putExtra(SearchActivity.KEY_TRACK, track)
            startActivity(viewHolder.itemView.context, intent, intent.extras)
            App.instance.trackStorage.addTrack(track)
            trackAdapter.replaceItem(position, position)
        }

        // delete item
        if (direction == 4) {
            App.instance.trackStorage.removeTrack(track)
            trackAdapter.removeAt(position)
        }
    }


}