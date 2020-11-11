package ru.appintheair.flightreviewssubmit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.appintheair.flightreviewssubmit.SubmitScreen.Companion.aircraft
import ru.appintheair.flightreviewssubmit.SubmitScreen.Companion.crew
import ru.appintheair.flightreviewssubmit.SubmitScreen.Companion.food
import ru.appintheair.flightreviewssubmit.SubmitScreen.Companion.people
import ru.appintheair.flightreviewssubmit.SubmitScreen.Companion.seat

class RatingAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data =
        listOf(
            "How crowded was the flight?",
            "How do you rate the aircraft?",
            "How do you rate the seats?",
            "How do you rate the crew?",
            "How do you rate the food?"
        )

    private var mRatingList: HashMap<String, String?> = HashMap()
    private var listener: OnItemClickListener? = null

    fun refreshRating(ratingList: HashMap<String, String?>) {
        mRatingList = ratingList
        notifyDataSetChanged()
    }

    fun attachListener(onItemClickListener: OnItemClickListener) {
        listener = onItemClickListener
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            1
        } else {
            2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            1 -> CrowdViewHolder(inflater.inflate(R.layout.item_crowd, parent, false), listener)
            else -> ViewHolder(inflater.inflate(R.layout.item_recycler, parent, false), listener)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> holder.bind(data[position], position, mRatingList)
            is CrowdViewHolder -> holder.bind(data[position], position, mRatingList)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View, onItemClickListener: OnItemClickListener?) :
        RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.rating_text)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.rating_bar)
        private val listener = onItemClickListener
        fun bind(text: String, position: Int, ratingList: HashMap<String, String?>) {

            if (text.endsWith("$aircraft?")) {
                ratingBar.rating = ratingList[aircraft]?.toFloat() ?: 0f
            } else if (text.endsWith(seat + "s?")) {
                ratingBar.rating = ratingList[seat]?.toFloat() ?: 0f
            } else if (text.endsWith("$crew?")) {
                ratingBar.rating = ratingList[crew]?.toFloat() ?: 0f
            } else if (text.endsWith("$food?")) {
                ratingBar.rating = ratingList[food]?.toFloat() ?: 0f
            }
            textView.text = text
            ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
                listener?.onClick(rating.toString(), position)
            }
        }
    }

    class CrowdViewHolder(itemView: View, onItemClickListener: OnItemClickListener?) :
        RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.rating_text)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.rating_bar)
        private val listener = onItemClickListener
        fun bind(text: String, position: Int, ratingList: HashMap<String, String?>) {
            ratingBar.rating = ratingList[people]?.toFloat() ?: 0f
            textView.text = text
            ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
                listener?.onClick(rating.toString(), position)
            }
        }
    }
}