package ui

import actors.TrafficObserverActor
import actors.TrafficObserverActor.AddCar
import akka.actor.{ActorRef, Props, ActorSystem}
import data.Car
import processing.core._

object Visualization extends PApplet {
    def main(args: Array[String]) = {
        val visualization = new Visualization()
        val frame = new javax.swing.JFrame("CarDisplay")
        visualization.init()
        frame.pack()
        frame.setVisible(true)


    }

    def initActorSystem(viz: Visualization) = {
        val system = ActorSystem("traffic-simulation")

        val trafficObserver = system.actorOf(Props(new TrafficObserverActor(viz)))

        fillTraffic(trafficObserver)
    }

    def fillTraffic(trafficObserver: ActorRef) = {
        trafficObserver ! AddCar(speed = 100.0, accel = 0.0, distance = 300)
        trafficObserver ! AddCar(speed = 100.0, accel = 0.0, distance = 200)
        trafficObserver ! AddCar(speed = 100.0, accel = 0.0, distance = 100)
        trafficObserver ! AddCar(speed = 100.0, accel = 0.0, distance = 0)
    }
}

trait CarVisualization {
    def updateCarsData(cars: Vector[Car])
}

class Visualization extends PApplet with CarVisualization {
    override def setup() = {
        size(640, 240)
        background(102)
        smooth()
        noStroke()
        fill(0, 102)
    }

    override def draw() = {
        fill(255, 255, 255)
        rect(width-60, 110, 60, 30)
        fill(0, 0, 0)
        text("-0.2m/s", width-45, 130)
    }

    override def updateCarsData(cars: Vector[Car]): Unit = ()
}
