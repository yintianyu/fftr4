package fftr4

trait constant{
    val DATAWIDTH = 10
    val RADIX = 5
    val POINT = 64
}

trait r4MDC_architectureParameters{
    val QUEUE_LENGTH_S0_R0 = 48
    val QUEUE_LENGTH_S0_R1 = 32
    val QUEUE_LENGTH_S0_R2 = 16
    val QUEUE_LENGTH_S1_Pre_R1 = 4
    val QUEUE_LENGTH_S1_Pre_R2 = 8
    val QUEUE_LENGTH_S1_Pre_R3 = 12
    val QUEUE_LENGTH_S1_Post_R0 = 12
    val QUEUE_LENGTH_S1_Post_R1 = 8
    val QUEUE_LENGTH_S1_Post_R2 = 4
    val QUEUE_LENGTH_S2_Pre_R1 = 1
    val QUEUE_LENGTH_S2_Pre_R2 = 2
    val QUEUE_LENGTH_S2_Pre_R3 = 3
    val QUEUE_LENGTH_S2_Post_R0 = 3
    val QUEUE_LENGTH_S2_Post_R1 = 2
    val QUEUE_LENGTH_S2_Post_R2 = 1
}

object Const extends constant with r4MDC_architectureParameters {

}
