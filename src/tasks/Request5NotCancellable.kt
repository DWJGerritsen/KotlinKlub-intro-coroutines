package tasks

import contributors.*
import kotlinx.coroutines.*

suspend fun loadContributorsNotCancellable(service: GitHubService, req: RequestData): List<User> {
    val repos = service.getOrgRepos(req.org)
        .also { logRepos(req, it) }
        .bodyList()
    log("Uncancellable getRepoContributors from here on")
    val deferreds = repos
        .map { repo ->
            GlobalScope.async{
                delay(3000)
                service.getRepoContributors(req.org, repo.name)
                    .also { logUsers(repo, it) }
                    .bodyList()
            }
        }
    return deferreds.awaitAll().flatten().aggregate()
}
