package dev.brella.poc

import kotlin.math.floor
import kotlin.math.roundToInt

data class EnergyType(
    val name: String,
    val units: String,
    val energyIndex: Int,
    val convertTo: (dst: EnergyType, amount: Int) -> Int,
    val convertFrom: (src: EnergyType, amount: Int) -> Int,
) {
    class Builder {
        var name: String? = null
        var units: String? = null
        var energyIndex: Int? = null

        var convertTo: ((dst: EnergyType, amount: Int) -> Int)? = null
        var convertFrom: ((src: EnergyType, amount: Int) -> Int)? = null

        inline fun name(value: String): Builder =
            apply { this.name = value }

        inline fun units(value: String): Builder =
            apply { this.units = value }

        inline fun energyIndex(value: Int): Builder =
            apply { this.energyIndex = value }

        inline fun convertFrom(noinline func: (dst: EnergyType, amount: Int) -> Int): Builder =
            apply { this.convertFrom = func }

        inline fun convertFrom(ratio: Double): Builder =
            convertFrom { _, amount -> floor(amount * ratio).toInt() }

        inline fun convertTo(noinline func: (src: EnergyType, amount: Int) -> Int): Builder =
            apply { this.convertTo = func }

        inline fun convertTo(ratio: Double): Builder =
            convertTo { _, amount -> floor(amount * ratio).toInt() }

        fun build(): EnergyType =
            EnergyType(
                requireNotNull(name) { "Name required" },
                requireNotNull(units) { "Units required" },
                requireNotNull(energyIndex) { "Energy Index required" },
                requireNotNull(convertFrom),
                requireNotNull(convertTo)
            )
    }

    public infix fun ratioFor(type: EnergyType): Double =
        (this.energyIndex.toDouble() / type.energyIndex.toDouble())
}

public inline fun buildEnergyType(block: EnergyType.Builder.() -> Unit): EnergyType =
    EnergyType.Builder()
        .apply(block)
        .build()