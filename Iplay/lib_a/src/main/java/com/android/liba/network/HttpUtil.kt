package com.android.liba.network

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

object HttpUtil {

    fun getFileRequestBody(file: File, mediaType: String): RequestBody {
        return file.asRequestBody(mediaType.toMediaTypeOrNull())
    }

    fun getUploadMultipartBodyPart(file: File, mediaType: String): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            "file",
            file.name,
            getFileRequestBody(file, mediaType)
        )
    }
}