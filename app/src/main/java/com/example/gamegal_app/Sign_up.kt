package com.example.gamegal_app
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*
class Sign_up : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        signin_link_btn.setOnClickListener {
            startActivity(Intent(this,Sign_in::class.java))
        }
        signup_btn.setOnClickListener {
            CreateAccount()
        }
    }
    private fun CreateAccount() {
        val fullName = fullname_signup.text.toString()
        val userName = username_signup.text.toString()
        val email = email_signup.text.toString()
        val password = password_signup.text.toString()

        when{
            TextUtils.isEmpty(fullName) -> Toast.makeText(this,"İsim Gerekli", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(userName) -> Toast.makeText(this,"Kullanıcı adı Gerekli.", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(email) -> Toast.makeText(this,"Email Gerekli.", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(password) -> Toast.makeText(this,"Şifre Gerekli.", Toast.LENGTH_LONG).show()

            else ->{
                val progressDialog= ProgressDialog(this@Sign_up)
                progressDialog.setTitle("SignUp")
                progressDialog.setMessage("Lütfen bekleyiniz.")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()
                val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
                mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener { task->
                        if(task.isSuccessful){
                            saveUserInfo(fullName,userName,email,progressDialog)

                        }else{
                            val message=task.exception!!.toString()
                            Toast.makeText(this,"Error: $message", Toast.LENGTH_LONG).show()
                            mAuth.signOut()
                            progressDialog.dismiss()
                        }
                    }
            }
        }
    }
    private fun saveUserInfo(fullName: String, userName: String, email: String, progressDialog: ProgressDialog) {

        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val usersRef : DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")
        val userMap= HashMap<String,Any>()
        userMap["uid"] = currentUserId
        userMap["fullname"] = fullName.toLowerCase()
        userMap["username"] = userName.toLowerCase()
        userMap["email"] = email
        userMap["bio"] = "Hey! Burası benim kişisel alanım."
        userMap["image"] ="https://firebasestorage.googleapis.com/v0/b/gamegal-26535.appspot.com/o/Default%20Images%2Fprofile.png?alt=media&token=4e83b86e-d2bb-40c7-b481-2cdb38d64cc4"

        usersRef.child(currentUserId).setValue(userMap)
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    progressDialog.dismiss()
                    Toast.makeText(this,"Hesap başarıyla oluşturuldu.Oyun dünyasına hoşgeldin",Toast.LENGTH_LONG).show()

                    FirebaseDatabase.getInstance().reference
                        .child("Follow").child(currentUserId)
                        .child("Following").child(currentUserId)
                        .setValue(true)

                    val intent = Intent(this@Sign_up,MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }else{
                    val message=task.exception!!.toString()
                    Toast.makeText(this,"Error: $message",Toast.LENGTH_LONG).show()
                    FirebaseAuth.getInstance().signOut()
                    progressDialog.dismiss()
                }
            }
    }
}