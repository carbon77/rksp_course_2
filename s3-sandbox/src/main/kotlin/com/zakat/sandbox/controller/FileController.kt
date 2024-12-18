package com.zakat.sandbox.controller

import com.zakat.sandbox.service.GrayLogService
import com.zakat.sandbox.service.S3Service
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File

@RestController
@RequestMapping("/api/files")
class FileController(
    private val s3Service: S3Service,
    private val grayLogService: GrayLogService,
) {
    val logger = LoggerFactory.getLogger(FileController::class.java)

    @PostMapping("/upload")
    fun fileUpload(
        @RequestParam("filename") filename: String,
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<String> {
        logger.info("File uploading: {}", filename)
        s3Service.fileUpload(file, filename)
        return ResponseEntity.ok().body("File uploaded")
    }

    @GetMapping("/download/{filename}", produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun fileDownload(
        @PathVariable("filename") filename: String,
    ): ResponseEntity<Resource> {
        logger.info("File downloading: {}", filename)
        val resource = s3Service.fileDownload(filename)
        val headers = HttpHeaders()
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$filename")
        return ResponseEntity
            .ok()
            .headers(headers)
            .contentLength(resource.contentLength())
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource)
    }

    @DeleteMapping("/{filename}")
    fun fileDelete(
        @PathVariable filename: String
    ): ResponseEntity<String> {
        logger.info("File deleting: {}", filename)
        s3Service.deleteFile(filename)
        return ResponseEntity.ok().body("File deleted")
    }

    @GetMapping("/logo")
    fun downloadLogo(): ResponseEntity<Resource> {
        val file = File("/MIREA_Gerb_Colour.png")
        val res = UrlResource(file.toURI())
        return ResponseEntity
            .ok()
            .contentType(MediaType.IMAGE_PNG)
            .body(res)
    }

    @GetMapping("/export")
    fun exportLogs(response: HttpServletResponse) {
        val logs = grayLogService.getLogs()

        response.contentType = "text/csv"
        response.setHeader("Content-Disposition", "attachment; filename=\"logs.csv\"")

        val writer = response.writer
        writer.println(logs)

        writer.flush()
        writer.close()
    }
}

