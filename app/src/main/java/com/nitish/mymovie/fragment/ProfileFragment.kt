package com.nitish.mymovie.fragment

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.nitish.mymovie.R
import com.nitish.mymovie.activity.LoginActivity
import com.nitish.mymovie.databinding.FragmentProfileBinding
import com.nitish.mymovie.utils.isInternetAvailable
import java.io.File
import java.io.FileOutputStream

class ProfileFragment : Fragment() {
    private val GALLERY_REQ_CODE = 0
    private lateinit var binding:FragmentProfileBinding
    private lateinit var auth:FirebaseAuth
    private  var  imagePath:String?=null
    lateinit var bitmap:Bitmap
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=FragmentProfileBinding.inflate(inflater, container, false)
        spinnerView()
        auth = Firebase.auth
        if (isInternetAvailable(requireContext())) {
            val email = auth.currentUser?.email
            binding.etEmailProfile.text = email
        }else{
            binding.etEmailProfile.hint = "Check internet connection"
        }
        binding.btnSave.setOnClickListener{
            sharedPref()
            Toast.makeText(requireContext(),"Save Successfully",Toast.LENGTH_SHORT).show()
        }
        getSharedPref()
        binding.btnLogOut.setOnClickListener {
            logOut()
        }

        binding.imgProfile.setOnClickListener{
            setProfileImage()
        }
        return binding.root
    }

    private fun setProfileImage() {
        Intent(Intent.ACTION_PICK).also {
            it.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(it,GALLERY_REQ_CODE)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==RESULT_OK){
            if (requestCode==GALLERY_REQ_CODE){
                binding.imgProfile.setImageURI(data?.data)
                bitmap = BitmapFactory.decodeStream(context?.contentResolver?.openInputStream(data?.data!!))
                imagePath = saveImageToInternalStorage(bitmap)
            }
        }
    }

    private fun spinnerView(){
        val items = resources.getStringArray(R.array.genderString)
//        val adapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,items)
        val adapter = ArrayAdapter(requireContext(),R.layout.custom_spinner_bar,items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGender.adapter = adapter
    }

    private fun sharedPref(){
        val sharePref = context?.getSharedPreferences("shared",Context.MODE_PRIVATE)

        sharePref?.edit {
            this.putString("Name",binding.etNameProfile.text.toString())
            this.putInt("Gender",binding.spinnerGender.selectedItemPosition)
            this.putString("profile",imagePath)
            this.apply()
        }
    }

    private fun getSharedPref(){
        val sharePref = context?.getSharedPreferences("shared",Context.MODE_PRIVATE)

        val name = sharePref?.getString("Name","")
        val gender = sharePref?.getInt("Gender",0)
        val profile = sharePref?.getString("profile","")
        binding.etNameProfile.setText(name)
        binding.spinnerGender.setSelection(gender!!)
        try {
            binding.imgProfile.also {
                it.setImageBitmap(BitmapFactory.decodeFile(profile))
            }

        }catch (e:Exception){
            binding.imgProfile.setImageResource(R.drawable.girl_music)
            Log.d("TAG","image error")
        }
    }
    private fun logOut() {
        val sharePref = context?.getSharedPreferences("shared",Context.MODE_PRIVATE)
        auth.signOut()
        sharePref?.edit{
            this.clear()
            this.apply()
        }
        Intent(context,LoginActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): String? {
        val fileName = bitmap.toString() // Change the file name as needed
        val directory = context?.filesDir // Get the internal storage directory
        val file = File(directory, fileName) // Create a new file in the directory

        return try {
            val outputStream = FileOutputStream(file) // Open a file output stream
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream) // Compress bitmap to JPEG and write to output stream
            outputStream.flush() // Flush the stream
            outputStream.close() // Close the stream
            file.absolutePath // Return the absolute path of the saved image file
        } catch (e: Exception) {
            e.printStackTrace()
            null // Return null if there's an error
        }
    }

}