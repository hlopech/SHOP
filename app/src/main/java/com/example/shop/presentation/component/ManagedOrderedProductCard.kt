package com.example.shop.presentation.component

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shop.domain.model.OrderedProduct
import com.example.shop.domain.model.Product
import com.example.shop.domain.model.User
import com.example.shop.presentation.common.viewmodel.MainViewModel
import com.example.shop.presentation.seller.viewmodel.ManageOrderedViewModel
import com.example.shop.ui.theme.LightBlue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagedOrderedProductCard(
    orderedProduct: OrderedProduct,
    navController: NavController,
    viewModel: MainViewModel,
    manageOrderedViewModel: ManageOrderedViewModel
) {
    val product = remember { mutableStateOf<Product?>(null) }
    val context = LocalContext.current
    val showCallSellerDialog = remember { mutableStateOf(false) }
    val buyer = remember { mutableStateOf<User?>(null) }
    LaunchedEffect(orderedProduct) {
        manageOrderedViewModel.getProductByUID(orderedProduct.productId, product)
        buyer.value =
            manageOrderedViewModel.getBuyerNumber(orderedProduct.sellerId)
    }
    val trackingCode = remember { mutableStateOf("") }

    Log.d("BBV", orderedProduct.toString())

    if (showCallSellerDialog.value) {
        AlertDialog(
            onDismissRequest = { showCallSellerDialog.value = false },
            title = { Text(text = "${buyer.value}") },
            text = { Text(text = "Вы можете позвонить продавцу для уточнения деталей доставки.") },
            confirmButton = {
                Button(onClick = {
                    showCallSellerDialog.value = false
                }, colors = ButtonDefaults.buttonColors(LightBlue)) {
                    Text("Закрыть")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showCallSellerDialog.value = false
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:${buyer.value}")
                        }
                        if (intent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(intent)
                        } else {
                            Toast
                                .makeText(context, "Нет приложения для звонков", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LightBlue)
                ) {
                    Text("Позвонить")
                }
            }
        )
    }
    if (product.value == null || buyer.value == null) {
        Box(modifier = Modifier.fillMaxSize()) {

            CircularProgressIndicator(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.Center),
                color = LightBlue,
                strokeWidth = 4.dp
            )
        }
    } else {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Spacer(Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                // Номер заказа
          /*      Text(
                    text = "Заказ №${product.value?.id}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )*/

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Картинка
                    AsyncImage(
                        model = product.value!!.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.width(16.dp))

                    Column {
                        Text(
                            text = product.value!!.name!!,
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Покупатель: ${buyer.value?.userName}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = buyer.value?.phone!!,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Адрес доставки",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Text(
                    text = buyer.value?.address!!,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )

                Spacer(Modifier.height(20.dp))

                OutlinedTextField(
                    value = trackingCode.value,
                    onValueChange = { trackingCode.value = it },
                    label = { Text("Трек‑номер посылки") },
                    placeholder = { Text("Введите трек‑номер") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                )

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = {
                        manageOrderedViewModel.confirmSending(
                            orderedProduct.productId,
                            trackNumber = trackingCode.value,
                            buyer.value!!.userName,
                            buyer.value!!.phone,
                            buyer.value!!.address
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Подтвердить отправку",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

    }
}