package com.proxy.location

@SuppressWarnings("MagicNumber", "VarCouldBeVal")
interface LocationRequest {

    var priority: Int
    var interval: Long
    var maxWaitTime: Long
    var fastestInterval: Long
    fun isFastestIntervalExplicitlySet(): Boolean
    fun setExpirationDuration(duration: Long): LocationRequest
    var expirationTime: Long
    var setNumUpdates: Int
    var smallestDisplacement: Float

    companion object {

        const val PRIORITY_HIGH_ACCURACY = 100
        const val PRIORITY_BALANCED_POWER_ACCURACY = 102
        const val PRIORITY_LOW_POWER = 104
        const val PRIORITY_NO_POWER = 105

        operator fun invoke() = object : LocationRequest {

            private var priorityInternal: Int = PRIORITY_BALANCED_POWER_ACCURACY
            private var intervalInternal: Long = 3_600_000L
            private var maxWaitTimeInternal: Long = 0L
            private var fastestIntervalInternal: Long = 600_000L
            private var expirationDurationInternal: Long = 9_223_372_036_854_775_807L
            private var expirationTimeInternal: Long = expirationDurationInternal
            private var setNumUpdatesInternal: Int = 2_147_483_647
            private var smallestDisplacementInternal: Float = .0f

            override var priority: Int
                get() = priorityInternal
                set(value) {
                    priorityInternal = value
                }

            override var interval: Long
                get() = intervalInternal
                set(value) {
                    intervalInternal = value
                }

            override var maxWaitTime: Long
                get() = maxWaitTimeInternal
                set(value) {
                    maxWaitTimeInternal = value
                }

            override var fastestInterval: Long
                get() = fastestIntervalInternal
                set(value) {
                    fastestIntervalInternal = value
                }

            override fun isFastestIntervalExplicitlySet() =
                fastestInterval != fastestIntervalInternal

            override fun setExpirationDuration(duration: Long): LocationRequest {
                expirationDurationInternal = duration
                return this
            }

            override var expirationTime: Long
                get() = expirationTimeInternal
                set(value) {
                    expirationTimeInternal = value
                }

            override var setNumUpdates: Int
                get() = setNumUpdatesInternal
                set(value) {
                    setNumUpdatesInternal = value
                }

            override var smallestDisplacement: Float
                get() = smallestDisplacementInternal
                set(value) {
                    smallestDisplacementInternal = value
                }
        }
    }
}
