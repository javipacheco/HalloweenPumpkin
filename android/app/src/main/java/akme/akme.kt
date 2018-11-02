package akme

import arrow.effects.*
import kotlinx.coroutines.experimental.async

class Akme {
    companion object
}


sealed class AkmeException() : Throwable() {

    data class MqttException(override val message: String) : AkmeException()

}

fun String.toDeferred(): DeferredK<Unit> = DeferredK {
    this.logD()
    Unit
}

fun <A> DeferredK<A>.unsafeRunAsyncWithLog(message: String? = null): Unit = this.unsafeRunAsync {
    it.mapLeft {
        (it.message ?: "No message error").logE(it)
    }
}

fun <A> DeferredK<A>.unsafeRunAsyncWithException(ex: ((Throwable) -> Unit)): Unit = this.unsafeRunAsync {
    it.mapLeft {
        ex(it)
    }
}

fun <A> AkmeException.orDeferred(default: () -> A): DeferredK<A> = async { default() }.k().handleErrorWith {
    this.initCause(it)
    DeferredK.raiseError(this)
}