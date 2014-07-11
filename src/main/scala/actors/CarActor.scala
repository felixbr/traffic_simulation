package actors

import actors.CarActor.{ChangeAcceleration, ReceiveMovement, RequestMovement}
import actors.TickActor.Tick
import actors.TrafficObserverActor.UpdateCarData
import akka.actor.{Actor, ActorRef}
import data.Car
import data.TypeSynonyms.{Acceleration, CarID, Distance, Speed}

object CarActor {
    sealed trait Message
	//case object RequestData extends Message
    case class ChangeAcceleration(accel: Acceleration) extends Message

	sealed trait CarMessage
	case object RequestMovement extends CarMessage
	case class ReceiveMovement(speed: Speed, accel: Acceleration, distance: Distance) extends CarMessage
}

class CarActor(observer: ActorRef, id: CarID, carData: Car, carInFront: Option[ActorRef]) extends Actor {

    def receive = {
        case Tick => progress()
	    case RequestMovement => sender ! ReceiveMovement(carData.speed, carData.accel, carData.distanceTravelled)
        //case RequestData => sender ! UpdateCarData(id, carData.speed, carData.accel, carData.distanceTravelled)
        case ChangeAcceleration(accel) => carData.accel = accel
    }

    def progress(): Unit = {
        carData.distanceTravelled += carData.speed
        carData.speed += carData.accel

        // dummy
        //carData.accel += (Random.nextDouble() - 0.5) * 0.1

        observer ! UpdateCarData(id, carData)
    }
}
