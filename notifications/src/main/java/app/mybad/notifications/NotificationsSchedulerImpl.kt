package app.mybad.notifications

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.domain.repos.CoursesRepo
import app.mybad.domain.repos.MedsRepo
import app.mybad.domain.repos.UsagesRepo
import app.mybad.domain.scheduler.NotificationsScheduler
import javax.inject.Inject
@SuppressLint("UnspecifiedImmutableFlag")
class NotificationsSchedulerImpl
@Inject constructor(
    private val context: Context,
    private val medsRepo: MedsRepo,
    private val usagesRepo: UsagesRepo,
    private val coursesRepo: CoursesRepo,
) : NotificationsScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override suspend fun add(usages: List<UsageCommonDomainModel>) {
        usages.forEach {
            val med = medsRepo.getSingle(it.medId)
            val pi = generateUsagePi(med, it, context)
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, it.useTime*1000L, pi)
        }
    }

    override suspend fun add(course: CourseDomainModel) {
        val med = medsRepo.getSingle(course.medId)
        val pi = generateCoursePi(course, med, context)
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, course.remindDate*1000L, pi)
    }

    override suspend fun cancel(usages: List<UsageCommonDomainModel>) {
        usages.forEach {
            val med = medsRepo.getSingle(it.medId)
            val pi = generateUsagePi(med, it, context)
            alarmManager.cancel(pi)
            pi.cancel()
        }
    }

    override suspend fun cancel(course: CourseDomainModel) {
        val med = medsRepo.getSingle(course.medId)
        val pi = generateCoursePi(course, med, context)
        alarmManager.cancel(pi)
        pi.cancel()
    }

    override suspend fun cancelAll() {
        val usages = usagesRepo.getCommonAll()
        cancel(usages)
    }

    override suspend fun cancelByMedId(medId: Long, onComplete: suspend () -> Unit) {
        val usages = usagesRepo.getUsagesByMedId(medId)
        cancel(usages)
        onComplete()
    }

    override suspend fun rescheduleAll(onComplete: () -> Unit) {
        val now = System.currentTimeMillis()/1000
        usagesRepo.getCommonAll().forEach {
            if(it.useTime >= now) {
                val med = medsRepo.getSingle(it.medId)
                val pi = generateUsagePi(med, it, context)
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, it.useTime*1000, pi)
            }
        }
        coursesRepo.getAll().forEach {
            if(it.remindDate > now) {
                val med = medsRepo.getSingle(it.medId)
                val pi = generateCoursePi(it, med, context)
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, it.remindDate*1000, pi)
            }
        }
        onComplete()
    }

    private fun generateUsagePi(med: MedDomainModel, usage: UsageCommonDomainModel, context: Context) : PendingIntent {
        val i = Intent(context.applicationContext, AlarmReceiver::class.java)
        i.action = NOTIFICATION_INTENT
        i.data = Uri.parse("custom://${(usage.useTime + med.id).toInt()}")
        i.putExtra(Extras.MED_NAME.name, med.name ?: "no name")
        i.putExtra(Extras.MED_ID.name, med.id)
        i.putExtra(Extras.TYPE.name, med.type)
        i.putExtra(Extras.ICON.name, med.icon)
        i.putExtra(Extras.COLOR.name, med.color)
        i.putExtra(Extras.DOSE.name, med.dose)
        i.putExtra(Extras.UNIT.name, med.measureUnit)
        i.putExtra(Extras.USAGE_TIME.name, usage.useTime)
        i.putExtra(Extras.QUANTITY.name, usage.quantity)
        return PendingIntent.getBroadcast(context, (usage.useTime + med.id).toInt(), i, 0)
    }
    private fun generateCoursePi(course: CourseDomainModel, med: MedDomainModel, context: Context) : PendingIntent {
        val i = Intent(context.applicationContext, AlarmReceiver::class.java)
        i.action = COURSE_NOTIFICATION_INTENT
        i.data = Uri.parse("custom://${(course.id + med.id).toInt()}")
        i.putExtra(Extras.MED_NAME.name, med.name ?: "no name")
        i.putExtra(Extras.MED_ID.name, med.id)
        i.putExtra(Extras.TYPE.name, med.type)
        i.putExtra(Extras.ICON.name, med.icon)
        i.putExtra(Extras.COLOR.name, med.color)
        i.putExtra(Extras.DOSE.name, med.dose)
        i.putExtra(Extras.UNIT.name, med.measureUnit)
        i.putExtra(Extras.NEW_COURSE_START_DATE.name, (course.startDate + course.interval))
        i.putExtra(Extras.COURSE_REMIND_TIME.name, course.remindDate)
        return PendingIntent.getBroadcast(context, (course.remindDate + med.id).toInt(), i, 0)
    }
    companion object {
        const val NOTIFICATION_INTENT = "android.intent.action.NOTIFICATION"
        const val COURSE_NOTIFICATION_INTENT = "android.intent.action.COURSE_NOTIFICATION"
        enum class Extras {
            MED_ID,
            MED_NAME,
            TYPE,
            ICON,
            COLOR,
            DOSE,
            UNIT,
            USAGE_TIME,
            QUANTITY,
            COURSE_REMIND_TIME,
            NEW_COURSE_START_DATE,
        }
    }
}