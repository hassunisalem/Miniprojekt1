package kotlin.server

import com.google.gson.Gson
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.memberFunctions
import java.io.FileWriter

val gson = Gson()
interface WebContent {
    fun save()
}

class WebServer(val content: WebContent, val port: Int) {
    fun start() { 
        val serverSocket = ServerSocket(port)
        while (true)
        {
            val socket = serverSocket.accept()

            thread {
                handle(Request(socket.getInputStream()), Response(socket.getOutputStream()))
            }
        }}
    fun stop() { TODO("Implement stop") }
}


data class Member(val id: Int, val name: String)

class ChoirContent : WebContent {
    var nextId = 20
    val members = mutableMapOf<Int, Member>(
        7 to Member(7, "Kurt"),
        17 to Member(17, "Sonja")
    )
    fun getMember(): List<Member> = members.values.toList()

    fun getMember(id: Int): Member? = members[id]

    fun putMember(member: Member): Member {
        if (member.id == 0 || member.id > nextId) {
            val m = member.copy(id = nextId++)
            members[m.id] = m
            return m
        }
        members[member.id] = member
        return member
    }

    override fun save(contet: String) {
        var fw = FileWriter("ChoirData")
        fw.write(contet)
        fw.close()

    }
}

fun listFunctions(content: Any) {
    val contentType = content::class
    println(contentType.simpleName)
    contentType.memberFunctions.forEach {
        println(it)
    }
}

fun callFunction(content: Any, method: Method, resource: String, body: String) : Any? {
    val parts = (resource.split("/") + body).filter { !it.isEmpty() }
    println(parts)
    if (parts.size == 0) return null

    val methodName = method.toString().toLowerCase() + (parts[0].capitalize())
    println(methodName)
    val type = content::class
    val function = type.declaredFunctions
        .filter { it.name == methodName }
        .filter { it.parameters.size == parts.size }
        .firstOrNull()
    if (function == null) return null
    if (function.parameters.size > 1) {
        val p = function.parameters[1]
        val klass = p.type.classifier
        println(klass)
        when (klass) {
            Int::class -> {
                val v1 = parts[1].toInt()
                return function.call(content, v1)
            }
            else -> {
                val p1 = gson.fromJson(parts[1], p.type.javaClass)
                return function.call(content, p1)
            }
        }
    }
    else return function.call(content)
}

fun main() {
    val content = ChoirContent()
    println(callFunction(content, Method.GET, "/member/7", "body"))
    println(callFunction(content, Method.GET, "/member/17", ""))
    save(callFunction(content, Method.GET, "/member", ""))

/*
  val server = WebServer(content, 4711)
  server.start()
 */
}