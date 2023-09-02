package app.mybad.domain.models

import android.util.Log
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

object AuthToken {
    private val _isAuthorize = MutableStateFlow(false)
    val isAuthorize = _isAuthorize.asStateFlow()

    private val _synchronization = MutableSharedFlow<Long>()
    val synchronization = _synchronization.asSharedFlow()

    private val _updateUsage = MutableSharedFlow<Pair<Long, Long>>()
    val updateUsage = _updateUsage.asSharedFlow()

    var userId = -1L

    var email = ""

    var token = ""
        set(value) {
            field = value
            _isAuthorize.value = value.isNotBlank() && userId != -1L
            Log.w("VTTAG", "AuthToken::token: ${_isAuthorize.value} userId=${userId} token=$value")
        }
    var tokenDate = 0L

    var tokenRefresh = ""
    var tokenRefreshDate = 0L

    suspend fun requiredSynchronize(time: Long){
        if (isAuthorize.value) {
            Log.v("VTTAG", "AuthToken::token: requiredSynchronize date=$time")
            _synchronization.emit(time)
        }
    }

    suspend fun requiredSetUsagesFactTime(usageId: Long){
        if (isAuthorize.value) {
            Log.v("VTTAG", "AuthToken::token: requiredCheckUsage id=$usageId")
            _updateUsage.emit(userId to usageId)
        }
    }

    fun clear() {
        Log.e("VTTAG", "AuthToken::token: clear")
        userId = -1L
        tokenDate = 0L
        tokenRefresh = ""
        tokenRefreshDate = 0L
        email = ""
        token = ""
    }
}
