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
package christophedelory.playlist.asx;

/**
 * Instructs Windows Media Player to submit any log data to the specified URL.
 * <br>
 * The LOGURL element enables a client metafile playlist to send additional logging information to specified servers.
 * Logging information is automatically sent to the origin server of a playlist when it is opened and to each LOGURL specified for the ASX element,
 * if any are present.
 * Logging information is also sent to each LOGURL specified for an ENTRY element when that entry is reached.
 * The URL specified in the HREF attribute of a LOGURL element must be the address of a host that is able to process logging requests.
 * The URL can be any valid HTTP URL.
 * The URL can also indicate the location of a CGI script.
 * <br>
 * The only valid protocols for a LOGURL element are HTTP and HTTPS.
 * <br>
 * A LOGURL element within the scope of an ASX element is applicable only to the metafile in which it resides,
 * regardless of whether that metafile is referenced from another metafile.
 * A LOGURL element forces the submission of log data for all content streamed from within its defined scope
 * and only for content streamed from within its defined scope.
 * Log data will be submitted to the origin server and to all URLs listed in every LOGURL element in scope.
 * Log data will be submitted only once to each listed URL, even if the same URL is listed more than once in a given scope.
 * A repeat of an ENTRY would result in another submission to the listed URLs.
 * <br>
 * There is no limit to the number of LOGURL elements in a metafile playlist.
 * <br>
 * Not supported on Windows CE.
 * @version $Revision: 91 $
 * @author Christophe Delory
 * @castor.class xml="logurl"
 */
public class LogURL extends URLElement
{
}
