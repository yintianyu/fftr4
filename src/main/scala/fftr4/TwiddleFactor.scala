package fftr4

import chisel3._
import chisel3.util._

import scala.math._
import Const._
import chisel3.core.FixedPoint



trait hasTwiddleFactor{
    def sinTable(k: Int, point: Int): Vec[FixedPoint] = {
        val times = Seq(0, 2, 1, 3).map(i => -(i * k * 2 * Pi) / point.toDouble)
        val inits = times.map(t => FixedPoint.fromDouble(sin(t), DATAWIDTH.W, RADIX.BP))
        VecInit(inits)
    }
    def cosTable(k: Int, point: Int): Vec[FixedPoint] = {
        val times = Seq(0, 2, 1, 3).map(i => -(i * k * 2 * Pi) / point.toDouble)
        val inits = times.map(t => FixedPoint.fromDouble(cos(t), DATAWIDTH.W, RADIX.BP))
        VecInit(inits)
    }


    def wnTable1(point: Int)= {
        val real: Vec[Vec[FixedPoint]] = VecInit((0 until point / 4).map(i => cosTable(i, point)))
        val imag: Vec[Vec[FixedPoint]] = VecInit((0 until point / 4).map(i => sinTable(i, point)))
        (real, imag)
    }
}
