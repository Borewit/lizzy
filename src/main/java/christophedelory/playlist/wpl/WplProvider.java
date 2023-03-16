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
package christophedelory.playlist.wpl;

import christophedelory.content.type.ContentType;
import christophedelory.player.PlayerSupport;
import christophedelory.playlist.smil.SmilProvider;

/**
 * WPL (Windows Media Player Playlist) is a proprietary XML file format used in Microsoft Windows Media Player versions 9-11.
 *
 * @author Christophe Delory
 * @version $Revision: 91 $
 */
public class WplProvider extends SmilProvider
{
    /**
     * A list of compatible content types.
     */
    private static final ContentType[] FILETYPES =
        {
            new ContentType(new String[]{".wpl"},
                new String[]{"application/vnd.ms-wpl"},
                new PlayerSupport[]
                    {
                        new PlayerSupport(PlayerSupport.Player.WINAMP, false, null),
                        new PlayerSupport(PlayerSupport.Player.WINDOWS_MEDIA_PLAYER, true, null),
                    },
                "Windows Media Player Playlist (WPL)"),
        };

    @Override
    public String getId()
    {
        return "wpl";
    }

    @Override
    public ContentType[] getContentTypes()
    {
        return FILETYPES.clone();
    }

}
