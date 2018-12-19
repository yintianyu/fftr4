
package fftr4
import chisel3._
import chisel3.util._

class Controller extends Module{
    val io = IO(new Bundle{
        val valid = Input(Bool())
        val stage0_valid = Output(Bool())
        val stage1_sw_sel = Output(UInt(2.W))
        val stage1_valid = Output(Bool())
        val stage2_sw_sel = Output(UInt(2.W))
        val reorder_valid = Output(Bool())
        val output_valid = Output(Bool())
        val cnt = Output(UInt(32.W))
    })
    val cnt = RegInit(0.U(32.W))    // TODO: Specify the width of cnt
    when(io.valid === true.B){
        cnt := cnt + 1.U
    }otherwise {
        cnt := 0.U
    }
    io.stage0_valid := (cnt >= 48.U)
    io.stage1_sw_sel := cnt(3, 2)
    io.stage1_valid := (cnt >= 60.U)
    io.stage2_sw_sel := cnt(1,0)
    io.reorder_valid := (cnt >= 63.U) && (cnt < (63+16).U)
    io.output_valid := (cnt >= 127.U)
    io.cnt := cnt

//    printf("cnt = %d\n", cnt)
}
