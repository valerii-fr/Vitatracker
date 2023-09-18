package app.mybad.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toLocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

const val SECONDS_IN_DAY = 86400L
const val MINUTES_IN_DAY = 1440
private const val MINUTES_IN_HOUR = 60
const val MILES_SECONDS = 1000
const val WEEKS_PER_MONTH = 6 // строк недель месяца
const val DAYS_A_WEEK = 7
const val TIME_IS_UP = 3600
const val CORRECTION_SERVER_TIME = 1000

// форматирование даты и времени
private val dateDisplayFormatter = DateTimeFormatter
    .ofPattern("dd.MM.yyyy")
    .withZone(ZoneOffset.systemDefault())

fun Long.toDateDisplay(): String = Instant.fromEpochSeconds(this).formatDate()

fun Instant.formatDate(): String = dateDisplayFormatter.format(this.toJavaInstant())

private val dateTimeDisplayFormatter = DateTimeFormatter
    .ofPattern("dd.MM.yyyy HH:mm:ss")
    .withZone(ZoneOffset.systemDefault())

fun LocalDateTime.toDateTimeDisplay() = this.toInstant(TimeZone.currentSystemDefault())
    .formatDateTime()

fun Long.toDateTimeDisplay(): String = Instant.fromEpochSeconds(this).formatDateTime()

fun Instant.formatDateTime(): String = dateTimeDisplayFormatter.format(this.toJavaInstant())

private val dateTimeShortDisplayFormatter = DateTimeFormatter
    .ofPattern("dd.MM HH:mm")
    .withZone(ZoneOffset.systemDefault())

fun Long.toDateTimeShortDisplay(): String = Instant.fromEpochSeconds(this).formatDateTimeShort()

fun Instant.formatDateTimeShort(): String = dateTimeShortDisplayFormatter.format(this.toJavaInstant())

private val dateFullDisplayFormatter
    get() = DateTimeFormatter
        .ofPattern(
            "dd MMMM yyyy",
            Locale.getDefault()
        )
        .withZone(ZoneOffset.systemDefault())

fun LocalDateTime.toDateFullDisplay(): String =
    this.toInstant(TimeZone.currentSystemDefault()).formatDateFull()

fun Instant.formatDateFull(): String = dateFullDisplayFormatter.format(this.toJavaInstant())

private val timeDisplayFormatter = DateTimeFormatter
    .ofPattern("HH:mm")
    .withZone(ZoneOffset.systemDefault())

fun Long.toTimeDisplay(): String = Instant.fromEpochSeconds(this).formatTime()

fun LocalDateTime.toTimeDisplay(): String = this.toInstant(TimeZone.UTC).formatTime()

fun Instant.formatTime(): String = timeDisplayFormatter.format(this.toJavaInstant())

private val dayDisplayFormatter
    get() = DateTimeFormatter
        .ofPattern(
            "dd MMMM",
            Locale.getDefault()
        )
        .withZone(ZoneOffset.systemDefault())

fun Long.toDayDisplay(): String = Instant.fromEpochSeconds(this).formatDay()

fun LocalDateTime.toDayDisplay(): String = this.toInstant(TimeZone.UTC).formatDay()

fun Instant.formatDay(): String = dayDisplayFormatter.format(this.toJavaInstant())

//DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")
private const val dateTimeIsoFormatter = "%04d-%02d-%02dT%02d:%02d:%02dZ"

fun Long.toDateTimeIsoDisplay(): String {
    return if (this == 0L) ""
    else this.toLocalDateTime().formatISO()
}

fun LocalDateTime.formatISO() = dateTimeIsoFormatter.format(
    year,
    month.value,
    dayOfMonth,
    hour,
    minute,
    second,
)

// названия месяцев и дней
fun Int.monthShortDisplay(): String = Month(this + 1).getDisplayName(
    TextStyle.SHORT_STANDALONE,
    Locale.getDefault()
).uppercase(Locale.getDefault()).replace(".", "")

fun Int.monthFullDisplay(): String = Month(this + 1).getDisplayName(
    TextStyle.FULL_STANDALONE,
    Locale.getDefault()
).replaceFirstChar { it.uppercase(Locale.getDefault()) }

fun DayOfWeek.displayName() = this.getDisplayName(
    TextStyle.SHORT_STANDALONE,
    Locale.getDefault()
).uppercase(Locale.getDefault())

fun Int.dayShortDisplay() = DayOfWeek(this + 1).displayName()

fun LocalDateTime.dayShortDisplay() = this.dayOfWeek.displayName()

fun Int.dayFullDisplay(): String = DayOfWeek(this + 1).getDisplayName(
    TextStyle.FULL_STANDALONE,
    Locale.getDefault()
).replaceFirstChar { it.uppercase(Locale.getDefault()) }

// Прибавление
fun Long.plusDay(days: Int = 1): Long = this + SECONDS_IN_DAY * days
fun Long.plusDay(days: Long): Long = this.plusDay(days.toInt())

fun Long.secondsToDay() = (this / SECONDS_IN_DAY)

fun Long.daysBetween(date: Long) = Instant.fromEpochSeconds(this)
    .minus(Instant.fromEpochSeconds(date)).inWholeDays.plus(1)

fun Long.toLocalDateTime() = Instant.fromEpochSeconds(this)
    .toLocalDateTime(TimeZone.UTC)

fun Long.toSystemDateTime() = Instant.fromEpochSeconds(this)
    .toLocalDateTime(TimeZone.currentSystemDefault())

fun Long.toSecondsLeftFromStartOfDay() = this - (this % SECONDS_IN_DAY)

fun LocalDateTime.toSecondsLeftFromStartOfDay() = this.toEpochSecond()
    .toSecondsLeftFromStartOfDay()

fun LocalDateTime.toEpochSecond(isUTC: Boolean = true) = this.toInstant(
    if (isUTC) TimeZone.UTC else TimeZone.currentSystemDefault()
).epochSeconds

// Преобразование и замена даты и времени
fun Long.changeTime(minutes: Int) = this.toLocalDateTime()
    .changeTime(hour = 0, minute = minutes)
    .toEpochSecond()

fun LocalDateTime.changeTime(
    hour: Int? = null,
    minute: Int? = null,
    second: Int = 0,
    nanosecond: Int = 0
): LocalDateTime {
    var h = hour
    var m = minute ?: this.minute
    if ((h == null || h == 0) && m > 59) {
        h = m / MINUTES_IN_HOUR
        m %= MINUTES_IN_HOUR
    }
    return LocalDateTime(
        year = this.year,
        monthNumber = this.monthNumber,
        dayOfMonth = this.dayOfMonth,
        hour = h ?: this.hour,
        minute = m,
        second = second,
        nanosecond = nanosecond,
    )
}

fun LocalDateTime.changeTime(time: LocalDateTime) = this.changeTime(
    hour = time.hour,
    minute = time.minute,
    second = time.second,
    nanosecond = time.nanosecond
)

fun LocalDateTime.changeTime(time: Long) = this.changeTime(time.toLocalDateTime())

// month: 1..12, days: 1..31
fun LocalDateTime.changeDate(year: Int? = null, month: Int? = null, dayOfMonth: Int? = null) =
    LocalDateTime(
        year = year ?: this.year,
        monthNumber = month ?: this.monthNumber,
        dayOfMonth = dayOfMonth ?: this.dayOfMonth,
        hour = this.hour,
        minute = this.minute,
        second = this.second,
        nanosecond = this.nanosecond,
    )

fun LocalDateTime.atStartOfDay() = this.changeTime(
    hour = 0,
    minute = 0,
    second = 0,
    nanosecond = 0
)

// преобразует текущее локальное время в UTC
fun LocalDateTime.atStartOfDaySystemToUTC() = this.atStartOfDay()
    .toInstant(TimeZone.currentSystemDefault())
    .toLocalDateTime(TimeZone.UTC)

fun Long.atStartOfDay() = this.toLocalDateTime()
    .atStartOfDay()
    .toEpochSecond()

fun Long.atStartOfDaySystemToUTC() = this.toLocalDateTime()
    .atStartOfDaySystemToUTC()

fun LocalDateTime.atEndOfDay() = this.changeTime(
    hour = 23,
    minute = 59,
    second = 59,
    nanosecond = 0
)

fun LocalDateTime.atEndOfDaySystemToUTC() = this.atEndOfDay()
    .toInstant(TimeZone.currentSystemDefault())
    .toLocalDateTime(TimeZone.UTC)

fun Long.atEndOfDay() = this.toLocalDateTime()
    .atEndOfDay()
    .toEpochSecond()

fun Long.atEndOfDaySystemToUTC() = this.toLocalDateTime()
    .atEndOfDaySystemToUTC()
//    .toEpochSeconds()

fun LocalDateTime.atStartOfMonth() = this.changeDate(dayOfMonth = 1)

// Прибавление и удаление
fun LocalDateTime.minus(period: DateTimePeriod) = this.toInstant(TimeZone.UTC)
    .minus(period, TimeZone.UTC)
    .toLocalDateTime(TimeZone.UTC)

fun LocalDateTime.minusDays(days: Int = 1): LocalDateTime {
    return if (days == 0) this
    else this.toInstant(TimeZone.UTC)
        .minus(days, DateTimeUnit.DAY, TimeZone.UTC)
        .toLocalDateTime(TimeZone.UTC)
}

fun LocalDateTime.minusMonths(months: Int): LocalDateTime {
    return if (months == 0) this
    else this.toInstant(TimeZone.UTC)
        .minus(months, DateTimeUnit.MONTH, TimeZone.UTC)
        .toLocalDateTime(TimeZone.UTC)
}

fun Long.minusMonths(months: Int) = this.toLocalDateTime()
    .minusMonths(months)
    .toEpochSecond()

fun LocalDateTime.plus(period: DateTimePeriod) = this.toInstant(TimeZone.UTC)
    .plus(period, TimeZone.UTC)
    .toLocalDateTime(TimeZone.UTC)

fun LocalDateTime.plusDays(days: Int = 1): LocalDateTime {
    return if (days == 0) this
    else this.toInstant(TimeZone.UTC)
        .plus(days, DateTimeUnit.DAY, TimeZone.UTC)
        .toLocalDateTime(TimeZone.UTC)
}

fun LocalDateTime.plusMonths(months: Int): LocalDateTime {
    return if (months == 0) this
    else this.toInstant(TimeZone.UTC)
        .plus(months, DateTimeUnit.MONTH, TimeZone.UTC)
        .toLocalDateTime(TimeZone.UTC)
}

fun Long.plusMonths(months: Int) = this.toLocalDateTime()
    .plusMonths(months)
    .toEpochSecond()

// Получение даты + времени
fun currentDateTime() = Clock.System.now().toLocalDateTime(TimeZone.UTC)
fun currentDateTimeInSecond() = currentDateTime().toEpochSecond()
fun currentSystemDateTime() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

// текущее время преобразованное в минуты
fun Int.timeInMinutesToText() = "%02d:%02d".format(this.hour(), this.minute())

// UTC время в минутах отображает с учетом часового пояса
fun Int.timeInMinutesToDisplay() = this.toSystemTimeInMinutes().run {
    "%02d:%02d".format(hour(), minute())
}

fun Int.hour() = this / MINUTES_IN_HOUR

fun Int.minute() = this % MINUTES_IN_HOUR

fun LocalDateTime.timeInMinutes() = this.run {
    time.hour * MINUTES_IN_HOUR + time.minute
}

fun currentTimeInMinutes() = currentDateTime().timeInMinutes()

fun Long.timeInMinutes() = this.toLocalDateTime().timeInMinutes()

// изменить время у текущей даты и преобразовать в UTC и отдать только время
fun systemTimeInMinutes(hour: Int, minute: Int) = currentDateTime()
    .changeTime(hour = hour, minute = minute)
    .toInstant(TimeZone.currentSystemDefault())
    .toLocalDateTime(TimeZone.UTC)
    .timeInMinutes()

// изменить время у текущей даты и преобразовать с учетом часового пояяса и отдать только время
fun Int.toSystemTimeInMinutes() = currentDateTime()
    .changeTimeToSystemTimeInMinutes(this)

fun Long.toSystemTimeInMinutes() = this.toLocalDateTime()
    .toInstant(TimeZone.UTC)
    .toLocalDateTime(TimeZone.currentSystemDefault())
    .timeInMinutes()

fun Long.changeTimeToSystemTimeInMinutes(minutes: Int) = this.toLocalDateTime()
    .changeTimeToSystemTimeInMinutes(minutes)

// изменить время и преобразовать с учетом часового пояса и отдать только время
fun LocalDateTime.changeTimeToSystemTimeInMinutes(minutes: Int) = this
    .changeTime(hour = 0, minute = minutes)
    .toInstant(TimeZone.UTC)
    .toLocalDateTime(TimeZone.currentSystemDefault())
    .timeInMinutes()

// сложный маневр для правильной сортировки, дата остается той же, а время из UTC в часовой пояс
fun Long.changeTimeOfSystem(minutes: Int): Long = this.toLocalDateTime()
    .changeTimeOfSystem(minutes)

fun LocalDateTime.changeTimeOfSystem(minutes: Int): Long {
    val time = minutes.toSystemTimeInMinutes()
    return LocalDateTime(
        year = year,
        monthNumber = monthNumber,
        dayOfMonth = dayOfMonth,
        hour = time.hour(),
        minute = time.minute(),
        second = 0,
        nanosecond = 0,
    )
        .toInstant(TimeZone.currentSystemDefault()).epochSeconds
}

// высокосный год
val Int.isLeapYear
    get() = when {
        this % 4 == 0 -> {
            when {
                this % 100 == 0 -> this % 400 == 0
                else -> true
            }
        }

        else -> false
    }

fun LocalDateTime.getDaysOfMonth() = month.length(year.isLeapYear)

fun String.toLocalDateTime(): LocalDateTime {
    return if (this == "") currentDateTime()
    else LocalDateTime.parse(this)
}

// интервал и дата оповещения
// endDate
fun Long.nextCourseStart(
    coursesInterval: DateTimePeriod,
    remindBeforePeriod: DateTimePeriod,
    remindTime: Int
): Pair<Long, Long> {
    return try {
        val nextCourseStart = this.toLocalDateTime()
            .plus(coursesInterval)
            .atStartOfDay()
        val remindDate = if (coursesInterval.months > 0 || coursesInterval.days > 0
            || remindBeforePeriod.months > 0 || remindBeforePeriod.days > 0
        ) {
            nextCourseStart
                .minus(remindBeforePeriod)
                .changeTime(minute = remindTime, hour = 0)
                .toEpochSecond()
        } else 0L
        val interval = if (remindDate > 0) {
            (nextCourseStart.toEpochSecond() - this).secondsToDay() + 1 // +1 день, значит следующий день
        } else 0L
        remindDate to interval
    } catch (_: Error) {
        0L to 0L
    }
}

//remindDate
fun Long.nextCourseIntervals(
    remindDate: Long,
    interval: Long,
): Triple<Int, Int, Int> {
    // начало нового курса + интервал за сколько дней сообщить - последний день курса, который может быть больше
    // remindTime, coursesInterval (day), remindBeforePeriod (day)
    if (remindDate <= 0) return Triple(840, 0, 0)

    return try {
        val remindTime = remindDate.timeInMinutes()
        val nextCourseStart = this
            .plusDay(interval)
            .atStartOfDay()
        val days = (nextCourseStart - remindDate.atStartOfDay()).secondsToDay()
            .toInt()
        // не больше 12 месяцев и 30 дней
        val beforeDay = if (days in 0..390) days else 0
        val intervalCorrect = if (interval in 0..390) {
            interval.toInt() - 1 // -1 день, значит следующий день
        } else 0

        Triple(remindTime, intervalCorrect, beforeDay)
    } catch (_: Error) {
        Triple(840, 0, 0)
    }
}