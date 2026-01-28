package com.example.shop.domain.useCases


import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.lifecycle.MutableLiveData
import com.example.shop.domain.model.Category
import com.example.shop.domain.model.OrderedProduct
import com.example.shop.domain.model.Product
import com.example.shop.domain.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class GetProductsByCategoryUseCase @Inject constructor(
    private val repo: ProductRepository
) {
    operator fun invoke(
        products: MutableLiveData<List<Product?>>,
        category: Category?
    ) = repo.getProductsByCategory(products, category)
}
class GetProductsByCategoriesUseCase @Inject constructor(
    private val repo: ProductRepository
) {
    operator fun invoke(
        products: MutableLiveData<List<Product?>>,
        categories: List<String>
    ) = repo.getProductsByCategories(products, categories)
}

class GetProductsByQueryUseCase @Inject constructor(
    private val repo: ProductRepository
) {
    operator fun invoke(
        products: MutableLiveData<List<Product?>>,
        query: String
    ) = repo.getProductsByQuery(products, query)
}
class GetAllProductsUseCase @Inject constructor(
    private val repo: ProductRepository
) {
    operator fun invoke(products: MutableLiveData<List<Product?>>) =
        repo.getAllProducts(products)
}

class GetPopularProductsUseCase @Inject constructor(
    private val repo: ProductRepository
) {
    operator fun invoke(products: MutableLiveData<List<Product?>>) =
        repo.getPopularProducts(products)
}

class GetProductsBySellerIdUseCase @Inject constructor(
    private val repo: ProductRepository
) {
    operator fun invoke(
        products: MutableLiveData<List<Product?>>,
        sellerId: String?
    ) = repo.getProductsBySellerId(products, sellerId)
}

class GetProductByUidUseCase @Inject constructor(
    private val repo: ProductRepository
) {
    operator fun invoke(
        uid: String,
        product: MutableState<Product?>
    ) = repo.getProductByUid(uid, product)
}

class GetProductsByUidListUseCase @Inject constructor(
    private val repo: ProductRepository
) {
    operator fun invoke(
        uids: List<String>,
        products: MutableLiveData<List<Product>>
    ) = repo.getProductsByUidList(uids, products)
}

class DeleteProductUseCase @Inject constructor(
    private val repo: ProductRepository
) {
    operator fun invoke(context: Context, product: Product?) =
        repo.deleteProduct(context, product)
}

class UpdateProductUseCase @Inject constructor(
    private val repo: ProductRepository
) {
    operator fun invoke(product: Product) = repo.updateProduct(product)
}

class CreateOrderedProductUseCase @Inject constructor(
    private val repo: ProductRepository
) {
    operator fun invoke(productId: String, userId: String) =
        repo.createOrderedProduct(productId, userId)
}


class GetOrderedProductsUIDSUseCase @Inject constructor(
    private val repo: ProductRepository
) {
    operator fun invoke(
        products: MutableStateFlow<List<OrderedProduct>>,
        userId: String?
    ) = repo.getOrderedProductsUIDS(products, userId)
}
class GetSellerOrderedProductsUIDSUseCase @Inject constructor(
    private val repo: ProductRepository
) {
    operator fun invoke(
        products: MutableStateFlow<List<OrderedProduct>>,
        sellerId: String?
    ) = repo.getSellerOrderedProductsUIDS(products, sellerId)
}

class UpdateOrderedProductUseCase @Inject constructor(
    private val repo: ProductRepository
) {
    operator fun invoke(product: OrderedProduct) =
        repo.updateOrderedProduct(product)
}

class CreateProductUseCase @Inject constructor(
    private val repo: ProductRepository
) {
    operator fun invoke(context: Context, product: Product) =
        repo.createProduct(context, product)
}

class RegisterPackageUseCase @Inject constructor(
    private val repo: ProductRepository
) {
    suspend operator fun invoke(
        orderId: String,
        trackNumber: String,
        recipientName: String,
        recipientPhone: String,
        destinationAddress: String
    ) = repo.registerPackage(orderId, trackNumber, recipientName, recipientPhone, destinationAddress)
}

