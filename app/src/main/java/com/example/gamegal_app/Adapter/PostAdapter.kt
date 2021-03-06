package com.example.gamegal_app.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.gamegal_app.CommentsActivity
import com.example.gamegal_app.Fragments.PostDetailsFragment
import com.example.gamegal_app.Fragments.ProfileFragment
import com.example.gamegal_app.MainActivity
import com.example.gamegal_app.Model.Post
import com.example.gamegal_app.Model.User
import com.example.gamegal_app.R
import com.example.gamegal_app.ShowUsersActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


    class PostAdapter
        (private val mContext: Context,
         private val  mPost: List<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder>()
    {
        private var firebaseUser: FirebaseUser? = null

        inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView){
            var profileImage: CircleImageView
            var postImage: ImageView
            var likeButton: ImageView
            var commentButton: ImageView
            var saveButton: ImageView
            var userName: TextView
            var likes: TextView
            var publisher: TextView
            var description: TextView
            var comments: TextView

            init {
                profileImage = itemView.findViewById(R.id.user_profile_image_post)
                postImage = itemView.findViewById(R.id.post_image_home)
                likeButton = itemView.findViewById(R.id.post_image_like_btn)
                commentButton = itemView.findViewById(R.id.post_image_comment_btn)
                saveButton = itemView.findViewById(R.id.post_save_comment_btn)
                userName = itemView.findViewById(R.id.user_name_post)
                likes = itemView.findViewById(R.id.likes)
                publisher = itemView.findViewById(R.id.publisher)
                description = itemView.findViewById(R.id.description)
                comments = itemView.findViewById(R.id.comments)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(mContext).inflate(R.layout.posts_layout,parent,false)
            return  ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            firebaseUser = FirebaseAuth.getInstance().currentUser


            val post = mPost[position]
            publisherInfo(holder.profileImage,holder.userName,holder.publisher,post.getPublisher())
            Picasso.get().load(post.getPostimage()).into(holder.postImage)
            isLikes(post.getPostid(),holder.likeButton)
            numberOfLikes(holder.likes,post.getPostid())
            getTotalComments(holder.comments,post.getPostid())
            checkSavedStatus(post.getPostid(),holder.saveButton)

            holder.postImage.setOnClickListener {
                val editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
                editor.putString("postId",post.getPostid())
                editor.apply()
                (mContext as FragmentActivity).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    PostDetailsFragment()
                ).commit()
            }
            holder.publisher.setOnClickListener {
                val editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
                editor.putString("profileId",post.getPublisher())
                editor.apply()
                (mContext as FragmentActivity).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    ProfileFragment()
                ).commit()
            }
            holder.profileImage.setOnClickListener {
                val editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
                editor.putString("profileId",post.getPublisher())
                editor.apply()
                (mContext as FragmentActivity).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    ProfileFragment()
                ).commit()
            }
            holder.postImage.setOnClickListener {
                val editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
                editor.putString("postId",post.getPostid())
                editor.apply()
                (mContext as FragmentActivity).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    PostDetailsFragment()
                ).commit()
            }
            if(post.getDescription().equals("") ){
                holder.description.visibility = View.GONE
            }else{
                holder.description.visibility = View.VISIBLE
                holder.description.setText(post.getDescription())
            }




            holder.likeButton.setOnClickListener {
                if(holder.likeButton.tag == "Like")
                {
                    FirebaseDatabase.getInstance().reference
                        .child("Likes")
                        .child(post.getPostid())
                        .child(firebaseUser!!.uid)
                        .setValue(true)

                    addNotification(post.getPublisher(),post.getPostid())
                }else{
                    FirebaseDatabase.getInstance().reference
                        .child("Likes")
                        .child(post.getPostid())
                        .child(firebaseUser!!.uid)
                        .removeValue()

                    val intent = Intent(mContext, MainActivity::class.java)
                    mContext.startActivity(intent)
                }
            }

            holder.likes.setOnClickListener {
                val intent = Intent(mContext, ShowUsersActivity::class.java)
                intent.putExtra("id",post.getPostid())
                intent.putExtra("title","likes")
                mContext.startActivity(intent)
            }

            holder.commentButton.setOnClickListener {
                val intentComment = Intent(mContext, CommentsActivity::class.java)
                intentComment.putExtra("postId",post.getPostid())
                intentComment.putExtra("publisherId",post.getPublisher())
                mContext.startActivity(intentComment)
            }
            holder.comments.setOnClickListener {
                val intentComment = Intent(mContext,CommentsActivity::class.java)
                intentComment.putExtra("postId",post.getPostid())
                intentComment.putExtra("publisherId",post.getPublisher())
                mContext.startActivity(intentComment)
            }
            holder.saveButton.setOnClickListener {
                if(holder.saveButton.tag == "Save"){
                    FirebaseDatabase.getInstance().reference.child("Saves").child(firebaseUser!!.uid).child(post.getPostid()).setValue(true)
                }else{
                    FirebaseDatabase.getInstance().reference.child("Saves").child(firebaseUser!!.uid).child(post.getPostid()).removeValue()
                }

            }
        }

        private fun numberOfLikes(likes: TextView, postid: String) {


            val LikesRef = FirebaseDatabase.getInstance().reference
                .child("Likes").child(postid)

            LikesRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.exists()){
                        likes.text = p0.childrenCount.toString() + " likes"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

        private fun getTotalComments(comments: TextView, postid: String) {


            val commentsRef = FirebaseDatabase.getInstance().reference
                .child("Comments").child(postid)

            commentsRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.exists()){
                        comments.text ="view all" + p0.childrenCount.toString() + " comments"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }


        override fun getItemCount(): Int {
            return mPost.size
        }
        private fun publisherInfo(profileImage: CircleImageView, userName: TextView, publisher: TextView, publisherID: String) {

            val usersRef = FirebaseDatabase.getInstance().reference.child("Users").child(publisherID)

            usersRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val user = snapshot.getValue<User>(User::class.java)

                        Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(profileImage)
                        userName.text = user!!.getUsername()
                        publisher.text = user!!.getFullname()


                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
        private fun isLikes(postid: String, likeButton: ImageView) {
            val firebaseUser = FirebaseAuth.getInstance().currentUser

            val LikesRef = FirebaseDatabase.getInstance().reference
                .child("Likes").child(postid)

            LikesRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.child(firebaseUser!!.uid).exists()){
                        likeButton.setImageResource(R.drawable.check) // like
                        likeButton.tag = "Liked"
                    }else{
                        likeButton.setImageResource(R.drawable.arrowleft)  // unlike
                        likeButton.tag = "Like"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })


        }
        private fun checkSavedStatus(postid: String, imageView: ImageView){
            val savesRef = FirebaseDatabase.getInstance().reference.child("Saves").child(firebaseUser!!.uid)

            savesRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.child(postid).exists()){
                        imageView.setImageResource(R.drawable.joystick) //save
                        imageView.tag = "Saved"
                    }else{
                        imageView.setImageResource(R.drawable.joystick)//dont save
                        imageView.tag = "Save"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
        private fun addNotification(userId: String,postId: String){
            val notiRef = FirebaseDatabase.getInstance().reference.child("Notifications")
                .child(userId)

            val notiMap = HashMap<String,Any>()
            notiMap ["userid"] = firebaseUser!!.uid
            notiMap ["text"] = "like your post"
            notiMap ["postid"] = postId
            notiMap ["ispost"] = true
            notiRef.push().setValue(notiMap)
        }
    }
