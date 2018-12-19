
package fft
import chisel3._
import chisel3.util._
import Const._

class MDC_IO extends Bundle{
    val din_re = Input(UInt(DATAWIDTH.W))
    val din_im = Input(UInt(DATAWIDTH.W))
    val din_valid = Input(Bool())
    val dout_re = Output(UInt(DATAWIDTH.W))
    val dout_im = Output(UInt(DATAWIDTH.W))
    val dout_valid = Output(Bool())
}

class MDC extends Module{
    val io = new MDC_IO


}

