package ru.appintheair.flightreviewssubmit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class RatingAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val viewTypeValues = listOf(
        ViewHolderType.Crowd("How crowded was the flight?", MyCell.CROWD),
        ViewHolderType.Ordinary("How do you rate the aircraft?", MyCell.AIRCRAFT),
        ViewHolderType.Ordinary("How do you rate the seats?", MyCell.SEATS),
        ViewHolderType.Ordinary("How do you rate the crew?", MyCell.CREW),
        ViewHolderType.Ordinary("How do you rate the food?", MyCell.FOOD),
    )

    private var mRatingList: EnumMap<MyCell, String?> = EnumMap(MyCell::class.java)
    private var listener: OnItemClickListener? = null

    private var mIsEnabled = true

    fun setEnabled(isEnabled: Boolean) {
        mIsEnabled = isEnabled
        notifyDataSetChanged()
    }

    fun refreshRating(ratingList: EnumMap<MyCell, String?>) {
        mRatingList = ratingList
        notifyDataSetChanged()
    }

    fun clearFoodRating() {
        mRatingList[MyCell.FOOD] = "0"
        notifyItemChanged(viewTypeValues.size - 1)
    }

    fun attachListener(onItemClickListener: OnItemClickListener) {
        listener = onItemClickListener
    }

    override fun getItemViewType(position: Int): Int = viewTypeValues[position].viewType.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (ViewTypeEnum.values()[viewType]) {
            ViewTypeEnum.CROWD -> CrowdViewHolder(
                inflater.inflate(R.layout.item_crowd, parent, false), listener
            )
            ViewTypeEnum.ORDINARY -> OrdinaryViewHolder(
                inflater.inflate(R.layout.item_recycler, parent, false), listener
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val viewTypeValue = viewTypeValues[position]) {
            is ViewHolderType.Ordinary -> (holder as OrdinaryViewHolder).bind(
                viewTypeValue,
                mRatingList,
                mIsEnabled
            )
            is ViewHolderType.Crowd -> (holder as CrowdViewHolder).bind(
                viewTypeValue,
                mRatingList,
                mIsEnabled
            )
        }
    }

    override fun getItemCount(): Int {
        return viewTypeValues.size
    }

    class OrdinaryViewHolder(itemView: View, onItemClickListener: OnItemClickListener?) :
        RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.rating_text)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.rating_bar)
        private val listener = onItemClickListener
        private lateinit var cell: MyCell
        fun bind(
            data: ViewHolderType.Ordinary,
            ratingList: EnumMap<MyCell, String?>,
            isEnabled: Boolean
        ) {
            ratingBar.isEnabled = isEnabled
            cell = data.cell
            ratingBar.rating = ratingList[cell]?.toFloat() ?: 0f
            textView.text = data.text
        }

        init {
            ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
                listener?.onClick(rating.toString(), cell)
            }
        }
    }

    class CrowdViewHolder(itemView: View, onItemClickListener: OnItemClickListener?) :
        RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.rating_text)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.rating_bar)
        private val listener = onItemClickListener
        fun bind(
            data: ViewHolderType.Crowd,
            ratingList: EnumMap<MyCell, String?>,
            isEnabled: Boolean
        ) {
            ratingBar.isEnabled = isEnabled
            ratingBar.rating = ratingList[data.cell]?.toFloat() ?: 0f
            textView.text = data.text
        }

        init {
            ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
                listener?.onClick(rating.toString(), MyCell.CROWD)
            }
        }
    }
}