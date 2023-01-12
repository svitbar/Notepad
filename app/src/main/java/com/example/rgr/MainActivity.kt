package com.example.rgr

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rgr.databinding.ActivityMainBinding
import com.example.rgr.db.DbManager
import com.example.rgr.db.MyAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val myDbManager = DbManager(this)
    private val myAdapter = MyAdapter(ArrayList(), this)
    private lateinit var movingToolbar: MovingToolbar
    private var job: Job? = null
    private var isChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar()
        init()
        initSearchView()
        movingToolbar = MovingToolbar(binding.toolbar)
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
        fillAdapter("")
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

        val swapHelper = getSwapManager()
        swapHelper.attachToRecyclerView(binding.recyclerNote)

        binding.recyclerNote.adapter = myAdapter
    }

    private fun initSearchView() {
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                fillAdapter(newText!!)

                return true
            }

        })
    }

    private fun fillAdapter(text: String) {

        job?.cancel()
        job = CoroutineScope(Dispatchers.Main).launch {
            myAdapter.updateAdapter(myDbManager.readDbData(text))
        }
    }

    private fun getSwapManager(): ItemTouchHelper {
        return ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                myAdapter.removeItem(viewHolder.adapterPosition, myDbManager)
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val colorState = ResourcesCompat.getColor(resources, R.color.hint_color, null)

        when(item.itemId) {
            R.id.createNote -> {
                val new = Intent(this, EditActivity::class.java)
                startActivity(new)
            }
            R.id.searchNote -> {
                binding.searchView.isGone = !binding.searchView.isGone

                if(!binding.searchView.isGone) item.icon.setTint(colorState)
                else item.icon.setTint(Color.WHITE)
            }
            R.id.move -> {
                isChecked = !isChecked

                if(isChecked) item.icon.setTint(colorState)
                else item.icon.setTint(Color.WHITE)

                movingToolbar.moveItem(isChecked)
            }
            R.id.exit -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}