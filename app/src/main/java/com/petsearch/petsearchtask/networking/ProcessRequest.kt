import retrofit2.Call
import java.net.SocketTimeoutException
import java.net.UnknownHostException

 fun <T> processRequest(call: Call<T>): Either<Failure, T> {
    return try {
        val response = call.execute()
        when (response.isSuccessful) {
            true -> {
                    when(response.code()){
                        200-> response.body()?.let {
                            Either.Right(response.body()!!)
                        }?:run{
                            Either.Left(Failure.NoResults)
                        }
                        in 201..207->
                            Either.Left(Failure.SuccessFullResponse(response.code()))
                        in 300..308->
                            Either.Left(Failure.RedirectionResponse(response.code()))
                        in 400..451->
                            Either.Left(Failure.ClientErrorResponse(response.code()))
                        in 500..511->
                            Either.Left(Failure.ServerErrorResponse(response.code()))
                        else-> Either.Left(Failure.ServerError)
                    }
            }
            false ->{
                Either.Left(Failure.ServerError)
            }
        }
    } catch (exception: IllegalStateException) {
        Either.Left(Failure.ResponseExpectedChangedError)
    } catch (exception: UnknownHostException){
        Either.Left(Failure.NetworkConnection)
    } catch (exception: SocketTimeoutException){
        Either.Left(Failure.NetworkConnection)
    } catch( e:Exception){
        e.printStackTrace()
        Either.Left(Failure.NetworkConnection)
    }

 }

sealed class Either<out L, out R> {
    /** * Represents the left side of [Either] class which by convention is a "Failure". */
    data class Left<out L>(val a: L) : Either<L, Nothing>()
    /** * Represents the right side of [Either] class which by convention is a "Success". */
    data class Right<out R>(val b: R) : Either<Nothing, R>()

    val isRight get() = this is Right<R>
    val isLeft get() = this is Left<L>

    fun <L> left(a: L) = Either.Left(a)
    fun <R> right(b: R) = Either.Right(b)

    fun either(fnL: (L) -> Any, fnR: (R) -> Any): Any =
            when (this) {
                is Left -> fnL(a)
                is Right -> fnR(b)
            }

}

sealed class Failure {
    object NetworkConnection : Failure()
    object ServerError : Failure()
    object NoResults : Failure()
    object TransformError : Failure()
    object ResponseExpectedChangedError : Failure()
    data class SuccessFullResponse(val errorCode: Int) : Failure()
    data class RedirectionResponse(val errorCode: Int) : Failure()
    data class ClientErrorResponse(val errorCode: Int) : Failure()
    data class ServerErrorResponse(val errorCode: Int) : Failure()
}