package com.hari.tmdb.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.hari.tmdb.di.PageScope
import com.hari.tmdb.ext.assistedViewModels
import com.hari.tmdb.model.NightMode
import com.hari.tmdb.settings.viewmodel.SettingsViewModel
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject
import javax.inject.Provider

class SettingsFragment : PreferenceFragmentCompat(), HasAndroidInjector {

    @Inject
    lateinit var settingsModelFactory: Provider<SettingsViewModel>
    private val settingsViewModel by assistedViewModels {
        settingsModelFactory.get()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting, rootKey)
    }

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*     val appComponent = (requireContext().applicationContext as App).appComponent
             val component = DaggerSettingsComponent.factory()
                 .create(appComponent, SettingsModule(this))
             component.inject(this)

     */
        preferenceManager?.findPreference<ListPreference>(DARK_THEME_KEY)?.also {
            settingsViewModel.setNightMode(it.value.toNightMode())
            it.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
                settingsViewModel.setNightMode((newValue as String).toNightMode())
                return@OnPreferenceChangeListener true
            }
        }

        settingsViewModel.uiModel.observe(viewLifecycleOwner) { uiModel ->
            AppCompatDelegate.setDefaultNightMode(uiModel.nightMode.platformValue)
        }
    }

    // region temporary functions until appropriate structure have built
    private fun String.toNightMode() = when (this) {
        getString(R.string.pref_theme_value_default) -> NightMode.SYSTEM
        getString(R.string.pref_theme_value_battery) -> NightMode.BATTERY
        getString(R.string.pref_theme_value_dark) -> NightMode.YES
        getString(R.string.pref_theme_value_light) -> NightMode.NO
        else -> throw IllegalArgumentException("should not happen")
    }

    private val NightMode.platformValue: Int
        get() = when (this) {
            NightMode.SYSTEM -> {
                if (android.os.Build.VERSION.SDK_INT < 29) androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                else androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
            NightMode.BATTERY -> androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
            NightMode.YES -> androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
            NightMode.NO -> androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
        }
    // endregion

    companion object {
        private const val DARK_THEME_KEY = "darkTheme"
    }
}

@Module
class SettingsModule {

    companion object {
        @PageScope
        @Provides
        fun providesLifecycleOwnerLiveData(
            settingsFragment: SettingsFragment
        ): LiveData<LifecycleOwner> {
            return settingsFragment.viewLifecycleOwnerLiveData
        }
    }
}


/*
@PageScope
@Component(
    modules = [
        SettingsModule::class,
        SettingsAssistedInjectModule::class
    ],
    dependencies = [AppComponent::class]
)
interface SettingsComponent {
    @Component.Factory
    interface Factory {
        fun create(
            appComponent: AppComponent,
            settingsModule: SettingsModule
        ): SettingsComponent
    }

    fun inject(fragment: SettingsFragment)
}*/
