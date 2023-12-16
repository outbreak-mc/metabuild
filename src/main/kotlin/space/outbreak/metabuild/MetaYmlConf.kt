package space.outbreak.metabuild

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.apache.commons.text.StringSubstitutor
import java.io.File


data class MetaYmlConf(
    val name: String = "%dirname%-%version%",
    @JsonProperty("pack-format")
    var packFormat: String = "1.20",
    var description: String = "Example pack\nv%version%",
    var placeholders: MutableMap<String, String> = mutableMapOf("version" to "1.0"),
) {
    @JsonIgnore
    fun getNameWithPlaceholders(): String {
        val substitutor = StringSubstitutor(placeholders, "%", "%", '\\')
        return substitutor.replace(name)
    }

    @JsonIgnore
    fun getNumberPackFormat(): Int? {
        if (packFormat.contains("."))
            return PackMcMeta.packFormatFromVersion(packFormat)
        return packFormat.toInt()
    }

    @JsonIgnore
    fun toMcMeta(): PackMcMeta {
        val meta = PackMcMeta(getNumberPackFormat()!!)
        meta.setMMDescription(description, placeholders = placeholders)
        return meta
    }

    companion object {
        fun fromFile(packYmlFile: File): MetaYmlConf {
            val meta = mapper.readValue(packYmlFile, MetaYmlConf::class.java)
            if ("dirname" !in meta.placeholders)
                meta.placeholders["dirname"] = packYmlFile.parentFile.name
            return meta
        }
    }
}
