
package fftr4

import chisel3._
import chisel3.util._

class UnitPort extends Bundle{
    val x0 = new FFTComplex
    val x1 = new FFTComplex
    val x2 = new FFTComplex
    val x3 = new FFTComplex

    def apply (index: Int) ={
        if (index == 0){
            x0
        }
        else if (index == 1){
            x1
        }
        else if (index == 2){
            x2
        }
        else if (index == 3){
            x3
        }
        else{
            throw new ArrayIndexOutOfBoundsException(index.toString() + " out of Bound 4\n")
        }
    }

    def toVec = {
        VecInit(Seq(x0, x1, x2, x3))
    }
}


class AddrPort extends Bundle{
    val x0 = UInt(6.W)
    val x1 = UInt(6.W)
    val x2 = UInt(6.W)
    val x3 = UInt(6.W)

    def apply (index: Int) ={
        if (index == 0){
            x0
        }
        else if (index == 1){
            x1
        }
        else if (index == 2){
            x2
        }
        else if (index == 3){
            x3
        }
        else{
            throw new ArrayIndexOutOfBoundsException(index.toString() + " out of Bound 4\n")
        }
    }

    def toVec = {
        VecInit(Seq(x0, x1, x2, x3))
    }
}
