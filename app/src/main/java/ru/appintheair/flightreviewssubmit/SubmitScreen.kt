package ru.appintheair.flightreviewssubmit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class SubmitScreen : Fragment() {
    private var mSubmitScreenViewModel: SubmitScreenViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.submit_screen_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSubmitScreenViewModel =
            activity?.let { ViewModelProvider(it) }?.get(SubmitScreenViewModel::class.java)
    }
}