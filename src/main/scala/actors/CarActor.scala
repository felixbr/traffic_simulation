package actors

import actors.CarActor.{ReceiveMovement, RequestData, RequestMovement, ChangeAcceleration}
import data.TypeSynonyms.{CarID, Acceleration, Distance, Speed}
import akka.actor.Actor
import data.Car
import actors.TrafficObserverActor.UpdateCarData

object CarActor {
    sealed trait Message
	case object RequestData extends Message
    case class ChangeAcceleration(accel: Acceleration) extends Message

	sealed trait CarMessage
	case object RequestMovement extends CarMessage
	case class ReceiveMovement(speed: Speed, accel: Acceleration, distance: Distance) extends CarMessage
}

class CarActor(id: CarID, carData: Car, carInFront: Option[CarActor]) extends Actor {
    def receive = {
	    case RequestMovement => sender ! ReceiveMovement(carData.speed, carData.accel, carData.distanceTravelled)
        case RequestData => sender ! UpdateCarData(id, carData.speed, carData.accel, carData.distanceTravelled)
        case ChangeAcceleration(accel) => carData.accel = accel
    }
}
