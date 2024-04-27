package tasks

import contributors.GitHubService
import contributors.RequestData
import contributors.User
import kotlin.concurrent.thread

fun loadContributorsBackground(service: GitHubService, req: RequestData, updateResults: (List<User>) -> Unit) {
    thread {
        loadContributorsBlocking(service, req)
            // The updateResults function, provided as a lambda has to be called with
            // the result of the loadContributorsBlocking function to update the UI
            .also { updateResults(it) }
    }
}
