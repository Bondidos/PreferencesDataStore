package com.example.preferencesdatastore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import com.example.preferencesdatastore.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = requireNotNull(_binding)

    private lateinit var dataStore:DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataStore = createDataStore(name = "settings")
        binding.saveBtn.setOnClickListener {
            lifecycleScope.launch {
                save(
                    binding.editKey.text.toString(),
                    binding.editTextValue.text.toString()
                )
            }
        }

        binding.getBtn.setOnClickListener {
            lifecycleScope.launch {
                binding.resultText.text=get(
                    binding.editKeyToGet.text.toString()
                )
            }
        }
    }

    private suspend fun save(key: String, value: String){
        val dataStoreKey = preferencesKey<String>(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    private suspend fun get(key: String): String? {
        val dataStoreKey = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
