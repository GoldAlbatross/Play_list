package com.practicum.playlistmaker.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R


class SwipeHandlerCallback(
    private val context: Context,
    private val trackAdapter: TrackAdapter
) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
    ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
) {
    private lateinit var icon: Drawable
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
            background.color = context.getColor(R.color.blue)
            background.setBounds(
                itemView.left + dX.toInt(),
                itemView.top,
                itemView.left,
                itemView.bottom
            )
            background.draw(c)

            // Calculate position of play icon
            icon = ContextCompat.getDrawable(context, R.drawable.recycler_swipe_play)!!
            iconWidth = icon.intrinsicWidth
            iconHeight = icon.intrinsicHeight
            val iconMargin = 44
            val top = itemView.top + (itemHeight - iconHeight) / 2
            val right = itemView.left + iconMargin + iconWidth
            val left = itemView.left + iconMargin
            val bottom = top + iconHeight

            // Draw the icon
            if (dX > 140f) {
                icon.setBounds(left, top, right, bottom)
                icon.draw(c)
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
                itemView.bottom
            )
            background.draw(c)

            // Calculate position of delete icon
            val icon = ContextCompat.getDrawable(context, R.drawable.recycler_swipe_delete)!!
            iconWidth = icon.intrinsicWidth
            iconHeight = icon.intrinsicHeight
            val iconMargin = 44
            val top = itemView.top + (itemHeight - iconHeight) / 2
            val left = itemView.right - iconMargin - iconWidth
            val right = itemView.right - iconMargin
            val bottom = top + iconHeight

            // Draw the icon
            if (dX < -140f) {
                icon.setBounds(left, top, right, bottom)
                icon.draw(c)
            }

        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
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
        trackAdapter.removeAt(viewHolder.adapterPosition)
    }


}