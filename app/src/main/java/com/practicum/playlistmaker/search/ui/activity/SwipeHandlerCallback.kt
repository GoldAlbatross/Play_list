package com.practicum.playlistmaker.search.ui.activity

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.ui.router.SearchRouter
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel


class SwipeHandlerCallback(
    private val context: SearchActivity,
    private val handler: Handler,
    private val router: SearchRouter,
    private val viewModel: SearchViewModel,
    private val trackAdapter: TrackAdapter,
) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
    ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
) {

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

        if (dX > 30f) { // to the right

            // Draw the blue background
            background.color = context.getColor(R.color.blue_background)
            background.setBounds(
                itemView.left + dX.toInt(),
                itemView.top,
                itemView.left,
                itemView.bottom)
            background.draw(c)

            // Draw the icon
            if (dX > 150f) {
                drawLeftIcon(R.drawable.player_swipe_play, 44, itemView, itemHeight, c)
            }
        }

        // swipe to the left
        if (dX < -30f) {

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
                drawRightIcon(R.drawable.recycler_swipe_delete,40,itemView,itemHeight, c)
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }

    private fun drawLeftIcon(id: Int, margin: Int, view: View, viewHeight: Int, c: Canvas) {

        // Calculate position of left icon
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
    private fun drawRightIcon(id: Int, margin: Int, view: View, viewHeight: Int, c: Canvas) {

        // Calculate position of right icon
        val icon = ContextCompat.getDrawable(context, id)!!
        val iconWidth = icon.intrinsicWidth
        val iconHeight = icon.intrinsicHeight
        val top = view.top + (viewHeight - iconHeight) / 2
        val right = view.right - margin
        val left = view.right - margin - iconWidth
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
        if (direction == RIGHT) {
            viewModel.onSwipeRight(track = track)
            router.openPlayerActivity(track)
            trackAdapter.notifyItemRangeChanged(0,position+1)
        }

        // delete item
        if (direction == LEFT) {
            viewModel.onSwipeLeft(track = track)
            trackAdapter.removeAt(position)
        }
    }

    private companion object {
        const val RIGHT = 8
        const val LEFT = 4
    }


}