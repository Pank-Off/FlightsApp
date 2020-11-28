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

    private var mRecyclerView: RecyclerView? = null
    private var mSubmitBtn: MaterialButton? = null
    private var mFlightRatingBar: RatingBar? = null
    private var mRadioButton: RadioButton? = null
    private var mEditTextComment: EditText? = null
    private var customToolbar: Toolbar? = null
    private var mProgressBar: ProgressBar? = null
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
        initViews(view)
        initRecyclerView()
        initToolbar()
        mSubmitBtn?.setOnClickListener {
            mSubmitScreenViewModel?.refreshMap(MyCell.TEXT, mEditTextComment?.text.toString())
            GlobalScope.launch(Dispatchers.IO) {
                mSubmitScreenViewModel?.setRating()
            }
        }

        setOnRadioButtonClickListener()
        setOnRatingBarChangeListener()

        mSubmitScreenViewModel?.getRating()?.observe(viewLifecycleOwner, { data ->
            mRatingAdapter.refreshRating(data)
        })

        mSubmitScreenViewModel?.getData()?.observe(viewLifecycleOwner, { data ->
            val content = data.getContentIfNotHandled()
            if (content != null) {
                Toast.makeText(context, content.toString(), Toast.LENGTH_LONG).show()
            }
        })

        mSubmitScreenViewModel?.getProgressBarState()?.observe(viewLifecycleOwner) { progress ->
            when (progress) {
                SubmitScreenViewModel.ProgressBarState.VISIBLE -> {
                    mProgressBar?.visibility = ProgressBar.VISIBLE
                    mSubmitBtn?.visibility = MaterialButton.INVISIBLE
                    CollapsingPopupLayout.isEnabledRatingBar = false
                    mRadioButton?.isEnabled = false
                    mEditTextComment?.isEnabled = false
                    mRatingAdapter.setEnabled(false)
                }
                SubmitScreenViewModel.ProgressBarState.INVISIBLE -> {
                    mProgressBar?.visibility = ProgressBar.INVISIBLE
                    mSubmitBtn?.visibility = MaterialButton.VISIBLE
                    CollapsingPopupLayout.isEnabledRatingBar = true
                    mRadioButton?.isEnabled = true
                    mEditTextComment?.isEnabled = true
                    mRatingAdapter.setEnabled(true)
                }
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
        mRecyclerView?.layoutManager = LinearLayoutManager(context)
        mRatingAdapter.attachListener(object : OnItemClickListener {
            override fun onClick(rating: String, cell: MyCell) {
                if (cell == MyCell.FOOD) {
                    mRadioButton?.isSelected = false
                    mRadioButton?.isChecked = false
                }
                mSubmitScreenViewModel?.refreshMap(cell, rating)
            }
        })
        mRecyclerView?.adapter = mRatingAdapter
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
        customToolbar?.setNavigationOnClickListener {
            Toast.makeText(context, resources.getString(R.string.close), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setOnRatingBarChangeListener() {
        mFlightRatingBar?.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                mSubmitScreenViewModel?.refreshMap(MyCell.FLIGHT, rating.toString())
            }
    }

    private fun setOnRadioButtonClickListener() {
        mRadioButton?.setOnClickListener {
            if (it.isSelected) {
                mSubmitScreenViewModel?.refreshMap(MyCell.FOOD, "0")
                mRadioButton?.isSelected = false
                mRadioButton?.isChecked = false
            } else {
                mRatingAdapter.clearFoodRating()
                mSubmitScreenViewModel?.refreshMap(MyCell.FOOD, null)
                mRadioButton?.isSelected = true
                mRadioButton?.isChecked = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mRecyclerView = null
        mSubmitBtn = null
        mFlightRatingBar = null
        mRadioButton = null
        mEditTextComment = null
        customToolbar = null
        mProgressBar = null
    }
}
