package actors

import actors.TrafficObserverActor.{UpdateCarData, AddCar}
import akka.actor.{Props, Actor}
import data.TypeSynonyms.{Distance, CarID, Acceleration, Speed}
import ui.CarVisualization
import data.Car

object TrafficObserverActor {
    sealed trait CarResponse
    case class AddCar(speed: Speed, accel: Acceleration, distance: Distance)
    case class UpdateCarData(id: CarID, speed: Speed, accel: Acceleration, distanceTravelled: Distance)
}

class TrafficObserverActor(visualization: CarVisualization) extends Actor {
    private val carActors = Vector[CarActor]()
	private val cars = Vector[Car]()

    def receive = {
        case AddCar(speed, accel, distance) => {
	        println(s"adding car to traffic at $distance")

            val carInFront = carActors.lastOption
            val carActor = context.actorOf(Props(new CarActor(id = carActors.size, new Car(speed, accel, distance), carInFront)))

            carActors :+ carActor
	        cars :+ new Car()
        }
        case UpdateCarData(id, speed, accel, distance) => {
	        cars(id).speed = speed
	        cars(id).accel = accel
	        cars(id).distanceTravelled = distance

            visualization.updateCarsData(cars)
        }
    }
}
