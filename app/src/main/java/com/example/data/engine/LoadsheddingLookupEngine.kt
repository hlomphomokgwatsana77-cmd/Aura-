package com.example.data.engine

import com.example.data.model.AreaStatus
import com.example.data.model.LoadsheddingArea
import com.example.data.model.TimeSlot
import java.util.Calendar

object LoadsheddingLookupEngine {

    val allAreas: List<LoadsheddingArea> = listOf(
        LoadsheddingArea(
            id = "jhb_sandton",
            suburb = "Sandton / Bryanston",
            municipality = "City Power (Johannesburg)",
            province = "Gauteng",
            blockNumber = 3,
            scheduleByStage = mapOf(
                1 to listOf(
                    TimeSlot("Today", "16:00", "18:30", 1),
                    TimeSlot("Tomorrow", "00:00", "02:30", 1)
                ),
                2 to listOf(
                    TimeSlot("Today", "08:00", "10:30", 2),
                    TimeSlot("Today", "16:00", "18:30", 1),
                    TimeSlot("Tomorrow", "00:00", "02:30", 1),
                    TimeSlot("Tomorrow", "16:00", "18:30", 2)
                ),
                3 to listOf(
                    TimeSlot("Today", "08:00", "10:30", 2),
                    TimeSlot("Today", "16:00", "18:30", 1),
                    TimeSlot("Today", "22:00", "00:30", 3),
                    TimeSlot("Tomorrow", "00:00", "02:30", 1),
                    TimeSlot("Tomorrow", "16:00", "18:30", 2)
                ),
                4 to listOf(
                    TimeSlot("Today", "00:00", "02:30", 4),
                    TimeSlot("Today", "08:00", "10:30", 2),
                    TimeSlot("Today", "16:00", "18:30", 1),
                    TimeSlot("Today", "22:00", "00:30", 3),
                    TimeSlot("Tomorrow", "00:00", "02:30", 1),
                    TimeSlot("Tomorrow", "08:00", "10:30", 4),
                    TimeSlot("Tomorrow", "16:00", "18:30", 2)
                )
            )
        ),
        LoadsheddingArea(
            id = "jhb_soweto",
            suburb = "Soweto (Diepkloof & Orlando)",
            municipality = "City Power / Eskom",
            province = "Gauteng",
            blockNumber = 7,
            scheduleByStage = mapOf(
                1 to listOf(
                    TimeSlot("Today", "14:00", "16:30", 1),
                    TimeSlot("Tomorrow", "22:00", "00:30", 1)
                ),
                2 to listOf(
                    TimeSlot("Today", "06:00", "08:30", 2),
                    TimeSlot("Today", "14:00", "16:30", 1),
                    TimeSlot("Tomorrow", "14:00", "16:30", 2),
                    TimeSlot("Tomorrow", "22:00", "00:30", 1)
                ),
                3 to listOf(
                    TimeSlot("Today", "06:00", "08:30", 2),
                    TimeSlot("Today", "14:00", "16:30", 1),
                    TimeSlot("Today", "20:00", "22:30", 3),
                    TimeSlot("Tomorrow", "14:00", "16:30", 2),
                    TimeSlot("Tomorrow", "22:00", "00:30", 1)
                ),
                4 to listOf(
                    TimeSlot("Today", "06:00", "08:30", 2),
                    TimeSlot("Today", "14:00", "16:30", 1),
                    TimeSlot("Today", "20:00", "22:30", 3),
                    TimeSlot("Tomorrow", "04:00", "06:30", 4),
                    TimeSlot("Tomorrow", "14:00", "16:30", 2),
                    TimeSlot("Tomorrow", "22:00", "00:30", 1)
                )
            )
        ),
        LoadsheddingArea(
            id = "jhb_randburg",
            suburb = "Randburg / Ferndale",
            municipality = "City Power (Johannesburg)",
            province = "Gauteng",
            blockNumber = 2,
            scheduleByStage = mapOf(
                1 to listOf(
                    TimeSlot("Today", "10:00", "12:30", 1),
                    TimeSlot("Tomorrow", "18:00", "20:30", 1)
                ),
                2 to listOf(
                    TimeSlot("Today", "10:00", "12:30", 1),
                    TimeSlot("Today", "18:00", "20:30", 2),
                    TimeSlot("Tomorrow", "02:00", "04:30", 2),
                    TimeSlot("Tomorrow", "18:00", "20:30", 1)
                ),
                3 to listOf(
                    TimeSlot("Today", "02:00", "04:30", 3),
                    TimeSlot("Today", "10:00", "12:30", 1),
                    TimeSlot("Today", "18:00", "20:30", 2),
                    TimeSlot("Tomorrow", "02:00", "04:30", 2),
                    TimeSlot("Tomorrow", "18:00", "20:30", 1)
                ),
                4 to listOf(
                    TimeSlot("Today", "02:00", "04:30", 3),
                    TimeSlot("Today", "10:00", "12:30", 1),
                    TimeSlot("Today", "18:00", "20:30", 2),
                    TimeSlot("Tomorrow", "02:00", "04:30", 2),
                    TimeSlot("Tomorrow", "10:00", "12:30", 4),
                    TimeSlot("Tomorrow", "18:00", "20:30", 1)
                )
            )
        ),
        LoadsheddingArea(
            id = "cpt_city",
            suburb = "Cape Town CBD & Atlantic Seaboard",
            municipality = "City of Cape Town",
            province = "Western Cape",
            blockNumber = 7,
            scheduleByStage = mapOf(
                1 to listOf(
                    TimeSlot("Today", "06:00", "08:30", 1),
                    TimeSlot("Tomorrow", "14:00", "16:30", 1)
                ),
                2 to listOf(
                    TimeSlot("Today", "06:00", "08:30", 1),
                    TimeSlot("Today", "22:00", "00:30", 2),
                    TimeSlot("Tomorrow", "14:00", "16:30", 1)
                ),
                3 to listOf(
                    TimeSlot("Today", "06:00", "08:30", 1),
                    TimeSlot("Today", "14:00", "16:30", 3),
                    TimeSlot("Today", "22:00", "00:30", 2),
                    TimeSlot("Tomorrow", "06:00", "08:30", 2),
                    TimeSlot("Tomorrow", "14:00", "16:30", 1)
                ),
                4 to listOf(
                    TimeSlot("Today", "06:00", "08:30", 1),
                    TimeSlot("Today", "14:00", "16:30", 3),
                    TimeSlot("Today", "22:00", "00:30", 2),
                    TimeSlot("Tomorrow", "06:00", "08:30", 2),
                    TimeSlot("Tomorrow", "14:00", "16:30", 1),
                    TimeSlot("Tomorrow", "22:00", "00:30", 4)
                )
            )
        ),
        LoadsheddingArea(
            id = "cpt_bellville",
            suburb = "Bellville & Northern Suburbs",
            municipality = "City of Cape Town",
            province = "Western Cape",
            blockNumber = 2,
            scheduleByStage = mapOf(
                1 to listOf(
                    TimeSlot("Today", "12:00", "14:30", 1),
                    TimeSlot("Tomorrow", "20:00", "22:30", 1)
                ),
                2 to listOf(
                    TimeSlot("Today", "04:00", "06:30", 2),
                    TimeSlot("Today", "12:00", "14:30", 1),
                    TimeSlot("Tomorrow", "12:00", "14:30", 2),
                    TimeSlot("Tomorrow", "20:00", "22:30", 1)
                ),
                3 to listOf(
                    TimeSlot("Today", "04:00", "06:30", 2),
                    TimeSlot("Today", "12:00", "14:30", 1),
                    TimeSlot("Today", "20:00", "22:30", 3),
                    TimeSlot("Tomorrow", "12:00", "14:30", 2),
                    TimeSlot("Tomorrow", "20:00", "22:30", 1)
                ),
                4 to listOf(
                    TimeSlot("Today", "04:00", "06:30", 2),
                    TimeSlot("Today", "12:00", "14:30", 1),
                    TimeSlot("Today", "20:00", "22:30", 3),
                    TimeSlot("Tomorrow", "04:00", "06:30", 4),
                    TimeSlot("Tomorrow", "12:00", "14:30", 2),
                    TimeSlot("Tomorrow", "20:00", "22:30", 1)
                )
            )
        ),
        LoadsheddingArea(
            id = "dbn_central",
            suburb = "Durban Central & Berea",
            municipality = "eThekwini Electricity",
            province = "KwaZulu-Natal",
            blockNumber = 1,
            scheduleByStage = mapOf(
                1 to listOf(
                    TimeSlot("Today", "08:00", "10:30", 1),
                    TimeSlot("Tomorrow", "16:00", "18:30", 1)
                ),
                2 to listOf(
                    TimeSlot("Today", "08:00", "10:30", 1),
                    TimeSlot("Today", "16:00", "18:30", 2),
                    TimeSlot("Tomorrow", "00:00", "02:30", 2),
                    TimeSlot("Tomorrow", "16:00", "18:30", 1)
                ),
                3 to listOf(
                    TimeSlot("Today", "08:00", "10:30", 1),
                    TimeSlot("Today", "16:00", "18:30", 2),
                    TimeSlot("Today", "22:00", "00:30", 3),
                    TimeSlot("Tomorrow", "00:00", "02:30", 2),
                    TimeSlot("Tomorrow", "16:00", "18:30", 1)
                ),
                4 to listOf(
                    TimeSlot("Today", "00:00", "02:30", 4),
                    TimeSlot("Today", "08:00", "10:30", 1),
                    TimeSlot("Today", "16:00", "18:30", 2),
                    TimeSlot("Today", "22:00", "00:30", 3),
                    TimeSlot("Tomorrow", "00:00", "02:30", 2),
                    TimeSlot("Tomorrow", "08:00", "10:30", 4),
                    TimeSlot("Tomorrow", "16:00", "18:30", 1)
                )
            )
        ),
        LoadsheddingArea(
            id = "pta_east",
            suburb = "Pretoria East / Menlyn",
            municipality = "City of Tshwane",
            province = "Gauteng",
            blockNumber = 1,
            scheduleByStage = mapOf(
                1 to listOf(
                    TimeSlot("Today", "11:00", "13:30", 1),
                    TimeSlot("Tomorrow", "19:00", "21:30", 1)
                ),
                2 to listOf(
                    TimeSlot("Today", "03:00", "05:30", 2),
                    TimeSlot("Today", "11:00", "13:30", 1),
                    TimeSlot("Tomorrow", "11:00", "13:30", 2),
                    TimeSlot("Tomorrow", "19:00", "21:30", 1)
                ),
                3 to listOf(
                    TimeSlot("Today", "03:00", "05:30", 2),
                    TimeSlot("Today", "11:00", "13:30", 1),
                    TimeSlot("Today", "19:00", "21:30", 3),
                    TimeSlot("Tomorrow", "11:00", "13:30", 2),
                    TimeSlot("Tomorrow", "19:00", "21:30", 1)
                ),
                4 to listOf(
                    TimeSlot("Today", "03:00", "05:30", 2),
                    TimeSlot("Today", "11:00", "13:30", 1),
                    TimeSlot("Today", "19:00", "21:30", 3),
                    TimeSlot("Tomorrow", "03:00", "05:30", 4),
                    TimeSlot("Tomorrow", "11:00", "13:30", 2),
                    TimeSlot("Tomorrow", "19:00", "21:30", 1)
                )
            )
        )
    )

    fun calculateAreaStatus(area: LoadsheddingArea, stage: Int): AreaStatus {
        if (stage <= 0) {
            return AreaStatus(
                area = area,
                activeStage = 0,
                isPowerOffNow = false,
                currentSlot = null,
                nextSlot = null,
                formattedStatus = "No load shedding scheduled. Grid is normal."
            )
        }

        val slots = area.scheduleByStage[stage] ?: area.scheduleByStage[2] ?: emptyList()
        val cal = Calendar.getInstance()
        val currentMinutes = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)

        var powerOffNow = false
        var currentSlot: TimeSlot? = null
        var nextSlot: TimeSlot? = null

        val todaySlots = slots.filter { it.day.equals("Today", ignoreCase = true) }

        for (slot in todaySlots) {
            val startMin = parseTimeToMinutes(slot.startTime)
            val endMin = parseTimeToMinutes(slot.endTime)

            if (startMin <= endMin) {
                if (currentMinutes in startMin..endMin) {
                    powerOffNow = true
                    currentSlot = slot
                    break
                } else if (currentMinutes < startMin && nextSlot == null) {
                    nextSlot = slot
                }
            } else {
                // Wraps midnight (e.g. 22:00 to 00:30)
                if (currentMinutes >= startMin || currentMinutes <= endMin) {
                    powerOffNow = true
                    currentSlot = slot
                    break
                }
            }
        }

        if (nextSlot == null && !powerOffNow) {
            nextSlot = slots.firstOrNull { it.day.equals("Tomorrow", ignoreCase = true) }
        }

        val statusText = when {
            powerOffNow && currentSlot != null -> "Outage in progress: Power returns around ${currentSlot.endTime}"
            nextSlot != null -> "Power is ON: Next outage starts ${nextSlot.day.lowercase()} at ${nextSlot.startTime}"
            else -> "Power is ON: No immediate outages scheduled"
        }

        return AreaStatus(
            area = area,
            activeStage = stage,
            isPowerOffNow = powerOffNow,
            currentSlot = currentSlot,
            nextSlot = nextSlot,
            formattedStatus = statusText
        )
    }

    private fun parseTimeToMinutes(timeStr: String): Int {
        val parts = timeStr.split(":")
        if (parts.size >= 2) {
            val hours = parts[0].trim().toIntOrNull() ?: 0
            val minutes = parts[1].trim().toIntOrNull() ?: 0
            return hours * 60 + minutes
        }
        return 0
    }

    fun findAreaByQuery(query: String): LoadsheddingArea? {
        val q = query.lowercase().trim()
        return allAreas.firstOrNull { area ->
            val mainName = area.suburb.lowercase().substringBefore("/").trim()
            val muniName = area.municipality.lowercase().substringBefore("(").trim()
            area.suburb.lowercase().contains(q) ||
            q.contains(mainName) ||
            area.municipality.lowercase().contains(q) ||
            q.contains(muniName) ||
            area.province.lowercase().contains(q)
        }
    }
}
