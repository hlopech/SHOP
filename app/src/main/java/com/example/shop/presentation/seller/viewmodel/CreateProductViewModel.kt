package com.example.shop.presentation.seller.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.shop.domain.model.Product
import com.example.shop.domain.model.RequestToCreateProduct
import com.example.shop.data.repositoryImpl.ProductRepositoryImpl
import com.example.shop.domain.useCases.CreateOrderedProductUseCase
import com.example.shop.domain.useCases.CreateProductUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateProductViewModel @Inject constructor(
    private val auth : FirebaseAuth,
    private val fs : FirebaseFirestore,
    private val createProductUseCase: CreateProductUseCase
) : ViewModel() {

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
        createProductUseCase.invoke(context, product)
    }

}