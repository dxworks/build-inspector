package org.dxworks.buildinspector.statistics

import org.dxworks.buildinspector.Build
import java.io.File
import java.nio.file.Paths

class BranchDistributionStatistic {

    fun getBuildDistribution(buildsMap: Map<String,List<Build>>, file_name : String){
        writeToFile(
            buildsMap.mapValues { (buildName, buildsList) ->
                buildsList.map { build ->
                    Pair(build.branch, buildsList.count{it.branch ==build.branch}.toDouble()/buildsList.size * 100)
                }
            }, file_name)
    }

    private fun writeToFile (statistic: Map<String, List<Pair<String?, Double>>>, file_name: String){
        val fileName = "D:/--WORK-- Facultate/build-inspector/build-inspector-app/results/statistics/".toString() + file_name + "_branchDistribution.txt"
        if(File(fileName).createNewFile())
            File(fileName).outputStream().write(statistic.toString().toByteArray())
        else
            println("Error")
    }

    fun analyze() {
        File("D:/--WORK-- Facultate/build-inspector/build-inspector-app/results/builds/").walk().forEach { file ->
            file.inputStream().bufferedReader().use {
                val readMap = it.readLines() as Map<String,List<Build>>
                getBuildDistribution(readMap, file.getName())
            }
        }
    }
}