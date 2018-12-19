package fftr4

import chisel3._
import chisel3.core.FixedPoint
import Const._

class FFTComplex extends Bundle {
    val real = FixedPoint(DATAWIDTH.W, RADIX.BP)
    val imag = FixedPoint(DATAWIDTH.W, RADIX.BP)
    def *(that: FFTComplex): FFTComplex ={
        FFTComplexMul(true)(this, that)
    }
    def -(that: FFTComplex): FFTComplex ={
        FFTComplexMinus(this, that)
    }
    def +(that: FFTComplex): FFTComplex ={
        FFTComplexAdd(this, that)
    }
    def xj: FFTComplex = {
        FFTComplexMulj(this)
    }
    def fromDouble(real:Double, imag:Double): FFTComplex ={
        this.real:= FixedPoint.fromDouble(real, DATAWIDTH.W, RADIX.BP)
        this.imag:= FixedPoint.fromDouble(imag, DATAWIDTH.W, RADIX.BP)
        this
    }
}

class FFTComplexMul(useGauss: Boolean) extends Module{
    val io = IO(new Bundle{
        val x1 = Input(new FFTComplex())
        val x2 = Input(new FFTComplex())
        val y = Output(new FFTComplex())
    })
    if(useGauss){
        val t1 = io.x2.real * (io.x1.real + io.x1.imag)
        val t2 = io.x1.real * (io.x2.imag - io.x2.real)
        val t3 = io.x1.imag * (io.x2.real + io.x2.imag)
        io.y.real := t1 - t3
        io.y.imag := t1 + t2
    } else {
        io.y.real := (io.x1.real * io.x2.real) - (io.x1.imag * io.x2.imag)
        io.y.imag := (io.x1.real * io.x2.imag) + (io.x1.imag * io.x2.real)
    }
}

object FFTComplexMul{
    def apply(useGauss: Boolean)(x1: FFTComplex, x2: FFTComplex): FFTComplex = {
        val inst = Module(new FFTComplexMul(useGauss=useGauss))
        inst.io.x1 := x1
        inst.io.x2 := x2
        inst.io.y
    }
}

class FFTComplexAdd extends Module{
    val io = IO(new Bundle{
        val x1 = Input(new FFTComplex())
        val x2 = Input(new FFTComplex())
        val y = Output(new FFTComplex())
    })
    io.y.real := io.x1.real + io.x2.real
    io.y.imag := io.x1.imag + io.x2.imag
}

object FFTComplexAdd{
    def apply(x1: FFTComplex, x2: FFTComplex): FFTComplex = {
        val inst = Module(new FFTComplexAdd)
        inst.io.x1 := x1
        inst.io.x2 := x2
        inst.io.y
    }
}

class FFTComplexMinus extends Module{
    val io = IO(new Bundle{
        val x1 = Input(new FFTComplex())
        val x2 = Input(new FFTComplex())
        val y = Output(new FFTComplex())
    })
    io.y.real := io.x1.real - io.x2.real
    io.y.imag := io.x1.imag - io.x2.imag
}

object FFTComplexMinus{
    def apply(x1: FFTComplex, x2: FFTComplex): FFTComplex = {
        val inst = Module(new FFTComplexMinus)
        inst.io.x1 := x1
        inst.io.x2 := x2
        inst.io.y
    }
}

class FFTComplexMulj extends Module{
    val io = IO(new Bundle{
        val x = Input(new FFTComplex())
        val y = Output(new FFTComplex())
    })
    io.y.real := -io.x.imag
    io.y.imag := io.x.real
}

object FFTComplexMulj{
    def apply(x1: FFTComplex): FFTComplex = {
        val inst = Module(new FFTComplexMulj)
        inst.io.x := x1
        inst.io.y
    }
}
