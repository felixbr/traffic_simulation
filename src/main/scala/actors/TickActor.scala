package actors

import actors.TickActor._
import akka.actor.{Cancellable, ActorSystem, Actor, ActorRef}

import scala.collection.mutable
import scala.concurrent.duration._

object TickActor {

	sealed trait TickMessage
    case object Tick
	case object StartTicking extends TickMessage
	case object StopTicking extends TickMessage
    case class RegisterActor(actor: ActorRef) extends TickMessage
	case class AdjustTickInterval(ms: Double) extends TickMessage
}

class TickActor(implicit system: ActorSystem) extends Actor {
    import system.dispatcher

    var tickInterval = 1000.0
    var nextTick: Option[Cancellable] = None

    val actors = mutable.HashSet[ActorRef]()

	def receive = {
        case Tick =>
            actors.foreach { _ ! Tick }
            scheduleNextTick()
        case StartTicking => scheduleNextTick()
        case StopTicking => nextTick.map(_.cancel())
        case RegisterActor(actor) => actors.add(actor)
        case AdjustTickInterval(ms) => tickInterval = ms
	}

    def scheduleNextTick(): Unit = {
        nextTick = Some(system.scheduler.scheduleOnce(tickInterval milliseconds, self, Tick))
    }
}
