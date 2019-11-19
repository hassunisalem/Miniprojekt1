package test

import junit.framework.Assert.assertEquals
import org.junit.Test
import server.Method
import server.Request
import server.Response
import java.io.ByteArrayOutputStream

class  ProtocolTest{
    val  textRequest = """
            GET /greeter HTTP/1.1
            """.trimIndent()

    @Test
    fun testRequestResource(){
        val input = textRequest.byteInputStream()
        val request = Request(input)
        assertEquals("/greeter", request.resource)
    }

    @Test
    fun testRequestMethod(){
        val input = textRequest.byteInputStream()
        val request = Request(input)
        assertEquals(Method.GET, request.method)
    }

    val responTextValue = """
        HTTP/1.1 200 OK
        Content-Type: text/html; charset=UTF-8
        Content-Length: 18
        Connection: close
        
        <p>Hello Kurt!</p>        
        """.trimIndent()

    @Test
    fun testSayHelloToKurt()
    {
        val output = ByteArrayOutputStream(1024)
        val response = Response(output)
        response.appende("<p>Hello Kurt!</p>")
        response.send()
        assertEquals(responTextValue, output.toString())
    }
}