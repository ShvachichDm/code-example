package io.polydev.public_transport.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import io.polydev.public_transport.App
import io.polydev.public_transport.R
import io.polydev.public_transport.screens.authorization.AuthorizationActivity
import io.polydev.public_transport.screens.main.MainActivity
import io.polydev.public_transport.utils.data.PreferencesManager

class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (PreferencesManager.getAuthorizationStatus()) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            startActivity(Intent(this, AuthorizationActivity::class.java))
        }
    }


}