package pl.rafalmag.ev3.irlink

import lejos.hardware.Button
import lejos.hardware.device.{IRLink, PFMotorPort}
import lejos.hardware.motor.RCXMotor
import lejos.hardware.port.{BasicMotorPort, I2CPort, SensorPort}

object Main extends App {
  println("hello")
  Button.setKeyClickVolume(1)

  var power = 0

  // Using the HiTechnic IRLink device, one way to drive PF motors is to use the PFMotorPort class. This provides a BasicMotorPort interface and so can be used as the BasicMotorPort parameter for the RCXMotor constructor. This allows RCXMotor to drive remotely connected PF motors. Another option is to use the methods of the PFLink class directly. This gives greater control but the methods are specific to this device.


  // http://blog.hsh.one.pl/?p=194
  // http://blog.hsh.one.pl/?p=200
  val irLink = new IRLink(SensorPort.S2)
  val channel = 0
  val slotA = 0
  val slotB = 1
  val engineMotor = new RCXMotor(new PFMotorPort(irLink, channel, slotA))
  val steeringMotor = new RCXMotor(new PFMotorPort(irLink, channel, slotB))
  controlLoop


  def controlLoop: Unit = {
    while (true) {
      Button.getButtons match {
        case Button.ID_ENTER => power = (power + 1) % 8; println(s"ENTER, power=$power")
        case Button.ID_UP => engineMotor.forward(); println("UP")
        case Button.ID_DOWN => engineMotor.backward(); println("DOWN")
        case Button.ID_LEFT => steeringMotor.forward(); println("LEFT")
        case Button.ID_RIGHT => steeringMotor.backward(); println("RIGHT")
        case Button.ID_ESCAPE => return
        case 0 =>engineMotor.flt(); steeringMotor.flt();
        case _@x => println(s"default $x")
      }
      Thread.sleep(100)
    }
  }

  println("exit")

}