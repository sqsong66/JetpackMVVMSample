package com.example.jetpacksample.http

/**
 * A discriminated union that encapsulates successful outcome with a value of type [T]
 * or a failure with an arbitrary [Throwable] exception.
 */
@Suppress("UNCHECKED_CAST")
class ResponseResult<out T> constructor(
        @PublishedApi
        internal val value: Any?
) {
    // discovery

    /**
     * Returns `true` if this instance represents successful outcome.
     * In this case [isFailure] returns `false`.
     */
    val isSuccess: Boolean get() = value !is Failure

    /**
     * Returns `true` if this instance represents failed outcome.
     * In this case [isSuccess] returns `false`.
     */
    val isFailure: Boolean get() = value is Failure

    // value & exception retrieval

    /**
     * Returns the encapsulated value if this instance represents [success][Result.isSuccess] or `null`
     * if it is [failure][Result.isFailure].
     *
     * This function is shorthand for `getOrElse { null }` (see [getOrElse]) or
     * `fold(onSuccess = { it }, onFailure = { null })` (see [fold]).
     */
    fun getOrNull(): T? =
            when {
                isFailure -> null
                else -> value as T
            }

    /**
     * Returns the encapsulated exception if this instance represents [failure][isFailure] or `null`
     * if it is [success][isSuccess].
     *
     * This function is shorthand for `fold(onSuccess = { null }, onFailure = { it })` (see [fold]).
     */
    fun exceptionOrNull(): ApiException? =
            when (value) {
                is Failure -> value.exception
                else -> null
            }

    /**
     * Returns a string `Success(v)` if this instance represents [success][Result.isSuccess]
     * where `v` is a string representation of the value or a string `Failure(x)` if
     * it is [failure][isFailure] where `x` is a string representation of the exception.
     */
    override fun toString(): String =
            when (value) {
                is Failure -> value.toString() // "Failure($exception)"
                else -> "Success($value)"
            }

    // companion with constructors

    /**
     * Companion object for [Result] class that contains its constructor functions
     * [success] and [failure].
     */
    companion object {
        /**
         * Returns an instance that encapsulates the given [value] as successful value.
         */
        fun <T> success(value: T): ResponseResult<T> =
                ResponseResult(value)

        /**
         * Returns an instance that encapsulates the given [exception] as failure.
         */
        fun <T> failure(exception: Throwable): ResponseResult<T> =
                ResponseResult(ApiException.parseException(exception)?.let { Failure(it) })

        /**
         * Returns an instance that encapsulates the given [bean] as failure.
         */
        fun <T> failure(code: Int, showMsg: String?, message: String?): ResponseResult<T> =
                ResponseResult(Failure(ApiException(code, showMsg, message)))
    }

    class Failure(@JvmField val exception: ApiException) {
        override fun equals(other: Any?): Boolean = other is Failure && exception == other.exception
        override fun hashCode(): Int = exception.hashCode()
        override fun toString(): String = "Failure($exception)"
    }
}
