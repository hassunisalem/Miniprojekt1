package server

import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.lang.StringBuilder

enum class Method {GET, POST, PUT, DELETE}

class Request (input: InputStream)
{
    val resource: String
    val method: Method
    val body: String
    val headers = mutableMapOf<String, String>()

    init
    {
        val reader = input.bufferedReader()
        val line = reader.readLine()
        val parts = line.split(" ")
        resource = parts[1]
        method = Method.valueOf(parts[0])

        while (line.isNotEmpty()) {
            val headerParts = line.split(":")
            headers[headerParts[0].trim().toLowerCase()] = headerParts[1].trim()
            line = input.readLine()
        }
        

        val contentLenghtText = headers["content-length"]
        if (contentLenghtText == null) body = ""
        else body = input.readString(contentLenghtText.toInt())
    }
}

class Response(val output: OutputStream)
{
    val body = StringBuilder()
    fun append(text: String)
    {
        body.append(text)
    }
    fun send()
    {
        val head = """
            HTTP/1.1 200 OK
            Content-Type: text/html; charset=UTF-8
            Content-Length: ${body.length}
            Connection: close
            
        """.trimIndent()
        val writer = output.bufferedWriter()
        writer.append(head)
        writer.newLine()
        writer.append(body)
        writer.close()
    }
}
/*
fun main() {
    var output = ByteArrayOutputStream(1024)
    val writer = output.bufferedWriter()
}
 */