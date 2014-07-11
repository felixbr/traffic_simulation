package ui

import actors.TickActor.{AdjustTickInterval, StartTicking}
import actors.TrafficObserverActor.AddCar
import actors.{TickActor, TrafficObserverActor}
import akka.actor.{ActorRef, ActorSystem, Props}
import data.Car
import processing.core._

trait ActorStartup {

	def initActorSystem(viz: Visualization) = {
	    println("init")

        implicit val system = ActorSystem("traffic-simulation")

        val tickActor = system.actorOf(Props(new TickActor()))
        val trafficObserver = system.actorOf(Props(new TrafficObserverActor(viz, tickActor)))

        fillTraffic(trafficObserver)

        tickActor ! AdjustTickInterval(50)
        tickActor ! StartTicking
    }

    def fillTraffic(trafficObserver: ActorRef) = {
        println("bla")

        trafficObserver ! AddCar(distance = 300)
        trafficObserver ! AddCar(distance = 200)
        trafficObserver ! AddCar(distance = 100)
        trafficObserver ! AddCar(distance = 0)
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

	    initActorSystem(this)
    }

    override def draw() = {
        background(102)

        val farthestDistance =
            if (cars.nonEmpty) cars.maxBy(_.distanceTravelled).distanceTravelled.toInt
            else 0

        cars.foreach(drawCar(_, farthestDistance))

        drawStreetMarkers(farthestDistance)
    }

    override def updateCarsData(cars: Vector[Car]): Unit = this.cars = cars

	def drawCar(car: Car, offset: Int): Unit = {
        val xPos: Float = (640 - Car.LENGTH + (car.distanceTravelled - offset)).toFloat

		fill(255, 255, 255)
		rect(xPos, 110, Car.LENGTH, 30)
		fill(0, 0, 0)
		text(f"${car.accel}%2.2f m/s", xPos + 5, 130)
	}

    def drawStreetMarkers(offset: Int): Unit = {
        val markers = Seq(0, 240, 480, 720).map { _ - offset % 240 }

        fill(255, 255, 255)
        markers.foreach { rect(_, 5, 40, 5) }
    }
}
