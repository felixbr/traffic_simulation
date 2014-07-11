package actors

import actors.TickActor.RegisterActor
import actors.TrafficObserverActor.{UpdateCarData, AddCar}
import akka.actor.{ActorRef, Props, Actor}
import data.TypeSynonyms.{Distance, CarID, Acceleration, Speed}
import ui.CarVisualization
import data.Car

object TrafficObserverActor {
    sealed trait CarResponse
    case class AddCar(speed: Speed = 10.0, accel: Acceleration = 0.0, distance: Distance)
    case class UpdateCarData(id: CarID, carData: Car)
}

class TrafficObserverActor(visualization: CarVisualization, tickActor: ActorRef) extends Actor {
    private var carActors = Vector[ActorRef]()
	private var cars = Vector[Car]()

    def receive = {
        case AddCar(speed, accel, distance) =>
            val newId = cars.size
            val carInFront = carActors.lastOption
            val carData = new Car(speed, accel, distance)

            val carActor = context.actorOf(Props(new CarActor(observer = self, id = newId, carData, carInFront)))

            tickActor ! RegisterActor(carActor)

            carActors = carActors :+ carActor
            cars = cars :+ carData

        case UpdateCarData(id, carData) =>
            cars(id).speed = carData.speed
            cars(id).accel = carData.accel
            cars(id).distanceTravelled = carData.distanceTravelled

            visualization.updateCarsData(cars)
    }
}
