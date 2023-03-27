package com.example.activitylauncher

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.activitylauncher.data.User
import com.example.activitylauncher.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference // создали обьект для записи в БД


    private val signInLauncher = registerForActivityResult( //создания обьект авторизации экрана
        FirebaseAuthUIActivityResultContract()
    )
    { res ->
        this.onSignInResult(res) // запуск экрана
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database.reference // инициализация базы данных с помощью Firebase.database.reference

        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build()
        ) // список регистрации

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build() // создание Intent для списка регистрации
        signInLauncher.launch(signInIntent) // запустили екран Firebase.auth

    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in

            val authUser =
                FirebaseAuth.getInstance().currentUser // создание обьекта текущего пользователя
            authUser?.let {
                val email = it.email
                val uid = it.uid
                val firebaseUser = User(email, uid)
                database.child("users").child(uid).setValue(firebaseUser)

                val intentToAnotherScreen = Intent(this, MoviesActivity::class.java)
                startActivity(intentToAnotherScreen)
               }

            } else {
                Toast.makeText(
                    this,"Something wrong with registration", Toast.LENGTH_SHORT
                ).show()
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...

            }
    }
}


