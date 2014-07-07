package ui

import actors.TrafficObserverActor
import actors.TrafficObserverActor.AddCar
import akka.actor.{ActorRef, Props, ActorSystem}
import data.Car
import processing.core._
import java.awt.Dimension

trait ActorStartup {

	def initActorSystem(viz: Visualization) = {
	    println("init")

        val system = ActorSystem("traffic-simulation")

        val trafficObserver = system.actorOf(Props(new TrafficObserverActor(viz)))

        fillTraffic(trafficObserver)
    }

    def fillTraffic(trafficObserver: ActorRef) = {
        println("bla")

        trafficObserver ! AddCar(speed = 100.0, accel = 0.0, distance = 300)
        trafficObserver ! AddCar(speed = 100.0, accel = 0.0, distance = 200)
        trafficObserver ! AddCar(speed = 100.0, accel = 0.0, distance = 100)
        trafficObserver ! AddCar(speed = 100.0, accel = 0.0, distance = 0)
    }
}

trait CarVisualization {
    def updateCarsData(cars: Vector[Car])
}

class Visualization extends PApplet with CarVisualization with ActorStartup {
	private var cars = Vector[Car]()

    override def setup() = {
        size(640, 240)
        background(102)
        smooth()
        noStroke()
        fill(0, 102)

	    fill(255, 255, 255)
	    rect(20, 20, 20, 20)

	    initActorSystem(this)
    }

    override def draw() = {
	    cars.foreach(drawCar)
    }

    override def updateCarsData(cars: Vector[Car]): Unit = this.cars = cars

	def drawCar(car: Car): Unit = {
		println(s"drawing car at $car.distanceTravelled")

		fill(255, 255, 255)
		rect(car.distanceTravelled.toFloat, 110, Car.LENGTH, 30)
		fill(0, 0, 0)
		text("-0.2m/s", car.distanceTravelled.toFloat, 130)
	}
}
