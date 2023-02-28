package app.mybad.domain.repos

import app.mybad.domain.models.med.MedDomainModel
import kotlinx.coroutines.flow.Flow

interface MedsRepo {
    fun getAll() : List<MedDomainModel>

    fun getAllFlow() : Flow<List<MedDomainModel>>
    fun getSingle(medId: Long) : MedDomainModel
    fun add(med: MedDomainModel)
    fun updateSingle(medId: Long, item: MedDomainModel)
    fun deleteSingle(medId: Long)
}