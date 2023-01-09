package com.example.rgr

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.example.rgr.databinding.ActivityEditBinding
import com.example.rgr.db.DbManager
import com.example.rgr.db.MyIntentConstants

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
        getMyIntents()
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
                finish()
            }
        }
    }

    private fun editImage() {
        binding.editImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.type = "image/*"
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            startActivityForResult(intent, imageRequestCode)
        }
    }

    private fun getMyIntents() {

        val i = intent

        if (i != null) {
            if (i.getStringExtra(MyIntentConstants.I_TITLE_KEY) != null) {
                binding.addImage.visibility = View.GONE

                binding.edTitle.setText(i.getStringExtra(MyIntentConstants.I_TITLE_KEY))
                binding.edContent.setText(i.getStringExtra(MyIntentConstants.I_DESC_KEY))

                if (i.getStringExtra(MyIntentConstants.I_URI_KEY) != "empty") {
                    binding.myImageLayout.visibility = View.VISIBLE
                    binding.editImage.setImageURI(Uri.parse(i.getStringExtra(MyIntentConstants.I_URI_KEY)))
                    binding.editImage.visibility = View.GONE
                    binding.deleteImage.visibility = View.GONE
                }
            }
        }
    }
}