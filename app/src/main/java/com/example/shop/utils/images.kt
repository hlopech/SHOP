package com.example.shop.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.example.shop.data.Product
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

fun bitmapToByteArray(context: Context, uri: Uri): ByteArray {
    val inputStream = context.contentResolver.openInputStream(uri)
    val bitmap = BitmapFactory.decodeStream(inputStream)
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos)
    return baos.toByteArray()
}


@Composable
fun setProductImage(
    context: Context,
    imageUrl: MutableState<String?>,
    product: Product
): ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?> {
    val auth = Firebase.auth
    val storage = Firebase.storage.reference.child("product-images")
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        val task =
            storage.child(auth.currentUser?.uid.toString() + "-image-" + product.id).putBytes(
                bitmapToByteArray(context, uri)
            )
        task.addOnSuccessListener { uploadTask ->
            uploadTask.metadata?.reference?.downloadUrl?.addOnCompleteListener { uriTask ->
                imageUrl.value = uriTask.result.toString()
            }
        }

    }
    return launcher

}


@Composable
fun setUserAvatarImage(
    context: Context,
    imageUrl: MutableState<String?>,

    ): ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?> {
    val auth = Firebase.auth
    val storage = Firebase.storage.reference.child("users-avatar-images")
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        val task = storage.child(auth.currentUser?.uid.toString() + "-avatar").putBytes(
            bitmapToByteArray(context, uri)
        )
        task.addOnSuccessListener { uploadTask ->
            uploadTask.metadata?.reference?.downloadUrl?.addOnCompleteListener { uriTask ->
                imageUrl.value = uriTask.result.toString()
            }
        }

    }
    return launcher

}

@Composable
fun setCategoryImage(
    context: Context,
    title: String,
    imageUrl: MutableState<String?>,

    ): ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?> {
    val storage = Firebase.storage.reference.child("category-images")
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        val task = storage.child(title).putBytes(
            bitmapToByteArray(context, uri)
        )
        task.addOnSuccessListener { uploadTask ->
            uploadTask.metadata?.reference?.downloadUrl?.addOnCompleteListener { uriTask ->
                imageUrl.value = uriTask.result.toString()
            }
        }

    }
    return launcher

}
