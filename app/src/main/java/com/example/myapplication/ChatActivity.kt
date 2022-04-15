package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbRef: DatabaseReference
    private var receiverRoom : String? = null
    private var senderRoom : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        supportActionBar?.title = name

        mDbRef = FirebaseDatabase.getInstance().getReference()
        messageList = ArrayList()
        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.sentButton)


        messageAdapter = MessageAdapter(this,messageList)
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter



        mDbRef.child("chats").child(senderRoom!!).child("message").addValueEventListener (object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               messageList.clear()
                for(postSnapshot in snapshot.children){
                   val message = postSnapshot.getValue(Message::class.java)
                   messageList.add(message!!)
               }
                this@ChatActivity.messageAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })



        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        senderRoom = receiverRoom + senderUid
        receiverRoom = senderUid

        sendButton.setOnClickListener{
                val message = messageBox.text.toString()
                val messageObject = Message(message,senderUid)
                mDbRef.child("chats").child(senderRoom!!).child("message").push()
                    .setValue(messageObject).addOnSuccessListener {
                    mDbRef.child("chats").child(receiverRoom!!).child("message").push().setValue(messageObject)

                }
            messageBox.setText("")
        }
    }
}