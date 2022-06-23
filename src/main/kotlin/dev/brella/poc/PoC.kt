package dev.brella.poc

import kotlin.math.log10
import kotlin.math.log2
import kotlin.math.roundToInt

fun main() {
    val InstaNoodles = buildEnergyType {
        name = "InstaNoodles"
        units = "IN"
        energyIndex = 100

        convertFrom(1.0)
        convertTo { _, amount -> if (amount < 0) 0 else (log2(1 + amount.toDouble() / energyIndex!!) * energyIndex!!).roundToInt() }
    }
    val RF = buildEnergyType {
        name = "Redstone Flux"
        units = "RF"
        energyIndex = 24_000

        convertFrom(0.8)
        convertTo(0.8)
    }

    val IF = buildEnergyType {
        name = "Industrial Foregoing"
        units = "IF"
        energyIndex = 128_000

        convertFrom(0.8)
        convertTo(0.8)
    }

    // Receiving into a Thermal cell
    // Ratio for each is .8

    for (i in 0 until 8) {
        val thermalCell = ForgeEnergyStorage(InstaNoodles, 1_000_000)
        thermalCell.receive(EnergyAmount(RF, RF.energyIndex * (i + 1)))
        println(thermalCell.energy)
    }
}