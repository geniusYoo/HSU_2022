package com.example.firebasetest.navigation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebasetest.ContentDTO
import com.example.firebasetest.R

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import org.w3c.dom.Text

class DetailViewFragment : Fragment() {
    var fireStore : FirebaseFirestore? = null
    var uid : String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_detail,container,false)
        fireStore = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser?.uid

        view.findViewById<RecyclerView>(R.id.detailViewFragment_recyclerView).adapter = DetailViewRecyclerViewAdapter()
        view.findViewById<RecyclerView>(R.id.detailViewFragment_recyclerView).layoutManager = LinearLayoutManager(activity)
        return view
    }

    inner class DetailViewRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        var contentDTOs : ArrayList<ContentDTO> = arrayListOf()
        var contentUidList : ArrayList<String> = arrayListOf()
        init {
            fireStore?.collection("images")?.orderBy("timestamp", Query.Direction.DESCENDING)?.addSnapshotListener { querySnapShot, firebaseFirestoreException ->
                contentDTOs.clear()
                contentUidList.clear()
                if(querySnapShot == null) return@addSnapshotListener
                for(snapshot in querySnapShot!!.documents){
                    var item = snapshot.toObject(ContentDTO::class.java)
                    contentDTOs.add(item!!)
                    contentUidList.add(snapshot.id)
                }
                notifyDataSetChanged()
            }
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail,parent,false)
            return CustomViewHolder(view)
        }

        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as CustomViewHolder).itemView

            viewHolder.findViewById<TextView>(R.id.detailViewItem_profile_textView).text = contentDTOs!![position].userId
            Glide.with(holder.itemView.context).load(contentDTOs!![position].imageUrl).into(viewHolder.findViewById<ImageView>(R.id.detailViewItem_imageView_content))
            viewHolder.findViewById<TextView>(R.id.detailViewItem_explain_textView).text = contentDTOs!![position].explain
            viewHolder.findViewById<TextView>(R.id.detailViewItem_favoriteCounter_textView).text= contentDTOs!![position].favoriteCount.toString()
            Glide.with(holder.itemView.context).load(contentDTOs!![position].imageUrl).into(viewHolder.findViewById<ImageView>(R.id.detailViewItem_profile_image))

            viewHolder.findViewById<ImageView>(R.id.detailViewItem_favorite_imageView).setOnClickListener{
                favoriteEvent(position)
            }

            if(contentDTOs!![position].favorites.containsKey(uid)){
                viewHolder.findViewById<ImageView>(R.id.detailViewItem_favorite_imageView).setImageResource(R.drawable.ic_favorite)
            }else{
                viewHolder.findViewById<ImageView>(R.id.detailViewItem_favorite_imageView).setImageResource(R.drawable.ic_favorite_border)
            }

            viewHolder.findViewById<ImageView>(R.id.detailViewItem_profile_image).setOnClickListener {
                var fragment = UserFragment()
                var bundle = Bundle()
                bundle.putString("destinationUid", contentDTOs[position].uid)
                bundle.putString("userId", contentDTOs[position].userId)
                fragment.arguments = bundle
                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.main_content, fragment)?.commit()
            }

            viewHolder.findViewById<ImageView>(R.id.detailViewItem_comment_imageView).setOnClickListener { v ->
                var intent = Intent(v.context, CommentActivity::class.java)
                intent.putExtra("contentUid", contentUidList[position])
                startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return contentDTOs.size
        }

        fun favoriteEvent(position: Int){
            var tsDoc = fireStore?.collection("images")?.document(contentUidList[position])
            fireStore?.runTransaction {transaction ->

                var contentDTO = transaction.get(tsDoc!!).toObject(ContentDTO::class.java)

                if(contentDTO!!.favorites.containsKey(uid)) {
                    contentDTO?.favoriteCount = contentDTO?.favoriteCount-1
                    contentDTO?.favorites.remove(uid)
                } else {
                    contentDTO?.favoriteCount = contentDTO?.favoriteCount+1
                    contentDTO?.favorites[uid!!] = true
                }
                transaction.set(tsDoc,contentDTO)
            }
        }
    }
}