package app.mybad.domain.models.med

data class MedDomainModel(
    val id: String = "medId",
    val userId: String = "userId",
    val name: String? = null,
    val description: String? = null,
    val comment: String? = null,
    val details: MedDetailsDomainModel = MedDetailsDomainModel()
)