package space.outbreak.metabuild.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import space.outbreak.metabuild.MetaYmlConf
import space.outbreak.metabuild.mapper
import java.io.File

class InitCommand : CliktCommand(name = "init", help = "Initialize default pack.yml and pack.mcmeta") {
    private val inputDirRaw by option("-d", "--dir", help = "Path to the resourcepack root directory").default(".")
    private val inputDir by lazy { File(inputDirRaw).absoluteFile.normalize() }
    private val overwrite by option("--overwrite").flag("--preserve", default = false)

    override fun run() {
        if (!inputDir.exists()) {
            echo("Error: input directory (${inputDir}) does not exist!", err = true)
            return
        }

        val packYmlFile = inputDir.resolve("pack.yml")
        val packMcmetaFile = inputDir.resolve("pack.mcmeta")

        val metaConf = MetaYmlConf(name = inputDir.name)
        val meta = metaConf.toMcMeta()

        if (!packYmlFile.exists() || overwrite)
            packYmlFile.bufferedWriter(Charsets.UTF_8).use { out ->
                mapper.writeValue(out, metaConf)
            }
        else
            echo("pack.yml already exist")

        if (!packMcmetaFile.exists() || overwrite)
            packMcmetaFile.bufferedWriter(Charsets.UTF_8).use { out ->
                out.write(meta.generatePackMcMetaJsonString())
            }
        else
            echo("pack.mcmeta already exist")
    }
}