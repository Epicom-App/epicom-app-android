package org.ebolapp.logging.printer

import org.ebolapp.logging.adapters.LogAdapter


class PrinterConfig private constructor(val adapters: Array<LogAdapter>) {

    companion object {
        fun build(
            adapters: Array<LogAdapter> = emptyArray()
        ): PrinterConfig {
            return PrinterConfig(adapters)
        }
    }

}
