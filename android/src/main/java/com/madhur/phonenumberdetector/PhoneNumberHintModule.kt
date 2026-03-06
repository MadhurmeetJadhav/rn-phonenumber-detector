package com.madhur.phonenumberdetector

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import com.facebook.react.bridge.ActivityEventListener
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.google.android.gms.auth.api.identity.GetPhoneNumberHintIntentRequest
import com.google.android.gms.auth.api.identity.Identity

class PhoneNumberHintModule(private val reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    private var pickerPromise: Promise? = null

    private val activityEventListener = object : ActivityEventListener {
        override fun onActivityResult(
            activity: Activity,
            requestCode: Int,
            resultCode: Int,
            data: Intent?
        ) {
            if (requestCode != PHONE_HINT_REQUEST_CODE) return
            pickerPromise?.let { promise ->
                try {
                    val phoneNumber = Identity
                        .getSignInClient(activity)
                        .getPhoneNumberFromIntent(data)
                    promise.resolve(phoneNumber)
                } catch (e: Exception) {
                    promise.reject("HINT_FAILED", e.message)
                } finally {
                    pickerPromise = null
                }
            }
        }

        override fun onNewIntent(intent: Intent) {}
    }

    init {
        reactContext.addActivityEventListener(activityEventListener)
    }

    override fun getName() = "PhoneNumberHint"

    @ReactMethod
    fun requestHint(promise: Promise) {
        val activity = reactContext.currentActivity ?: run {
            promise.reject("NO_ACTIVITY", "Activity is null")
            return
        }

        pickerPromise = promise

        val request = GetPhoneNumberHintIntentRequest.builder().build()

        Identity.getSignInClient(activity)
            .getPhoneNumberHintIntent(request)
            .addOnSuccessListener { pendingIntent ->
                try {
                    activity.startIntentSenderForResult(
                        pendingIntent.intentSender,
                        PHONE_HINT_REQUEST_CODE,
                        null, 0, 0, 0
                    )
                } catch (e: IntentSender.SendIntentException) {
                    promise.reject("INTENT_ERROR", e.message)
                    pickerPromise = null
                }
            }
            .addOnFailureListener { e ->
                promise.reject("HINT_UNAVAILABLE", e.message)
                pickerPromise = null
            }
    }

    companion object {
        private const val PHONE_HINT_REQUEST_CODE = 11000
    }
}