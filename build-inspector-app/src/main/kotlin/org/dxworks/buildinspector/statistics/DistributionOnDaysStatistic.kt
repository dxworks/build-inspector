package org.dxworks.buildinspector.statistics

import org.dxworks.buildinspector.Build
import java.io.FileWriter
import java.io.IOException

class DistributionOnDaysStatistic : Statistic() {
    private val CSV_HEADER = "day,runs_made"
    override fun getAnalysis(buildsMap: Map<String,List<Build>>, file_name : String){
        writeToFile(
            buildsMap.map{ it.value }.flatten()
                .groupBy{ build -> build.timestamp?.substringBefore("T") ?: "notimestamp" }
                .mapValues { it.value.count() }
            , file_name)
    }

    private fun writeToFile (statistic: Map<String, Int>, file_name: String){
        val fileName = "./results/statistics/" + file_name.removeSuffix(".json") + "_distributionOnDays.csv"
        var fileWriter: FileWriter? = null
        try {
            fileWriter = FileWriter(fileName)
            fileWriter.append(CSV_HEADER)
            fileWriter.append('\n')
            statistic.forEach {
                fileWriter.append(it.key)
                fileWriter.append(',')
                fileWriter.append(it.value.toString())
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