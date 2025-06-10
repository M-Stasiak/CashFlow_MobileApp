package com.example.cashflow.ui.core

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

object TextRecognitionManager {
    fun recognizeTextFromBitMap(bitmap: Bitmap, onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                onSuccess(visionText.text)
            }
            .addOnFailureListener { exception ->
                onFailure(exception.localizedMessage ?: "Unknown error")
            }
    }
}