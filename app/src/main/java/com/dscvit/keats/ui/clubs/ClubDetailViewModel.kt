package com.dscvit.keats.ui.clubs

import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.ViewModel
import com.dscvit.keats.model.clubs.KickMemberRequest
import com.dscvit.keats.model.clubs.LeaveClubRequest
import com.dscvit.keats.repository.AppRepository
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClubDetailViewModel @Inject constructor(private val repo: AppRepository) : ViewModel() {

    fun getClubDetails(clubId: String) = repo.getClubDetails(clubId)

    fun kickMember(kickMemberRequest: KickMemberRequest) = repo.kickMember(kickMemberRequest)

    fun leaveClub(leaveClubRequest: LeaveClubRequest) = repo.leaveClub(leaveClubRequest)

    @Throws(WriterException::class)
    fun encodeAsBitmap(str: String): Bitmap? {
        val result: BitMatrix
        val bitmap: Bitmap?
        try {
            result = MultiFormatWriter().encode(
                str,
                BarcodeFormat.QR_CODE,
                250,
                250,
                null
            )
            val w = result.width
            val h = result.height
            val pixels = IntArray(w * h)
            for (y in 0 until h) {
                val offset = y * w
                for (x in 0 until w) {
                    pixels[offset + x] = if (result[x, y]) Color.BLACK else Color.WHITE
                }
            }
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, 250, 0, 0, w, h)
        } catch (iae: Exception) {
            iae.printStackTrace()
            return null
        }
        return bitmap
    }
}
