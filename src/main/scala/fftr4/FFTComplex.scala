package fft

import chisel3.core._

class Complex(val real:FixedPoint, val imag:FixedPoint) extends Bundle {
    override def cloneType: this.type =
        new Complex(real.cloneType, imag.cloneType).asInstanceOf[this.type]
    def +(b: Complex): Complex =
        new Complex(real + b.real, imag + b.imag)
    def -(b: Complex): Complex =
        new Complex(real - b.real, imag - b.imag)
    def *(b: Complex): Complex =
        new Complex(real * b.real - imag * b.imag, real * b.imag + imag * b.real)
}

//class Complex[T <: Data](val re: T, val im: T) extends Bundle{
//    override def cloneType: Complex.this.type =
//        new Complex(re.cloneType, im.cloneType).asInstanceOf[Complex.this.type]
//    def +(other: T): T =
//        new Complex(re + other.re, im + other.)
//    def -(other: T): T
//    def *(other: T): T
//}
