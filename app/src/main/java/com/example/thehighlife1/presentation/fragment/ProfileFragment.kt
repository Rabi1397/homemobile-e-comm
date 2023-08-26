package com.example.thehighlife1.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.thehighlife1.R
import com.example.thehighlife1.data.localroom.Card.CardViewModel
import com.example.thehighlife1.presentation.activity.LoginActivity
import com.example.thehighlife1.presentation.activity.PaymentMethodActivity
import com.example.thehighlife1.presentation.activity.SettingActivity
import com.example.thehighlife1.presentation.activity.ShippingAddressActivity
import com.example.thehighlife1.utils.FirebaseUtils.firebaseAuth
import com.example.thehighlife1.utils.FirebaseUtils.storageReference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.UUID

class ProfileFragment: Fragment() {

    lateinit var animationView: LottieAnimationView

    lateinit var profileImage_profileFrag: CircleImageView

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null

    lateinit var uploadImage_profileFrag: Button
    lateinit var profileName_profileFrag: TextView
    lateinit var profileEmail_profileFrag: TextView

    private lateinit var cardViewModel: CardViewModel

    val database = FirebaseDatabase.getInstance()
    val userDatabaseRef = database.reference.child("Users")

    var cards: Int = 0

    lateinit var linearLayout2: LinearLayout
    lateinit var linearLayout3: LinearLayout
    lateinit var linearLayout4: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        profileImage_profileFrag = view.findViewById(R.id.profileImage_profileFrag)
        val settingCd_profileFrag = view.findViewById<CardView>(R.id.settingCd_profileFrag)
        val logout_profileFag = view.findViewById<CardView>(R.id.logout_profileFag)
        val shippingAddressCard_ProfilePage = view.findViewById<CardView>(R.id.shippingAddressCard_ProfilePage)
        val paymentMethod_ProfilePage = view.findViewById<CardView>(R.id.paymentMethod_ProfilePage)
        val cardsNumber_profileFrag:TextView = view.findViewById(R.id.cardsNumber_profileFrag)
        uploadImage_profileFrag = view.findViewById(R.id.uploadImage_profileFrag)
        profileName_profileFrag = view.findViewById(R.id.profileName_profileFrag)
        profileEmail_profileFrag = view.findViewById(R.id.profileEmail_profileFrag)
        animationView = view.findViewById(R.id.animationView)
        linearLayout2 = view.findViewById(R.id.linearLayout2)
        linearLayout3 = view.findViewById(R.id.linearLayout3)
        linearLayout4 = view.findViewById(R.id.linearLayout4)
        cardViewModel = ViewModelProviders.of(this).get(CardViewModel::class.java)

        cardViewModel.allCards.observe(viewLifecycleOwner,androidx.lifecycle.Observer {
            cards=it.size
        })

        if(cards==0){
            cardsNumber_profileFrag.text = "You Have no Cards."
        }else{
            cardsNumber_profileFrag.text = "You Have "+ cards.toString() + " Cards."
        }

        shippingAddressCard_ProfilePage.setOnClickListener {
            startActivity(Intent(context, ShippingAddressActivity::class.java))
        }

        paymentMethod_ProfilePage.setOnClickListener {
            startActivity(Intent(context, PaymentMethodActivity::class.java))
        }

        logout_profileFag.setOnClickListener {
            startActivity(Intent(context, LoginActivity::class.java))
        }

        hideLayout()
        uploadImage_profileFrag.visibility = View.GONE

        getUserData()
        uploadImage_profileFrag.setOnClickListener {
            uploadImage()
        }

        settingCd_profileFrag.setOnClickListener {
            val intent = Intent(context, SettingActivity::class.java)
            startActivity(intent)
        }

        profileImage_profileFrag.setOnClickListener {

            val popupMenu = PopupMenu(context,profileImage_profileFrag)
            popupMenu.menuInflater.inflate(R.menu.profile_photo_storage,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.galleryMenu ->
                        launchGallery()
                    R.id.cameraMenu ->
                        uploadImage()

                }
                true
            })
            popupMenu.show()
        }

        return view
    }




    private fun hideLayout() {
        animationView.playAnimation()
        animationView.loop(true)
        linearLayout2.visibility = View.GONE
        linearLayout3.visibility = View.GONE
        linearLayout4.visibility = View.GONE
        animationView.visibility = View.VISIBLE
    }

    private fun showLayout() {
        animationView.pauseAnimation()
        animationView.visibility = View.GONE
        linearLayout2.visibility = View.VISIBLE
        linearLayout3.visibility = View.VISIBLE
        linearLayout4.visibility = View.VISIBLE
    }

    private fun getUserData() = CoroutineScope(Dispatchers.IO).launch {

        try {
            val userReference = userDatabaseRef.child(firebaseAuth.uid.toString())
            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userImage = snapshot.child("userImage").getValue(String::class.java) ?: ""
                    val userName = snapshot.child("userName").getValue(String::class.java) ?: ""
                    val userEmail = snapshot.child("userEmail").getValue(String::class.java) ?: ""

                    profileName_profileFrag.text = userName
                    profileEmail_profileFrag.text = userEmail
                    Glide.with(this@ProfileFragment)
                        .load(userImage)
                        .placeholder(R.drawable.ic_profile)
                        .into(profileImage_profileFrag)

                    showLayout()
                }

                override fun onCancelled(error: DatabaseError) {
                    val errorMessage = "Database Error: ${error.message}"
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }

            })

        }catch (e:Exception){

        }

    }
    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    private fun uploadImage() {
        if (filePath != null) {
            val ref = storageReference.child("userImage/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)

            uploadTask?.addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { downloadUri ->
                    addUploadRecordToDb(downloadUri.toString())
                }
            }?.addOnFailureListener {
                // Handle failure
                val errorMessage = "Upload Failed"
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addImageUrlToDatabase(imageUrl: String) {
        val userReference = userDatabaseRef.child(firebaseAuth.uid.toString())
        userReference.child("userImage").setValue(imageUrl)
            .addOnSuccessListener {
                Toast.makeText(context, "Image Uploaded", Toast.LENGTH_SHORT).show()
                // You might want to update your UI here if needed
            }
            .addOnFailureListener { exception ->
                val errorMessage = "Database Error: ${exception.message}"
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, filePath)
                profileImage_profileFrag.setImageBitmap(bitmap)
                uploadImage_profileFrag.visibility = View.VISIBLE
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun addUploadRecordToDb(uri: String) {
        val userReference = userDatabaseRef.child(firebaseAuth.uid.toString())
        userReference.child("userImage").setValue(uri)
            .addOnSuccessListener {
                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                try {
                    throw exception
                } catch (e: Exception) {
                    // Handle the exception
                    val errorMessage = "Database Error: ${e.message}"
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }



}