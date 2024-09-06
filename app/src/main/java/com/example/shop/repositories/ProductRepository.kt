package com.example.shop.repositories

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import com.example.shop.data.Category
import com.example.shop.data.Product
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ProductRepository {
    private val fs = Firebase.firestore
    private val storage = Firebase.storage


    fun getProductsByCategory(
        products: MutableLiveData<List<Product?>>,
        category: Category?
    ) {
        fs.collection("products").get()
            .addOnSuccessListener { documents ->
                val productsList = mutableStateOf(ArrayList<Product?>())
                for (document in documents) {
                    val product = document.toObject(Product::class.java)
                    if (product.category == category?.title) {
                        productsList.value.add(product)
                    }
                }
                products.value = productsList.value
            }
            .addOnFailureListener {
                products.value = null
            }
    }

    fun getProductsByCategories(
        products: MutableLiveData<List<Product?>>,
        caregories: List<String>,
    ) {
        if (caregories.isEmpty()) {
            products.value = emptyList()
            return
        }

        fs.collection("products").whereIn("category", caregories).get()
            .addOnSuccessListener { result ->
                val _products = result.documents.mapNotNull { it.toObject(Product::class.java) }
                products.value = _products

            }
            .addOnFailureListener { exception ->
                products.value = emptyList()
            }

    }

    fun getProductsByQuery(
        products: MutableLiveData<List<Product?>>,
        query: String
    ) {
        fs.collection("products").get()
            .addOnSuccessListener { documents ->
                val productsList = mutableStateOf(ArrayList<Product?>())
                for (document in documents) {
                    val product = document.toObject(Product::class.java)
                    if (product.name.contains(query) || product.category.contains(query) || product.description.contains(
                            query
                        ) || product.properties.first().value.contains(query) || product.properties.last().value.contains(
                            query
                        )
                    ) {
                        productsList.value.add(product)
                    }
                }
                products.value = productsList.value
            }
            .addOnFailureListener {
                products.value = null
            }
    }

    fun getAllProducts(
        products: MutableLiveData<List<Product?>>
    ) {
        fs.collection("products").get()
            .addOnSuccessListener { documents ->
                val productsList = mutableListOf<Product?>()
                for (document in documents) {
                    val product = document.toObject(Product::class.java)
                    productsList.add(product)
                }
                products.value = productsList
            }
            .addOnFailureListener {
                products.value = null
            }

    }

    fun getPopularProducts(
        products: MutableLiveData<List<Product?>>
    ) {
        fs.collection("products").get()
            .addOnSuccessListener { documents ->
                val productsList = mutableListOf<Product?>()
                for (document in documents) {
                    val product = document.toObject(Product::class.java)
                    productsList.add(product)
                }
                products.value = productsList.sortedByDescending { it?.sales }
            }
            .addOnFailureListener {
                products.value = null
            }

    }


    fun getProductsBySellerId(
        products: MutableLiveData<List<Product?>>,
        sellerId: String?
    ) {

        fs.collection("products").get()
            .addOnSuccessListener { documents ->
                val productsList = mutableListOf<Product?>()
                for (document in documents) {
                    val product = document.toObject(Product::class.java)
                    if (product.sellerId == sellerId) {
                        productsList.add(product)
                    }
                }
                products.value = productsList

            }
            .addOnFailureListener {
                products.value = null
            }

    }


    fun getProductByUid(uid: String, product: MutableState<Product?>) {
        fs.collection("products").document(uid).get().addOnSuccessListener { document ->
            product.value = document.toObject(Product::class.java)
        }
    }

    fun getProductsByUidList(
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
            .addOnSuccessListener { result ->
                val _products = result.documents.mapNotNull { it.toObject(Product::class.java) }
                products.value = _products

            }
            .addOnFailureListener { exception ->
                Log.e("HomeScreenViewModel", "Ошибка при загрузке избранных товаров", exception)
                products.value = emptyList()
            }

    }

    fun deleteProduct(context: Context, product: Product?) {
        val productDocRef = fs.collection("products").document(product?.id!!)
        productDocRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val imageUrl = document.getString("imageUrl")
                if (!imageUrl.isNullOrEmpty()) {
                    val imageRef = storage.getReferenceFromUrl(imageUrl)
                    imageRef.delete().addOnCompleteListener { deleteImageTask ->
                        if (deleteImageTask.isSuccessful) {
                            productDocRef.delete().addOnCompleteListener { deleteProductDocTask ->
                                if (deleteProductDocTask.isSuccessful) {
                                    Toast.makeText(
                                        context,
                                        "Продукт и его изображение успешно удалены.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Ошибка при удалении продукта.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Ошибка при удалении изображения продукта.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    productDocRef.delete().addOnCompleteListener { deleteProductDocTask ->
                        if (deleteProductDocTask.isSuccessful) {
                            Toast.makeText(
                                context,
                                "Продукт успешно удален (без изображения).",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                "Ошибка при удалении продукта.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } else {
                Toast.makeText(
                    context,
                    "Документ продукта не существует.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }.addOnFailureListener { e ->
            Toast.makeText(
                context,
                "Ошибка при получении документа продукта: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    fun updateProduct(
        product: Product
    ) {

        fs.collection("products").document(product.id).set(product)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                } else {

                }
            }
    }

    fun createProduct(
        context: Context,
        product: Product
    ) {
        fs.collection("requestsToCreateProduct").document(product.id).delete()
        fs.collection("products").document(product.id)
            .set(product)
            .addOnSuccessListener {
                Toast.makeText(context, "Товар добавлен успешно!", Toast.LENGTH_SHORT)
                    .show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Произошла ошибка: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

}