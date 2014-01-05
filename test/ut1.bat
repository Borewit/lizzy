@echo off
for %%t in (pla asx b4s wpl smil rss atom hypetape xspf rmp plist kpl pls mpcpl plp m3u) do (
    call Transcode -t %%t %*
    if %%t==plp call Transcode -t %%t -plp:disk HD %*
    if %%t==m3u call Transcode -t %%t -m3u:ext %*
    if %%t==rss call Transcode -t %%t -rss:media %*
)
