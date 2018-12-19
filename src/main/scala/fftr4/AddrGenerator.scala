
package fftr4

import chisel3._
import chisel3.util._

trait hasAddrGenerator{
    def addrTable: Vec[UInt] = {
        val list = (0 until 64).map(i => {
            val bit0 = i % 2
            val bit1 = (i % 4) / 2
            val bit2 = (i % 8) / 4
            val bit3 = (i % 16) / 8
            val bit4 = (i % 32) / 16
            val bit5 = i / 32
            (bit0 * 32 + bit1 * 16 + bit2 * 8 + bit3 * 4 + bit4 * 2 + bit5).asUInt(6.W)
        })
        VecInit(list)
    }

    def getAddr(x:UInt):UInt = {
        addrTable(x)
    }
}
