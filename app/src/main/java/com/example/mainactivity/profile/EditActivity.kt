package com.example.mainactivity.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.mainactivity.R
import com.example.mainactivity.models.User
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_edit.*

class EditActivity : AppCompatActivity() {
    private lateinit var UserDatabase: DatabaseReference
    private lateinit var StorageImage: StorageReference
    private val GALLERY_PICK : Int = 1
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        //toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.title = "Edit Informations"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        auth = FirebaseAuth.getInstance()
        //get Data from FirebaseDatabase
        val current = FirebaseAuth.getInstance().currentUser
        val currentId = current!!.uid
        UserDatabase = FirebaseDatabase.getInstance().getReference("Users")
        StorageImage = FirebaseStorage.getInstance().reference
        UserDatabase.child(currentId).addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(snap: DataSnapshot) {
                val user : User = snap.getValue(User::class.java)!!
                if(user.imageURL == "default"){
                    editImage.setImageResource(R.drawable.avatar)
                }else{
                    Glide.with(applicationContext)
                        .load(user.imageURL)
                        .centerCrop()
                        .into(editImage)
                }
                my_name.setText(user.userName)
            }
        })

        //delete name
        change_name.setOnClickListener {
            my_name.setText("")
            change_name.visibility = View.GONE
        }
        // update avatar
        change_image.setOnClickListener {
            val galleryIntent = Intent()
            galleryIntent.type = "image/*"
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGE"), GALLERY_PICK)
        }
        // saved change
        upInfo.setOnClickListener {
            val txt_name = my_name.text.toString()
            UserDatabase.child(currentId).child("userName").setValue(txt_name)
                .addOnSuccessListener {
                    Toast.makeText(this@EditActivity, "Informations were updated Successfully ", Toast.LENGTH_SHORT).show()
                    val intentP = Intent(this@EditActivity,ProfileActivity::class.java)
                    startActivity(intentP)
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val current = auth.currentUser
        val currentId = current!!.uid
        if(requestCode==GALLERY_PICK && resultCode== Activity.RESULT_OK){

            val imageUri: Uri? = data?.data
            CropImage.activity(imageUri)
                .setAspectRatio(1,1)
                .start(this)
        }
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            val result:  CropImage.ActivityResult = CropImage.getActivityResult(data)
            if(resultCode == RESULT_OK){
                val resultUri : Uri = result.uri
                //  upload file and get a download URL
                val  filepath: StorageReference = StorageImage.child("profile_image").child("$currentId.jpg")
                val uploadTask: UploadTask = filepath.putFile(resultUri)
                uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    return@Continuation filepath.downloadUrl
                }).addOnCompleteListener { task ->
                    // write link image to ImageURL children of node currentId
                    if(task.isSuccessful){
                        val download_Uri : Uri? = task.result
                        val downloadUrl: String = download_Uri.toString()
                        val userDatabase: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
                            .child(currentId)
                        userDatabase.child("imageURL").setValue(downloadUrl).addOnCompleteListener(this){tasks ->
                            if(tasks.isSuccessful){
                                Toast.makeText(this@EditActivity, "Avatar was updated successfully", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    else{
                        Toast.makeText(this@EditActivity, "there are some error in changing image", Toast.LENGTH_SHORT).show()
                    }

                }
            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                // val error: Exception = result.error
            }
        }
    }
}
