
package fftr4

import chisel3._
import chisel3.core.{FixedPoint, VecInit}
import Const._

class FFTQueue(depth: Int, verbose: Boolean = false) extends Module{
    val io = IO(new Bundle {
        val in = Input(new FFTComplex())
        val out = Output(new FFTComplex())
    })
    assert(depth > 0)
    val list_temp = VecInit(Seq.fill(depth)(Reg(new FFTComplex())))
    list_temp(0) := RegNext(io.in)
    for (i <- 1 until depth){
        list_temp(i) := RegNext(list_temp(i-1))
    }
    io.out := list_temp(depth - 1)
    if(verbose){
        printf("in = %d + %dj, tp0 = %d + %dj, tp1 = %d + %dj, tp2 = %d + %dj, tp3 = %d + %dj, out = %d + %dj\n",
            io.in.real.asSInt(),
            io.in.imag.asSInt(),
            list_temp(0).real.asSInt(),
            list_temp(0).imag.asSInt(),
            list_temp(1).real.asSInt(),
            list_temp(1).imag.asSInt(),
            list_temp(2).real.asSInt(),
            list_temp(2).imag.asSInt(),
            list_temp(3).real.asSInt(),
            list_temp(3).imag.asSInt(),
            io.out.real.asSInt(),
            io.out.imag.asSInt())
    }

}

object FFTQueue{
    def apply(depth: Int, x: FFTComplex, verbose: Boolean = false): FFTComplex ={
        val inst = Module(new FFTQueue(depth, verbose))
        inst.io.in := x
        inst.io.out
    }
}
