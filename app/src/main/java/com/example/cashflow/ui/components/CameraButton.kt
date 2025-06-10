package com.example.cashflow.ui.components

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun CameraButton(
    onImageCaptured: (Bitmap) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val cameraPermission = android.Manifest.permission.CAMERA

    // Launcher do robienia zdjęcia
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap ->
            if (bitmap != null) {
                onImageCaptured(bitmap)
            } else {
                Toast.makeText(context, "Nie udało się zrobić zdjęcia", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // Launcher do pytania o uprawnienie
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                takePictureLauncher.launch(null)
            } else {
                Toast.makeText(context, "Brak uprawnień do kamery", Toast.LENGTH_SHORT).show()
            }
        }
    )

    Button(
        onClick = {
            when {
                ContextCompat.checkSelfPermission(context, cameraPermission) == PackageManager.PERMISSION_GRANTED -> {
                    // Mamy już uprawnienie
                    takePictureLauncher.launch(null)
                }
                else -> {
                    // Poproś o uprawnienie
                    permissionLauncher.launch(cameraPermission)
                }
            }
        },
        modifier = modifier
    ) {
        Text("Zrób zdjęcie Paragonu")
    }
}