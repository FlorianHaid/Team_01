package at.tu.graz.coffee.ui.add_coffee

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import at.tu.graz.coffee.R
import at.tu.graz.coffee.model.Coffee
import at.tu.graz.coffee.model.CoffeeData
import at.tu.graz.coffee.model.CoffeeType
import kotlinx.android.synthetic.main.fragment_add_coffee.*
import kotlinx.android.synthetic.main.fragment_filter_result.*
import com.stfalcon.frescoimageviewer.ImageViewer


class AddCoffeeFragment : Fragment() {

    private lateinit var addCoffeeViewModel: AddCoffeeViewModel

    private var uriPicture: Uri? = null

    private val pictureSelector = 100

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addCoffeeViewModel =
            ViewModelProvider(this).get(AddCoffeeViewModel::class.java)

        return inflater.inflate(R.layout.fragment_add_coffee, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            CoffeeType.values()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        button_addCoffee.setOnClickListener{
            var missingField = false

            if(coffee_name.text.isEmpty()) {
                missingField = true
                coffee_name.setBackgroundColor(Color.RED)
            }
            else {
                coffee_name.setBackgroundColor(Color.WHITE)
            }

            if(coffee_shop.text.isEmpty()) {
                missingField = true
                coffee_shop.setBackgroundColor(Color.RED)
            }
            else {
                coffee_shop.setBackgroundColor(Color.WHITE)
            }

            if(coffee_price.text.isEmpty()) {
                missingField = true
                coffee_price.setBackgroundColor(Color.RED)
            }
            else {
                coffee_price.setBackgroundColor(Color.WHITE)
            }

            if(imageView.drawable == null) {
                missingField = true
                button_addPicture.setBackgroundColor(Color.RED)
            }
            else {
                button_addPicture.setBackgroundColor(resources.getColor(R.color.purple_500))
            }

            if(missingField) {
                mandatory_field.visibility = View.VISIBLE
            } else {
                mandatory_field.visibility = View.GONE
                addCoffee(view)
            }
        }

        button_addPicture.setOnClickListener{
            val phonePictures = Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                    startActivityForResult(phonePictures, pictureSelector)
        }

        spinner_type?.adapter = adapter;

        spinner_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    fun addCoffee(view: View) {
        val coffee = Coffee(1234, coffee_name.text.toString(), coffee_price.text.toString().toDouble(),
            coffee_shop.text.toString(), spinner_type.selectedItem as CoffeeType, coffee_qty.text.toString().toDouble(),
            coffee_strength.values[0].toInt(), txt_additional_information.text.toString(), uriPicture.toString())

        CoffeeData.addCoffee(coffee)

        val action = AddCoffeeFragmentDirections.actionOpenHome()
        Navigation.findNavController(view).navigate(action)

        Toast.makeText(activity,R.string.coffee_added, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(neededPart: Int, outcomePart: Int, info: Intent?) {
        super.onActivityResult(neededPart, outcomePart, info)

        if (neededPart == pictureSelector) {
            if (outcomePart == RESULT_OK) {
                uriPicture = info?.data
                imageView.setImageURI(uriPicture)

                val uri: MutableList<Uri> = ArrayList<Uri>()
                uri.add(uriPicture!!)

                imageView.setOnClickListener{
                    ImageViewer.Builder(context, uri)
                        .setStartPosition(0)
                        .show()
                }
            }
        }
    }
}