package io.vpf.sharecode.controller

import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.qrcode.QRCodeWriter
import io.vpf.sharecode.persistence.SharecodeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import java.io.ByteArrayOutputStream
import java.util.*
import javax.servlet.http.HttpServletRequest


@Controller
class SharecodeController @Autowired constructor(val repo: SharecodeRepository) {

    @RequestMapping("code/{key}", produces = [MediaType.IMAGE_PNG_VALUE])
    fun showCode(@PathVariable key: UUID, req: HttpServletRequest): ResponseEntity<ByteArray> {
        val m = QRCodeWriter().encode( "${req.scheme}://${req.serverName}:${req.serverPort}/value/$key", BarcodeFormat.QR_CODE, 200, 200)
        val out = ByteArrayOutputStream()
        MatrixToImageWriter.writeToStream(m, "PNG", out)
        return ResponseEntity.ok(out.toByteArray())
    }


    @RequestMapping("value/{key}", method = [RequestMethod.PUT])
    fun putValue(@PathVariable key: UUID, @RequestBody value: String): ResponseEntity<Unit> {
        repo.putCode(key, value)
        return ResponseEntity.ok(Unit)
    }

    @RequestMapping("value/{key}", produces = [MediaType.TEXT_PLAIN_VALUE])
    fun getValue(@PathVariable key: UUID) : ResponseEntity<String>{
        return ResponseEntity.ok(repo.getCode(key)?:"vpf:empty")
    }


    @RequestMapping("qr/{key}",  produces = [MediaType.IMAGE_PNG_VALUE])
    fun getQR(@PathVariable key: UUID) : ResponseEntity<ByteArray>{
        val m = QRCodeWriter().encode( repo.getCode(key)?:"vpf:empty", BarcodeFormat.QR_CODE, 200, 200)
        val out = ByteArrayOutputStream()
        MatrixToImageWriter.writeToStream(m, "PNG", out)
        return ResponseEntity.ok(out.toByteArray())
    }


    @RequestMapping("qr/direct/{value}",  produces = [MediaType.IMAGE_PNG_VALUE])
    fun getQRDirect(@PathVariable value: String) : ResponseEntity<ByteArray>{
        val v = String(Base64.getDecoder().decode(value))
        val m = QRCodeWriter().encode( v, BarcodeFormat.QR_CODE, 200, 200)
        val out = ByteArrayOutputStream()
        MatrixToImageWriter.writeToStream(m, "PNG", out)
        return ResponseEntity.ok(out.toByteArray())
    }
}