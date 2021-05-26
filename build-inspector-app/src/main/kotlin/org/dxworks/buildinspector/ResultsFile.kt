package org.dxworks.buildinspector

import com.google.gson.GsonBuilder
import java.io.File

class ResultsFile {

    fun writeToFile(results: Map<String, List<Build>>, file_name: String) {

        val fileName = "D:/--WORK-- Facultate/build-inspector/build-inspector-app/results/builds/" + file_name + ".json"
        if(File(fileName).createNewFile())
            File(fileName).outputStream().write(GsonBuilder().setPrettyPrinting().create().toJson(results).toByteArray())
    }
}
