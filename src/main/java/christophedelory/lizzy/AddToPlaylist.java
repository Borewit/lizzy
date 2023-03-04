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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import christophedelory.content.Content;
import christophedelory.content.type.ContentType;
import christophedelory.content.type.ContentTypesFileFilter;
import christophedelory.playlist.Media;
import christophedelory.playlist.Playlist;
import christophedelory.playlist.Sequence;
import christophedelory.playlist.SpecificPlaylist;
import christophedelory.playlist.SpecificPlaylistFactory;
import christophedelory.playlist.SpecificPlaylistProvider;
import christophedelory.playlist.m3u.M3U;
import christophedelory.playlist.plp.PLP;
import christophedelory.playlist.rss.RSSProvider;
import christophedelory.xml.Version;

/**
 * Adds URLs, files and/or directories to a single playlist.
 * @version $Revision: 92 $
 * @author Christophe Delory
 */
@SuppressWarnings("PMD.SystemPrintln")
public final class AddToPlaylist
{
    /**
     * Adds a given list of URLs, files and/or directories to a single playlist.
     * <br>
     * Usage:
     * <pre>
     * AddToPlaylist [options] &lt;input-file(s)&gt;
     * </pre>
     * @param args the program arguments.
     * @throws MalformedURLException if at least one of the input strings specifies an unknown protocol.
     * @throws IOException if an I/O exception occurs.
     */
    public static void main(final String[] args) throws Exception
    {
        // Initializes the current project version.
        Transcode.buildCurrentVersion("christophedelory/lizzy/VERSION"); // May throw Exception.
        System.err.println("*** Lizzy AddToPlaylist v" + Version.CURRENT);
        System.err.println("*** Copyright (c) 2008-2009, Christophe Delory");
        System.err.println();

        final AddToPlaylist program = new AddToPlaylist();
        final CmdLineParser parser = new CmdLineParser(program);

        try
        {
            parser.parseArgument(args);
        }
        catch (CmdLineException e)
        {
            System.err.println(e.getMessage());
            System.err.println();
            System.err.println("Adds URLs, files and/or directories to a (possibly new) single playlist");
            System.err.println("usage: AddToPlaylist [options] <input-file(s)>");
            parser.printUsage(System.err);
            System.err.println("Supported playlist provider types: " + program.getSupportedProviderIds());
            System.exit(1);
        }

        program.run(); // May throw Exception.
    }

    /**
     * The playlist type, if specified.
     */
    @Option(name="-t",usage="The output playlist type\nAllowed values: see below",metaVar="type")
    private String _type = null;

    /**
     * Specifies if the content metadata shall be fetched, if possible.
     */
    @Option(name="-m",usage="Fetch if possible the media content metadata")
    private boolean _fetchContentMetadata = false;

    /**
     * The sub-directories shall be recursively scanned.
     */
    @Option(name="-r",usage="Recursively add sub-directories contents")
    private boolean _recursive = false;

    /**
     * The output file or URL.
     */
    @Option(name="-o",usage="The output file or URL\nIf missing, a file save dialog is prompted\nIf the output playlist type is not specified (-t), it will be inferred from the output file name extension",metaVar="file/URL")
    private String _output = null;

    /**
     * Specifies that the marshalled M3U playlist must use the Extension M3U format.
     */
    @Option(name="-m3u:ext",usage="The output M3U playlist must use the Extension M3U format")
    private boolean _extM3U = false;

    /**
     * Specifies that the output RSS shall make use of the RSS Media extension.
     */
    @Option(name="-rss:media",usage="The output RSS playlist must use the RSS Media format")
    private boolean _useRSSMedia = false;

    /**
     * Specifies the disk identifier of the output PLP playlist.
     */
    @Option(name="-plp:disk",usage="The disk identifier of the output PLP playlist\nExamples: HARP, HDD",metaVar="disk")
    private String _diskSpecifier = null;

    /**
     * The list of input files or directories.
     */
    @Argument(usage="One or more files or directories to add to the output playlist",metaVar="input-files(s)",required=true)
    private ArrayList<String> _arguments = new ArrayList<>(); // NOPMD Avoid using implementation types; use the interface instead

    /**
     * The default no-arg constructor shall not be publicly available.
     */
    private AddToPlaylist()
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
     * @throws IOException if an I/O exception occurs.
     */
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
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

        String outputPath = null;
        File outputFile = null;
        URL outputUrl;

        if (_output == null)
        {
            final JFileChooser fc = new JFileChooser();
            fc.setDialogType(JFileChooser.SAVE_DIALOG); // Shall not throw IllegalArgumentException.
            fc.setDialogTitle("Lizzy");
            fc.setFileHidingEnabled(true);
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY); // Shall not throw IllegalArgumentException.
            fc.setMultiSelectionEnabled(false);
            fc.setApproveButtonText("Save playlist");
            //fc.setApproveButtonToolTipText();
            fc.setAcceptAllFileFilterUsed(true);

            if (provider == null)
            {
                final ContentTypesFileFilter allFilter = new ContentTypesFileFilter("All Playlists", false);
                final List<SpecificPlaylistProvider> providers = SpecificPlaylistFactory.getInstance().getProviders();

                for (SpecificPlaylistProvider p : providers)
                {
                    final ContentTypesFileFilter providerFilter = new ContentTypesFileFilter("All " + p.getId() + "-like", true);
                    providerFilter.setProvider(p);

                    for (ContentType type : p.getContentTypes())
                    {
                        final ContentTypesFileFilter typeFilter = new ContentTypesFileFilter(type.getDescription(), true);
                        typeFilter.setProvider(p);
                        fc.setFileFilter(typeFilter);

                        typeFilter.addContentType(type);
                        providerFilter.addContentType(type);
                        allFilter.addContentType(type);
                    }

                    if (providerFilter.getContentTypes().size() > 1)
                    {
                        fc.setFileFilter(providerFilter);
                    }
                }

                if (allFilter.getContentTypes().size() > 1)
                {
                    fc.setFileFilter(allFilter);
                }
            }
            else
            {
                final ContentTypesFileFilter providerFilter = new ContentTypesFileFilter("All " + provider.getId() + "-like", true);
                providerFilter.setProvider(provider);

                for (ContentType type : provider.getContentTypes())
                {
                    final ContentTypesFileFilter typeFilter = new ContentTypesFileFilter(type.getDescription(), true);
                    typeFilter.setProvider(provider);
                    fc.setFileFilter(typeFilter);

                    typeFilter.addContentType(type);
                    providerFilter.addContentType(type);
                }

                if (providerFilter.getContentTypes().size() > 1)
                {
                    fc.setFileFilter(providerFilter);
                }
            }

            final String userDir = System.getProperty("user.dir"); // May throw SecurityException. Shall not throw NullPointerException, IllegalArgumentException.

            if (userDir != null)
            {
                fc.setCurrentDirectory(new File(userDir));
            }

            if (fc.showDialog(null, null) != JFileChooser.APPROVE_OPTION) // May throw HeadlessException.
            {
                System.exit(2);
            }

            outputFile = fc.getSelectedFile();
            outputUrl = outputFile.toURI().toURL(); // May throw IllegalArgumentException, MalformedURLException.
            outputPath = outputFile.getName();

            if (provider == null)
            {
                final ContentTypesFileFilter f = (ContentTypesFileFilter) fc.getFileFilter(); // Shall not throw ClassCastException.
                provider = f.getProvider(); // May still be null.
            }
        }
        else
        {
            try
            {
                outputUrl = new URL(_output); // May throw MalformedURLException.
                outputPath = outputUrl.getPath(); // May still be null.

                // Go back to a real file whenever possible, because URLConnection does not support writing to files.
                if ("file".equals(outputUrl.getProtocol()))
                {
                    final URI uri = outputUrl.toURI(); // May throw URISyntaxException.
                    outputFile = new File(uri); // May throw IllegalArgumentException.
                }
            }
            catch (MalformedURLException e)
            {
                outputFile = new File(_output);
                outputUrl = outputFile.toURI().toURL(); // May throw IllegalArgumentException, MalformedURLException.
            }
        }

        if (outputFile != null)
        {
            outputFile = outputFile.getCanonicalFile(); // May throw SecurityException, IOException.
        }

        if (provider == null)
        {
            if (outputPath == null)
            {
                outputPath = _output;
            }

            provider = SpecificPlaylistFactory.getInstance().findProviderByExtension(outputPath);

            if (provider == null)
            {
                System.err.println("Unknown type of specific playlist <" + _output + '>');
                System.exit(1);
            }
        }

        Playlist playlist = new Playlist();

        try
        {
            final SpecificPlaylist inputSpecificPlaylist = SpecificPlaylistFactory.getInstance().readFrom(outputUrl); // May throw IOException.

            if (inputSpecificPlaylist == null)
            {
                System.err.println("ERROR: Invalid playlist format from URL <" + outputUrl + '>');
            }
            else
            {
                // Override the generic playlist.
                playlist = inputSpecificPlaylist.toPlaylist();
            }
        }
        catch (FileNotFoundException e)
        {
            System.err.println("WARNING: Cannot fetch URL <" + outputUrl + '>' + e);
        }
        catch (Exception e)
        {
            System.err.println("ERROR: Cannot read playlist from URL <" + outputUrl + '>' + e);
        }

        for (String arg : _arguments)
        {
            final File file = new File(arg);

            // Caution: the input string can also be an URL.
            // Check it now.
            if (file.exists())
            {
                // The file exists: begin the file/directory scan process.
                addToPlaylist(playlist.getRootSequence(), file, true, outputFile); // May throw SecurityException, IOException.
            }
            else
            {
                // Add it as it is, even if it's really a local file:
                // it has been explicitely specified through the command line or through the file chooser.
                final Media media = new Media();
                final Content content = new Content(arg);
                media.setSource(content);

                playlist.getRootSequence().addComponent(media);
            }
        }

        // Normalize the generic playlist, in case of an "append to an existing file".
        playlist.normalize();

        final FetchContentMetadata metadataVisitor = new FetchContentMetadata();
        metadataVisitor.setConnect(_fetchContentMetadata);
        playlist.acceptDown(metadataVisitor);

        if (provider instanceof RSSProvider)
        {
            ((RSSProvider) provider).setUseRSSMedia(_useRSSMedia);
        }

        final SpecificPlaylist outputSpecificPlaylist = provider.toSpecificPlaylist(playlist); // May throw Exception. Shall not throw NullPointerException because of playlist.

        if (outputSpecificPlaylist instanceof M3U)
        {
            ((M3U) outputSpecificPlaylist).setExtensionM3U(_extM3U);
        }

        if ((outputSpecificPlaylist instanceof PLP) && (_diskSpecifier != null))
        {
            ((PLP) outputSpecificPlaylist).setDiskSpecifier(_diskSpecifier);
        }

        OutputStream out;

        if (outputFile == null)
        {
            final URLConnection conn = outputUrl.openConnection(); // May throw IOException.
            conn.setAllowUserInteraction(true); // Shall not throw IllegalStateException.
            conn.setDoInput(false); // Shall not throw IllegalStateException.
            conn.setDoOutput(true); // Shall not throw IllegalStateException.
            conn.connect(); // May throw SocketTimeoutException, IOException.
            out = conn.getOutputStream(); // May throw IOException, UnknownServiceException.
        }
        else
        {
            out = new FileOutputStream(outputFile); // May throw FileNotFoundException, SecurityException.
        }

        outputSpecificPlaylist.writeTo(out, null); // May throw Exception.
        out.flush(); // May throw IOException.
        out.close(); // May throw IOException.
    }

    /**
     * Adds the specified file or directory, and optionally its sub-directories, to the input sequence.
     * @param sequence the playlist sequence to add to. Shall not be <code>null</code>.
     * @param file a file or directory. Shall not be <code>null</code>.
     * @param recurse specifies if the sub-directories of this directory shall be recursively scanned or not.
     * @param playlistFile an optional file to exclude from the sequence. May be <code>null</code>.
     * @throws NullPointerException if <code>sequence</code> is <code>null</code>.
     * @throws NullPointerException if <code>file</code> is <code>null</code>.
     * @throws SecurityException if a security manager exists and its {@link SecurityManager#checkRead(String)} method denies read access to a file.
     * @throws IOException if an I/O error occurs.
     */
    private void addToPlaylist(final Sequence sequence, final File file, final boolean recurse, final File playlistFile) throws IOException
    {
        if (file.isDirectory()) // Throws NullPointerException if file is null. May throw SecurityException.
        {
            if (recurse)
            {
                final File[] files = file.listFiles(); // May throw SecurityException.

                if (files != null)
                {
                    for (File child : files)
                    {
                        addToPlaylist(sequence, child, _recursive, playlistFile); // Throws NullPointerException if sequence is null. May throw SecurityException, IOException.
                    }
                }
            }
        }
        else if (file.isFile()) // May throw SecurityException.
        {
            boolean include = true;
            String filePath = file.getPath();

            if (playlistFile != null)
            {
                final File canonicalFile = file.getCanonicalFile(); // May throw IOException, SecurityException.

                if (canonicalFile.equals(playlistFile))
                {
                    include = false;
                }
                else
                {
                    // Try to make the playlist entry file name RELATIVE to the playlist file.
                    File parentFile = canonicalFile.getParentFile(); // Shall not be null, it is a file, not a directory.
                    final File playlistParentFile = playlistFile.getParentFile(); // Shall not be null, it is a file, not a directory.

                    if (parentFile.equals(playlistParentFile))
                    {
                        filePath = file.getName();
                    }
                    else
                    {
                        final StringBuilder sb = new StringBuilder(file.getName());
                        File previousFile = parentFile;
                        parentFile = previousFile.getParentFile();

                        while (parentFile != null)
                        {
                            sb.insert(0, '/'); // Shall not throw StringIndexOutOfBoundsException.
                            final String previousFileName = previousFile.getName();

                            if (!previousFileName.equals("/") && !previousFileName.equals("\\"))
                            {
                                sb.insert(0, previousFileName); // Shall not throw StringIndexOutOfBoundsException.
                            }

                            if (parentFile.equals(playlistParentFile))
                            {
                                filePath = sb.toString();
                                break;
                            }

                            previousFile = parentFile;
                            parentFile = previousFile.getParentFile();
                        }
                    }
                }
            }

            if (include)
            {
                final Media media = new Media();
                final Content content = new Content(filePath);
                media.setSource(content);

                sequence.addComponent(media); // Throws NullPointerException if sequence is null.
            }
        }
    }
}
