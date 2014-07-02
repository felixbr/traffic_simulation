package data

import data.TypeSynonyms._

object Car {
    val LENGTH: Double = 5
}

class Car(var speed: Speed = 100.0, var accel: Acceleration = 0.0, var distanceTravelled: Distance = 0.0) {

}
