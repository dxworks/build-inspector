package org.dxworks.buildinspector.statistics

import org.dxworks.buildinspector.Build
import java.io.File

class BuildsPassedStatistic {

    private fun getSucceededBuildsPercentage(buildsMap: Map<String,List<Build>>, file_name : String){
        writeToFile(
            buildsMap.map { (buildName, buildsList) ->
                Pair(buildName, buildsList.count{it.result == "SUCCESS"}.toDouble()/buildsList.size * 100)
            }, file_name)
    }

    private fun writeToFile (statistic: List<Pair<String, Double>>, file_name : String){
        val fileName = "D:/--WORK-- Facultate/build-inspector/build-inspector-app/results/statistics/" + file_name + "_succesfullBuilds.txt"
        if(File(fileName).createNewFile())
            File(fileName).outputStream().write(statistic.toString().toByteArray())
        else
            println("Error")
    }

    fun analyze() {
        File("D:/--WORK-- Facultate/build-inspector/build-inspector-app/results/builds/").walk().forEach { file ->
            file.inputStream().bufferedReader().use {
                val readMap = it.readLines() as Map<String,List<Build>>
                getSucceededBuildsPercentage(readMap, file.getName())
            }
        }
    }
}