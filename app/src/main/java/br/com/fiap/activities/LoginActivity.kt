package br.com.fiap.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import br.com.fiap.R
import br.com.fiap.firestore.FirestoreClass
import br.com.fiap.models.User
import br.com.fiap.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.et_email
import kotlinx.android.synthetic.main.activity_login.et_password
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        @Suppress("DEPRECATION")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        tv_forgot_password.setOnClickListener (this)

        btn_login.setOnClickListener(this)

        tv_register.setOnClickListener(this)
    }

    fun userLoggedInSuccess(user: User) {

        hideProgressDialog()

        if (user.profileCompleted == 0) {

            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
            startActivity(intent)
        } else {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
        finish()
    }

    override fun onClick(v: View?){
        if(v != null) {
            when (v.id){


                R.id.tv_forgot_password ->{
                    val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                    startActivity(intent)
                }

                R.id.btn_login -> {
                    logInRegisteredUser()
                }

                R.id.tv_register -> {

                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_email.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(et_password.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                showErrorSnackBar("Suas credenciais são válidas", false)
                true
            }
        }
    }

    private fun logInRegisteredUser(){

        if(validateLoginDetails()) {

            showProgressDialog(resources.getString(R.string.please_wait))

            val email = et_email.text.toString().trim { it <= ' '}
            val password = et_password.text.toString().trim { it <= ' '}

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener{ task ->

                        if(task.isSuccessful) {
                            FirestoreClass().getUserDetails(this@LoginActivity)
                        } else {
                            hideProgressDialog()
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }

                    }
        }

    }
}