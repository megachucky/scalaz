package scalaz

trait Pointed[F[_]] {
  def point[A](a: => A): F[A]

  def **[G[_] : Pointed]: Pointed[({type λ[α] = (F[α], G[α])})#λ] =
    new Pointed[({type λ[α] = (F[α], G[α])})#λ] {
      def point[A](a: => A) =
        (Pointed.this.point(a), implicitly[Pointed[G]].point(a))
    }

  def deriving[G[_]](implicit n: ^**^[G, F]): Pointed[G] =
    new Pointed[G] {
      def point[A](a: => A) =
        n.pack(Pointed.this.point(a))
    }

}

object Pointed extends Pointeds

trait Pointeds extends PointedsLow {

  import java.util.concurrent.Callable

  implicit val OptionPointed: Pointed[Option] = new Pointed[Option] {
    def point[A](a: => A) = Some(a)
  }

  implicit val ListPointed: Pointed[List] = new Pointed[List] {
    def point[A](a: => A) = List(a)
  }

  implicit val StreamPointed: Pointed[Stream] = new Pointed[Stream] {
    def point[A](a: => A) = Stream(a)
  }

  implicit def CallablePointed: Pointed[Callable] = new Pointed[Callable] {
    def point[A](a: => A) = new Callable[A] {
      def call = a
    }
  }
  
  implicit def Tuple1Pointed = new Pointed[Tuple1] {
    def point[A](a: => A) = Tuple1(a)
  }

  implicit def Tuple2Pointed[R: Zero]: Pointed[({type λ[α]=(R, α)})#λ] = new Pointed[({type λ[α]=(R, α)})#λ] {
    def point[A](a: => A) = (implicitly[Zero[R]].zero, a)
  }

  implicit def Tuple3Pointed[R: Zero, S: Zero]: Pointed[({type λ[α]=(R, S, α)})#λ] = new Pointed[({type λ[α]=(R, S, α)})#λ] {
    def point[A](a: => A) = (implicitly[Zero[R]].zero, implicitly[Zero[S]].zero, a)
  }

  implicit def Tuple4Pointed[R: Zero, S: Zero, T: Zero]: Pointed[({type λ[α]=(R, S, T, α)})#λ] = new Pointed[({type λ[α]=(R, S, T, α)})#λ] {
    def point[A](a: => A) = (implicitly[Zero[R]].zero, implicitly[Zero[S]].zero, implicitly[Zero[T]].zero, a)
  }

  implicit def Tuple5Pointed[R: Zero, S: Zero, T: Zero, U: Zero]: Pointed[({type λ[α]=(R, S, T, U, α)})#λ] = new Pointed[({type λ[α]=(R, S, T, U, α)})#λ] {
    def point[A](a: => A) = (implicitly[Zero[R]].zero, implicitly[Zero[S]].zero, implicitly[Zero[T]].zero, implicitly[Zero[U]].zero, a)
  }

  implicit def Tuple6Pointed[R: Zero, S: Zero, T: Zero, U: Zero, V: Zero]: Pointed[({type λ[α]=(R, S, T, U, V, α)})#λ] = new Pointed[({type λ[α]=(R, S, T, U, V, α)})#λ] {
    def point[A](a: => A) = (implicitly[Zero[R]].zero, implicitly[Zero[S]].zero, implicitly[Zero[T]].zero, implicitly[Zero[U]].zero, implicitly[Zero[V]].zero, a)
  }

  implicit def Tuple7Pointed[R: Zero, S: Zero, T: Zero, U: Zero, V: Zero, W: Zero]: Pointed[({type λ[α]=(R, S, T, U, V, W, α)})#λ] = new Pointed[({type λ[α]=(R, S, T, U, V, W, α)})#λ] {
    def point[A](a: => A) = (implicitly[Zero[R]].zero, implicitly[Zero[S]].zero, implicitly[Zero[T]].zero, implicitly[Zero[U]].zero, implicitly[Zero[V]].zero, implicitly[Zero[W]].zero, a)
  }

  implicit def Function0Pure: Pointed[Function0] = new Pointed[Function0] {
    def point[A](a: => A) = new Function0[A] {
      def apply = a
    }
  }

  implicit def Function1Pointed[R]: Pointed[({type λ[α]=(R) => α})#λ] = new Pointed[({type λ[α]=(R) => α})#λ] {
    def point[A](a: => A) = (_: R) => a
  }

  implicit def Function2Pointed[R, S]: Pointed[({type λ[α]=(R, S) => α})#λ] = new Pointed[({type λ[α]=(R, S) => α})#λ] {
    def point[A](a: => A) = (_: R, _: S) => a
  }

  implicit def Function3Pointed[R, S, T]: Pointed[({type λ[α]=(R, S, T) => α})#λ] = new Pointed[({type λ[α]=(R, S, T) => α})#λ] {
    def point[A](a: => A) = (_: R, _: S, _: T) => a
  }

  implicit def Function4Pointed[R, S, T, U]: Pointed[({type λ[α]=(R, S, T, U) => α})#λ] = new Pointed[({type λ[α]=(R, S, T, U) => α})#λ] {
    def point[A](a: => A) = (_: R, _: S, _: T, _: U) => a
  }

  implicit def Function5Pointed[R, S, T, U, V]: Pointed[({type λ[α]=(R, S, T, U, V) => α})#λ] = new Pointed[({type λ[α]=(R, S, T, U, V) => α})#λ] {
    def point[A](a: => A) = (_: R, _: S, _: T, _: U, _: V) => a
  }

  implicit def Function6Pointed[R, S, T, U, V, W]: Pointed[({type λ[α]=(R, S, T, U, V, W) => α})#λ] = new Pointed[({type λ[α]=(R, S, T, U, V, W) => α})#λ] {
    def point[A](a: => A) = (_: R, _: S, _: T, _: U, _: V, _: W) => a
  }
  
  
}

trait PointedsLow {

  import collection.TraversableLike

  implicit def TraversablePointed[CC[X] <: TraversableLike[X, CC[X]] : CanBuildAnySelf]: Pointed[CC] = new Pointed[CC] {
    def point[A](a: => A) = {
      val builder = implicitly[CanBuildAnySelf[CC]].apply[Nothing, A]
      builder += a
      builder.result
    }
  }

}