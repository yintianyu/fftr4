
package fftr4

import chisel3._
import chisel3.core.VecInit
import Const._

class FFT_Queue(depth: Int) extends Module{
    val io = IO(new Bundle {
        val in_re = Input(UInt(DATAWIDTH.W))
        val in_im = Input(UInt(DATAWIDTH.W))
//        val in_valid = Input(Bool())
        val out_re = Output(UInt(DATAWIDTH.W))
        val out_im = Output(UInt(DATAWIDTH.W))
    })
    val list_re_temp = VecInit(Seq.fill(depth)(RegInit(0.U(DATAWIDTH.W))))
    val list_im_temp = VecInit(Seq.fill(depth)(RegInit(0.U(DATAWIDTH.W))))
    list_re_temp(0) := RegNext(io.in_re)
    list_im_temp(0) := RegNext(io.in_im)
    for (i <- 1 until depth){
        list_re_temp(i) := RegNext(list_re_temp(i-1))
        list_im_temp(i) := RegNext(list_im_temp(i-1))
    }
    io.out_re := list_re_temp(depth - 1)
    io.out_im := list_im_temp(depth - 1)
}
