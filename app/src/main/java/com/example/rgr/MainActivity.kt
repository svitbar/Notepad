package com.example.rgr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rgr.databinding.ActivityMainBinding
import com.example.rgr.db.DbManager
import com.example.rgr.db.MyAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val myDbManager = DbManager(this)
    private val myAdapter = MyAdapter(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar()
        init()
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
        fillAdapter()
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    private fun setToolbar() {
        val toolbar: Toolbar = binding.toolbar

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.isVisible = false

        showToolbar()
    }

    private fun showToolbar() {
        binding.showToolbar.setOnClickListener {
            binding.toolbar.isVisible = !binding.toolbar.isVisible
        }
    }

    private fun init() {
        binding.recyclerNote.layoutManager = LinearLayoutManager(this)
        binding.recyclerNote.adapter = myAdapter
    }

    private fun fillAdapter() {
        myAdapter.updateAdapter(myDbManager.readDbData())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.createNote -> {
                val new = Intent(this, EditActivity::class.java)
                startActivity(new)
            }
            R.id.searchNote -> {}
            R.id.setColor -> {}
            R.id.editNote -> {}
            R.id.deleteNote -> {}
            R.id.move -> {
                //binding.toolbar.setOnTouchListener(CustomTouchListener())
            }
        }
        return super.onOptionsItemSelected(item)
    }
}