package org.dxworks.buildinspector.statistics

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.dxworks.buildinspector.Build
import java.io.File

abstract class Statistic {
    abstract fun getAnalysis(buildsMap: Map<String,List<Build>>, file_name : String)

    fun analyze() {
        File("./results/builds/").walk()
            .filter { file -> file.toString().endsWith(".json") }
            .forEach { file ->
                file.inputStream().bufferedReader().use {
                    val text = it.readText()
                    val mapper = ObjectMapper().registerKotlinModule()
                    val readMap : Map<String,List<Build>> = mapper.readValue(text)
                    getAnalysis(readMap, file.name)
                }
            }
    }
}