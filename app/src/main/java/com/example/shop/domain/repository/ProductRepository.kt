package com.example.shop.domain.repository

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.lifecycle.MutableLiveData
import com.example.shop.domain.model.Category
import com.example.shop.domain.model.OrderedProduct
import com.example.shop.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow

interface ProductRepository {
    fun getProductsByCategory(
        products: MutableLiveData<List<Product?>>,
        category: Category?
    )
    fun getProductsByCategories(
        products: MutableLiveData<List<Product?>>,
        categories: List<String>
    )
    fun getProductsByQuery(
        products: MutableLiveData<List<Product?>>,
        query: String
    )
    fun getAllProducts(
        products: MutableLiveData<List<Product?>>
    )
    fun getPopularProducts(
        products: MutableLiveData<List<Product?>>
    )
    fun getProductsBySellerId(
        products: MutableLiveData<List<Product?>>,
        sellerId: String?
    )
    fun getProductByUid(
        uid: String,
        product: MutableState<Product?>
    )
    fun getProductsByUidList(
        uids: List<String>,
        products: MutableLiveData<List<Product>>
    )
    fun deleteProduct(
        context: Context,
        product: Product?
    )
    fun updateProduct(
        product: Product
    )
    fun createOrderedProduct(
        productId: String,
        userId: String
    )
    fun getOrderedProductsUIDS(
        products: MutableStateFlow<List<OrderedProduct>>,
        userId: String?
    )
    fun getSellerOrderedProductsUIDS(
        products: MutableStateFlow<List<OrderedProduct>>,
        sellerId: String?
    )
    fun updateOrderedProduct(
        product: OrderedProduct
    )
    fun createProduct(
        context: Context,
        product: Product
    )
    suspend fun registerPackage(
        orderId: String,
        trackNumber: String,
        recipientName: String,
        recipientPhone: String,
        destinationAddress: String
    )
}