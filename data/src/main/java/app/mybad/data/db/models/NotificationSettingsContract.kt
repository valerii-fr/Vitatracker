package app.mybad.data.db.models

object NotificationSettingsContract {
    const val TABLE_NAME = "notification_settings" //

    object Columns {
        const val USER_ID = "user_id" //
        const val IS_ENABLED = "is_enabled" //
        const val IS_FLOAT = "is_float" //
        const val MEDICAL_CONTROL = "medical_control" //
        const val NEXT_COURSE_START = "next_course_start" //

        const val UPDATED_NETWORK_DATE = "updated_network" // дата последней передачи данных на бек
        const val UPDATED_LOCAL_DATE = "updated_local" // дата последнеого получения данных
    }
}
