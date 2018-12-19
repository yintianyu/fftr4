
package fftr4

import chisel3._
import Const._
import chisel3.core.FixedPoint
import chisel3.util.log2Ceil

class MDC_IO extends Bundle{
    val din = Input(new FFTComplex())
    val din_valid = Input(Bool())
    val dout = Output(new FFTComplex())
    val dout_valid = Output(Bool())
}


class MDC extends Module with hasTwiddleFactor
                         with hasAddrGenerator {
    val io = IO(new MDC_IO())

    val controller = Module(new Controller)
    controller.io.valid := io.din_valid
    val stage0_valid = controller.io.stage0_valid
    val stage1_valid = controller.io.stage1_valid
    val reorder_valid = controller.io.reorder_valid

    //----------------------------------
    // Stage 0
    //----------------------------------
    val bf1_in_0 = FFTQueue(48, io.din)
    val bf1_in_1 = FFTQueue(32, io.din)
    val bf1_in_2 = FFTQueue(16, io.din)
    val bf1_in_3 = io.din

//    val test = bf1_in_0 * bf1_in_1

    val bf1 = Module(new Butterfly_R4())
    bf1.io.xs(0) := bf1_in_0
    bf1.io.xs(1) := bf1_in_1
    bf1.io.xs(2) := bf1_in_2
    bf1.io.xs(3) := bf1_in_3

    val s0_counter = RegInit(0.U(log2Ceil(POINT / 4).W))
    when(stage0_valid){
        s0_counter := s0_counter + 1.U
    }.otherwise{
        s0_counter := 0.U
    }

    val s0_twiddle_factor_table = wnTable1(POINT)

    val tfs_s0 = Wire(new UnitPort())
    for(i <- 0 until 4){
        tfs_s0(i).real := s0_twiddle_factor_table._1(s0_counter)(i)
        tfs_s0(i).imag := s0_twiddle_factor_table._2(s0_counter)(i)
    }


    val s0_twinddled = Wire(new UnitPort())
    s0_twinddled.x0 := bf1.io.ys(0) * tfs_s0.x0
    s0_twinddled.x1 := bf1.io.ys(1) * tfs_s0.x1
    s0_twinddled.x2 := bf1.io.ys(2) * tfs_s0.x2
    s0_twinddled.x3 := bf1.io.ys(3) * tfs_s0.x3

    val cnt = controller.io.cnt
//    when(cnt === 50.U){
//        printf("cnt = %d, X0_S0 = %d + %dj\n", cnt, s0_twinddled.x0.real.asSInt(), s0_twinddled.x0.imag.asSInt())
//        printf("cnt = %d, X1_S0 = %d + %dj\n", cnt, s0_twinddled.x1.real.asSInt(), s0_twinddled.x1.imag.asSInt())
//        printf("cnt = %d, tfs_s0_1 = %d + %dj\n", cnt, tfs_s0(1).real.asSInt(), tfs_s0(1).imag.asSInt())
//        printf("cnt = %d, bf1.io.ys(1) = %d + %dj\n", cnt, bf1.io.ys(1).real.asSInt(), bf1.io.ys(1).imag.asSInt())
//    }
//    when(cnt === 49.U || cnt === 53.U || cnt === 57.U || cnt === 61.U){
//        printf("cnt = %d, X1_S0 = %d + %dj\n", cnt, s0_twinddled.x1.real.asSInt(), s0_twinddled.x1.imag.asSInt())
//    }


    //----------------------------------
    // Stage 1
    //----------------------------------
    val sw1_ins = Wire(new UnitPort)
    sw1_ins.x0 := s0_twinddled.x0
    sw1_ins.x1 := FFTQueue(4, s0_twinddled.x1, verbose = false)
    sw1_ins.x2 := FFTQueue(8, s0_twinddled.x2)
    sw1_ins.x3 := FFTQueue(12, s0_twinddled.x3)


    val sw1_sel = controller.io.stage1_sw_sel
    val sw1_out = Switch(sw1_ins.toVec, sw1_sel)
//    when(cnt ===61.U || cnt === 65.U){
//        printf("cnt = %d, sw1_ins_x1 = %d + %dj, sw1_out_0 = %d + %dj\n", cnt, sw1_ins(1).real.asSInt(), sw1_ins(1).imag.asSInt(), sw1_out(0).real.asSInt(), sw1_out(0).imag.asSInt())
//    }
//    when(cnt === 49.U || cnt === 53.U || cnt === 57.U || cnt === 61.U){
//        printf("cnt = %d, sw1_sel = %d\n", cnt, sw1_sel)
//    }


//    val bf2_in = VecInit(Seq.fill(4)(Wire(new FFTComplex)))
    val bf2_in = Wire(new UnitPort)
    bf2_in(0) := FFTQueue(12, sw1_out(0))
    bf2_in(1) := FFTQueue(8, sw1_out(1))
    bf2_in(2) := FFTQueue(4, sw1_out(2))
    bf2_in(3) := sw1_out(3)

    val bf2 = Module(new Butterfly_R4)
    bf2.io.xs := bf2_in.toVec

    val s1_counter = RegInit(0.U(log2Ceil(POINT / 16).W))
    when(stage1_valid){
        s1_counter := s0_counter + 1.U
    }.otherwise{
        s1_counter := 0.U
    }

    val s1_twiddle_factor_table = wnTable1(POINT / 4)
//    val tfs_s1 = VecInit(Seq.fill(4)(Wire(new FFTComplex)))
    val tfs_s1 = Wire(new UnitPort)
    for(i <- 0 until 4){
        tfs_s1(i).real := s1_twiddle_factor_table._1(s1_counter)(i)
        tfs_s1(i).imag := s1_twiddle_factor_table._2(s1_counter)(i)
    }



//    val s1_twinddled = VecInit(Seq.fill(4)(Wire(new FFTComplex)))
    val s1_twinddled = Wire(new UnitPort)
    for(i <- 0 until 4){
        s1_twinddled(i) := bf2.io.ys(i) * tfs_s1(i)
    }

//    when(cnt === 65.U){
//        printf("cnt = %d, X0_S1 = %d + %dj\n", cnt, s1_twinddled.x0.real.asSInt(), s1_twinddled.x0.imag.asSInt())
//        printf("cnt = %d, X1_S1 = %d + %dj\n", cnt, s1_twinddled.x1.real.asSInt(), s1_twinddled.x1.imag.asSInt())
//        printf("cnt = %d, tfs_s1_1 = %d + %dj\n", cnt, tfs_s1(1).real.asSInt(), tfs_s1(1).imag.asSInt())
//        printf("cnt = %d, bf2.io.ys(1) = %d + %dj\n", cnt, bf2.io.ys(1).real.asSInt(), bf2.io.ys(1).imag.asSInt())
//        printf("cnt = %d, bf2.io.xs(0) = %d + %dj\n", cnt, bf2.io.xs(0).real.asSInt(), bf2.io.xs(0).imag.asSInt())
//        printf("cnt = %d, bf2.io.xs(1) = %d + %dj\n", cnt, bf2.io.xs(1).real.asSInt(), bf2.io.xs(1).imag.asSInt())
//        printf("cnt = %d, bf2.io.xs(2) = %d + %dj\n", cnt, bf2.io.xs(2).real.asSInt(), bf2.io.xs(2).imag.asSInt())
//        printf("cnt = %d, bf2.io.xs(3) = %d + %dj\n", cnt, bf2.io.xs(3).real.asSInt(), bf2.io.xs(3).imag.asSInt())
//    }

    //----------------------------------
    // Stage 2
    //----------------------------------
//    val sw2_ins = VecInit(Seq.fill(4)(Wire(new FFTComplex)))
    val sw2_ins = Wire(new UnitPort)
    sw2_ins(0) := s1_twinddled(0)
    sw2_ins(1) := FFTQueue(1, s1_twinddled(1))
    sw2_ins(2) := FFTQueue(2, s1_twinddled(2))
    sw2_ins(3) := FFTQueue(3, s1_twinddled(3))

    val sw2_sel = controller.io.stage2_sw_sel
    val sw2_out = Switch(sw2_ins.toVec, sw2_sel)

//    val bf3_in = VecInit(Seq.fill(4)(Wire(new FFTComplex)))
    val bf3_in = Wire(new UnitPort)
    bf3_in(0) := FFTQueue(3, sw2_out(0))
    bf3_in(1) := FFTQueue(2, sw2_out(1))
    bf3_in(2) := FFTQueue(1, sw2_out(2))
    bf3_in(3) := sw2_out(3)

    val bf3 = Module(new Butterfly_R4)
    bf3.io.xs := bf3_in.toVec

//    when(cnt === 63.U){
//        printf("cnt = %d, X0_S2 = %d + %dj\n", cnt, bf3.io.ys(0).real.asUInt(), bf3.io.ys(0).imag.asUInt())
//    }
//    when(cnt >= 60.U && cnt <= 63.U){
//        printf("cnt = %d, bf3_in(0) = %d + %dj\n", cnt, bf3_in(0).real.asSInt(), bf3_in(0).imag.asSInt())
//        printf("cnt = %d, bf3_in(1) = %d + %dj\n", cnt, bf3_in(1).real.asSInt(), bf3_in(1).imag.asSInt())
//        printf("cnt = %d, bf3_in(2) = %d + %dj\n", cnt, bf3_in(2).real.asSInt(), bf3_in(2).imag.asSInt())
//        printf("cnt = %d, bf3_in(3) = %d + %dj\n", cnt, bf3_in(3).real.asSInt(), bf3_in(3).imag.asSInt())
//    }
//    when(cnt === 63.U){
//        printf("cnt = %d, bf3_out(2) = %d + %dj\n", cnt, bf3.io.ys(2).real.asSInt(), bf3.io.ys(2).imag.asSInt())
//    }


    val reorder_mem_real = SyncReadMem(64, FixedPoint(DATAWIDTH.W, RADIX.BP))
    val reorder_mem_imag = SyncReadMem(64, FixedPoint(DATAWIDTH.W, RADIX.BP))
    val reorder_counter = RegInit(0.U(6.W))
    when(reorder_valid){
        reorder_counter := reorder_counter + 4.U
    }
//    val write_addr = VecInit(Seq.fill(4)(Wire(UInt(6.W))))
    val write_addr = Wire(new AddrPort)
    (0 until 4).foreach(i => {
        write_addr(i) := getAddr(reorder_counter + i.U)
    })
    (0 until 4).foreach(i => {
        when(reorder_valid){
            reorder_mem_real.write(write_addr(i), bf3.io.ys(i).real)
            reorder_mem_imag.write(write_addr(i), bf3.io.ys(i).imag)
        }

    })

//    when(cnt === 63.U){
//        printf("cnt = %d, write_addr_0 = 0x%x, reorder_valid = %d, bf3.io.ys(0).real = %d\n", cnt, write_addr(0), reorder_valid, bf3.io.ys(0).real.asUInt())
//    }

    val read_addr = RegInit(0.U(6.W))
    when(controller.io.output_valid){
        read_addr := read_addr + 1.U
    }
    io.dout.real := reorder_mem_real.read(read_addr, controller.io.output_valid)
    io.dout.imag := reorder_mem_imag.read(read_addr, controller.io.output_valid)


    io.dout_valid := RegNext(controller.io.output_valid)

}

object elaborateR4MDC extends App {
    chisel3.Driver.execute(args, () => new MDC())
}

