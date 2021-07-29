import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import retrofit2.Response


private val onStartStub: () -> Unit = {}
private val onNextStub: (Any) -> Unit = {}
private val onErrorStub: (error: String?) -> Unit = { _ -> }

suspend fun <T : Any> retrofitCallLaunch(
        asyncCall: suspend () -> Response<T>,
        onStart: () -> Unit = onStartStub,
        onError: (error: String?) -> Unit = onErrorStub,
        onSuccess: (T) -> Unit = onNextStub
) {
    coroutineScope {
        onStart()
        val result = async(Dispatchers.Main) {
            asyncCall
        }
        if ((result.await().invoke()).isSuccessful) {
            onSuccess((result.await().invoke()).body() as T)
        } else {
            onError((result.await().invoke()).message())
        }
    }
}