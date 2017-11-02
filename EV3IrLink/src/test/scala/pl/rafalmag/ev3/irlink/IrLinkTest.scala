package pl.rafalmag.ev3.irlink

import lejos.hardware.device.{IRLink, NewIRLink, PFMotorPort}
import lejos.hardware.motor.RCXMotor
import lejos.hardware.port.I2CPort
import org.scalamock.scalatest.MockFactory
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.{FreeSpec, _}

class IrLinkTest extends FreeSpec with MockFactory with Matchers {

  def toHex(b: Byte): String = String.format("%02X", new Integer(b & 0xff))

  "Lejos IrLink" - {
    val port = mock[I2CPort]

    val irlinkImpls = Table(
      "implementation",
      () => new NewIRLink(port),
      () => new IRLink(port)
    )

    "should ComboDirect A motor forward v1" in {
      forAll(irlinkImpls) { (irlink: () => IRLink) =>
        port.i2cTransaction _ expects where {
          (deviceAddress: Int, writeBuf: Array[Byte], writeOffset: Int, writeLen: Int,
           readBuf: Array[Byte], readOffset: Int, readLen: Int) => {
            deviceAddress should equal(2)
            writeBuf.map(toHex).mkString("[", ",", "]") should equal(
              "[40,80,92,49,24,24,90,84,21,08,00,00,00,00,0D,02,01,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00]")
            writeOffset should equal(0)
            writeLen should equal(17)
            readBuf should equal(null)
            readOffset should equal(0)
            readLen should equal(0)
            true
          }
        } once()

        irlink().sendPFComboDirect(0, IRLink.PF_FORWARD, 0)
      }
    }

    "should ComboDirect A motor forward v2" in {
      forAll(irlinkImpls) { (irlink: () => IRLink) =>
        val pfmotor = new PFMotorPort(irlink(), 0, 0)
        val rcxMotor = new RCXMotor(pfmotor)

        port.i2cTransaction _ expects where {
          (deviceAddress: Int, writeBuf: Array[Byte], writeOffset: Int, writeLen: Int,
           readBuf: Array[Byte], readOffset: Int, readLen: Int) => {
            deviceAddress should equal(2)
            writeBuf.map(toHex).mkString("[", ",", "]") should equal(
              "[40,80,92,49,24,24,90,84,21,08,00,00,00,00,0D,02,01,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00]")
            writeOffset should equal(0)
            writeLen should equal(17)
            readBuf should equal(null)
            readOffset should equal(0)
            readLen should equal(0)
            true
          }
        } once()

        rcxMotor.forward()
      }
    }


    "should ComboDirect A motor backward v1" in {
      forAll(irlinkImpls) { (irlink: () => IRLink) =>
        port.i2cTransaction _ expects where {
          (deviceAddress: Int, writeBuf: Array[Byte], writeOffset: Int, writeLen: Int,
           readBuf: Array[Byte], readOffset: Int, readLen: Int) => {
            deviceAddress should equal(2)
            writeBuf.map(toHex).mkString("[", ",", "]") should equal(
              "[40,80,92,49,24,24,84,84,24,80,00,00,00,00,0D,02,01,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00]")
            writeOffset should equal(0)
            writeLen should equal(17)
            readBuf should equal(null)
            readOffset should equal(0)
            readLen should equal(0)
            true
          }
        } once()

        irlink().sendPFComboDirect(0, IRLink.PF_BACKWARD, 0)
      }
    }

    "should ComboDirect A motor backward v2" in {
      forAll(irlinkImpls) { (irlink: () => IRLink) =>
        val pfmotor = new PFMotorPort(irlink(), 0, 0)
        val rcxMotor = new RCXMotor(pfmotor)

        port.i2cTransaction _ expects where {
          (deviceAddress: Int, writeBuf: Array[Byte], writeOffset: Int, writeLen: Int,
           readBuf: Array[Byte], readOffset: Int, readLen: Int) => {
            deviceAddress should equal(2)
            writeBuf.map(toHex).mkString("[", ",", "]") should equal(
              "[40,80,92,49,24,24,84,84,24,80,00,00,00,00,0D,02,01,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00]")
            writeOffset should equal(0)
            writeLen should equal(17)
            readBuf should equal(null)
            readOffset should equal(0)
            readLen should equal(0)
            true
          }
        } once()

        rcxMotor.backward()
      }
    }
  }
}
