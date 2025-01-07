module io.github.borewit.lizzy {

  requires org.apache.logging.log4j;
  requires java.desktop;
  requires jakarta.activation;
  requires org.apache.commons.io;
  requires jakarta.xml.bind;
  requires org.glassfish.jaxb.runtime; // Add the JAXB RI implementation module
  requires dd.plist;
  requires com.fasterxml.jackson.annotation;
  requires com.fasterxml.jackson.databind;

  exports io.github.borewit.lizzy.playlist;
  exports io.github.borewit.lizzy.playlist.asx;
  exports io.github.borewit.lizzy.playlist.atom;
  exports io.github.borewit.lizzy.playlist.b4s;
  exports io.github.borewit.lizzy.playlist.jspf;
  exports io.github.borewit.lizzy.playlist.m3u;
  exports io.github.borewit.lizzy.playlist.mpcpl;
  exports io.github.borewit.lizzy.playlist.pla;
  exports io.github.borewit.lizzy.playlist.plist;
  exports io.github.borewit.lizzy.playlist.plp;
  exports io.github.borewit.lizzy.playlist.pls;
  exports io.github.borewit.lizzy.playlist.rmp;
  exports io.github.borewit.lizzy.playlist.rss;
  exports io.github.borewit.lizzy.playlist.smil20;
  exports io.github.borewit.lizzy.playlist.wpl;
  exports io.github.borewit.lizzy.playlist.xspf;
  exports io.github.borewit.lizzy.content;
  exports io.github.borewit.lizzy.content.type;

  opens io.github.borewit.lizzy.playlist.jspf.json;
  opens io.github.borewit.lizzy.playlist.xml.asx;
  opens io.github.borewit.lizzy.playlist.xml.atom;
  opens io.github.borewit.lizzy.playlist.xml.b4s;
  opens io.github.borewit.lizzy.playlist.xml.rmp;
  opens io.github.borewit.lizzy.playlist.xml.rss20;
  opens io.github.borewit.lizzy.playlist.xml.smil20;
  opens io.github.borewit.lizzy.playlist.xml.xspf;
  opens io.github.borewit.lizzy.playlist.smil20.xml;
  opens io.github.borewit.lizzy.playlist.xml.rss20.media;

  provides io.github.borewit.lizzy.playlist.SpecificPlaylistProvider with
    io.github.borewit.lizzy.playlist.pla.PLAProvider,
    io.github.borewit.lizzy.playlist.asx.AsxProvider,
    io.github.borewit.lizzy.playlist.b4s.WinampXmlProvider,
    io.github.borewit.lizzy.playlist.wpl.WplProvider,
    io.github.borewit.lizzy.playlist.smil20.SmilProvider,
    io.github.borewit.lizzy.playlist.rss.RSSProvider,
    io.github.borewit.lizzy.playlist.atom.AtomProvider,
    io.github.borewit.lizzy.playlist.xspf.XspfProvider,
    io.github.borewit.lizzy.playlist.rmp.RmpProvider,
    io.github.borewit.lizzy.playlist.plist.PlistProvider,
    io.github.borewit.lizzy.playlist.pls.PLSProvider,
    io.github.borewit.lizzy.playlist.mpcpl.MPCPLProvider,
    io.github.borewit.lizzy.playlist.plp.PLPProvider,
    io.github.borewit.lizzy.playlist.m3u.M3UProvider,
    io.github.borewit.lizzy.playlist.m3u.M3U8Provider,
    io.github.borewit.lizzy.playlist.jspf.JspfProvider;

  uses io.github.borewit.lizzy.playlist.SpecificPlaylistProvider;

  provides io.github.borewit.lizzy.content.type.IContentTypeProvider with
    io.github.borewit.lizzy.content.type.SpecificPlaylistTypeProvider;

  provides io.github.borewit.lizzy.content.ContentMetadataProvider with
    io.github.borewit.lizzy.content.SoundMetadataProvider,
    io.github.borewit.lizzy.content.ImageMetadataProvider;

}
