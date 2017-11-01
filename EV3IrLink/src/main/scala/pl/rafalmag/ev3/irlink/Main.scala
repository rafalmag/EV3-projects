package pl.rafalmag.ev3.irlink

import lejos.hardware.Button
import lejos.hardware.device.{IRLink, PFMotorPort}
import lejos.hardware.motor.RCXMotor
import lejos.hardware.port.{BasicMotorPort, SensorPort}

object Main extends App {
  println("hello")
  Button.setKeyClickVolume(1)

  var power = 0

  // Using the HiTechnic IRLink device, one way to drive PF motors is to use the PFMotorPort class. This provides a BasicMotorPort interface and so can be used as the BasicMotorPort parameter for the RCXMotor constructor. This allows RCXMotor to drive remotely connected PF motors. Another option is to use the methods of the PFLink class directly. This gives greater control but the methods are specific to this device.

  val port = SensorPort.S4
  val channel = 4
  val slotA = 0
  val slotB = 1
  val engineMotor = new RCXMotor(new PFMotorPort(new IRLink(port), channel, slotA))
  val steeringMotor = new RCXMotor(new PFMotorPort(new IRLink(port), channel, slotB))
  controlLoop


  def controlLoop: Unit = {
    while (true) {
      Button.getButtons match {
        case Button.ID_ENTER => power = (power + 1) % 8
        case Button.ID_UP => engineMotor.forward()
        case Button.ID_DOWN => engineMotor.backward()
        case Button.ID_LEFT => steeringMotor.forward()
        case Button.ID_RIGHT => steeringMotor.backward()
        case Button.ID_ESCAPE => return
        case 0 =>
        case _ =>
      }
      Thread.sleep(100)
    }
  }

  println("exit")

}