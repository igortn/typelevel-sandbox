// https://www.youtube.com/watch?v=8fbpF70YCV0

package misc

import cats.effect.{Concurrent, Resource}
import cats.implicits._

trait Socket[F[_]] {
  def read: F[Vector[Byte]]

  def write(bytes: Vector[Byte]): F[Unit]
}

case class Request(
  method: String,
  headers: List[(String, String)],
  query: Option[String],
  body: Option[Vector[Byte]]
)

case class Response(
  status: Int,
  headers: List[(String, String)],
  body: Option[Vector[Byte]]
)

object HttpServer {

  import fs2.{INothing, Stream}

  def serve[F[_] : Concurrent](
    sockets: Stream[F, Resource[F, Socket[F]]],
    parser: Socket[F] => F[Request],
    httpApp: Request => F[Response],
    renderer: Socket[F] => Response => F[Unit],
    maxParallelism: Int
  ): Stream[F, INothing] =
    sockets.map { resource =>
      Stream.resource(resource)
        .flatMap { socket =>
          Stream.eval_ {
            for {
              req <- parser(socket)
              resp <- httpApp(req)
              _ <- renderer(socket)(resp)
            } yield ()
          }
        }
    }.parJoin(maxParallelism)
}
