/*
 * Copyright (c) 2008, Christophe Delory
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY CHRISTOPHE DELORY ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CHRISTOPHE DELORY BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package christophedelory.lizzy;

import java.util.List;

import christophedelory.content.type.ContentType;
import christophedelory.player.PlayerSupport;
import christophedelory.playlist.SpecificPlaylistFactory;
import christophedelory.playlist.SpecificPlaylistProvider;

/**
 * Various representations of a {@link ContentType content type}.
 * @since 0.2.0
 * @version $Revision: 91 $
 * @author Christophe Delory
 */
public final class ContentTypeInfo
{
    /**
     * The line separator.
     */
    private static final String LINE_SEPARATOR = System.getProperty("line.separator"); // May throw SecurityException. Shall not throw NullPointerException, IllegalArgumentException.

    /**
     * Returns a string representation of the input content type.
     * @param contentType a content type. Shall not be <code>null</code>.
     * @return a string representing the specified content type. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>contentType</code> is <code>null</code>.
     */
    public static String toString(final ContentType contentType)
    {
        final StringBuilder sb = new StringBuilder(contentType.getDescription()); // Throws NullPointerException if contentType is null.
        sb.append(LINE_SEPARATOR);
        sb.append("  Typical file name extension(s) [case-insensitive]: ");
        boolean first = true;

        for (String ext : contentType.getExtensions())
        {
            if (!first)
            {
                sb.append(' ');
            }

            first = false;
            sb.append(ext);
        }

        sb.append(LINE_SEPARATOR);
        sb.append("  MIME type(s): ");
        first = true;

        for (String mimeType : contentType.getMimeTypes())
        {
            if (!first)
            {
                sb.append(' ');
            }

            first = false;
            sb.append(mimeType);
        }

        sb.append(LINE_SEPARATOR);
        sb.append("  Supported player(s): ");
        first = true;

        for (PlayerSupport playerSupport : contentType.getPlayerSupports())
        {
            if (!first)
            {
                sb.append(' ');
            }

            first = false;
            sb.append(PlayerSupport.toString(playerSupport.getPlayer()));
            sb.append(" [R");

            if (playerSupport.isSaved())
            {
                sb.append('W');
            }

            sb.append(']');
        }

        sb.append(LINE_SEPARATOR);

        return sb.toString();
    }

    /**
     * Returns an HTML fragment representing the input content type.
     * @param contentType a content type. Shall not be <code>null</code>.
     * @return an HTML fragment representing the specified content type. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>contentType</code> is <code>null</code>.
     */
    public static String toHTML(final ContentType contentType)
    {
        final StringBuilder sb = new StringBuilder("<tr valign=top><td>");
        sb.append(contentType.getDescription()); // Throws NullPointerException if contentType is null.

        sb.append("</td><td><code>");
        boolean first = true;

        for (String ext : contentType.getExtensions()) // Throws NullPointerException if contentType is null.
        {
            if (!first)
            {
                sb.append(' ');
            }

            first = false;
            sb.append(ext);
        }

        sb.append("</code></td><td><code>");
        first = true;

        for (String mimeType : contentType.getMimeTypes())
        {
            if (!first)
            {
                sb.append("<br>");
            }

            first = false;
            sb.append(mimeType);
        }

        sb.append("</code></td><td>");
        first = true;

        for (PlayerSupport playerSupport : contentType.getPlayerSupports())
        {
            if (!first)
            {
                sb.append("<br>");
            }

            first = false;
            sb.append(PlayerSupport.toString(playerSupport.getPlayer()));
            sb.append(" [R");

            if (playerSupport.isSaved())
            {
                sb.append('W');
            }

            sb.append(']');
        }

        sb.append("</td></tr>");

        return sb.toString();
    }

    /**
     * The test program entry point.
     * @param args the input parameters.
     */
    public static void main(final String[] args)
    {
        final List<SpecificPlaylistProvider> providers = SpecificPlaylistFactory.getInstance().getProviders();
        final boolean txt = (args.length > 0);

        final StringBuilder sb = new StringBuilder();

        if (txt)
        {
            sb.append("PLAYLIST FORMATS:");
            sb.append(LINE_SEPARATOR);
        }
        else
        {
            sb.append("<html><head><title>Playlist formats</title></head><body><table border=1 cellpadding=4 cellspacing=0><thead><tr valign=top><th>Playlist type</th><th>Typical file name extension(s) [case-insensitive]</th><th>MIME type(s)</th><th>Supported player(s)</th></tr></thead><tbody>");
        }

        for (SpecificPlaylistProvider provider : providers)
        {
            for (ContentType type : provider.getContentTypes())
            {
                if (txt)
                {
                    sb.append(toString(type));
                }
                else
                {
                    sb.append(toHTML(type));
                }
            }
        }

        if (!txt)
        {
            sb.append("</tbody></table></body></html>");
        }

        System.out.println(sb); // NOPMD System.out.print is used
    }

    /**
     * The default no-arg constructor shall not be accessible.
     */
    private ContentTypeInfo()
    {
    }
}
