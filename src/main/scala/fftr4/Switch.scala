
package fftr4

import chisel3._
import chisel3.util._

class Switch extends Module{
    val io = IO(new Bundle{
        val in = Input(Vec(4, new FFTComplex))
        val sel = Input(UInt(2.W))
        val out = Output(Vec(4, new FFTComplex))
    })
    // Default
    io.out(0) := io.in(0)
    io.out(1) := io.in(3)
    io.out(2) := io.in(2)
    io.out(3) := io.in(1)
    switch(io.sel){
        is(0.U) {
            io.out(0) := io.in(0)
            io.out(1) := io.in(3)
            io.out(2) := io.in(2)
            io.out(3) := io.in(1)
        }
        is(1.U) {
            io.out(0) := io.in(1)
            io.out(1) := io.in(0)
            io.out(2) := io.in(3)
            io.out(3) := io.in(2)
        }
        is(2.U) {
            io.out(0) := io.in(2)
            io.out(1) := io.in(1)
            io.out(2) := io.in(0)
            io.out(3) := io.in(3)
        }
        is(3.U) {
            io.out(0) := io.in(3)
            io.out(1) := io.in(2)
            io.out(2) := io.in(1)
            io.out(3) := io.in(0)
        }

    }
}

object Switch{
    def apply(in: Vec[FFTComplex], sel: UInt): Vec[FFTComplex] = {
        val inst = Module(new Switch)
        inst.io.in := in
        inst.io.sel := sel
        inst.io.out
    }
}
