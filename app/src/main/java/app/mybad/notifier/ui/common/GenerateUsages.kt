package app.mybad.notifier.ui.common

import android.util.Log
import app.mybad.data.models.UsageFormat
import app.mybad.domain.models.UsageDomainModel
import app.mybad.utils.atEndOfDay
import app.mybad.utils.atStartOfDaySystemToUTC
import app.mybad.utils.changeTime
import app.mybad.utils.currentDateTimeInSecond
import app.mybad.utils.daysBetween
import app.mybad.utils.plusDay
import app.mybad.utils.plusDays
import app.mybad.utils.timeInMinutes
import app.mybad.utils.toEpochSecond
import app.mybad.utils.toLocalDateTime

fun generateUsages(
    usagesByDay: List<UsageFormat>,
    remedyId: Long,
    courseId: Long,
    userId: Long,
    startDate: Long,
    endDate: Long,
    regime: Int,
): List<UsageDomainModel> {
    val currentTime = currentDateTimeInSecond()
    val startUsageTimeToDay = currentTime.atStartOfDaySystemToUTC().toEpochSecond()
    val interval = endDate.daysBetween(startDate).toInt()
    Log.w(
        "VTTAG",
        "generateUsages: interval=$interval startDate=${startDate.toLocalDateTime()} endDate=${endDate.toLocalDateTime()}"
    )
    val usage = mutableListOf<UsageDomainModel>()
    Log.w(
        "VTTAG",
        "generateUsages: userId=${userId} interval=${interval} regime=$regime usagesByDay=${usagesByDay}"
    )
    if (interval <= 0 || usagesByDay.isEmpty()) return emptyList()
    repeat(interval) { position ->
        if (position % (regime + 1) == 0) {
            usagesByDay.forEach { (time, quantity) ->
                val useTime = startDate.toLocalDateTime()
                    .changeTime(minute = time)
                    .plusDays(position)
                    .toEpochSecond()
                Log.w(
                    "VTTAG",
                    "generateUsages: useTime=${useTime.toLocalDateTime()}"
                )
                if (useTime > startUsageTimeToDay) {
                    usage.add(
                        UsageDomainModel(
                            remedyId = remedyId,
                            courseId = courseId,
                            userId = userId,
                            createdDate = currentTime,
                            useTime = useTime,
                            quantity = quantity,
                        )
                    )
                }
            }
        }
    }
    Log.w("VTTAG", "generateUsages: size=${usage.size} usage=${usage}")
    return usage.toList()
}

fun generatePattern(
    courseId: Long,
    regime: Int,
    usages: List<UsageDomainModel>
): List<UsageFormat> {
    var usagesPattern: List<UsageFormat> = emptyList()
    if (usages.isNotEmpty()) {
        val list = usages.filter { it.courseId == courseId }
        if (list.isNotEmpty()) {
            //берем все usages между 2 приемами, берем только время приема в минутах и сортируем
            val nextAdmissionDay = list.minBy { it.useTime }.useTime.plusDay(regime + 1)
                .atEndOfDay()
            usagesPattern = list.mapNotNull { usage ->
                if (usage.useTime < nextAdmissionDay) {
                    UsageFormat(
                        timeInMinutes = usage.useTime.timeInMinutes(),
                        quantity = usage.quantity
                    )
                } else null
            }.toSortedSet(comparator = { uf1, uf2 -> uf1.timeInMinutes - uf2.timeInMinutes })
                .toList()
        }
    }
    return usagesPattern
}
