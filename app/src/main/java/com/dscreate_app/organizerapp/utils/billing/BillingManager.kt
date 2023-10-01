package com.dscreate_app.organizerapp.utils.billing

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.AcknowledgePurchaseResponseListener
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.SkuDetailsParams
import com.dscreate_app.organizerapp.R
import com.dscreate_app.organizerapp.utils.OrganizerAppConsts
import com.dscreate_app.organizerapp.utils.showToast

class BillingManager(private val activity: AppCompatActivity) {

    private var bClient: BillingClient? = null

    init {
        setupBillingClient()
    }

    private fun setupBillingClient() {
        bClient = BillingClient.newBuilder(activity)
            .setListener(getPurchaseListener())
            .enablePendingPurchases()
            .build()
    }

    private fun getPurchaseListener(): PurchasesUpdatedListener {
        return PurchasesUpdatedListener {
                billingResult, list ->
            run {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    list?.get(0)?.let { nonConsumableItem(it) }
                }
            }
        }
    }

    private fun nonConsumableItem(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken).build()
                bClient?.acknowledgePurchase(acParams) {
                    if (it.responseCode == BillingClient.BillingResponseCode.OK) {
                        savePref(true) //если успешно прошла покупка
                        activity.showToast(activity.getString(R.string.purchase_done))
                    } else {
                        savePref(false) //если неуспешно прошла покупка
                        activity.showToast(activity.getString(R.string.purchase_error))
                    }
                }
            }
        }
    }

    fun startConnection() {
        bClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
            }

            override fun onBillingSetupFinished(bResult: BillingResult) {
                getItem()
            }
        })
    }

    fun closeConnection() {
        bClient?.endConnection()
    }

    private fun getItem() {
        val skuList = mutableListOf<String>()
        skuList.add(OrganizerAppConsts.REMOVE_AD_ITEM)
        val skuDetails = SkuDetailsParams.newBuilder()
        skuDetails.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
        bClient?.querySkuDetailsAsync(skuDetails.build()) {
            bResult, list ->
            run {
                if (bResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    list?.let {
                        if (list.isNotEmpty()) {
                            val billingFlowParams = BillingFlowParams.newBuilder()
                                .setSkuDetails(list[0]).build()
                            bClient?.launchBillingFlow(activity, billingFlowParams)
                        }
                    }
                }
            }
        }
    }

    private fun savePref(isPurchase: Boolean) {
        val sharedPref = activity.getSharedPreferences(
            OrganizerAppConsts.MAIN_PREF, Context.MODE_PRIVATE
        )
        val editor = sharedPref.edit()
        editor.putBoolean(OrganizerAppConsts.REMOVE_ADS_KEY, isPurchase)
        editor.apply()
    }
}