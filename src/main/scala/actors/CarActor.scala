package actors

import actors.CarActor.ChangeAcceleration
import data.TypeSynonyms.{CarID, Acceleration, Distance, Speed}
import akka.actor.Actor

object CarActor {
    sealed trait Message
    case object RequestMovement extends Message
    case class ChangeAcceleration(accel: Acceleration) extends Message
}

class CarActor(id: CarID, carInFront: Option[CarActor], var speed: Speed = 100.0, var distanceTravelled: Distance = 0.0) extends Actor {
    private var acceleration: Acceleration = 0.0

    def receive = {
        case ChangeAcceleration(accel) => acceleration = accel
    }
}
