package ru.appintheair.flightreviewssubmit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RatingAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data =
        listOf(
            "How crowded was the flight?",
            "How do you rate the aircraft?",
            "How do you rate the aircraft?",
            "How do you rate the aircraft?",
            "How do you rate the food?"
        )

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
            1 -> CrowdViewHolder(inflater.inflate(R.layout.item_crowd, parent, false))
            else -> ViewHolder(inflater.inflate(R.layout.item_recycler, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> holder.bind(data[position])
            is CrowdViewHolder -> holder.bind(data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.rating_text)

        fun bind(text: String) {
            textView.text = text
        }
    }

    class CrowdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.rating_text)

        fun bind(text: String) {
            textView.text = text
        }
    }

}