package com.example.firebasetest

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class AddPostActivity : AppCompatActivity() {
    private lateinit var launcher : ActivityResultLauncher<Intent>
    var PICK_IMAGE_FROM_ALBUM = 0
    var storage : FirebaseStorage? = null
    var photoUri : Uri? = null
    var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        var photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.putExtra("outputX", 100)
        photoPickerIntent.putExtra("outputY", 100)

        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)
//        var result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            if(it.resultCode == RESULT_OK) {
//                photoUri = photoPickerIntent?.data
//                findViewById<ImageView>(R.id.addphoto_image).setImageURI(photoUri)
//            }
//            else {
//                finish()
//            }
//        }
        findViewById<Button>(R.id.addButton).setOnClickListener {
            contentUpload()
            System.out.println("add button clicked!!!!!!!")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_FROM_ALBUM) {
            if(resultCode == Activity.RESULT_OK) {
                photoUri = data?.data
                findViewById<ImageView>(R.id.addImageView).setImageURI(photoUri)
            }
            else {
                finish()
            }
        }
    }
        fun contentUpload() {
            var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            var imageFileName = "IMAGE_" + timestamp + "_.png"

            var storageRef = storage?.reference?.child("images")?.child(imageFileName)
            storageRef?.putFile(photoUri!!)?.addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    var contentDTO = ContentDTO()

                    contentDTO.imageUrl = uri.toString()
                    contentDTO.uid = auth?.currentUser?.uid
                    contentDTO.userId = auth?.currentUser?.email
                    contentDTO.explain = findViewById<EditText>(R.id.addTextByUser).text.toString()
                    contentDTO.timestamp = System.currentTimeMillis()

                    firestore?.collection("images")?.document()?.set(contentDTO)
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }



    }

