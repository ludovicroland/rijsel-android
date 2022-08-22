package fr.rolandl.sample.compose.exception

/**
 * The exception that will be thrown if any problem occurs during a web service call.
 *
 * @author Ludovic Roland
 * @since 2022.04.13
 */
class CallException
  : Exception
{

  val code: Int

  constructor() : this(0)

  constructor(code: Int) : this(null, null, code)

  constructor(message: String, cause: Throwable) : this(message, cause, 0)

  constructor(message: String) : this(message, 0)

  constructor(message: String, code: Int) : this(message, null, code)

  constructor(cause: Throwable) : this(cause, 0)

  constructor(cause: Throwable, code: Int) : this(null, cause, code)

  constructor(message: String?, cause: Throwable?, code: Int) : super(message, cause)
  {
    this.code = code
  }

}
