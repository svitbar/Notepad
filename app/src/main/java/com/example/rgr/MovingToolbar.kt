package com.example.rgr

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View

class MovingToolbar(private var viewItem: View) {

    private var rightDX = 0f
    private var rightDY  = 0f

    @SuppressLint("ClickableViewAccessibility")
    fun moveItem(state: Boolean) {
        viewItem.setOnTouchListener(View.OnTouchListener { view, event ->
            if (state){
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {

                        rightDX = view!!.x - event.rawX
                        rightDY = view.y - event.rawY

                    }
                    MotionEvent.ACTION_MOVE -> {

                        val displacement = event.rawX + rightDX

                        view!!.animate()
                            .x(displacement)
                            .y(event.rawY + rightDY)
                            .setDuration(0)
                            .start()
                    }
                }
                return@OnTouchListener true
            }
            true
        })
    }
}