package com.video.player.videoplayer.xvxvideoplayer.vid;

import android.content.res.Resources;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ui.DefaultTrackNameProvider;
import com.google.android.exoplayer2.util.MimeTypes;

class VP_CustomDefaultTrackNameProvider extends DefaultTrackNameProvider {
    public VP_CustomDefaultTrackNameProvider(Resources resources) {
        super(resources);
    }

    @Override
    public String getTrackName(Format format) {
        String vp_trackName = super.getTrackName(format);
        if (format.sampleMimeType != null) {
            String sampleFormat = vp_formatNameFromMime(format.sampleMimeType);
            if (sampleFormat == null) {
                sampleFormat = format.sampleMimeType;
            }
            vp_trackName += " (" + sampleFormat + ")";
        }
        if (format.label != null) {
            if (!vp_trackName.startsWith(format.label)) { // HACK
                vp_trackName += " - " + format.label;
            }
        }
        return vp_trackName;
    }

    private String vp_formatNameFromMime(final String mimeType) {
        switch (mimeType) {
            case MimeTypes.AUDIO_DTS:
                return "DTS";
            case MimeTypes.AUDIO_DTS_HD:
                return "DTS-HD";
            case MimeTypes.AUDIO_DTS_EXPRESS:
                return "DTS Express";
            case MimeTypes.AUDIO_TRUEHD:
                return "TrueHD";
            case MimeTypes.AUDIO_AC3:
                return "AC-3";
            case MimeTypes.AUDIO_E_AC3:
                return "E-AC-3";
            case MimeTypes.AUDIO_E_AC3_JOC:
                return "E-AC-3-JOC";
            case MimeTypes.AUDIO_AC4:
                return "AC-4";
            case MimeTypes.AUDIO_AAC:
                return "AAC";
            case MimeTypes.AUDIO_MPEG:
                return "MP3";
            case MimeTypes.AUDIO_MPEG_L2:
                return "MP2";
            case MimeTypes.AUDIO_VORBIS:
                return "Vorbis";
            case MimeTypes.AUDIO_OPUS:
                return "Opus";
            case MimeTypes.AUDIO_FLAC:
                return "FLAC";
            case MimeTypes.AUDIO_ALAC:
                return "ALAC";
            case MimeTypes.AUDIO_WAV:
                return "WAV";
            case MimeTypes.AUDIO_AMR:
                return "AMR";
            case MimeTypes.AUDIO_AMR_NB:
                return "AMR-NB";
            case MimeTypes.AUDIO_AMR_WB:
                return "AMR-WB";

            case MimeTypes.APPLICATION_PGS:
                return "PGS";
            case MimeTypes.APPLICATION_SUBRIP:
                return "SRT";
            case MimeTypes.TEXT_SSA:
                return "SSA";
            case MimeTypes.TEXT_VTT:
                return "VTT";
            case MimeTypes.APPLICATION_TTML:
                return "TTML";
            case MimeTypes.APPLICATION_TX3G:
                return "TX3G";
            case MimeTypes.APPLICATION_DVBSUBS:
                return "DVB";
        }
        return null;
    }
}