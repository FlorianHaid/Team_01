package at.tu.graz.coffee.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.tu.graz.coffee.MainActivity
import at.tu.graz.coffee.R
import at.tu.graz.coffee.ui.home.SettingsViewModel
import java.util.*

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val radioGroupLanguage: RadioGroup = view.findViewById(R.id.radio_group_language)
        val radioBtnEnglish: RadioButton = view.findViewById(R.id.radio_btn_english)
        val radioBtnRussian: RadioButton = view.findViewById(R.id.radio_btn_russian)
        val radioBtnHungarian: RadioButton = view.findViewById(R.id.radio_btn_hungarian)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val savedLanguage = sharedPref!!.getString(getString(R.string.saved_language), "en")

        when (savedLanguage) {
            "en" -> radioGroupLanguage.check(R.id.radio_btn_english)
            "ru" -> radioGroupLanguage.check(R.id.radio_btn_russian)
            "hu" -> radioGroupLanguage.check(R.id.radio_btn_hungarian)
        }

        val applyButton = view.findViewById<Button>(R.id.settings_apply_button)
        applyButton.setOnClickListener {
            var language = "en"

            if (radioBtnRussian.isChecked) {
                language = "ru"
            }

            if (radioBtnHungarian.isChecked) {
                language = "hu"
            }

            with(sharedPref.edit()) {
                putString(getString(R.string.saved_language), language)
                apply()
            }

            changeLanguage(language)
        }
    }

    private fun changeLanguage(languageToSet: String) {
        val usedResource = resources
        val configurationToSet = usedResource.configuration
        val displayMetricsToSet = usedResource.displayMetrics

        configurationToSet.setLocale(Locale(languageToSet))
        usedResource.updateConfiguration(configurationToSet, displayMetricsToSet)
        val currentUpdater = Intent(context, MainActivity::class.java)
        startActivity(currentUpdater)
    }
}