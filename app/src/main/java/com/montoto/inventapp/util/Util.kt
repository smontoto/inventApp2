package com.montoto.inventapp.util

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class Util (var context: Context? = null) {

    companion object {
        private const val TAG = "Util"

        fun saveImageInternalStorageAndGetPath(bitmap: Bitmap, context: Context): String {
            val wrapper = ContextWrapper(context)

            var file = wrapper.getDir(Environment.DIRECTORY_PICTURES, Context.MODE_PRIVATE)
            file = File(file, "${UUID.randomUUID()}.jpg")

            try {
                val stream: OutputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                stream.flush()
                stream.close()
            } catch (e: IOError) {
                Log.i("Catch", "saveImageInternalStorage: Error")
            }

            return file.absolutePath

        }

        fun createImageFile(context: Context): File? {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val imageFileName = "JPEG_" + timeStamp + "_"
            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            //String mCurrentPhotoPath = image.getAbsolutePath();
            return File.createTempFile(imageFileName, ".jpg", storageDir)
        }
    }
}