package com.example.rgr

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.example.rgr.databinding.ActivityEditBinding
import com.example.rgr.db.DbManager
import com.example.rgr.db.MyIntentConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private val myDbManager = DbManager(this)
    private val imageRequestCode = 10
    private var id = 0
    private var tempImageUri = "empty"
    private var isEditState = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        saveData()
        addImage()
        editImage()
        editFields()
        getMyIntents()
        deleteImage()
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == imageRequestCode) {
            binding.imgView.setImageURI(data?.data)
            tempImageUri = data?.data.toString()
            contentResolver.takePersistableUriPermission(data?.data!!, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    private fun addImage() {
        binding.addImage.setOnClickListener {
            binding.myImageLayout.isVisible = !binding.myImageLayout.isVisible
            binding.addImage.visibility = View.GONE
        }
    }

    private fun saveData() {
        binding.save.setOnClickListener {
            val myTitle = binding.edTitle.text.toString()
            val myContent = binding.edContent.text.toString()

            if (myTitle != "" && myContent != "") {

                CoroutineScope(Dispatchers.Main).launch {
                    if (isEditState) myDbManager.updateItem(
                        myTitle,
                        myContent,
                        tempImageUri,
                        id,
                        getCurrentTime()
                    )
                    else myDbManager.insertToDb(myTitle, myContent, tempImageUri, getCurrentTime())

                    finish()
                }
            }
        }
    }

    private fun editFields() {

        binding.editFields.setOnClickListener {

            binding.editFields.visibility = View.GONE
            binding.addImage.visibility = View.VISIBLE

            binding.edTitle.isEnabled = true
            binding.edContent.isEnabled = true

            if (tempImageUri == "empty") return@setOnClickListener
            binding.editImage.visibility = View.VISIBLE
            binding.deleteImage.visibility = View.VISIBLE
        }
    }

    private fun editImage() {
        binding.editImage.setOnClickListener {

            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.type = "image/*"
            startActivityForResult(intent, imageRequestCode)
        }
    }

    private fun deleteImage() {
        binding.deleteImage.setOnClickListener {

            binding.myImageLayout.visibility = View.GONE
            binding.addImage.visibility = View.VISIBLE

            tempImageUri = "empty"
        }
    }

    private fun getMyIntents() {

        binding.editFields.visibility = View.GONE

        val i = intent

        if (i != null) {
            if (i.getStringExtra(MyIntentConstants.I_TITLE_KEY) != null) {
                isEditState = true

                binding.edTitle.isEnabled = false
                binding.edContent.isEnabled = false

                binding.addImage.visibility = View.GONE
                binding.editFields.visibility = View.VISIBLE

                binding.edTitle.setText(i.getStringExtra(MyIntentConstants.I_TITLE_KEY))
                binding.edContent.setText(i.getStringExtra(MyIntentConstants.I_DESC_KEY))

                id = i.getIntExtra(MyIntentConstants.I_ID_KEY, 0)

                if (i.getStringExtra(MyIntentConstants.I_URI_KEY) != "empty") {
                    binding.myImageLayout.visibility = View.VISIBLE

                    tempImageUri = i.getStringExtra(MyIntentConstants.I_URI_KEY)!!
                    binding.imgView.setImageURI(Uri.parse(tempImageUri))

                    binding.editImage.visibility = View.GONE
                    binding.deleteImage.visibility = View.GONE
                }
            }
        }
    }

    private fun getCurrentTime(): String {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd-MM-yy kk:mm", Locale.getDefault())

        return formatter.format(time)
    }
}