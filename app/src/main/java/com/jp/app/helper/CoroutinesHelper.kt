import kotlinx.coroutines.*
import retrofit2.Response
import kotlin.coroutines.CoroutineContext


private val doOnStart: () -> Unit = {}
private val doOnSuccess: (Any) -> Unit = {}
private val doOnError: (error: String?) -> Unit = { _ -> }

suspend fun <T : Any> retrofitCallLaunch(
        dispatcher: CoroutineContext = Dispatchers.Main,
        asyncCall: suspend () -> Response<T>,
        onStart: () -> Unit = doOnStart,
        onError: (error: String?) -> Unit = doOnError,
        onSuccess: (T) -> Unit = doOnSuccess
) {
    coroutineScope {
        onStart()
        val result = async(dispatcher) {
            asyncCall
        }
        if ((result.await().invoke()).isSuccessful) {
            onSuccess((result.await().invoke()).body() as T)
        } else {
            onError((result.await().invoke()).message())
        }
    }
}