package com.mohsenoid.phoneid

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.text.Html
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

        val imei = getImei()
        val phoneNumber = getPhoneNumber()
        val bluetoothName = getBluetoothName()
        val deviceName = getDeviceName()
        val subscriptionInfo = getSubscriptionInfo()

        phoneId.text = Html.fromHtml(
            getString(
                R.string.phone_id,
                imei,
                phoneNumber,
                deviceName,
                bluetoothName,
                subscriptionInfo?.iccId,
                subscriptionInfo
            ), Html.FROM_HTML_MODE_COMPACT
        )
    }

    @SuppressLint("MissingPermission")
    private fun getImei(): String? {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }

        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        //---get the phone IMEI---
        return try {
            telephonyManager.imei
        } catch (e: Exception) {
            null
        }
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

    private fun getDeviceName() =
        Settings.Secure.getString(contentResolver, Settings.Global.DEVICE_NAME)

    private fun getSubscriptionInfo(): SubscriptionInfo? {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }

        val subscriptionManager: SubscriptionManager =
            getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager

        val subscriptionInfoList: MutableList<SubscriptionInfo> =
            subscriptionManager.activeSubscriptionInfoList ?: return null

        return if (subscriptionInfoList.isNotEmpty()) subscriptionInfoList[0] else null
    }
}