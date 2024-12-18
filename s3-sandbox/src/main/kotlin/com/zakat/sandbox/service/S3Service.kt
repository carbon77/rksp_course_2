package com.zakat.sandbox.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.DeleteObjectsRequest
import com.amazonaws.services.s3.model.GetObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream

@Service
class S3Service(
    private val s3Client: AmazonS3,
) {

    fun fileUpload(file: MultipartFile, filename: String) {
        val request = PutObjectRequest(
            "zakatov-dash-app",
            filename,
            file.inputStream,
            ObjectMetadata()
        )

        s3Client.putObject(request)
    }

    fun fileDownload(filename: String): Resource {
        val request = GetObjectRequest(
            "zakatov-dash-app",
            filename
        )

        val s3Object = s3Client.getObject(request)
        val file = File("./$filename")
        val outputStream = FileOutputStream(file)
        outputStream.write(s3Object.objectContent.readAllBytes())
        return UrlResource(file.toURI())
    }

    fun deleteFile(filename: String) {
        val request = DeleteObjectRequest(
            "zakatov-dash-app",
            filename
        )
        s3Client.deleteObject(request)
    }
}