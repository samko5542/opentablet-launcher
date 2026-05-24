package com.example.opentabletlauncher.root

import java.io.BufferedReader
import java.io.InputStreamReader

class RootShell {
    data class Result(val code: Int, val stdout: String, val stderr: String)

    fun isRootAvailable(): Boolean {
        return run("id -u").stdout.trim() == "0"
    }

    fun run(command: String): Result {
        val process = ProcessBuilder("su", "-c", command)
            .redirectErrorStream(false)
            .start()
        val out = BufferedReader(InputStreamReader(process.inputStream)).readText()
        val err = BufferedReader(InputStreamReader(process.errorStream)).readText()
        val code = process.waitFor()
        return Result(code, out, err)
    }
}
