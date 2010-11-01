//
// $Id$

package memory.client;

import com.google.gwt.dom.client.Document;
import com.threerings.gwt.util.WikiParser;

import memory.data.Datum;

/**
 * Handles wiki formatting.
 */
public class WikiUtil
{
    /**
     * Formats a full wiki page.
     */
    public static String format (String cortexId, Datum parent, String text)
    {
        return new MemoryWikiParser(cortexId, parent).format(text);
    }

    /**
     * Formats a wiki snippet, which should contain no block commands.
     */
    public static String formatSnippet (String cortexId, Datum parent, String text)
    {
        return new MemoryWikiParser(cortexId, parent).formatSnippet(text);
    }

    protected static class MemoryWikiParser extends WikiParser
    {
        public MemoryWikiParser (String cortexId, Datum parent) {
            _cortexId = cortexId;
            _parent = parent;
        }

        public String format (String text) {
            return doRender(text);
        }

        public String formatSnippet (String text) {
            return doRenderSnippet(text);
        }

        protected void appendInternalLink (String uri, String text) {
            String path = "/c/" + _cortexId + "/" + _parent.id + "/" + uri;

            // preserve the query string to make life in GWT devmode easier
            String url = Document.get().getURL();
            int qidx = url.indexOf("?");
            if (qidx != -1) {
                path = path + url.substring(qidx);
            }

            sb.append("<a href=\"" + path + "\">");
            appendText(text);
            sb.append("</a>");
        }

        protected String _cortexId;
        protected Datum _parent;
    }
}