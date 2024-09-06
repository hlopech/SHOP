import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shop.data.Product
import com.example.shop.ui.theme.LightBlue
import com.example.shop.ui.theme.Orange

@Composable
fun RatingDialog(
    navController: NavController,
    products: List<Product>,
    onDismiss: () -> Unit,
    onRate: (Product, Int) -> Unit
) {
    val currentProductIndex = remember { mutableStateOf(0) }
    val rating = remember { mutableStateOf(0) }

    if (currentProductIndex.value < products.size) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = {
                Text(
                    text = "Спасибо за покупку! Пожалуйста оцените товары.",
                    textAlign = TextAlign.Center,
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Card(
                        elevation = CardDefaults.cardElevation(5.dp),
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier.size(230.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = products[currentProductIndex.value].imageUrl,
                                contentDescription = "",
                                modifier = Modifier.size(230.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "${products[currentProductIndex.value].name}",
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        (1..5).forEach { star ->
                            IconButton(
                                onClick = {
                                    rating.value = star
                                    onRate(products[currentProductIndex.value], rating.value)
                                    rating.value = 0
                                    currentProductIndex.value++
                                    onDismiss
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "",
                                    tint = Orange,
                                    modifier = Modifier.size(40.dp)

                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {

            },
            dismissButton = {

            }
        )
    } else {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(text = "Спасибо!") },
            text = { Text(text = "Благодарим за обратную связь") },
            confirmButton = {
                Button(onClick = {
                    onDismiss
                    navController.popBackStack()
                }, colors = ButtonDefaults.buttonColors(LightBlue)) {
                    Text("Закрыть")
                }
            }
        )
    }
}
