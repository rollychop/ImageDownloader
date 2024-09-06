package ins.quivertech.app.data.data_source.remote.dto

class CResponse<Type>(
    val message: String?,
    val status: Int?,
    val logged: Boolean?,
    val data: Type?
) {
    val isSuccessful get() = (status) in 200..299
}