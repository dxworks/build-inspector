package org.dxworks.buildinspector.statistics

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.dxworks.buildinspector.Build
import java.io.File
import java.io.FileWriter
import java.io.IOException

class BuildsPassedStatistic : Statistic() {

    private val CSV_HEADER = "name,percentage"

    override fun getAnalysis(buildsMap: Map<String,List<Build>>, file_name : String){
        writeToFile(
            buildsMap.map { (buildName, buildsList) ->
                Pair(buildName, buildsList.count{it.result == "SUCCESS"}.toDouble()/buildsList.size * 100)
            }, file_name)
    }

    private fun writeToFile (statistic: List<Pair<String, Double>>, file_name : String){
        val fileName = "./results/statistics/" + file_name.removeSuffix(".json") + "_succesfullBuilds.csv"
        var fileWriter: FileWriter? = null
        try {
            fileWriter = FileWriter(fileName)
            fileWriter.append(CSV_HEADER)
            fileWriter.append('\n')
            statistic.forEach {
                fileWriter.append(it.first)
                fileWriter.append(',')
                fileWriter.append(it.second.toString())
                fileWriter.append('\n')
            }
        } catch (e: Exception) {
            println("Writing CSV error!")
            e.printStackTrace()
        } finally {
            try {
                fileWriter!!.flush()
                fileWriter.close()
            } catch (e: IOException) {
                println("Flushing/closing error!")
                e.printStackTrace()
            }
        }
    }
}