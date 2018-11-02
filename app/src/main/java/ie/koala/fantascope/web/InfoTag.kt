package ie.koala.fantascope.web

import ie.koala.fantascope.app.FantascopeApplication
import info.bliki.wiki.filter.ITextConverter
import info.bliki.wiki.model.IWikiModel
import info.bliki.wiki.tags.HTMLTag
import info.bliki.wiki.tags.util.INoBodyParsingTag
import java.io.IOException
import java.util.*

class InfoTag : HTMLTag("info"), INoBodyParsingTag {

    @Throws(IOException::class)
    override fun renderHTML(converter: ITextConverter, buf: Appendable, model: IWikiModel) {
        val node = this
        val tagAtttributes = node.attributes
        val keysSet = tagAtttributes.keys
        buf.append("<p>")
        for (str in keysSet) {
            when (str) {
                "timestamp" -> buf.append(FantascopeApplication.versionBuildTimestamp)
                "name" -> buf.append(FantascopeApplication.versionName)
                "code" -> buf.append(FantascopeApplication.versionCode)
                "githash" -> buf.append(FantascopeApplication.versionGitHash)
            }
        }
        buf.append("</p>")
    }

    override fun isAllowedAttribute(attName: String?): Boolean {
        return ALLOWED_ATTRIBUTES_SET.contains(attName)
    }

    companion object {
        val ALLOWED_ATTRIBUTES_SET = HashSet<String>()
        val ALLOWED_ATTRIBUTES = arrayOf("timestamp", "name", "code", "githash")

        init {
            for (i in ALLOWED_ATTRIBUTES.indices) {
                ALLOWED_ATTRIBUTES_SET.add(ALLOWED_ATTRIBUTES[i])
            }
        }
    }

}
