package com.example.gamegal_app.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamegal_app.Adapter.PostAdapter
import com.example.gamegal_app.Model.Post
import com.example.gamegal_app.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PostDetailsFragment : Fragment() {
    private var postAdapter : PostAdapter? = null
    private var postList: MutableList<Post>? = null
    private var postId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_post_details, container, false)
        val preferences = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        if(preferences !=null){
            postId = preferences.getString("postId","none").toString()
        }

        var recyclerView: RecyclerView
        recyclerView=view.findViewById(R.id.recycler_view_post_details)
        recyclerView.setHasFixedSize(true)
        var linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
        postList=ArrayList()
        postAdapter = context?.let { PostAdapter(it,postList as ArrayList<Post>) }
        recyclerView.adapter=postAdapter

        retrieveAllPosts()

        return view
    }
    private fun retrieveAllPosts(){
        val postsRef = FirebaseDatabase.getInstance().reference
            .child("Posts").child(postId)

        postsRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                postList?.clear()
                val post = p0.getValue(Post::class.java)
                postList!!.add(post!!)
                postAdapter!!.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }


}