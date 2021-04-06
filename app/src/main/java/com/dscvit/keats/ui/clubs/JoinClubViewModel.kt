package com.dscvit.keats.ui.clubs

import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import com.dscvit.keats.model.clubs.JoinClubRequest
import com.dscvit.keats.repository.AppRepository
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class JoinClubViewModel @Inject constructor(private val repo: AppRepository) : ViewModel() {
    fun getPublicClubsList() = repo.getPublicClubsList()

    fun joinClub(joinClubRequest: JoinClubRequest) = repo.joinClub(joinClubRequest)

    fun decodeQR(inputStream: InputStream): String {
        try {
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val width = bitmap.width
            val height = bitmap.height
            val pixels = IntArray(width * height)
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
            bitmap.recycle()
            val source = RGBLuminanceSource(width, height, pixels)
            val bBitmap = BinaryBitmap(HybridBinarizer(source))
            val reader = MultiFormatReader()
            return try {
                val decodedClubId = reader.decode(bBitmap)
                decodedClubId.toString()
            } catch (e: NotFoundException) {
                Timber.tag("DECODE QR").w("Image Picked Not a QR code")
                "Not a QR Code"
            }
        } catch (e: Exception) {
            Timber.tag("DECODE QR").w("This is here now")
            return "Not a QR Code"
        }
    }
}
