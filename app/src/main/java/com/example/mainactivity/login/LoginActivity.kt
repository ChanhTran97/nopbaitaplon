package com.example.mainactivity.login


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.example.mainactivity.R
import com.example.mainactivity.login.ForgetPasswordFragment
import com.example.mainactivity.movie.MainActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity(),
    LoginFragment.Listener,
    RegisterFragment.Listener, ForgetPasswordFragment.Listener {
    lateinit var auth: FirebaseAuth
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser!=null){
            val intentMain = Intent(this, MainActivity::class.java)
            startActivity(intentMain)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()

        addFirstFragment()
    }

    private fun addFirstFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.flContainer, LoginFragment())
        fragmentTransaction.commit()

    }


    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)
        if (fragment is LoginFragment) {
            val loginfragment = fragment
            loginfragment.setListener(this)

        }
        if (fragment is RegisterFragment) {
            val registerFragment = fragment
            registerFragment.setListener(this)

        }
        if (fragment is ForgetPasswordFragment) {
            val forgetFragment = fragment
            forgetFragment.setListener(this)

        }
    }

    override fun openRegisterScreen() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            R.anim.enter,
            R.anim.exit,
            R.anim.pop_enter,
            R.anim.pop_exit
        )
        fragmentTransaction.replace(
            R.id.flContainer,
            RegisterFragment()
        )
        fragmentTransaction.addToBackStack(RegisterFragment::class.java.simpleName)
        fragmentTransaction.commit()
    }

    override fun openLoginScreen() {
        onBackPressed()

    }

    override fun openForgotScreen() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            R.anim.enter,
            R.anim.exit,
            R.anim.pop_enter,
            R.anim.pop_exit
        );
        fragmentTransaction.replace(
            R.id.flContainer,
            ForgetPasswordFragment()
        )
        fragmentTransaction.addToBackStack(ForgetPasswordFragment::class.java.simpleName)
        fragmentTransaction.commit()
    }
}
