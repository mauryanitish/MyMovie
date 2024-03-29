package com.nitish.mymovie.fragment

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ApiException
import com.nitish.mymovie.R
import com.nitish.mymovie.activity.DetailActivity
import com.nitish.mymovie.adapter.ClickInterface
import com.nitish.mymovie.adapter.FilmListAdapter
import com.nitish.mymovie.databinding.FragmentSearchBinding
import com.nitish.mymovie.domain.MoviesName
import com.nitish.mymovie.domain.MyApi
import com.nitish.mymovie.utils.isInternetAvailable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.Locale

class Search : Fragment(),ClickInterface {
    private val coroutineScope=CoroutineScope(Dispatchers.IO)
    private val mainScope= MainScope()
    private lateinit var movieListArrayList:ArrayList<MoviesName>
    private lateinit var binding: FragmentSearchBinding
    private lateinit var filmListAdapter : FilmListAdapter

    private val allowPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            it?.let {
                if (it){
                    Toast.makeText(context,"Permission Granted",Toast.LENGTH_SHORT).show()
                }
            }
        }

    private val speechRecognizer:SpeechRecognizer by lazy { SpeechRecognizer.createSpeechRecognizer(context) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding= FragmentSearchBinding.inflate(inflater, container, false)

        binding.imgVoiceSearch.setOnClickListener {
            getPermissionOverO(requireContext()){
                startListen()
            }
        }

        binding.etSearch.setOnQueryTextListener(object:androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                filtereResult(newText)
                return true
            }

        })

        if (isInternetAvailable(requireContext())) {
            coroutineScope.launch {
                apiCall(binding.root)
            }
        }else{
            Toast.makeText(context, "You are offline..", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }


    private fun startListen(){
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault())

        speechRecognizer.setRecognitionListener(object :RecognitionListener{
            override fun onReadyForSpeech(params: Bundle?) {
            }

            override fun onBeginningOfSpeech() {
            }

            override fun onRmsChanged(rmsdB: Float) {
            }

            override fun onBufferReceived(buffer: ByteArray?) {
            }

            override fun onEndOfSpeech() {
            }

            override fun onError(error: Int) {
            }

            override fun onResults(results: Bundle?) {
                results?.let {
                    val result = it.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (result!=null) {
                        Log.d("Voice_result", result[0])
                        binding.etSearch.setQuery(result[0], true)
                    }
                    speechRecognizer.stopListening()
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
            }
        })

        speechRecognizer.startListening(intent)
    }

    private fun getPermissionOverO(context: Context, call:()->Unit){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            if (ActivityCompat.checkSelfPermission(context,android.Manifest.permission.RECORD_AUDIO)==PackageManager.PERMISSION_GRANTED){
                call.invoke()
            }
            else{
                allowPermission.launch(android.Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    private fun filtereResult(newText: String?) {
        val filteredArray: MutableList<MoviesName> = mutableListOf()
        if (newText != null) {
            movieListArrayList.forEachIndexed { index, moviesName ->
                if (movieListArrayList[index].title!!.lowercase(Locale.ROOT).contains(newText.lowercase(Locale.ROOT))){
                    filteredArray.add(moviesName)
                }
            }
            if (filteredArray.isEmpty()){
                Toast.makeText(context,"Not found",Toast.LENGTH_SHORT).show()
                filmListAdapter.setFilteredArray(filteredArray as ArrayList<MoviesName>)
            }
            else{
                filmListAdapter.setFilteredArray(filteredArray as ArrayList<MoviesName>)
            }
        }
        else{
            Toast.makeText(context,"enter data",Toast.LENGTH_SHORT).show()
        }
    }


    private suspend fun apiCall(view: View) {
        try {
            val myApi = MyApi()
            val response = myApi.moviesDetail()
            if (response.isSuccessful) {
                val body = response.body()
                Log.d("TAG", body.toString())
                if (body != null) {
                    movieListArrayList = body.data as ArrayList<MoviesName>
                    mainScope.launch {
                        setAdapter(view)
                    }

                }
                Log.d("TAG", movieListArrayList.iterator().toString())
            } else {
                Log.d("TAG", response.errorBody().toString())
            }
        }catch (e:ApiException){
            mainScope.launch {
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        catch (e:Exception){
            mainScope.launch{
                Toast.makeText(context, "check you internet connection..", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setAdapter(view: View) {
        val recycler:RecyclerView = view.findViewById(R.id.recyclerSearch)
        recycler.layoutManager = GridLayoutManager(view.context,3,GridLayoutManager.VERTICAL,false)
        filmListAdapter = FilmListAdapter(this,movieListArrayList)
        recycler.adapter = filmListAdapter
    }

    override fun clickItem(position: Int) {
        Intent(view?.context, DetailActivity::class.java).also {
            Log.d("TAG",position.toString())
            it.putExtra("movieID",position.toString())
            startActivity(it)
        }    }

    override fun onDestroyView() {
        super.onDestroyView()
        coroutineScope.cancel()
        mainScope.cancel()
    }

}