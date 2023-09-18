package app.mybad.domain.usecases.courses

import app.mybad.domain.repository.CourseRepository
import app.mybad.domain.repository.PatternUsageRepository
import app.mybad.domain.repository.UsageRepository
import javax.inject.Inject

class CloseCourseUseCase @Inject constructor(
    private val courseRepository: CourseRepository,
    private val patternUsageRepository: PatternUsageRepository,
    private val usageRepository: UsageRepository,
) {

    // закрыть курс, поставить дату завершения и удалить usages после даты
    suspend operator fun invoke(courseId: Long, dateTime: Long) {
        courseRepository.getCourseById(courseId).onSuccess { course ->
            courseRepository.updateCourse(
                course.copy(
                    updatedDate = dateTime,
                    endDate = dateTime,
                    notUsed = true,
                    isFinished = true,
                    updateNetworkDate = 0,
                )
            )
        }
        // пометить паттерн как закрытый
        patternUsageRepository.finishedPatternUsageByCourseId(courseId)
        // удалить usage после даты закрытия
        usageRepository.markDeletionUsagesAfterByCourseId(courseId = courseId, dateTime = dateTime)
    }
}