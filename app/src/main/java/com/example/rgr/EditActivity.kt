package com.example.rgr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.example.rgr.databinding.ActivityEditBinding
import com.example.rgr.db.DbManager

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private val myDbManager = DbManager(this)
    private val imageRequestCode = 10
    var tempImageUri = "empty"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        saveData()
        addImage()
        editImage()
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

        if (resultCode == RESULT_OK && requestCode == imageRequestCode) {
            binding.editImage.setImageURI(data?.data)
            tempImageUri = data?.data.toString()
        }
    }

    private fun addImage() {
        binding.addImage.setOnClickListener {
            binding.myImageLayout.isVisible = !binding.myImageLayout.isVisible

        }
    }

    private fun saveData() {
        binding.save.setOnClickListener {
            val myTitle = binding.edTitle.text.toString()
            val myContent = binding.edContent.text.toString()

            if (myTitle != "" && myContent != "") {
                myDbManager.insertToDb(myTitle, myContent, tempImageUri)
            }
        }
    }

    private fun editImage() {
        binding.editImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, imageRequestCode)
        }
    }
}