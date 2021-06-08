package org.dxworks.buildinspector.statistics

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.dxworks.buildinspector.Build
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BranchDistributionStatistic : Statistic() {

    private val CSV_HEADER = "branch,usage_percentage"
    override fun getAnalysis(buildsMap: Map<String,List<Build>>, file_name : String){
        writeToFile(
            buildsMap.map{ it.value }.flatten()
                .groupBy{ it.branch ?: "defaultbranch"}
                .mapValues { it.value.count() }
            , file_name)
    }

    private fun writeToFile (statistic: Map<String, Int>, file_name: String){
        val fileName = "./results/statistics/" + file_name.removeSuffix(".json") + "_branchDistribution.csv"
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