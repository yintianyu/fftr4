package fft
import chisel3._
import chisel3.core.FixedPoint
import chisel3.internal.firrtl._
import Const._
import chisel3.util._
//import dsptools.numbers

class Butterfly_R4 extends Module {
    val io = IO(new Bundle {
//        val x1 = Input(new Complex(FixedPoint(DATAWIDTH.W, 5.BP), FixedPoint(DATAWIDTH.W, 5.BP)))
//        val x2 = Input(new Complex(FixedPoint(DATAWIDTH.W, 5.BP), FixedPoint(DATAWIDTH.W, 5.BP)))
//        val x3 = Input(new Complex(FixedPoint(DATAWIDTH.W, 5.BP), FixedPoint(DATAWIDTH.W, 5.BP)))
//        val x4 = Input(new Complex(FixedPoint(DATAWIDTH.W, 5.BP), FixedPoint(DATAWIDTH.W, 5.BP)))
//        val y1 = Output(new Complex(FixedPoint(DATAWIDTH.W, 5.BP), FixedPoint(DATAWIDTH.W, 5.BP)))
//        val y2 = Output(new Complex(FixedPoint(DATAWIDTH.W, 5.BP), FixedPoint(DATAWIDTH.W, 5.BP)))
//        val y3 = Output(new Complex(FixedPoint(DATAWIDTH.W, 5.BP), FixedPoint(DATAWIDTH.W, 5.BP)))
//        val y4 = Output(new Complex(FixedPoint(DATAWIDTH.W, 5.BP), FixedPoint(DATAWIDTH.W, 5.BP)))
        val x1 = Input(FixedPoint(10.W, 2.BP))
        val x2 = Input(FixedPoint(10.W, 2.BP))
        val x3 = Input(FixedPoint(10.W, 2.BP))
        val x4 = Input(FixedPoint(10.W, 2.BP))
        val y1 = Output(FixedPoint(10.W, 2.BP))
        val y2 = Output(FixedPoint(10.W, 2.BP))
        val y3 = Output(FixedPoint(10.W, 2.BP))
        val y4 = Output(FixedPoint(10.W, 2.BP))
    })
//    val minus_j = new Complex(FixedPoint(0, DATAWIDTH.W, 5.BP), FixedPoint(-1, DATAWIDTH.W, 5.BP))
//    val plus_j = new Complex(FixedPoint(0, DATAWIDTH.W, 5.BP), FixedPoint(1, DATAWIDTH.W, 5.BP))
    val y1 = io.x1 + io.x2 + io.x3 + io.x4
//    val y2 = io.x1 + io.x2 * minus_j - io.x3 + io.x4 * plus_j
//    val y3 = io.x1 - io.x2 + io.x3 - io.x4
//    val y4 = io.x1 + io.x2 * plus_j - io.x3 + io.x4 * minus_j
    io.y1 := y1
    io.y2 := y1
    io.y3 := y1
    io.y4 := y1
//    io.y1.imag := y1.imag
//    io.y1.real := y1.real
//    io.y2.imag := y2.imag
//    io.y2.real := y2.real
//    io.y3.imag := y3.imag
//    io.y3.real := y3.real
//    io.y4.imag := y4.imag
//    io.y4.real := y4.real
}