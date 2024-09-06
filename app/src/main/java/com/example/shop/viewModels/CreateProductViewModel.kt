package com.example.shop.viewModels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.shop.data.Product
import com.example.shop.data.RequestToChangeRole
import com.example.shop.data.RequestToCreateProduct
import com.example.shop.repositories.AuthRepository
import com.example.shop.repositories.ProductRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class CreateProductViewModel : ViewModel() {
    private val productRepository = ProductRepository()
    private val auth = Firebase.auth
    private val fs = Firebase.firestore

    fun sendRequestToCreateProduct(context: Context, product: Product) {
        val request = RequestToCreateProduct(auth.uid!!, "waiting", product)
        fs.collection("requestsToCreateProduct").document(product.id)
            .set(request)
            .addOnSuccessListener {
                Toast.makeText(context, "Запрос на создание товара отправлен", Toast.LENGTH_SHORT)
                    .show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Ошибка отправки запроса: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }


    fun dismissRequest(product: Product) {
        fs.collection("requestsToCreateProduct").document(product.id).delete()

    }

    fun createProduct(context: Context, product: Product) {
        productRepository.createProduct(context, product)
    }

}