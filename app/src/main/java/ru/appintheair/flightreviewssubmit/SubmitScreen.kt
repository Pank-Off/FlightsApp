package ru.appintheair.flightreviewssubmit

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SubmitScreen : Fragment() {
    private var mSubmitScreenViewModel: SubmitScreenViewModel? = null

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mSubmitBtn: MaterialButton
    private lateinit var mFlightRatingBar: RatingBar
    private lateinit var mRadioButton: RadioButton
    private lateinit var mEditTextComment: EditText
    private lateinit var customToolbar: Toolbar
    private lateinit var mProgressBar: ProgressBar
    private val mRatingAdapter = RatingAdapter()
    private var mMapOfRating = HashMap<String, String?>()

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
        initViews(view)
        initRecyclerView()
        initToolbar()
        mSubmitBtn.setOnClickListener {
            mProgressBar.visibility = ProgressBar.VISIBLE
            mSubmitBtn.isEnabled = false
            mMapOfRating["text"] = mEditTextComment.text.toString()
            GlobalScope.launch(Dispatchers.IO) {
                mSubmitScreenViewModel?.setRating(mMapOfRating)
            }
        }

        setOnRadioButtonClickListener()
        setOnRatingBarChangeListener()

        mSubmitScreenViewModel?.getRating()?.observe(viewLifecycleOwner, { data ->
            mRatingAdapter.refreshRating(data)
            mMapOfRating = data
        })

        mSubmitScreenViewModel?.getData()?.observe(viewLifecycleOwner, { data ->
            mProgressBar.visibility = ProgressBar.INVISIBLE
            mSubmitBtn.isEnabled = true
            Toast.makeText(context, data.toString(), Toast.LENGTH_LONG).show()
        })
    }

    private fun initToolbar() {
        val xIcon: Drawable? =
            context?.let { ContextCompat.getDrawable(it, R.drawable.ic_letter_x) }

        (requireActivity() as AppCompatActivity).setSupportActionBar(customToolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setHomeAsUpIndicator(xIcon)
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }
        customToolbar.setNavigationOnClickListener {
            Toast.makeText(context, "Close", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setOnRatingBarChangeListener() {
        mFlightRatingBar.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                mMapOfRating["flight"] = rating.toInt().toString()
            }
    }

    private fun setOnRadioButtonClickListener() {
        mRadioButton.setOnClickListener {
            if (mRadioButton.isSelected) {
                mRadioButton.isSelected = false
                mRadioButton.isChecked = false
            } else {
                mMapOfRating["food"] = null
                mRadioButton.isSelected = true
                mRadioButton.isChecked = true
            }
        }
    }

    private fun initViews(view: View) {
        mRecyclerView = view.findViewById(R.id.rating_list)
        mSubmitBtn = view.findViewById(R.id.submitButton)
        mFlightRatingBar = view.findViewById(R.id.flight_rating_bar)
        mRadioButton = view.findViewById(R.id.radio_button)
        mEditTextComment = view.findViewById(R.id.comment)
        customToolbar = view.findViewById(R.id.toolbar)
        mProgressBar = view.findViewById(R.id.loading_progress_bar)

    }

    private fun initRecyclerView() {
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRatingAdapter.attachListener(object : OnItemClickListener {
            override fun onClick(rating: String, position: Int) {
                when (position) {
                    0 -> {
                        mMapOfRating["people"] = rating
                    }
                    1 -> {
                        mMapOfRating["aircraft"] = rating
                    }
                    2 -> {
                        mMapOfRating["seat"] = rating
                    }
                    3 -> {
                        mMapOfRating["crew"] = rating
                    }
                    4 -> {
                        mRadioButton.isSelected = false
                        mRadioButton.isChecked = false
                        mMapOfRating["food"] = rating
                    }
                }
            }
        })
        mRecyclerView.adapter = mRatingAdapter
    }

}
