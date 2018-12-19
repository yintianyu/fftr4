
package FFT

import chisel3.iotesters
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class Complex(val re: Double, val im: Double) {
  def +(rhs: Complex) = new Complex(re + rhs.re, im + rhs.im)
  def -(rhs: Complex) = new Complex(re - rhs.re, im - rhs.im)
  def *(rhs: Complex) = new Complex(re * rhs.re - im * rhs.im, rhs.re * im + re * rhs.im)

  def magnitude = Math.hypot(re, im)
  def phase = Math.atan2(im, re)

  override def toString = s"Complex($re, $im)"
}
//
//class FFTtest(c:R2MDC) extends PeekPokeTester(c) {
//  def fft(x: Array[Complex]): Array[Complex] = {
//    require(x.length > 0 && (x.length & (x.length - 1)) == 0, "array size should be power of two")
//    fft(x, 0, x.length, 1)
//  }
//
//  def fft(x: Array[Double]): Array[Complex] = fft(x.map(re => new Complex(re, 0.0)))
//  def rfft(x: Array[Double]): Array[Complex] = fft(x).take(x.length / 2 + 1)
//
//  private def fft(x: Array[Complex], start: Int, n: Int, stride: Int) : Array[Complex] = {
//    if (n == 1)
//      return Array(x(start))
//
//    val X = fft(x, start, n / 2, 2 * stride) ++ fft(x, start + stride, n / 2, 2 * stride)
//
//    for (k <- 0 until n / 2) {
//      val t = X(k)
//      val arg = -2 * math.Pi * k / n
//      val c = new Complex(math.cos(arg), math.sin(arg)) * X(k + n / 2)
//      X(k) = t + c
//      X(k + n / 2) = t - c
//    }
//    X
//  }
//
//  for (i <- 0 until 64){
//    poke(c.io.dIn.re, i << 3)
//    poke(c.io.dIn.im, 0)
//    poke(c.io.din_valid, 1)
//    step(1)
//  }
//  for(i <- 0 until 4) {
//    print(peek(c.io.dOut1) + "\n")
//    print(peek(c.io.dOut2) + "\n")
//    print(peek(c.io.dout_valid) + "\n")
//    step(1)
//  }
//}
//
//object FFTTestMain extends App {
//  iotesters.Driver.execute(args, () => new R2MDC(64)) {
//    c => new FFTtest(c)
//  }
//}
