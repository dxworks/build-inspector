package org.dxworks.buildinspector.statistics

import org.dxworks.buildinspector.Build
import java.io.FileWriter
import java.io.IOException

class BranchDistributionStatistic : Statistic() {

    private val CSV_HEADER = "branch,fail,success"
    override fun getAnalysis(buildsMap: Map<String,List<Build>>, file_name : String){
        writeToFile(
            buildsMap.map{ it.value }.flatten()
                .groupBy{ it.branch ?: "defaultbranch"}
                .mapValues { Pair(it.value.count{it.result != "SUCCESS"}, it.value.count{it.result == "SUCCESS"})}
                .map { (key, value) ->
                    BranchDistributionDTO(key, value.first, value.second)
                }
            , file_name)
    }

    private fun writeToFile (statistic: List<BranchDistributionDTO>, file_name: String){
        val fileName = "./results/statistics/" + file_name.removeSuffix(".json") + "_branchDistribution.csv"
        var fileWriter: FileWriter? = null
        try {
            fileWriter = FileWriter(fileName)
            fileWriter.append(CSV_HEADER)
            fileWriter.append('\n')
            statistic.forEach {
                fileWriter.append(it.branch_name)
                fileWriter.append(',')
                fileWriter.append(it.fail.toString())
                fileWriter.append(',')
                fileWriter.append(it.success.toString())
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