package at.tu.graz.coffee.ui.filter_result

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import at.tu.graz.coffee.R
import at.tu.graz.coffee.model.Coffee
import at.tu.graz.coffee.model.CoffeeWithReviews


class FilterResultAdapter :
    ListAdapter<CoffeeWithReviews, FilterResultAdapter.FilterResultViewHolder>(COFFEE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterResultViewHolder {
        return FilterResultViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: FilterResultViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.coffee)
    }

    class FilterResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView = itemView.findViewById<TextView>(R.id.home_textView_name)
        private val ratingTextView = itemView.findViewById<TextView>(R.id.home_textView_rating)
        private val imageView = itemView.findViewById<ImageView>(R.id.home_imageView)
        private val ratingBar = itemView.findViewById<RatingBar>(R.id.home_ratingBar)

        fun bind(coffee: Coffee) {
            itemView.setOnClickListener {
                val action = FilterResultFragmentDirections.actionOpenDetails(coffee.coffeeId)
                findNavController(itemView).navigate(action)
            }

            nameTextView.text = coffee.name
            ratingTextView.text = String.format("%.1f", coffee.evaluationTotal)

            val imageRes = itemView.resources.getIdentifier(
                coffee.image,
                "drawable",
                itemView.context.packageName
            )

            if (imageRes != 0) {
                imageView.setImageResource(imageRes)
            } else {
                val uri = Uri.parse(coffee.image)
                imageView.setImageURI(uri)
            }

            ratingBar.rating = coffee.evaluationTotal.toFloat()
        }

        companion object {
            fun create(parent: ViewGroup): FilterResultViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.home_row, parent, false)
                return FilterResultViewHolder(view)
            }
        }
    }

    companion object {
        private val COFFEE_COMPARATOR = object : DiffUtil.ItemCallback<CoffeeWithReviews>() {
            override fun areItemsTheSame(
                oldItem: CoffeeWithReviews,
                newItem: CoffeeWithReviews
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: CoffeeWithReviews,
                newItem: CoffeeWithReviews
            ): Boolean {
                return oldItem.coffee.coffeeId == newItem.coffee.coffeeId
            }
        }
    }
}