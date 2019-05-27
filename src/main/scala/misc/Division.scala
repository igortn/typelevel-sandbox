// https://www.youtube.com/watch?v=8fbpF70YCV0

package misc

import cats.{Applicative, ApplicativeError}
import cats.implicits._  // needed

object Division {

  // use applicative error instead of throwing exceptions

  def div[F[_] : ApplicativeError[?[_], Throwable]](
    nom: Int,
    den: Int
  ): F[Double] =
    if (den == 0) {
      ApplicativeError[F, Throwable].raiseError(
        new IllegalArgumentException("cannot divide by zero")
      )
    } else {
      Applicative[F].pure(nom/den)
    }

  def divOpt(nom: Int, den: Int): Option[Double] =
    div[Either[Throwable, ?]](nom, den).toOption


  // Avoid using kind projector by defining type aliases.

  type ResultOr[F[_]] = ApplicativeError[F, Throwable]
  type EitherA[F] = Either[Throwable, F]

  def div1[F[_]: ResultOr](
    nom: Int,
    den: Int
  ): F[Double] =
    if (den == 0) {
      implicitly[ResultOr[F]].raiseError(
        new IllegalArgumentException("cannot divide by zero")
      )
    } else {
      implicitly[ResultOr[F]].pure(nom/den)
    }

  def divOpt1(nom: Int, den: Int): Option[Double] =
    div1[EitherA](nom, den).toOption
}