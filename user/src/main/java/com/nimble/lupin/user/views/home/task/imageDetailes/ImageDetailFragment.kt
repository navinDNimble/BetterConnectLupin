package com.nimble.lupin.user.views.home.task.imageDetailes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.nimble.lupin.user.R
import com.nimble.lupin.user.adapters.ImagesAdapter
import com.nimble.lupin.user.api.ApiService
import com.nimble.lupin.user.api.ResponseHandler
import com.nimble.lupin.user.databinding.FragmentImageDetailBinding
import org.koin.java.KoinJavaComponent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ImageDetailFragment : Fragment() {
    private val apiService: ApiService by KoinJavaComponent.inject(ApiService::class.java)
    lateinit var binding :FragmentImageDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

       binding = FragmentImageDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val taskUpdateId = arguments?.getInt("taskUpdateId")
        apiService.getPhotosUrl(taskUpdateId!!).enqueue(object :Callback<ResponseHandler<List<String>>>{
            override fun onResponse(
                call: Call<ResponseHandler<List<String>>>,
                response: Response<ResponseHandler<List<String>>>
            ) {
                val result = response.body()
               if (result?.code==200){
                   val list = result.response
                   val imagesAdapter = ImagesAdapter(list)
                   binding.imageRecyclerView.layoutManager = LinearLayoutManager(context)
                   binding.imageRecyclerView.adapter  = imagesAdapter
                   binding.progressBarImages.visibility =View.GONE
               }else if (result?.code==400){
                   Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                   binding.progressBarImages.visibility =View.GONE
               }

            }

            override fun onFailure(call: Call<ResponseHandler<List<String>>>, t: Throwable) {
                binding.progressBarImages.visibility =View.GONE
            }

        })

    }

}