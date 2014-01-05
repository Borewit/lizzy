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
 * Defines an URL string appended to the front of URLs sent to the Windows Media Player control.
 * This element defines a Uniform Resource Locator (URL) string appended to the front of URL-flip URLs sent to the WMP control.
 * URL-flipping is a scripting mechanism that causes the WMP control to open a new URL in a browser or browser frame when it receives a script command.
 * <br>
 * The BASE element is similar to a home directory for relative links.
 * It only affects URLs sent using script commands as part of the content stream that is playing in the WMP control.
 * <br>
 * A BASE element defined as the child of an ASX element becomes the default for the entire Advanced Stream Redirector (ASX) file.
 * A BASE element defined in an ENTRY element overrides any BASE element in the parent ASX element (for that ENTRY element only).
 * <br>
 * <b>Note</b>: if the HREF parameter does not end with a '/' character, the WMP control appends one to the end of the string.
 * @version $Revision: 91 $
 * @author Christophe Delory
 * @castor.class xml="base"
 */
public class Base extends URLElement
{
}
