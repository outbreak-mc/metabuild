package space.outbreak.metabuild.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import space.outbreak.metabuild.MetaYmlConf
import space.outbreak.metabuild.PackMcMeta
import space.outbreak.metabuild.mapper
import java.io.File

class McMetaCommand : CliktCommand(name = "mcmeta", help = "Generate pack.mcmeta") {
    private val inputDirRaw by option("-d", "--dir", help = "Path to the resourcepack root directory").default(".")

    private val inputDir by lazy { File(inputDirRaw).absoluteFile.normalize() }

    private val packYmlPath by lazy { inputDir.resolve("pack.yml") }
    private val mcmetaPath by lazy { inputDir.resolve("pack.mcmeta") }

    override fun run() {
        if (!inputDir.exists()) {
            echo("Error: input directory (${inputDir}) does not exist!", err = true)
            return
        }

        val packYml = mapper.readValue(packYmlPath, MetaYmlConf::class.java)

        if ("dirname" !in packYml.placeholders)
            packYml.placeholders["dirname"] = inputDir.name

        val mcMeta = PackMcMeta(pack_format = packYml.getNumberPackFormat()!!)
        mcMeta.setMMDescription(packYml.description, placeholders = packYml.placeholders)

        mcmetaPath.bufferedWriter(Charsets.UTF_8).use { out ->
            out.write(mcMeta.generatePackMcMetaJsonString())
        }
    }

}