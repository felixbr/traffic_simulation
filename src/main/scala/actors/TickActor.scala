package actors

import actors.TickActor.StartTicking

object TickActor {
	sealed trait TickMessage
	case object StartTicking extends TickMessage
	case object StopTicking extends TickMessage
	case class AdjustTickInterval() extends TickMessage
}

class TickActor {
	def receive = {
		case StartTicking => ???
	}


}
