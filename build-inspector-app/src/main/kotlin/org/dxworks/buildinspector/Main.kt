package org.dxworks.buildinspector

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.dxworks.buildinspector.configuration.ConfigurationDTO
import org.dxworks.buildinspector.statistics.AverageDurationStatistic
import org.dxworks.buildinspector.statistics.BranchDistributionStatistic
import org.dxworks.buildinspector.statistics.BuildsPassedStatistic
import org.dxworks.buildinspector.statistics.DistributionOnDaysStatistic
import java.io.File

private val yamlMapper = ObjectMapper(YAMLFactory()).registerKotlinModule()
private val service = Service()
private val config: ConfigurationDTO = yamlMapper.readValue(
    File("config/config.yml"),
    ConfigurationDTO::class.java
)

fun extract() {
    config.jenkins?.forEach { (file_name, url) ->
        if (url != null) {
            val start = System.currentTimeMillis()
            ResultsFile().writeToFile(service.getJenkinsResults(url), file_name)
            println(file_name + " extraction time: " + (System.currentTimeMillis() - start).toString())
        }
    }

    if (config.github?.tokens?.isNotEmpty() == true) {
        val tokens = config.github.tokens
        config.github.repos?.forEach { (file_name, project_name) ->
            if (project_name != null) {
                val owner = project_name.substringBefore("/")
                val repo = project_name.substringAfter("/")
                val start = System.currentTimeMillis()
                ResultsFile().writeToFile(service.getGithubResults(owner, repo, tokens), file_name)
                println(project_name + " extraction time: " + (System.currentTimeMillis() - start).toString())
            }
        }
    }
}

fun analyze() {
    extract()
    BuildsPassedStatistic().analyze()
    AverageDurationStatistic().analyze()
    BranchDistributionStatistic().analyze()
    DistributionOnDaysStatistic().analyze()
}


fun main(args: Array<String>) {
    if (args.isNotEmpty()) {
        when {
            args[0] == "extract" -> extract()
            args[0] == "analyze" -> analyze()
        }
    } else {
        println("Please write what operation you would like to perform.")
    }
}
