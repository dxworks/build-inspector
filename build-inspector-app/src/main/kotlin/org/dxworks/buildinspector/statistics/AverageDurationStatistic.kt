package org.dxworks.buildinspector.statistics

import org.dxworks.buildinspector.Build
import java.io.File

class AverageDurationStatistic {

    fun getAverageDuration(buildsMap: Map<String,List<Build>>, file_name : String){
        writeToFile(
            buildsMap.map {  (buildName, buildsList) ->
                Pair(buildName, buildsList.map{ it.duration?.toDouble() ?: 0.0 }.average())
            }, file_name)
    }

    private fun writeToFile (statistic: List<Pair<String, Double>>, file_name: String){
        val fileName = "results/statistics/" + file_name + "_branchDistribution.txt"
        if(File(fileName).createNewFile())
            File(fileName).outputStream().write(statistic.toString().toByteArray())
        else
            println("Error")
    }

    fun analyze() {
        File("./results/builds/").walk()
            .filter { it.isFile }
            .forEach { file ->
            file.inputStream().bufferedReader().use {
                val readMap = it.readLines() as Map<String,List<Build>>
                getAverageDuration(readMap, file.name)
            }
        }
    }
}
