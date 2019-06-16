package com.example.mainactivity.profile

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import android.widget.Toast.makeText
import com.bumptech.glide.Glide
import com.example.mainactivity.R
import com.example.mainactivity.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    lateinit var databaseRef: DatabaseReference
    lateinit var firebaseDatabase: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initView()

        initFirebase()

        registerFirebase()
    }

    private fun registerFirebase() {
        databaseRef.child("Users")
            .child(mAuth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(User::class.java) as User
                        tvFullName.text = user.fullName
                        tvUserName.text = user.userName
                        tvPhone.text = user.email
                        if(user.imageURL =="default"){
                            profile.setImageResource(R.drawable.avatar)
                        }else{
                            Glide.with(this@ProfileActivity)
                                .load(user.imageURL)
                                .circleCrop()
                                .into(profile)
                        }
                    }
                }
            })
    }

    private fun initFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseRef = firebaseDatabase.reference
        mAuth = FirebaseAuth.getInstance()
    }

    private fun initView() {
        editP.setOnClickListener {
            val intentP = Intent(this@ProfileActivity, EditActivity::class.java)
            startActivity(intentP)
        }

    }

}
