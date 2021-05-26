package at.tu.graz.coffee.ui.filter_result

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.navigation.Navigation.findNavController
import at.tu.graz.coffee.R
import at.tu.graz.coffee.model.Coffee

class FilterResultAdapter(context: Context, coffeeList:List<Coffee>): BaseAdapter() {
    private val mContext: Context = context
    private val mCoffeeList: List<Coffee> = coffeeList

    override fun getCount(): Int {
        return mCoffeeList.size
    }

    override fun getItem(p0: Int): Any {
        return mCoffeeList[p0]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup): View {
        val homeRow: View
        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(mContext)
            homeRow = layoutInflater.inflate(R.layout.home_row, viewGroup, false)
            val nameTextView = homeRow.findViewById<TextView>(R.id.home_textView_name)
            val ratingTextView = homeRow.findViewById<TextView>(R.id.home_textView_rating)
            val imageView = homeRow.findViewById<ImageView>(R.id.home_imageView)
            val ratingBar = homeRow.findViewById<RatingBar>(R.id.home_ratingBar)
            val viewholder = ViewHolderPattern(nameTextView, ratingTextView, imageView, ratingBar)
            homeRow.tag = viewholder
        } else {
            homeRow = convertView
        }

        homeRow.setOnClickListener {
            val action = FilterResultFragmentDirections.actionOpenDetails(mCoffeeList[position].id)
            findNavController(viewGroup).navigate(action)
        }

        val viewholder = homeRow.tag as ViewHolderPattern
        viewholder.nameTextView.text = mCoffeeList[position].name

        viewholder.ratingTextView.text = String.format("%.1f", mCoffeeList[position].evaluationTotal)

        val imageRes = mContext.resources.getIdentifier(mCoffeeList[position].image, "drawable", mContext.packageName)

        if(imageRes != 0) {
            viewholder.imageView.setImageResource(imageRes)
        } else {
            val uri = Uri.parse(mCoffeeList[position].image)
            viewholder.imageView.setImageURI(uri)
        }

        viewholder.ratingBar.rating = mCoffeeList[position].evaluationTotal.toFloat()
        return homeRow
    }

    private class ViewHolderPattern(val nameTextView: TextView, val ratingTextView: TextView,
                                    val imageView: ImageView, val ratingBar: RatingBar)
}