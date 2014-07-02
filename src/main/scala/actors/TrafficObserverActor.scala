package actors

import actors.TrafficObserverActor.{AddCar, CarMovement}
import akka.actor.{Props, Actor}
import data.TypeSynonyms.{Distance, CarID, Acceleration, Speed}
import ui.CarVisualization

object TrafficObserverActor {
    sealed trait CarResponse
    case class AddCar(speed: Speed, accel: Acceleration, distance: Distance)
    case class CarMovement(id: CarID, speed: Speed, accel: Acceleration)
}

class TrafficObserverActor(visualization: CarVisualization) extends Actor {
    private val carActors = Vector[CarActor]()

    def receive = {
        case AddCar(speed, accel, distance) => {
            val carInFront = carActors.lastOption
            val carActor = context.actorOf(Props(new CarActor(id = carActors.size, carInFront, speed, distance)))

            carActors :+ carActor
        }
        case CarMovement(id, speed, accel) => {
            visualization.updateCarsData()
        }
    }
}
