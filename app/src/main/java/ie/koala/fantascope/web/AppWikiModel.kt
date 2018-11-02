package ie.koala.fantascope.web

import info.bliki.wiki.model.Configuration
import info.bliki.wiki.model.WikiModel

class AppWikiModel : WikiModel("assets://images/\${image}", "assets://wiki/\${title}.wiki") {
    companion object {

        init {
            Configuration().addTokenTag("info", InfoTag())
        }
    }

}
