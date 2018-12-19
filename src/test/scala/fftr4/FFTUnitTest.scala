// See README.md for license details.

package fft

import java.io.File

import chisel3.iotesters._
//import dsptools._

class queue_UnitTest(c: FFT_Queue) extends PeekPokeTester(c) {
    /**
      * compute the gcd and the number of steps it should take to do it
      *
      * @param a positive integer
      * @param b positive integer
      * @return the GCD of a and b
      */

    private val queue = c
    poke(c.io.in_re, 100)
    poke(c.io.in_im, 200)
    step(1)
    poke(c.io.in_re, 101)
    poke(c.io.in_im, 201)
    step(1)
    poke(c.io.in_re, 102)
    poke(c.io.in_im, 202)
    step(1)
    poke(c.io.in_re, 103)
    poke(c.io.in_im, 203)
    for(i <- 0 until 4){
        step(1)
        expect(c.io.out_re, 100 + i)
        expect(c.io.out_im, 200 + i)
    }

}

class butterfly_UnitTest(c: Butterfly_R4) extends PeekPokeTester(c){
//    poke(c.io.x1.real, value=3)
//    poke(c.io.x1.imag, value=4)
//    poke(c.io.x2.real, value=5)
//    poke(c.io.x2.imag, value=6)
//    poke(c.io.x3.real, value=7)
//    poke(c.io.x3.imag, value=8)
//    poke(c.io.x4.real, value=9)
//    poke(c.io.x4.imag, value=10)
    poke(c.io.x1, 35)

//    val y1_real = peek(c.io.y1.real)
//    val y1_imag = peek(c.io.y1.imag)
//    val y2_real = peek(c.io.y2.real)
//    val y2_imag = peek(c.io.y2.imag)
//    val y3_real = peek(c.io.y3.real)
//    val y3_imag = peek(c.io.y3.imag)
//    val y4_real = peek(c.io.y4.real)
//    val y4_imag = peek(c.io.y4.imag)
//    printf("%d\n", y1_real)
//    printf("%d\n", y1_imag)
//    printf("%d\n", y2_real)
//    printf("%d\n", y2_imag)
//    printf("%d\n", y3_real)
//    printf("%d\n", y3_imag)
//    printf("%d\n", y4_real)
//    printf("%d\n", y4_imag)
    val y1 = peek(c.io.y1)
    val y2 = peek(c.io.y2)
    val y3 = peek(c.io.y3)
    val y4 = peek(c.io.y4)
    printf("%d\n", y1)
    printf("%d\n", y2)
    printf("%d\n", y3)
    printf("%d\n", y4)

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
            Driver(() => new Butterfly_R4, backendName) {
                c => new butterfly_UnitTest(c)
            } should be (true)
        }
    }


}

