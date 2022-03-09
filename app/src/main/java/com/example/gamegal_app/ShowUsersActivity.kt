package com.example.gamegal_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamegal_app.Adapter.UserAdapter
import com.example.gamegal_app.Model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ShowUsersActivity : AppCompatActivity() {

    var id: String = ""
    var title: String = ""

    var userAdapter : UserAdapter? = null
    var userList: List<User>? = null
    var idList: List<String>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_users)

        val intent = intent
        id = intent.getStringExtra("id")!!
        title= intent.getStringExtra("title")!!

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = title
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        var recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        userList = ArrayList()
        userAdapter = UserAdapter(this,userList as ArrayList<User>,false)
        recyclerView.adapter = userAdapter

        idList = ArrayList()

        when(title){
            "likes" -> getLikes()
            "following" ->getFollowing()
            "followers" -> getFollowers()
        }
    }


    private fun getFollowers() {
        val followersRef =
            FirebaseDatabase.getInstance().reference
                .child("Follow").child(id!!)
                .child("Followers")


        followersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                (idList as ArrayList<String>).clear()
                for ( snapshot in p0.children){
                    (idList as ArrayList<String>).add(snapshot.key!!)
                }
                showUsers()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun getFollowing() {
        val followersRef =
            FirebaseDatabase.getInstance().reference
                .child("Follow").child(id!!)
                .child("Following")


        followersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                (idList as ArrayList<String>).clear()
                for ( snapshot in p0.children){
                    (idList as ArrayList<String>).add(snapshot.key!!)
                }
                showUsers()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun getLikes() {
        val LikesRef = FirebaseDatabase.getInstance().reference
            .child("Likes").child(id!!)

        LikesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    (idList as ArrayList<String>).clear()
                    for ( snapshot in p0.children){
                        (idList as ArrayList<String>).add(snapshot.key!!)
                    }
                    showUsers()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun showUsers() {
        val usersRef = FirebaseDatabase.getInstance().reference .child("Users")

        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                (userList as ArrayList<User>).clear()
                for(snapshot in dataSnapshot.children){
                    val user = snapshot.getValue(User::class.java)

                    for(id in idList!!){
                        if(user!!.getUID() ==id){
                            (userList as ArrayList<User>).add(user!!)
                        }
                    }

                }
                userAdapter?.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}