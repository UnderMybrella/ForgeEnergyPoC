package dev.brella.poc

import kotlin.math.*

open class ForgeEnergyStorage(
    val type: EnergyType,
    val capacity: Int,
    val maxReceive: Int,
    val maxExtract: Int,
    energy: Int = 0,
) {
    public var energy = max(0, min(capacity, energy))
        protected set

    constructor(type: EnergyType, capacity: Int) : this(type, capacity, capacity, capacity)
    constructor(type: EnergyType, capacity: Int, maxTransfer: Int) : this(type, capacity, maxTransfer, maxTransfer)

    public fun getEnergyAmount(): EnergyAmount =
        EnergyAmount(type, energy)

    public fun canExtract(type: EnergyType?): Boolean = maxExtract > 0
    public fun canReceive(type: EnergyType?): Boolean = maxReceive > 0

    public fun receive(amount: EnergyAmount, simulate: Boolean = false): Int {
        if (!canReceive(amount.type)) return 0

        if (amount.type === type) {
            val energyReceived = min(capacity - energy, min(this.maxReceive, amount.value))

            if (!simulate) energy += energyReceived
            return energyReceived
        }

        /**
         *         int localMaxReceive = ForgeEnergy.convertBetweenInverse(type, this.type, maxReceive);
        int maximumEnergyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, localMaxReceive));
        int energyReceived = ForgeEnergy.convertBetween(type, this.type, maximumEnergyReceived);

        if (!simulate)
        energy += energyReceived;

        return ForgeEnergy.convertBetweenInverse(type, this.type, energyReceived);
         */

        val ratio = amount.type ratioFor type
        val energyReceived = min(capacity - energy, min(this.maxReceive, floor(amount.value.toDouble() / ratio).toInt()))

        if (!simulate) energy += type.convertFrom(amount.type, amount.type.convertTo(type, energyReceived))

        return min(amount.value, ceil(energyReceived.toDouble() * ratio).roundToInt())
    }
}