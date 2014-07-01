package ui

import processing.core._

object CarDisplay extends PApplet {
    def main(args: Array[String]) = {
        val display = new CarDisplay()
        val frame = new javax.swing.JFrame("CarDisplay")
        display.init()
        frame.pack()
        frame.setVisible(true)
    }
}

class CarDisplay extends PApplet {
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

}
