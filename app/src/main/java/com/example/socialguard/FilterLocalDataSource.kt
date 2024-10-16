package com.example.socialguard
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FilterLocalDataSource(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("filters_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    // Sauvegarde la liste des filtres dans SharedPreferences
    fun saveFilters(filters: List<Filter>) {
        val json = gson.toJson(filters)
        sharedPreferences.edit()
            .putString("filters_key", json)
            .apply()
    }

    // Récupère la liste des filtres depuis SharedPreferences
    fun getFilters(): List<Filter> {
        val json = sharedPreferences.getString("filters_key", null) ?: return emptyList()
        val type = object : TypeToken<List<Filter>>() {}.type
        return gson.fromJson(json, type)
    }
}
