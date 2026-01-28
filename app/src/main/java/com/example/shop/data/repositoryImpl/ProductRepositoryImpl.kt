// data/repositoryImpl/ProductRepositoryImpl.kt
package com.example.shop.data.repositoryImpl

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.lifecycle.MutableLiveData
import com.example.shop.data.remote.NetworkModule
import com.example.shop.domain.model.*
import com.example.shop.domain.repository.ProductRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val fs :FirebaseFirestore,
    private val storage : FirebaseStorage
) : ProductRepository {


    override fun getProductsByCategory(
        products: MutableLiveData<List<Product?>>,
        category: Category?
    ) {
        fs.collection("products").get()
            .addOnSuccessListener { docs ->
                val list = docs.mapNotNull { it.toObject(Product::class.java) }
                    .filter { it.category == category?.title }
                products.value = list
            }
            .addOnFailureListener {
                products.value = null
            }
    }

    override fun getProductsByCategories(
        products: MutableLiveData<List<Product?>>,
        categories: List<String>
    ) {
        if (categories.isEmpty()) {
            products.value = emptyList()
            return
        }
        fs.collection("products")
            .whereIn("category", categories)
            .get()
            .addOnSuccessListener { docs ->
                products.value = docs.mapNotNull { it.toObject(Product::class.java) }
            }
            .addOnFailureListener {
                products.value = emptyList()
            }
    }

    override fun getProductsByQuery(
        products: MutableLiveData<List<Product?>>,
        query: String
    ) {
        fs.collection("products").get()
            .addOnSuccessListener { docs ->
                val list = docs.mapNotNull { it.toObject(Product::class.java) }
                    .filter {
                        it.name.contains(query, true) ||
                                it.category.contains(query, true) ||
                                it.description.contains(query, true) ||
                                it.properties.any { prop -> prop.value.contains(query, true) }
                    }
                products.value = list
            }
            .addOnFailureListener {
                products.value = null
            }
    }

    override fun getAllProducts(
        products: MutableLiveData<List<Product?>>
    ) {
        fs.collection("products").get()
            .addOnSuccessListener { docs ->
                products.value = docs.map { it.toObject(Product::class.java) }
            }
            .addOnFailureListener {
                products.value = null
            }
    }

    override fun getPopularProducts(
        products: MutableLiveData<List<Product?>>
    ) {
        fs.collection("products").get()
            .addOnSuccessListener { docs ->
                val list = docs.mapNotNull { it.toObject(Product::class.java) }
                    .sortedByDescending { it.sales }
                products.value = list
            }
            .addOnFailureListener {
                products.value = null
            }
    }

    override fun getProductsBySellerId(
        products: MutableLiveData<List<Product?>>,
        sellerId: String?
    ) {
        fs.collection("products").get()
            .addOnSuccessListener { docs ->
                products.value = docs.mapNotNull { it.toObject(Product::class.java) }
                    .filter { it.sellerId == sellerId }
            }
            .addOnFailureListener {
                products.value = null
            }
    }

    override fun getProductByUid(
        uid: String,
        product: MutableState<Product?>
    ) {
        fs.collection("products").document(uid).get()
            .addOnSuccessListener { doc ->
                product.value = doc.toObject(Product::class.java)
            }
    }

    override fun getProductsByUidList(
        uids: List<String>,
        products: MutableLiveData<List<Product>>
    ) {
        if (uids.isEmpty()) {
            products.value = emptyList()
            return
        }
        fs.collection("products")
            .whereIn("id", uids)
            .get()
            .addOnSuccessListener { docs ->
                products.value = docs.mapNotNull { it.toObject(Product::class.java) }
            }
            .addOnFailureListener {
                Log.e("ProductRepo", "Ошибка при загрузке избранных товаров", it)
                products.value = emptyList()
            }
    }

    override fun deleteProduct(context: Context, product: Product?) {
        product ?: return
        fs.collection("products").document(product.id).get()
            .addOnSuccessListener { doc ->
                val imageUrl = doc.getString("imageUrl") ?: ""
                if (imageUrl.isNotEmpty()) {
                    storage.getReferenceFromUrl(imageUrl)
                        .delete()
                        .addOnCompleteListener { /* ... */ }
                }
                doc.reference.delete()
                    .addOnSuccessListener {
                        Toast.makeText(context, "Продукт удалён", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Ошибка при удалении продукта", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
    }

    override fun updateProduct(product: Product) {
        fs.collection("products").document(product.id).set(product)
    }

    override fun createOrderedProduct(productId: String, userId: String) {
        fs.collection("products").document(productId).get()
            .addOnSuccessListener { snap ->
                val p = snap.toObject(Product::class.java)
                val sellerId = p?.sellerId ?: return@addOnSuccessListener
                val order = OrderedProduct(productId, userId, sellerId, "placed", 0)
                fs.collection("orderedProducts").document(productId).set(order)
            }
    }

    override fun getOrderedProductsUIDS(
        products: MutableStateFlow<List<OrderedProduct>>,
        userId: String?
    ) {
        fs.collection("orderedProducts").get()
            .addOnSuccessListener { docs ->
                products.value = docs.mapNotNull { it.toObject(OrderedProduct::class.java) }
                    .filter { it.userId == userId }
            }
            .addOnFailureListener {
                products.value = emptyList()
            }
    }

    override fun getSellerOrderedProductsUIDS(
        products: MutableStateFlow<List<OrderedProduct>>,
        sellerId: String?
    ) {
        fs.collection("orderedProducts").get()
            .addOnSuccessListener { docs ->
                products.value = docs.mapNotNull { it.toObject(OrderedProduct::class.java) }
                    .filter { it.sellerId == sellerId }
            }
            .addOnFailureListener {
                products.value = emptyList()
            }
    }

    override fun updateOrderedProduct(product: OrderedProduct) {
        fs.collection("orderedProducts").document(product.productId).set(product)
    }

    override fun createProduct(context: Context, product: Product) {
        fs.collection("requestsToCreateProduct").document(product.id).delete()
        fs.collection("products").document(product.id).set(product)
            .addOnSuccessListener {
                Toast.makeText(context, "Товар добавлен успешно!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override suspend fun registerPackage(
        orderId: String,
        trackNumber: String,
        recipientName: String,
        recipientPhone: String,
        destinationAddress: String
    ) {
        fs.collection("orderedProducts").document(orderId)
            .update(
                mapOf(
                    "status" to "inTransit",
                    "trackingCode" to trackNumber
                )
            ).await()

        val resp = NetworkModule.belpochtaApi.addPackage(
            token = "FmYgrJR8M43T_LeVQLCWcvh_UwRks5ng",
            trackNumber = trackNumber,
            recipientName = recipientName,
            recipientPhone = recipientPhone,
            destinationAddress = destinationAddress
        )
        if (resp.code != 200) {
            Log.d("BELPOST", "Ошибка кода: ${resp.code}")
        }
    }
}
