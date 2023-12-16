package space.outbreak.metabuild

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.ajalt.clikt.core.subcommands
import space.outbreak.metabuild.commands.InfoCommand
import space.outbreak.metabuild.commands.InitCommand
import space.outbreak.metabuild.commands.McMetaCommand
import space.outbreak.metabuild.commands.ResourcepackZipCommand

val mapper: ObjectMapper = YAMLMapper.builder()
    .configure(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS, true)
    .configure(YAMLGenerator.Feature.WRITE_DOC_START_MARKER, false)
    .build()
    .registerModule(
        KotlinModule.Builder()
            .withReflectionCacheSize(512)
            .configure(KotlinFeature.NullToEmptyCollection, false)
            .configure(KotlinFeature.NullToEmptyMap, false)
            .configure(KotlinFeature.NullIsSameAsDefault, false)
            .configure(KotlinFeature.SingletonSupport, false)
            .configure(KotlinFeature.StrictNullChecks, false)
            .build()
    )
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

val allowedExtensions = setOf("png", "mcmeta", "json", "dat", "lang", "ttf", "mcfunction", "ogg", "model")

fun main(args: Array<String>) = CLI().subcommands(
    ResourcepackZipCommand(),
    McMetaCommand(),
    InitCommand(),
    InfoCommand()
).main(args)