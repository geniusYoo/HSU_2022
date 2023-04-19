package com.example.firebasetest

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val checkPassword = findViewById<EditText>(R.id.checkpw)
        val nickname = findViewById<EditText>(R.id.nickname)
        val correctLabel = findViewById<TextView>(R.id.correctLabel)
        val signupBtn = findViewById<Button>(R.id.signupButton)
        val checkBtn = findViewById<Button>(R.id.checkButton)

        // 비밀번호 일치하는지 확인
        checkBtn.setOnClickListener {
            if(password.text.toString().equals(checkPassword.text.toString())) { // 일치하면 correctLabel에 표시
                correctLabel.setText("equal password !!")
            }
            else { // 일치하지 않으면 checkpw, pw 둘다 비우고 correctLabel에 표시
                correctLabel.setText("not equal password ..")
                checkPassword.setText("")
                password.setText("")
            }
        }

        // 비밀번호가 일치하고 signup 버튼을 누르면 firebase 회원가입
        signupBtn.setOnClickListener { // signup 버튼 눌리면
            if(correctLabel.text.toString().equals("equal password !!")) {
                Firebase.auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener {

                        if (it.isSuccessful) {
                            Firebase.auth.signInWithEmailAndPassword(
                                email.text.toString(),
                                password.text.toString()
                            )
                            val currentUser = Firebase.auth.currentUser
                            val db = Firebase.firestore
                            val user =
                                User(currentUser?.uid, email.text.toString(), nickname.text.toString())

                            if (currentUser != null) {
                                db.collection("user").document(currentUser.uid).set(user)
                                    .addOnCompleteListener {
                                        startActivity(
                                            Intent(this, MainActivity::class.java)
                                        )
                                    }
                                    .addOnFailureListener {
                                        correctLabel.setText("Failed. - check your informations. ")
                                    }
                            } else {
                                correctLabel.setText("Failed. - Current User is NULL. Retry.")
                            }
                        } else {
                            correctLabel.setText("Failed.")

                         }
                }
            }
        }
    }
}