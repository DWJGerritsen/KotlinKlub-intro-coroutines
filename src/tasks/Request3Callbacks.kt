package tasks

import contributors.GitHubService
import contributors.RequestData
import contributors.User
import contributors.log
import contributors.logRepos
import contributors.logUsers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun loadContributorsCallbacks(service: GitHubService, req: RequestData, updateResults: (List<User>) -> Unit) {
    service.getOrgReposCall(req.org).onResponse { responseRepos ->
        logRepos(req, responseRepos)
        val repos = responseRepos.bodyList()
        val allUsers = mutableListOf<User>()
        var reposToRead = repos.size
        for (repo in repos) {
            service.getRepoContributorsCall(req.org, repo.name).onResponse { responseUsers ->
                reposToRead--
                logUsers(repo, responseUsers)
                val users = responseUsers.bodyList()
                allUsers += users
            }
        }
        // We must wait for all API calls to return a result before we call updateResults with the results up to
        // that point. If we don't wait, we might call updateResults with an incomplete or even empty list of users.
        // This can be done in multiple ways, but for this example, we use a simple counter that is decremented each
        // time a response is received. When the counter reaches 0, we know that all responses have been received.
        while (reposToRead > 0) {
            Thread.sleep(100)
        }
        updateResults(allUsers.aggregate())
    }
}

inline fun <T> Call<T>.onResponse(crossinline callback: (Response<T>) -> Unit) {
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            callback(response)
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            log.error("Call failed", t)
        }
    })
}
