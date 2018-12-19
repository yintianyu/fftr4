// See README.md for license details.

package fftr4

import chisel3.core.FixedPoint
import chisel3.iotesters._
import Const._
import FFT.Complex
//import dsptools._

trait hasFFTTestFunction{
    def fft(x: Array[Complex]): Array[Complex] = {
        require(x.length > 0 && (x.length & (x.length - 1)) == 0, "array size should be power of two")
        fft(x, 0, x.length, 1)
    }

    def fft(x: Array[Double]): Array[Complex] = fft(x.map(re => new Complex(re, 0.0)))
    def rfft(x: Array[Double]): Array[Complex] = fft(x).take(x.length / 2 + 1)

    private def fft(x: Array[Complex], start: Int, n: Int, stride: Int) : Array[Complex] = {
        if (n == 1)
            return Array(x(start))

        val X = fft(x, start, n / 2, 2 * stride) ++ fft(x, start + stride, n / 2, 2 * stride)

        for (k <- 0 until n / 2) {
            val t = X(k)
            val arg = -2 * math.Pi * k / n
            val c = new Complex(math.cos(arg), math.sin(arg)) * X(k + n / 2)
            X(k) = t + c
            X(k + n / 2) = t - c
        }
        X
    }
}

class queue_UnitTest(c: FFTQueue) extends PeekPokeTester(c) {
    /**
      * compute the gcd and the number of steps it should take to do it
      *
      * @param a positive integer
      * @param b positive integer
      * @return the GCD of a and b
      */

//    private val queue = c
//    poke(c.io.in_re, 100)
//    poke(c.io.in_im, 200)
//    step(1)
//    poke(c.io.in_re, 101)
//    poke(c.io.in_im, 201)
//    step(1)
//    poke(c.io.in_re, 102)
//    poke(c.io.in_im, 202)
//    step(1)
//    poke(c.io.in_re, 103)
//    poke(c.io.in_im, 203)
//    for(i <- 0 until 4){
//        step(1)
//        expect(c.io.out_re, 100 + i)
//        expect(c.io.out_im, 200 + i)
//    }

}

class MDC_UnitTest(c: MDC) extends PeekPokeTester(c) with hasFFTTestFunction {
    poke(c.io.din_valid, value=1)
    var cycle = 0
    var xList = new Array[Complex](64)
    for(i <- 0 until 64){
        xList(i) = new Complex(re=i*0.01, im=0)
        poke(c.io.din.real, FixedPoint.toBigInt(i * 0.01, RADIX))
        poke(c.io.din.imag, FixedPoint.toBigInt(0, RADIX))
        step(1)
        cycle += 1
    }
    var outputCount = 0
    val X = fft(xList)
    var rel_err: Double = 0
    while(outputCount < 64){
        val output_valid = peek(c.io.dout_valid)
        if(output_valid == 1){
            val dout = peek(c.io.dout)
            val dout_real = dout("real").toFloat / math.pow(2, RADIX)
            val dout_imag = dout("imag").toFloat / math.pow(2, RADIX)
            val gt_real = X(outputCount).re
            val gt_imag = X(outputCount).im
            printf("Cycle %d: Output %d = %.3f+%.3fj, GroundTruth = %.3f+%.3fj\n", cycle, outputCount, dout_real,
                dout_imag, gt_real, gt_imag)
            rel_err += math.abs((dout_real - gt_real) / (gt_real + 1e-6)) + math.abs((dout_imag - gt_imag) / (gt_imag + 1e-6))
            outputCount += 1
        }
        step(1)
        cycle += 1
    }
    rel_err /= 64
    printf("relative error = %.4f%%\n", rel_err * 100)
}

/**
  * This is a trivial example of how to run this Specification
  * From within sbt use:
  * {{{
  * testOnly gcd.GCDTester
  * }}}
  * From a terminal shell use:
  * {{{
  * sbt 'testOnly gcd.GCDTester'
  * }}}
  */
class FFTTester extends ChiselFlatSpec {
    // Disable this until we fix isCommandAvailable to swallow stderr along with stdout
    private val backendNames = if(false && firrtl.FileUtils.isCommandAvailable(Seq("verilator", "--version"))) {
        Array("firrtl", "verilator")
    }
    else {
        Array("firrtl")
    }
//    for ( backendName <- backendNames ) {
//        "FFT" should s"(with $backendName)" in {
//            Driver(() => new FFT_Queue(depth = 4), backendName) {
//                c => new queue_UnitTest(c)
//            } should be (true)
//        }
//    }
    for ( backendName <- backendNames ) {
        "FFT" should s"(with $backendName)" in {
            Driver(() => new MDC, backendName) {
                c => new MDC_UnitTest(c)
            } should be (true)
        }
    }


}

