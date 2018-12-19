package fftr4

import chisel3._
import chisel3.core.FixedPoint
//import dsptools.numbers

class Butterfly_R4 extends Module {
    val io = IO(new Bundle {
        val xs = Input(Vec(4, new FFTComplex))
        val ys = Output(Vec(4, new FFTComplex))
//        val xs = VecInit(Seq.fill(4)(Input(new FFTComplex())))
//        val ys = VecInit(Seq.fill(4)(Output(new FFTComplex())))
    })
    val intermediate = Wire(new UnitPort)
    intermediate(0) := io.xs(0) + io.xs(2)
    intermediate(1) := io.xs(1) + io.xs(3)
    intermediate(2) := io.xs(0) - io.xs(2)
    intermediate(3) := io.xs(1) - io.xs(3)
    io.ys(0) := intermediate(0) + intermediate(1)
    io.ys(1) := intermediate(0) - intermediate(1)
    io.ys(2) := intermediate(2) - intermediate(3).xj
    io.ys(3) := intermediate(2) + intermediate(3).xj
//    io.ys(0) := io.xs(0) + io.xs(1) + io.xs(2) + io.xs(3)
//    io.ys(1) := io.xs(0) - io.xs(1) + io.xs(2) - io.xs(3)
//    io.ys(2) := io.xs(0) - io.xs(1).xj - io.xs(2) + io.xs(3).xj
//    io.ys(3) := io.xs(0) + io.xs(1).xj - io.xs(2) - io.xs(3).xj
}