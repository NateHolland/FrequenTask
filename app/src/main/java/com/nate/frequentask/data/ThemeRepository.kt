package com.nate.frequentask.data

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson

class ThemeRepository(context: Context) {

    companion object {
        private const val THEME_DATA = "THEME_DATA"
    }

    private val pref: SharedPreferences =
        context.getSharedPreferences("SESSION_DATA", Context.MODE_PRIVATE)
    private val editor = pref.edit()
    private val gson = Gson()

    private fun String.put(string: String) {
        editor.putString(this, string)
        editor.commit()
    }

    private fun String.getString() = pref.getString(this, "")!!

    private val _themesList = MutableLiveData(getThemes())
    val themesList: LiveData<List<Theme>> = _themesList

    private fun setThemes(theme: List<Theme>) {
        THEME_DATA.put(gson.toJson(ThemeList(theme)))
    }

    private fun getThemes(): List<Theme> {
        THEME_DATA.getString().also { theme ->
            return if (theme.isNotEmpty())
                gson.fromJson(theme, ThemeList::class.java).themes
            else
                emptyList()
        }
    }

    fun updateThemeName(themeID: String, name: String) {
        themeID.findTheme()?.apply {
            updateTheme(copy(name = name))
        }
    }

    private fun updateTheme(theme: Theme) {
        themesList.value?.apply {
            _themesList.value = filter { it.id != theme.id } + theme
        }
        saveThemes()
    }

    private fun saveThemes() {
        themesList.value?.also { setThemes(it) }
    }

    private fun String.findTheme(): Theme? {
        return themesList.value?.find { it.id == this }
    }

    fun addTheme(theme: Theme) {
        themesList.value?.apply {
            _themesList.value = this + theme
        }
        saveThemes()
    }

    fun updateTask(themeID: String, task: Theme.Task) {
        themeID.findTheme()?.apply {
            updateTheme(copy(tasks = tasks.filter { it.id != task.id } + task))
        }
    }

    fun addTask(themeID: String, task: Theme.Task) {
        themeID.findTheme()?.apply {
            updateTheme(copy(tasks = tasks + task))
        }
    }

    fun updateTaskName(themeId: String, task: Theme.Task, taskName: String) {
        updateTask(themeId, task.copy(name = taskName))
    }

    fun deleteTask(themeID: String, taskId: String) {
        themeID.findTheme()?.apply {
            updateTheme(copy(tasks = tasks.filter { it.id != taskId }))
        }
    }

    fun deleteTheme(theme: Theme) {
        themesList.value?.apply {
            _themesList.value = filter { it.id != theme.id }
        }
        saveThemes()
    }

    fun setActiveTheme(theme: Theme, active: Boolean) {
        updateTheme(theme.copy(active = active))
    }

    data class ThemeList(val themes: List<Theme>)
}