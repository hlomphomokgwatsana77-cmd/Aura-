package com.example.data.model

data class TimeSlot(
    val day: String,           // e.g. "Today", "Tomorrow", "Thursday"
    val startTime: String,     // e.g. "08:00"
    val endTime: String,       // e.g. "10:30"
    val stageRequired: Int     // stage at which this slot triggers
)

data class LoadsheddingArea(
    val id: String,
    val suburb: String,
    val municipality: String,
    val province: String,
    val blockNumber: Int,
    val scheduleByStage: Map<Int, List<TimeSlot>>
)

data class AreaStatus(
    val area: LoadsheddingArea,
    val activeStage: Int,
    val isPowerOffNow: Boolean,
    val currentSlot: TimeSlot?,
    val nextSlot: TimeSlot?,
    val formattedStatus: String
)
