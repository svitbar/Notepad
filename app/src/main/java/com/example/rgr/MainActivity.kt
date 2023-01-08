package com.example.rgr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import com.example.rgr.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar()
    }

    private fun setToolbar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        binding.toolbar.isVisible = false

        showToolbar()
        title = ""
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.createNote -> {}
            R.id.searchNote -> {}
            R.id.setColor -> {}
            R.id.editNote -> {}
            R.id.deleteNote -> {}
            R.id.move -> {
                binding.toolbar.setOnTouchListener(CustomTouchListener())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showToolbar() {
        binding.showToolbar.setOnClickListener {
            binding.toolbar.isVisible = !binding.toolbar.isVisible
        }
    }

    private var xDelta = 0
    private var yDelta = 0

    private inner class CustomTouchListener : View.OnTouchListener {
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            val x = event.rawX.toInt()
            val y = event.rawY.toInt()
            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {
                    val lParams = v.layoutParams as RelativeLayout.LayoutParams
                    xDelta = x - lParams.leftMargin
                    yDelta = y - lParams.topMargin
                }
                MotionEvent.ACTION_UP -> {}
                MotionEvent.ACTION_POINTER_DOWN -> {}
                MotionEvent.ACTION_POINTER_UP -> {}
                MotionEvent.ACTION_MOVE -> {
                    val layoutParams = v.layoutParams as RelativeLayout.LayoutParams
                    layoutParams.leftMargin = x - xDelta
                    layoutParams.topMargin = y - yDelta
                    layoutParams.rightMargin = 0
                    layoutParams.bottomMargin = 0
                    v.layoutParams = layoutParams
                }
            }
            binding.relativeLayout.invalidate()
            return true
        }
    }
}