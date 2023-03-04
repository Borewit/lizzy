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

import static java.nio.charset.StandardCharsets.UTF_8;

import christophedelory.playlist.Playlist;
import christophedelory.playlist.SpecificPlaylist;
import christophedelory.playlist.SpecificPlaylistFactory;
import christophedelory.playlist.SpecificPlaylistProvider;
import christophedelory.playlist.m3u.M3U;
import christophedelory.playlist.plp.PLP;
import christophedelory.playlist.rss.RSSProvider;
import christophedelory.xml.Version;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * Converts a given playlist file to a specified format.
 * @since 0.2.0
 * @version $Revision: 92 $
 * @author Christophe Delory
 */
@SuppressWarnings("PMD.SystemPrintln")
public final class Transcode
{
    /**
     * Initializes the {@link Version#CURRENT current} project version.
     * @param resourceName the name of the version resource; "package1/VERSION" for example. Shall not be <code>null</code>.
     * @throws SecurityException if a security manager exists and its <code>checkPermission</code> method denies access to the class loader of this class.
     * @throws java.io.IOException if an I/O exception occurs.
     * @throws IllegalArgumentException if the version string is malformed.
     * @throws IndexOutOfBoundsException if one of the numbers is strictly negative.
     * @throws NumberFormatException if the version string contains a non-parsable integer.
     * @see Version#CURRENT
     * @see Version#valueOf
     */
    public static void buildCurrentVersion(final String resourceName) throws Exception
    {
        final URL url = Version.class.getClassLoader().getResource(resourceName); // May throw SecurityException. Throws NullPointerException if resourceName is null.

        if (url != null) {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), UTF_8)); // May throw IOException. Throws NullPointerException if url is null.

            final String version = reader.readLine(); // May throw IOException.
            Version.CURRENT = Version.valueOf(version); // Throws NullPointerException if version is null. Should not throw IllegalArgumentException, IndexOutOfBoundsException, NumberFormatException.
        } else {
            Version.CURRENT = Version.valueOf("0.0.0");
        }
    }

    /**
     * Converts an input playlist to the specified playlist format.
     * <br>
     * Usage:
     * <pre>
     * Transcode [options] &lt;input-playlist&gt;
     * </pre>
     * @param args the program arguments.
     * @throws MalformedURLException if at least one of the input strings specifies an unknown protocol.
     * @throws java.io.IOException if an I/O exception occurs.
     */
    public static void main(final String[] args) throws Exception
    {
        // Initializes the current project version.
        Transcode.buildCurrentVersion("christophedelory/lizzy/VERSION"); // May throw Exception.
        System.err.println("*** Lizzy Transcode v" + Version.CURRENT);
        System.err.println("*** Copyright (c) 2008-2009, Christophe Delory");

        final Transcode program = new Transcode();
        final CmdLineParser parser = new CmdLineParser(program);

        try
        {
            parser.parseArgument(args);
        }
        catch (CmdLineException e)
        {
            System.err.println();
            System.err.println(e.getMessage());
            System.err.println();
            System.err.println("Converts an input playlist to a (possibly new) format");
            System.err.println("usage: Transcode [options] <input-playlist>");
            parser.printUsage(System.err);
            System.err.println("Supported playlist provider types: " + program.getSupportedProviderIds());
            System.exit(1);
        }

        program.run(); // May throw Exception.
    }

    /**
     * The playlist type, if specified.
     */
    @Option(name="-t",usage="The output playlist type\nAllowed values: see below\nIf missing, the input playlist type is used",metaVar="type")
    private volatile String _type = null;

    /**
     * Specifies if the intermediate generic playlist shall be displayed or not.
     */
    @Option(name="-g",usage="Show the intermediate generic playlist")
    private volatile boolean _showGenericPlaylist = false;

    /**
     * Specifies if the (parsed) input playlist shall be displayed or not.
     */
    @Option(name="-i",usage="Show the parsed input playlist")
    private volatile boolean _showInputPlaylist = false;

    /**
     * Specifies if the content metadata shall be fetched, if possible.
     */
    @Option(name="-m",usage="Fetch if possible the media content metadata")
    private volatile boolean _fetchContentMetadata = false;

    /**
     * The output file or URL.
     */
    @Option(name="-o",usage="The output file or URL\nIf missing, stdout is used\nIf the output playlist type is not specified (-t), it will be inferred from the output file name extension",metaVar="file/URL")
    private volatile String _output = null;

    /**
     * Specifies that the marshalled M3U playlist must use the Extension M3U format.
     */
    @Option(name="-m3u:ext",usage="The output M3U playlist must use the Extension M3U format")
    private volatile boolean _extM3U = false;

    /**
     * Specifies that the output RSS shall make use of the RSS Media extension.
     */
    @Option(name="-rss:media",usage="The output RSS playlist must use the RSS Media format")
    private volatile boolean _useRSSMedia = false;

    /**
     * Specifies the disk identifier of the output PLP playlist.
     */
    @Option(name="-plp:disk",usage="The disk identifier of the output PLP playlist\nExamples: HARP, HDD",metaVar="disk")
    private volatile String _diskSpecifier = null;

    /**
     * The input file or URL.
     */
    @Argument(usage="The input playlist file or URL",metaVar="input-playlist",required=true)
    private volatile ArrayList<String> _arguments = new ArrayList<String>(); // NOPMD Avoid using implementation types; use the interface instead

    /**
     * The default no-arg constructor shall not be publicly available.
     */
    private Transcode()
    {
    }

    /**
     * Returns a string representation of all supported {@link SpecificPlaylistProvider playlist providers}, through their identifier.
     * @return a string representing the list of all supported playlist providers. Shall not be <code>null</code>.
     * @see SpecificPlaylistProvider#getId
     */
    public String getSupportedProviderIds()
    {
        final StringBuilder sb = new StringBuilder();
        boolean first = true;

        for (SpecificPlaylistProvider provider : SpecificPlaylistFactory.getInstance().getProviders())
        {
            if (!first)
            {
                sb.append('/');
            }

            first = false;
            sb.append(provider.getId());
        }

        return sb.toString();
    }

    /**
     * Executes the required actions.
     * @throws MalformedURLException if at least one of the input strings specifies an unknown protocol.
     * @throws java.io.IOException if an I/O exception occurs.
     */
    private void run() throws Exception
    {
        SpecificPlaylistProvider provider = null;

        if (_type != null)
        {
            provider = SpecificPlaylistFactory.getInstance().findProviderById(_type); // Shall not throw NullPointerException because of _type.

            if (provider == null)
            {
                System.err.println("Unknown specific playlist type <" + _type + '>');
                System.exit(1);
            }
        }

        OutputStream out = null;

        if (_output != null)
        {
            String path = null;

            try
            {
                final URL url = new URL(_output); // May throw MalformedURLException.
                path = url.getPath(); // May still be null.
                final URLConnection conn = url.openConnection(); // May throw IOException.
                conn.setAllowUserInteraction(true); // Shall not throw IllegalStateException.
                conn.setDoInput(false); // Shall not throw IllegalStateException.
                conn.setDoOutput(true); // Shall not throw IllegalStateException.
                conn.connect(); // May throw SocketTimeoutException, IOException.
                out = conn.getOutputStream(); // May throw IOException, UnknownServiceException.
            }
            catch (MalformedURLException e)
            {
                final File file = new File(_output);
                out = new FileOutputStream(file); // May throw FileNotFoundException, SecurityException.
            }

            if (provider == null)
            {
                if (path == null) // NOPMD Deeply nested if..then statements are hard to read
                {
                    path = _output;
                }

                provider = SpecificPlaylistFactory.getInstance().findProviderByExtension(path);

                if (provider == null) // NOPMD Deeply nested if..then statements are hard to read
                {
                    System.err.println("Unknown type of specific playlist <" + _output + '>');
                    System.exit(1);
                }
            }
        }

        final FetchContentMetadata metadataVisitor = new FetchContentMetadata();
        metadataVisitor.setConnect(_fetchContentMetadata);

        if (_arguments.size() > 1)
        {
            System.err.println("Multiple input playlists specified. Only the first one is analyzed.");
        }

        final String arg = _arguments.get(0); // Shall not throw IndexOutOfBoundsException.
        URL url;

        try
        {
            url = new URL(arg); // May throw MalformedURLException.
        }
        catch (MalformedURLException e)
        {
            final File file = new File(arg);
            url = file.toURI().toURL(); // May throw IllegalArgumentException, MalformedURLException.
        }

        final SpecificPlaylist inputSpecificPlaylist = SpecificPlaylistFactory.getInstance().readFrom(url); // May throw IOException. Shall not throw NullPointerException because of url.

        if (inputSpecificPlaylist == null)
        {
            System.err.println("Invalid playlist format from URL <" + url + '>');
            System.exit(2);
        }

        if (_showInputPlaylist)
        {
            System.err.println("Input playlist " + inputSpecificPlaylist);
            inputSpecificPlaylist.writeTo(System.err, null); // May throw Exception. Shall not throw NullPointerException because of System.err.
            System.err.println();
        }

        final Playlist playlist = inputSpecificPlaylist.toPlaylist();
        playlist.acceptDown(metadataVisitor);

        if (_showGenericPlaylist)
        {
            System.err.println("Intermediate generic playlist:");
            final PlaylistToString visitor = new PlaylistToString();
            //visitor.setDebug(true);
            playlist.acceptDown(visitor);
            System.err.println(visitor);
            System.err.println();
        }

        SpecificPlaylistProvider tmpProvider = provider;

        if (tmpProvider == null)
        {
            tmpProvider = inputSpecificPlaylist.getProvider();
        }

        if (tmpProvider instanceof RSSProvider)
        {
            ((RSSProvider) tmpProvider).setUseRSSMedia(_useRSSMedia);
        }

        final SpecificPlaylist outputSpecificPlaylist = tmpProvider.toSpecificPlaylist(playlist); // May throw Exception. Shall not throw NullPointerException because of playlist.

        if (outputSpecificPlaylist instanceof M3U)
        {
            ((M3U) outputSpecificPlaylist).setExtensionM3U(_extM3U);
        }

        if ((outputSpecificPlaylist instanceof PLP) && (_diskSpecifier != null))
        {
            ((PLP) outputSpecificPlaylist).setDiskSpecifier(_diskSpecifier);
        }

        System.err.println("Output playlist " + outputSpecificPlaylist);
        OutputStream tmpOut = out;

        if (tmpOut == null)
        {
            tmpOut = System.out;
        }

        outputSpecificPlaylist.writeTo(tmpOut, null); // May throw Exception.
        tmpOut.flush(); // May throw IOException.

        if (out != null)
        {
            out.close(); // May throw IOException.
        }
    }
}
