package app.mybad.domain.repository.network

import app.mybad.domain.models.AuthorizationDomainModel
import app.mybad.domain.models.PasswordDomainModel
import app.mybad.domain.models.VerificationCodeDomainModel

interface AuthorizationNetworkRepository {

    suspend fun loginWithFacebook(): Result<AuthorizationDomainModel>

    suspend fun loginWithGoogle(): Result<AuthorizationDomainModel>

    suspend fun loginWithEmail(
        login: String,
        password: String
    ): Result<AuthorizationDomainModel>

    suspend fun registrationUser(
        login: String,
        password: String,
        userName: String
    ): Result<AuthorizationDomainModel>

    suspend fun refreshToken(): Result<AuthorizationDomainModel>

    suspend fun restorePassword(email: String): Result<PasswordDomainModel>
    suspend fun sendVerificationCode(code: Int): Result<VerificationCodeDomainModel>

    suspend fun changeUserPassword(oldPassword: String, newPassword: String): Result<Boolean>
    suspend fun setNewUserPassword(token: String, password: String, email: String): Result<Boolean>
}
