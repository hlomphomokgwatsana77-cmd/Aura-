package com.example

import com.example.data.engine.LoadsheddingLookupEngine
import com.example.data.engine.LocalAuraEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun localEngine_handlesSouthAfricanGreetings() {
        val response = LocalAuraEngine.generateLocalResponse("Howzit Aura!", "MZANSI")
        assertNotNull(response)
        assertTrue(response.contains("Mzansi") || response.contains("Aura"))
    }

    @Test
    fun localEngine_providesLoadsheddingAdvice() {
        val response = LocalAuraEngine.generateLocalResponse("What can I do during loadshedding stage 4?", "MZANSI")
        assertNotNull(response)
        assertTrue(response.contains("Loadshedding") || response.contains("loadshedding") || response.contains("Eskom"))
    }

    @Test
    fun localEngine_hasProverbsAndSlang() {
        assertTrue(LocalAuraEngine.dailyProverbs.isNotEmpty())
        assertTrue(LocalAuraEngine.slangDictionary.isNotEmpty())
        assertTrue(LocalAuraEngine.localRecipes.isNotEmpty())
    }

    @Test
    fun loadsheddingEngine_containsMajorMetros() {
        val areas = LoadsheddingLookupEngine.allAreas
        assertTrue(areas.size >= 5)

        val sandton = LoadsheddingLookupEngine.findAreaByQuery("Sandton")
        assertNotNull(sandton)
        assertEquals(3, sandton?.blockNumber)

        val capeTown = LoadsheddingLookupEngine.findAreaByQuery("Cape Town")
        assertNotNull(capeTown)

        val durban = LoadsheddingLookupEngine.findAreaByQuery("Durban")
        assertNotNull(durban)
    }

    @Test
    fun loadsheddingEngine_calculatesStatusForStages() {
        val sandton = LoadsheddingLookupEngine.allAreas.first()

        // Stage 0 -> No loadshedding
        val normalStatus = LoadsheddingLookupEngine.calculateAreaStatus(sandton, 0)
        assertFalse(normalStatus.isPowerOffNow)
        assertTrue(normalStatus.formattedStatus.contains("normal", ignoreCase = true) || normalStatus.formattedStatus.contains("No load shedding", ignoreCase = true))

        // Stage 2 -> Active schedule
        val stage2Status = LoadsheddingLookupEngine.calculateAreaStatus(sandton, 2)
        assertNotNull(stage2Status.formattedStatus)
        assertTrue(stage2Status.formattedStatus.isNotEmpty())
    }

    @Test
    fun chatEngine_queriesLoadsheddingScheduleDirectly() {
        val response = LocalAuraEngine.generateLocalResponse("What is the loadshedding schedule for Sandton?", "MZANSI")
        assertNotNull(response)
        assertTrue(response.contains("Sandton", ignoreCase = true))
        assertTrue(response.contains("Block 3", ignoreCase = true))
    }

    @Test
    fun notificationManager_hasProperChannelConfig() {
        assertEquals("loadshedding_alerts", com.example.data.notification.LoadsheddingNotificationManager.CHANNEL_ID)
        assertTrue(com.example.data.notification.LoadsheddingNotificationManager.CHANNEL_NAME.contains("Alerts"))
    }
}
