package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Singup : AppCompatActivity() {
    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    //private lateinit var btnLogin: Button
    private lateinit var btnSingup: Button
    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singup)
        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance()
        edtEmail = findViewById(R.id.edt_email)
        edtName = findViewById(R.id.edt_name)
        edtPassword = findViewById(R.id.edt_password)
        btnSingup = findViewById(R.id.btnSigUp)
        btnSingup.setOnClickListener{
            val name = edtName.text.toString()
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            singUp(name,email,password)
        }



    }

    private fun singUp(name:String,email:String, password:String){
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, go to homepage
                        addUserToDatabase(name,email,mAuth.currentUser?.uid!!)
                  val intent = Intent(this@Singup, MainActivity::class.java)
                    finish()
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@Singup," some erroe", Toast.LENGTH_SHORT).show()
                }
            }
    }
        private fun addUserToDatabase(name:String,email:String,uid:String){
        mDbRef = FirebaseDatabase.getInstance().getReference()
            mDbRef.child("user").child(uid).setValue(User(name,email,uid))
        }


}