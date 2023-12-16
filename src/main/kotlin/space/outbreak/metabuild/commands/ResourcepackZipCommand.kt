package space.outbreak.metabuild.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import space.outbreak.metabuild.MetaYmlConf
import space.outbreak.metabuild.PackMcMeta
import space.outbreak.metabuild.allowedExtensions
import space.outbreak.metabuild.mapper
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ResourcepackZipCommand : CliktCommand(name = "zip", help = "Build a resourcepack zip") {
    private val inputPathRaw by option("-i", "--input", help = "Resourcepack directory to zip").default(".")
    private val outputPathRaw by option("-o", "--output", help = "Location to save output zip file").default(".")

    private val inputDir by lazy { File(inputPathRaw).absoluteFile.normalize() }
    private val outputDir by lazy { File(outputPathRaw).absoluteFile.normalize() }

    private val packYmlPath by lazy { inputDir.resolve("pack.yml") }
    private val mcmetaPath by lazy { inputDir.resolve("pack.mcmeta") }

    private fun zipResourcePack(inputDirectory: File, outputZipFile: File) {
        ZipOutputStream(BufferedOutputStream(FileOutputStream(outputZipFile))).use { zos ->
            inputDirectory.walkTopDown()
                .filter {
                    it.extension in allowedExtensions
                }
                .forEach { file ->
                    val zipFileName = file.absolutePath.removePrefix(inputDirectory.absolutePath)
                        .removePrefix("/")
                        .removePrefix("\\")

                    // Only assets folder is allowed in the root of the resourcepack
                    if ((zipFileName.contains("\\") || zipFileName.contains("/")) && !zipFileName.contains("assets")) {
                        return@forEach
                    }

                    val entry = ZipEntry("$zipFileName${(if (file.isDirectory) "/" else "")}")
                    zos.putNextEntry(entry)
                    if (file.isFile)
                        file.inputStream().use { fis -> fis.copyTo(zos) }
                }
        }
    }

    override fun run() {
        if (inputDir.isFile) {
            echo("Error: $inputDir is a file!", err = true)
            return
        }

        if (outputDir.isFile) {
            echo("Error: $outputDir is a file!", err = true)
            return
        }

        if (!inputDir.exists()) {
            echo("Error: input directory (${inputDir}) does not exist!", err = true)
            return
        }

        if (!outputDir.exists()) {
            echo("Error: output directory (${outputDir}) does not exist!", err = true)
            return
        }

        if (!packYmlPath.exists()) {
            echo("Error: pack.yml does not exist in $outputDir", err = true)
            return
        }

        val packYml = mapper.readValue(packYmlPath, MetaYmlConf::class.java)

        if ("dirname" !in packYml.placeholders)
            packYml.placeholders["dirname"] = inputDir.name

        val mcMeta = PackMcMeta(pack_format = packYml.getNumberPackFormat()!!)

        mcmetaPath.bufferedWriter(Charsets.UTF_8).use { out ->
            out.write(mcMeta.generatePackMcMetaJsonString())
        }

        mcMeta.setMMDescription(packYml.description, placeholders = packYml.placeholders)

        outputDir.resolve("${packYml.getNameWithPlaceholders()}.zip").let { outPath ->
            zipResourcePack(inputDir, outPath)
            echo(outPath)
        }
    }

}