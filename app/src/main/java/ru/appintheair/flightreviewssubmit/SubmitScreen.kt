package ru.appintheair.flightreviewssubmit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SubmitScreen : Fragment() {
    private var mSubmitScreenViewModel: SubmitScreenViewModel? = null

    private lateinit var mRecyclerView: RecyclerView
    private val mRatingAdapter = RatingAdapter()

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
        mRecyclerView = view.findViewById(R.id.rating_list)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.adapter = mRatingAdapter
    }
}