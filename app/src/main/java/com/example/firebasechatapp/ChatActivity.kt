package com.example.firebasechatapp


import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasechatapp.adapter.ChatAdapter
import com.example.firebasechatapp.data.Message
import com.example.firebasechatapp.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    var REQS_CODE = 102

    @Inject
    lateinit var mbref: DatabaseReference
    @Inject
    lateinit var dbref: FirebaseAuth
    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter: ChatAdapter
    private lateinit var list: ArrayList<Message>

    var receiverRoom: String? = null

    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)


        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        val senderUid = dbref.currentUser?.uid


        if (receiverUid == null || senderUid == null) {
            finish()
            return
        }

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        binding.toolbar.title = name

        list = ArrayList()
        adapter = ChatAdapter(list, dbref)
        binding.recyclerView.adapter = adapter


        mbref.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    if (snapshot.exists()) {
                        for (value in snapshot.children) {
                            val msg = value.getValue(Message::class.java)
                            msg?.let {
                                list.add(it)
                            }
                        }
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("ChatActivity", "Database Error: ${error.message}")
                }
            })

        binding.voiceIcon.setOnClickListener {
            if (!SpeechRecognizer.isRecognitionAvailable(this)) {
                Toast.makeText(this, "Voice is Not Available ", Toast.LENGTH_LONG).show()
            } else {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something ")
                startActivityForResult(intent, REQS_CODE)
            }
        }


        binding.sndIcon.setOnClickListener {
            val messageText = binding.editText.text.toString().trim()

            if (messageText.isNotEmpty()) {
                val messageObject = Message(messageText, senderUid)


                mbref.child("chats").child(senderRoom!!).child("messages").push()
                    .setValue(messageObject)
                    .addOnSuccessListener {
                        mbref.child("chats").child(receiverRoom!!).child("messages").push()
                            .setValue(messageObject)
                    }
                scrollToBottom()
                binding.editText.text.clear()
            } else {
                Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }


    }


    private fun scrollToBottom() {
        binding.recyclerView.post {
            (binding.recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                list.size - 1,
                0
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQS_CODE && resultCode == Activity.RESULT_OK) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            var text = result?.get(0).toString()
            binding.editText.text.clear()
            binding.editText.text.append(text)
        }
    }
}
