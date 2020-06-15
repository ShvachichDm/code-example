package io.polydev.public_transport.screens.main.route.adapters

import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import io.polydev.public_transport.R
import io.polydev.public_transport.screens.main.route.models.TripTimeModel
import java.text.SimpleDateFormat

class TripTimesRecyclerViewAdapter :
    RecyclerView.Adapter<TripTimesRecyclerViewAdapter.ViewHolder>() {



    private var models: ArrayList<TripTimeModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            inflater.inflate(
                R.layout.item_trip_time,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return models.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(models[position])
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNumber = itemView.findViewById<AppCompatTextView>(R.id.tvNumber)
        private val tvTime = itemView.findViewById<AppCompatTextView>(R.id.tvTime)

        fun onBind(model: TripTimeModel) {
            tvNumber.text = model.number
            tvTime.text = model.time
        }
    }


    fun updateModels(models: ArrayList<TripTimeModel>) {
        this.models = models
        this.models.sortByDescending {
            it.number.replace("""\D+""".toRegex(), "").toInt()
        }
        notifyDataSetChanged()
    }


}