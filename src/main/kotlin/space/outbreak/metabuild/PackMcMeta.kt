package space.outbreak.metabuild

import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer
import org.apache.commons.text.StringSubstitutor

data class PackMcMeta(
    var pack_format: Int,
) {
    var description = ""

    companion object {
        fun packFormatFromVersion(version: String): Int? {
            return mapOf(
                "1.5" to 1,
                "1.5.1" to 1,
                "1.5.2" to 1,
                "1.6" to 1,
                "1.6.1" to 1,
                "1.6.2" to 1,
                "1.6.3" to 1,
                "1.6.4" to 1,
                "1.7" to 1,
                "1.7.1" to 1,
                "1.7.2" to 1,
                "1.7.10" to 1,
                "1.8" to 1,
                "1.8.1" to 1,
                "1.8.2" to 1,
                "1.8.4" to 1,
                "1.8.5" to 1,
                "1.8.6" to 1,
                "1.8.8" to 1,
                "1.8.8" to 1,
                "1.8.9" to 1,
                "1.9" to 2,
                "1.9.1" to 2,
                "1.9.2" to 2,
                "1.9.3" to 2,
                "1.9.4" to 2,
                "1.10" to 2,
                "1.10.1" to 2,
                "1.10.2" to 2,
                "1.11" to 3,
                "1.11.1" to 3,
                "1.11.2" to 3,
                "1.12" to 3,
                "1.12.1" to 3,
                "1.12.2" to 3,
                "1.13" to 4,
                "1.13.1" to 4,
                "1.13.2" to 4,
                "1.14" to 4,
                "1.14.1" to 4,
                "1.14.2" to 4,
                "1.14.3" to 4,
                "1.14.4" to 4,
                "1.15" to 5,
                "1.15.1" to 5,
                "1.16" to 5,
                "1.16.1" to 5,
                "1.16.2" to 6,
                "1.16.3" to 6,
                "1.16.4" to 6,
                "1.16.5" to 6,
                "1.17" to 7,
                "1.17.1" to 7,
                "1.18" to 8,
                "1.18.1" to 8,
                "1.18.2" to 8,
                "1.19" to 9,
                "1.19.1" to 9,
                "1.19.2" to 9,
                "1.19.3" to 12,
                "1.19.4" to 13,
                "1.20" to 15,
                "1.20.1" to 15,
                "1.20.2" to 18,
                "1.20.3" to 18,
                "1.20.4" to 18,
            )[version]
        }

        fun mmStrToJson(
            miniMessageString: String,
            escape: Boolean = false,
            placeholders: Map<String, String> = mapOf(),
        ): String {
            val substitutor = StringSubstitutor(placeholders, "%", "%", '\\')
            val input = substitutor.replace(miniMessageString)
                .replace("\r", "")
                .replace("\n", "<newline>")

            val comp = MiniMessage.miniMessage().deserialize(input)
            var json = JSONComponentSerializer.json().serialize(comp)

            if (escape)
                json = json.replace("\"", "\\\"")

            return json
        }
    }

    fun setMMDescription(miniMessageString: String, placeholders: Map<String, String> = mapOf()) {
        description = "[${
            mmStrToJson(
                miniMessageString,
                escape = false,
                placeholders = placeholders
            )
        }]"
    }

    fun generatePackMcMetaJsonString(): String {
        return "{\n" +
                " \"pack\": {\n" +
                "  \"pack_format\": ${pack_format},\n" +
                "  \"description\": ${description}\n" +
                " }\n" +
                "}\n"
    }
}
