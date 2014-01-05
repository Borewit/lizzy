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
 * Specifies a URL to an embedded skin.
 * This is an URL for a compressed border file with a file extension .wmz.
 * For Windows Media Player 9 Series or later, this value can reference only border files on the user's hard disk
 * located in the same directory as the metafile playlist.
 * <br>
 * It is used to create a border, which is similar to a skin but is displayed in the Now Playing area of the full mode Windows Media Player.
 * It is used only for borders, which appear inside the full mode Windows Media Player, and not for regular skins,
 * which entirely replace the compact mode Windows Media Player.
 * <br>
 * In a Windows Media Download Package (with a .wmd file name extension), the SKIN element enables a border to have content and link to other sites.
 * The Windows Media Download Package is a compressed file that contains a border file and a Windows Media metafile.
 * The border file (with a .wmz file name extension) is compressed, and includes a skin definition file (with a .wms file name extension).
 * <br>
 * A SKIN element has three components:
 * <ul>
 * <li>A skin</li>
 * <li>Some content</li>
 * <li>A metafile</li>
 * </ul>
 * Skins included with Windows Media Download Packages must be rectangular in shape.
 * Creating borders with skins that are not rectangular may yield unexpected results.
 * <br>
 * Windows Media Player version 7.0 or later.
 * @version $Revision: 91 $
 * @author Christophe Delory
 * @castor.class xml="skin"
 */
public class Skin extends URLElement
{
}
