[![CI](https://github.com/Borewit/lizzy/actions/workflows/ci.yml/badge.svg)](https://github.com/Borewit/lizzy/actions/workflows/ci.yml)

# Lizzy

Lizzy is an open source Java library allowing to parse, create, edit, 
convert and save almost any type of [multimedia playlist](https://en.wikipedia.org/wiki/Playlist) multimedia playlist.

The currently supported formats are the following: M3U/M3U8/M4U/RAM, ASX/WMX/WVX/WAX, WPL, XSPF, SMIL, RMP, PLS, B4S/BPL, RSS/MediaRSS, Atom, PLIST, MPCPL, Hypetape, KPL, PLA and PLP (see details <a href="docs/formats.md">here</a>).

Two versatile command-line tools are also available (see the [Getting started](#Getting started) guide).

## Table of contents

1. [Features](#features)
1. [License](#license)
1. [Download](#download)
1. [Documentation](#documentation)
1. [Getting started](#getting-started)
1. [Details](#details) 
1. [Build](#build)
1. [Playlist information](#playlist) 
1. [Other solutions](#other-solutions)
1. [Third-party libraries](#third-party-libraries)

## Features

There are so many types of media playlists, in different formats: plain text, XML, Windows .INI or binary.
I have the need to share, and thus often convert playlist files for different media players.
Why not you?
That's why I launched this project, mid-2008.

Here follows a list of the major features of Lizzy:

* Wide and extensible playlist support (see the detailed list of <a href="docs/formats.md">currently supported playlist formats</a>)</li>
* Automatic detection of the type of input playlist</li>
* Information on playlist format support by some media players (input and output)</li>
* Physical access to the individual contents of a playlist is not required (performed on demand)</li>
* Information on playlist items (media contents): MIME type, length, duration, width/height</li>
* Powerful yet simple generic playlist representation</li>
* Direct handling of a given specific playlist type (eg. ASX, WPL) is possible</li>
* Playlist normalization (empty items removal, simplification, reduction)</li>

## License
Lizzy is licensed through a BSD-like licensing model: see the text [here]<(LICENSE.txt).

## Download
Check the [GitHub releases](https://github.com/Borewit/lizzy/releases) page.

## Documentation
* The detailed list of currently supported playlist formats is presented [here](docs/formats.md)
* The Lizzy library [Java reference documentation](docs/javadoc/index.html) (javadoc)
* The project version history is <a href="CHANGES">here</a></li>
* Some <a href="docs/dtd/">DTDs</a> and <a href="docs/schema/">XML Schemas</a> that you may find useful</li>
* The <a href="docs/licenses/">licences</a> (and binaries) of the third-party libraries used at run-time</li>

## Getting started
First you have to download and install the Sun Java Runtime Environment (JRE) 6 (<a href="https://java.sun.com/javase/downloads">https://java.sun.com/javase/downloads</a>), or any compatible JRE.

Then, two choices, depending on whether you just want to <b>use</b> the Lizzy tools, or you want to <b>develop</b> a bigger project using in particular the Lizzy API:

* <u>User</u>: open a command prompt (Windows) or a shell terminal (Unix-like).
  Change the directory to the Lizzy binary distribution root.
  
  The usage of the Lizzy tools can be obtained through the following commands:
  * `Transcode` (Windows) or `sh Transcode.sh` (Unix). 
     This tool converts an input playlist to a (possibly new) format.
     A copy of the last recent usage is located [here](docs/Transcode.txt).
    
     Example: `Transcode -o new-playlist.xspf old-playlist.m3u`.
  * `AddToPlaylist` (Windows) or `sh AddToPlaylist.sh` (Unix). This tool adds URLs, files and/or directories to a (possibly new) single playlist.
     A copy of the last recent usage is located [here](docs/AddToPlaylist.txt)here. 
     
     Example: `AddToPlaylist -o new-playlist.asx music-directory this-file.mp3 anotherfile.wav`.

* **CAUTION**: in order to use FFMPEG as content metadata provider, you have to put the needed dynamic libraries in the "`lib`" folder:
  * On a Windows platform, they are "`avcodec-51.dll`", "`avformat-51.dll`" and "`avutil-49.dll`" (you can find them [here](https://arrozcru.no-ip.org));
  * On a Linux platform, they are "`libavcodec.so`", "`libavformat.so`" and "`libavutil.so`".</li>
* <u>Developer</u>: a sample introducing the main API methods is described [here](docs/javadoc/christophedelory/playlist/package-summary.html#package_description).
  For a more complete usage, the best is to refer to the source code of the [Transcode](docs/javadoc/christophedelory/lizzy/Transcode.html) or [AddToPlaylist](docs/javadoc/christophedelory/lizzy/AddToPlaylist.html) class, i.e. the command-line tools.

## Details

The library is built on the notion of service provider interface (<a href="https://en.wikipedia.org/wiki/Service_Provider_Interface">SPI</a>):
each "specific" playlist format (eg. ASX or SMIL) is associated with its own playlist provider implementation, through a generic interface.
Thus one can easily add, remove or modify a given playlist support.

At the center of the API lays a format-agnostic description of the playlist.
This "generic" playlist representation allows exchanges between different playlist providers, while retaining as much as possible their specificities.

Some design elements can be found <a href="docs/javadoc/christophedelory/playlist/package-summary.html#package_description">there</a>.
The Java design involves some well-known design patterns, such as the visitor, the factory, the facade, etc.

The main technologies involved in this project are the following:
* Java 2 SE (including the [Service Provider Interface](https://java.sun.com/developer/technicalArticles/javase/extensible/index.html"), [JAXB](https://java.sun.com/xml/jaxb/), [JavaSound](https://java.sun.com/products/java-media/sound/) and [ImageIO](https://java.sun.com/javase/6/docs/technotes/guides/imageio/index.html) facilities)
* Castor, a Java &harr; XML binding framework</li>
* Apache Ant
* Xdoclet 2
* XML, RSS, Atom<
* FFMPEG

Please refer to &ldquo;<a href="#Third-party libraries">Third-party libraries</a>&rdquo; for more details and credits.

XML playlists are marshalled (created) and unmarshalled (parsed) through 3 different means:

1. Through [JAXB](https://java.sun.com/xml/jaxb): see the RMP format.</li>
1. By using directly any available XML parser: see the KPL format.</li>
1. Through the [Castor framework](https://www.castor.org/): see all other XML-based formats.</li>

## Build
In order to build Lizzy from the sources, you first have to download and install the following tools:

1. Install Java SDK 15 (may work with other versions as well)
1. Install [Maven](https://maven.apache.org/download.cgi)
1. You may have to set `JAVA_HOME` and directory `bin` folder of the Maven installation to your [PATH System variable](https://en.wikipedia.org/wiki/PATH_(variable)).

Execute the following command in order to build the distribution:
```shell
mvn clean install
```

## Playlist information
You will find here different Web links to playlist-related information, that have been of particular interest during my work:
        <ul>
            <li>General information:</li>
            <ul>
                <li><a href="https://gonze.com/playlists/playlist-format-survey.html">A survey of playlist formats</a></li>
                <li><a href="https://en.wikipedia.org/wiki/Playlist">Playlist on Wikipedia</a></li>
                <li><a href="https://wiki.videolan.org/Playlist">Playlist on VideoLAN Wiki</a></li>
                <li><a href="https://www.streamalot.com/playlists.shtml">Streamalot Playlists</a></li>
                <li><a href="https://schworak.com/programming/music/">Schworak: Music And Sound</a></li>
                <li><a href="https://docs.wasabidev.org/wasabi_developer_manual/winamp_playlists_and_playlist_directory.php#playlists_formats">Wasabi Developer Manual: Winamp And Wasabi: Playlists and the Playlist Directory Service</a></li>
            </ul>
            <li>ASX:</li>
            <ul>
                <li><a href="https://msdn.microsoft.com/en-us/library/bb249663(VS.85).aspx">Windows Media Metafile Elements Reference</a></li>
                <li><a href="https://msdn.microsoft.com/en-us/library/ms910265.aspx">ASX Elements Reference</a></li>
            </ul>
            <li>Atom:</li>
            <ul>
                <li><a href="https://atompub.org/rfc4287.html">The Atom Syndication Format</a>
            </ul>
            <li>Hypetape:</li>
            <ul>
                <li><a href="https://lists.musicbrainz.org/pipermail/playlist/2008-December/002105.html">Hypetape's playlist format</a>
            </ul>
            <li>M3U:</li>
            <ul>
                <li><a href="https://forums.winamp.com/showthread.php?threadid=65772">WINAMP.COM M3U and PLS Specification</a></li>
                <li><a href="https://hanna.pyxidis.org/tech/m3u.html">The M3U (.m3u) Playlist File Format</a></li>
            </ul>
            <li>PLA:</li>
            <ul>
                <li><a href="https://personal.inet.fi/koti/phintsan/iriver-t50.html">Playlist format specification for iriver T50</a></li>
            </ul>
            <li>PLIST:</li>
            <ul>
                <li><a href="https://www.xml.com/pub/a/2004/11/03/itunes.html">XML.com: Hacking iTunes</a></li>
                <li><a href="https://developer.apple.com/documentation/CoreFoundation/Conceptual/CFPropertyLists/CFPropertyLists.html">Introduction to Property List Programming Topics for Core Foundation</a></li>
            </ul>
            <li>RSS / Media RSS:</li>
            <ul>
                <li><a href="https://cyber.law.harvard.edu/rss/index.html">RSS 2.0 at Harvard Law</a></li>
                <li><a href="https://www.rssboard.org/rss-specification">RSS 2.0 Specification (RSS Advisory Board)</a></li>
                <li><a href="https://search.yahoo.com/mrss">Media RSS Module - RSS 2.0 Module (Yahoo!)</a></li>
                <li><a href="https://www.feedforall.com/mediarss.htm">Media RSS Namespace Extension</a></li>
            </ul>
            <li>SMIL:</li>
            <ul>
                <li><a href="https://www.w3.org/AudioVideo/">W3C Synchronized Multimedia</a></li>
            </ul>
            <li>WPL:</li>
            <ul>
                <li><a href="https://msdn.microsoft.com/en-us/library/bb249685.aspx">Windows Media Playlist Elements Reference</a></li>
            </ul>
            <li>XSPF:</li>
            <ul>
                <li><a href="https://xspf.org/">XSPF: XML Shareable Playlist Format</a></li>
            </ul>
        </ul>

## Other solutions
I haven't found yet any similar API, both open source and extensible, supporting such a wide range of disparate formats (from the simple M3U format to the rich SMIL format), and still easy to use.
If you think you have one, or know one, please contact me: I would be very pleased to list you there.

## Third-party libraries
The next table describes all 3d-party libraries necessary to use the Lizzy library.
All the associated license files may be found in the directory <code><a href="docs/licenses/">docs/licenses</a></code> of the binary project distribution.</p>

<table border="1" cellpadding="4" cellspacing="0">
    <thead>
        <tr valign="top">
            <th>Component</th>
            <th>Version</th>
            <th>Web</th>
            <th>Specific notices</th>
        </tr>
    </thead>
    <tbody>
        <tr valign="top">
            <td>Castor</td>
            <td>1.3.1</td>
            <td><a href="https://www.castor.org/">www.castor.org</a></td>
            <td>Copyright 2004-2005 Werner Guttmann</td>
        </tr>
        <tr valign="top">
            <td>Apache Commons Logging</td>
            <td>1.1.1</td>
            <td><a href="https://commons.apache.org/logging/">commons.apache.org/logging</a></td>
            <td>This product includes software developed by The Apache Software Foundation (<a href="https://www.apache.org/">https://www.apache.org/</a>)</td>
        </tr>
        <tr valign="top">
            <td>FFMPEG-Java</td>
            <td>20070916-0351</td>
            <td><a href="https://fmj-sf.net/ffmpeg-java/getting_started.php">fmj-sf.net/ffmpeg-java/getting_started.php</a></td>
            <td>-</td>
        </tr>
        <tr valign="top">
            <td>JNA (Java Native Access)</td>
            <td>3.0</td>
            <td><a href="https://jna.dev.java.net/">jna.dev.java.net</a></td>
            <td>-</td>
        </tr>
        <tr valign="top"><th colspan="4">Used only by the Lizzy command-line tools:</th></tr>
        <tr valign="top">
            <td>args4j</td>
            <td>2.0.12</td>
            <td><a href="https://args4j.dev.java.net/">args4j.dev.java.net</a></td>
            <td>Copyright (c) 2003, Kohsuke Kawaguchi. All rights reserved.</td>
        </tr>
    </tbody>
</table>

The table below describes the additional 3d-party tools and libraries used during the development process (in particular to build the distribution).
In each mentioned directory, you will find the associated license.

<table border="1" cellpadding="4" cellspacing="0">
    <thead>
        <tr valign="top">
            <th>Component</th>
            <th>Version</th>
            <th>Web</th>
            <th>Directory</th>
            <th>Comment</th>
        </tr>
    </thead>
    <tbody>
        <tr valign="top"><th colspan="5">Ant-related:</th></tr>
        <tr valign="top">
            <td>Apache Ant</td>
            <td>1.8.1</td>
            <td><a href="https://ant.apache.org/">ant.apache.org</a></td>
            <td>-</td>
            <td>-</td>
        </tr>
        <tr valign="top">
            <td>XDoclet 2</td>
            <td>2.0.6</td>
            <td><a href="https://xdoclet.codehaus.org/">xdoclet.codehaus.org</a></td>
            <td><code>ext/xdoclet2</code></td>
            <td>-</td>
        </tr>
        <tr valign="top">
            <td>Apache Commons Beanutils</td>
            <td>1.7.0</td>
            <td><a href="https://commons.apache.org/beanutils/">commons.apache.org/beanutils</a></td>
            <td><code>ext/xdoclet2</code></td>
            <td>Used by XDoclet 2</td>
        </tr>
        <tr valign="top">
            <td>Apache Commons Collections</td>
            <td>3.2</td>
            <td><a href="https://commons.apache.org/collections/">commons.apache.org/collections</a></td>
            <td><code>ext/xdoclet2</code></td>
            <td>Used by XDoclet 2</td>
        </tr>
        <tr valign="top">
            <td>Apache Commons Jelly</td>
            <td>1.0</td>
            <td><a href="https://commons.apache.org/jelly/">commons.apache.org/jelly</a></td>
            <td><code>ext/xdoclet2</code></td>
            <td>Used by XDoclet 2</td>
        </tr>
        <tr valign="top">
            <td>Apache Commons JEXL</td>
            <td>1.0</td>
            <td><a href="https://commons.apache.org/jexl/">commons.apache.org/jexl</a></td>
            <td><code>ext/xdoclet2</code></td>
            <td>Used by XDoclet 2</td>
        </tr>
        <tr valign="top">
            <td>dom4j</td>
            <td>1.6</td>
            <td><a href="https://www.dom4j.org/">www.dom4j.org</a></td>
            <td><code>ext/xdoclet2</code></td>
            <td>Used by XDoclet 2</td>
        </tr>
        <tr valign="top">
            <td>Generama</td>
            <td>1.2.3</td>
            <td><a href="https://generama.codehaus.org/">generama.codehaus.org</a></td>
            <td><code>ext/xdoclet2</code></td>
            <td>Used by XDoclet 2</td>
        </tr>
        <tr valign="top">
            <td>Jaxen</td>
            <td>1.1.3</td>
            <td><a href="https://jaxen.codehaus.org/">jaxen.codehaus.org</a></td>
            <td><code>ext/jaxen</code></td>
            <td>Used by XDoclet 2 and PMD</td>
        </tr>
        <tr valign="top">
            <td>NanoContainer</td>
            <td>1.0 beta 3</td>
            <td><a href="https://nanocontainer.codehaus.org/">nanocontainer.codehaus.org</a></td>
            <td><code>ext/xdoclet2</code></td>
            <td>Used by XDoclet 2</td>
        </tr>
        <tr valign="top">
            <td>ObjectWeb ASM</td>
            <td>3.2</td>
            <td><a href="https://asm.ow2.org/">asm.ow2.org</a></td>
            <td><code>ext/pmd</code></td>
            <td>Used by PMD</td>
        </tr>
        <tr valign="top">
            <td>PicoContainer</td>
            <td>1.0</td>
            <td><a href="https://www.picocontainer.org/">www.picocontainer.org</a></td>
            <td><code>ext/xdoclet2</code></td>
            <td>Used by XDoclet 2</td>
        </tr>
        <tr valign="top">
            <td>PMD</td>
            <td>4.2.5</td>
            <td><a href="https://pmd.sourceforge.net/">pmd.sourceforge.net</a></td>
            <td><code>ext/pmd</code></td>
            <td>-</td>
        </tr>
        <tr valign="top">
            <td>QDox</td>
            <td>1.6-SNAPSHOT</td>
            <td><a href="https://qdox.codehaus.org/">qdox.codehaus.org</a></td>
            <td><code>ext/xdoclet2</code></td>
            <td>Used by XDoclet 2</td>
        </tr>
        <tr valign="top">
            <td>Velocity</td>
            <td>1.4</td>
            <td><a href="https://velocity.apache.org/">velocity.apache.org</a></td>
            <td><code>ext/xdoclet2</code></td>
            <td>Used by XDoclet 2</td>
        </tr>
    </tbody>
    <tbody>
        <tr valign="top"><th colspan="5">Separate tools:</th></tr>
        <tr valign="top">
            <td>Vim</td>
            <td>7.3</td>
            <td><a href="https://www.vim.org/">www.vim.org</a></td>
            <td>-</td>
            <td>A special and big &ldquo;thank you&rdquo; to Bram Moolenaar and his wonderful tool, which I'm using since so many years!</td>
        </tr>
        <tr valign="top">
            <td>Sun JDK</td>
            <td>6</td>
            <td><a href="https://java.sun.com/javase/">java.sun.com/javase</a></td>
            <td>-</td>
            <td>-</td>
        </tr>
        <tr valign="top">
            <td>Jude Community</td>
            <td>5.2.1</td>
            <td><a href="https://jude.change-vision.com/">jude.change-vision.com</a></td>
            <td>-</td>
            <td>-</td>
        </tr>
    </tbody>
</table>

## Remarks

Fork of [sourceforge.net/projects/lizzy](https://sourceforge.net/projects/lizzy/)
