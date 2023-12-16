package space.outbreak.metabuild.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import space.outbreak.metabuild.MetaYmlConf
import java.io.File

class InfoCommand : CliktCommand(name = "info", help = "Print info about resourcepack") {
    private val inputDirRaw by option("-d", "--dir", help = "Path to the resourcepack root directory").default(".")
    private val what by option("-w").choice(
        "name", "version", "description",
        "description-raw", "description-plain"
    )

    private val inputDir by lazy { File(inputDirRaw).absoluteFile.normalize() }

    override fun run() {
        if (!inputDir.exists()) {
            echo("Error: input directory (${inputDir}) does not exist!", err = true)
            return
        }

        val packYmlFile = inputDir.resolve("pack.yml")
        if (!packYmlFile.exists()) {
            echo("Error: pack.yml does not exist in $inputDir", err = true)
            return
        }

        val packYml = MetaYmlConf.fromFile(packYmlFile)
        val meta = packYml.toMcMeta()

        when (what) {
            "name" -> {
                echo(packYml.getNameWithPlaceholders())
            }

            "version" -> {
                echo(packYml.placeholders.getOrDefault("version", "1.0"))
            }

            "description" -> {
                echo(meta.description)
            }

            "description-raw" -> {
                echo(packYml.description)
            }

            "description-plain" -> {
                echo(
                    PlainTextComponentSerializer.plainText()
                        .serialize(MiniMessage.miniMessage().deserialize(packYml.description))
                )
            }

            else -> {
                echo("Error: unknown info parameter '$what'", err = true)
            }
        }
    }
}