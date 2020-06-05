package com.mohsenoid.phoneid

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE), 1000)
        }
    }

    override fun onResume() {
        super.onResume()

        val phoneNumber = getPhoneNumber()
        val bluetoothName = getBluetoothName()
        val deviceName = getDeviceName()

        phoneId.text = "Device IDs\n" +
                "phoneNumber: $phoneNumber\n" +
                "bluetoothName: $bluetoothName\n" +
                "deviceName: $deviceName"
    }

    @SuppressLint("HardwareIds")
    private fun getPhoneNumber(): String? {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }

        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        //---get the phone number---
        return telephonyManager.line1Number
    }

    private fun getBluetoothName() = Settings.Secure.getString(contentResolver, "bluetooth_name")

    private fun getDeviceName() = Settings.Secure.getString(contentResolver, Settings.Global.DEVICE_NAME)
}