package com.example.shop.data.local

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.shop.data.remote.NetworkModule
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class TrackingWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val firestore = Firebase.firestore
    private val api = NetworkModule.belpochtaApi
    private val token = "FmYgrJR8M43T_LeVQLCWcvh_UwRks5ng"

    override suspend fun doWork(): Result {
        try {
            val snap = firestore.collection("orderedProducts")
                .whereEqualTo("status", "inTransit")
                .get().await()

            val batch = firestore.batch()
            for (doc in snap.documents) {
                val orderId = doc.id
                val tn = doc.getString("trackingCode") ?: continue

                val resp = api.getPackage(token, tn)
                if (resp.code == 200) {
                    val status = resp.payload.status
                    if (status == 4 || status == 5) {
                        batch.update(
                            doc.reference,
                            mapOf(
                                "status" to "delivered",
                            )
                        )
                    }
                }
            }
            batch.commit().await()
            return Result.success()
        } catch (e: Exception) {
            return Result.retry()
        }
    }
}
