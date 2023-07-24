package Utils

// HydrationManager.kt
object HydrationManager {
    private var waterCount = 0

    fun addWaterGlass() {
        waterCount++
    }

    fun removeWaterGlass() {
        if (waterCount > 0) {
            waterCount--
        }
    }

    fun getTotalWaterConsumed(): Int {
        return waterCount
    }
}

