package tasks

import contributors.*
import retrofit2.Response

suspend fun loadContributorsSuspend(service: GitHubService, req: RequestData): List<User> {
    TODO()
    //Hint: Use the bodyList extension function below
}

fun <T> Response<List<T>>.bodyList(): List<T> {
    return body() ?: emptyList()
}
