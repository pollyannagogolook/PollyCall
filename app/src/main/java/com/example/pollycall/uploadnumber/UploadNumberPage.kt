package com.example.pollycall.uploadnumber

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pollycall.R


/**
 * A simple [Fragment] subclass.
 * Use the [UploadNumberPage.newInstance] factory method to
 * create an instance of this fragment.
 */
class UploadNumberPage : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload_number_page, container, false)
    }


}