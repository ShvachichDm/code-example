package io.polydev.public_transport.utils.data

import android.content.Context
import android.content.SharedPreferences


object PreferencesManager {

    private val PREF_NAME = "transport_preferences"
    private val PREF_TOKEN = "token_pref"
    private val PREF_LOGIN = "login_pref"
    private val PREF_AUTHORIZATION_STATUS = "authorization_status_pref"

    private var context: Context? = null
    private val pref: SharedPreferences by lazy {
        context!!.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun setContext(context: Context) {
        this.context = context
    }

    fun getToken(): String{
        return pref.getString(PREF_TOKEN,"")!!
    }

    fun updateToken(token: String){
        pref.edit()
            .putString(PREF_TOKEN,token)
            .apply()
    }

    fun getLogin(): String{
        return pref.getString(PREF_LOGIN,"")!!
    }

    fun updateLogin(login: String){
        pref.edit()
            .putString(PREF_LOGIN,login)
            .apply()
    }

    fun getAuthorizationStatus(): Boolean {
        return pref.getBoolean(PREF_AUTHORIZATION_STATUS,false)
    }

    fun updateAuthorizationStatus(status: Boolean) {
        pref.edit()
            .putBoolean(PREF_AUTHORIZATION_STATUS,status)
            .apply()
    }

}