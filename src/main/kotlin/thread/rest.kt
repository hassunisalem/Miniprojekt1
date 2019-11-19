package thread
class Path<T>(
    val first: T,
    val rest: Path<T>?
    ) : Iterable<T> {


    /*
    override fun iterator(): Iterator<T> = iterator(){
        yield(first)
        if(rest != null) yieldAll(rest)
    }
    */
    /**/
    override fun iterator(): Iterator<T> = Steps(this)

    class Steps<T>(var rest: Path<T>?) : Iterator<T> {
        /*override fun hasNext(): Boolean {
            TODO("Implement")
        }*/
        override fun hasNext() = rest != null
        override fun next(): T {
            //TODO("Implement")
            val next = rest!!.first
            rest = rest?.rest
            return next
        }

        //override fun next() = rest!!.first.also { rest = rest?.rest }
    }

    /**/
}

fun main(){
   
}