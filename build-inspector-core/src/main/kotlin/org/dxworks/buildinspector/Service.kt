package org.dxworks.buildinspector

import org.dxworks.githubminer.service.repository.actions.run.GithubRunService
import org.dxworks.jenkinsminer.JenkinsService
import java.io.FileInputStream
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class Service {

    fun getGithubResults(owner : String, repo : String, tokens : List<String>) : Map<String, List<Build>> {
        return GithubRunService(owner, repo, githubTokens = tokens).getAllRuns().map {
            Build(
                it.name,
                it.id,
                it.duration.toInt(),
                LocalDateTime.parse(it.startedAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toString(),
                it.conclusion,
                it.event,
                it.commitId,
                it.branch,
                it.parentName
            )
        }.groupBy { it.parentName?: "defaultparent" }
    }

    fun getJenkinsResults(url : String) : Map<String, List<Build>> {
        return JenkinsService(url).getAllBuilds().map {
            Build(
                it.name,
                it.id,
                it.duration,
                it.timestamp,
                it.result,
                it.event,
                it.commitId,
                it.branch,
                it.parentName
            )
        }.groupBy { it.parentName?: "defaultparent" }
    }
}