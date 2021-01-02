package br.com.fiap.activities

import android.content.Context
import android.os.Bundle
import br.com.fiap.R
import br.com.fiap.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences =
            getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)

        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "")!!

        tv_main.text= "The logged in user is $username."
    }
}