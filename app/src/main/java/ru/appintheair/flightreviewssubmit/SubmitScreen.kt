package ru.appintheair.flightreviewssubmit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class SubmitScreen : Fragment() {
    private var mSubmitScreenViewModel: SubmitScreenViewModel? = null

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mSubmitBtn: MaterialButton
    private lateinit var mFlightRatingBar: RatingBar
    private lateinit var mRadioButton: RadioButton
    private lateinit var mEditTextComment: EditText
    private val mRatingAdapter = RatingAdapter()
    private var mListOfRating = HashMap<String, String?>()

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
        initRecyclerView(view)
        initViews(view)

        mSubmitBtn.setOnClickListener {
            mListOfRating["text"] = mEditTextComment.text.toString()
            mSubmitScreenViewModel?.setRating(mListOfRating)
        }

        setOnRadioButtonClickListener()
        setOnRatingBarChangeListener()

        mSubmitScreenViewModel?.getRating()?.observe(viewLifecycleOwner, { data ->
            Toast.makeText(context, data.toString(), Toast.LENGTH_SHORT).show()
        })
    }

    private fun setOnRatingBarChangeListener() {
        mFlightRatingBar.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                mListOfRating["flight"] = rating.toString()
            }
    }

    private fun setOnRadioButtonClickListener() {
        mRadioButton.setOnClickListener {
            if (mRadioButton.isSelected) {
                mListOfRating["food"] = "NotNull"
                mRadioButton.isSelected = false
                mRadioButton.isChecked = false
            } else {
                mListOfRating["food"] = null
                mRadioButton.isSelected = true
                mRadioButton.isChecked = true
            }
        }
    }

    private fun initViews(view: View) {
        mSubmitBtn = view.findViewById(R.id.submitButton)
        mFlightRatingBar = view.findViewById(R.id.flight_rating_bar)
        mRadioButton = view.findViewById(R.id.radio_button)
        mEditTextComment = view.findViewById(R.id.comment)
    }

    private fun initRecyclerView(view: View) {
        mRecyclerView = view.findViewById(R.id.rating_list)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRatingAdapter.attachListener(object : OnItemClickListener {
            override fun onClick(rating: String) {
                Toast.makeText(context, rating, Toast.LENGTH_SHORT).show()

            }
        })
        mRecyclerView.adapter = mRatingAdapter

    }
}