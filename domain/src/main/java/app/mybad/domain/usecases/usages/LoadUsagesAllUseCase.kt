package app.mybad.domain.usecases.usages

import app.mybad.domain.models.AuthToken
import app.mybad.domain.repos.UsagesRepo
import javax.inject.Inject

class LoadUsagesAllUseCase @Inject constructor(
    private val usagesRepo: UsagesRepo
) {

    suspend operator fun invoke() = usagesRepo.getCommonAll(AuthToken.userId)
}
