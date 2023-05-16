package com.video.player.videoplayer.xvxvideoplayer.vid;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import androidx.documentfile.provider.DocumentFile;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.util.MimeTypes;
import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class VP_SubtitleUtils {

    public static String getSubtitleMime(Uri uri) {
        final String vp_path = uri.getPath();
        if (vp_path.endsWith(".ssa") || vp_path.endsWith(".ass")) {
            return MimeTypes.TEXT_SSA;
        } else if (vp_path.endsWith(".vtt")) {
            return MimeTypes.TEXT_VTT;
        } else if (vp_path.endsWith(".ttml") ||  vp_path.endsWith(".xml") || vp_path.endsWith(".dfxp")) {
            return MimeTypes.APPLICATION_TTML;
        } else {
            return MimeTypes.APPLICATION_SUBRIP;
        }
    }

    public static String getSubtitleLanguage(Uri uri) {
        final String vp_path = uri.getPath().toLowerCase();

        if (vp_path.endsWith(".srt")) {
            int vp_last = vp_path.lastIndexOf(".");
            int vp_prev = vp_last;

            for (int i = vp_last; i >= 0; i--) {
                vp_prev = vp_path.indexOf(".", i);
                if (vp_prev != vp_last)
                    break;
            }

            int vp_len = vp_last - vp_prev;

            if (vp_len >= 2 && vp_len <= 6) {
                // TODO: Validate lang
                return vp_path.substring(vp_prev + 1, vp_last);
            }
        }

        return null;
    }

    /*
    public static DocumentFile findUriInScope(DocumentFile documentFileTree, Uri uri) {
        for (DocumentFile file : documentFileTree.listFiles()) {
            if (file.isDirectory()) {
                final DocumentFile ret = findUriInScope(file, uri);
                if (ret != null)
                    return ret;
            } else {
                final Uri fileUri = file.getUri();
                if (fileUri.toString().equals(uri.toString())) {
                    return file;
                }
            }
        }
        return null;
    }
    */

    public static DocumentFile vp_findUriInScope(Context vp_context, Uri vp_scope, Uri vp_uri) {
        DocumentFile vp_treeUri = DocumentFile.fromTreeUri(vp_context, vp_scope);
        String[] vp_trailScope = vp_getTrailFromUri(vp_scope);
        String[] vp_trailVideo = vp_getTrailFromUri(vp_uri);

        for (int vp_i = 0; vp_i < vp_trailVideo.length; vp_i++) {
            if (vp_i < vp_trailScope.length) {
                if (!vp_trailScope[vp_i].equals(vp_trailVideo[vp_i]))
                    break;
            } else {
                vp_treeUri = vp_treeUri.findFile(vp_trailVideo[vp_i]);
                if (vp_treeUri == null)
                    break;
            }
            if (vp_i + 1 == vp_trailVideo.length)
                return vp_treeUri;
        }
        return null;
    }

    public static DocumentFile vp_findDocInScope(DocumentFile vp_scope, DocumentFile vp_doc) {
        if (vp_doc == null || vp_scope == null)
            return null;
        for (DocumentFile vp_file : vp_scope.listFiles()) {
            if (vp_file.isDirectory()) {
                final DocumentFile vp_ret = vp_findDocInScope(vp_file, vp_doc);
                if (vp_ret != null)
                    return vp_ret;
            } else {
                //if (doc.length() == file.length() && doc.lastModified() == file.lastModified() && doc.getName().equals(file.getName())) {
                // lastModified is zero when opened from Solid Explorer
                final String vp_docName = vp_doc.getName();
                final String vp_fileName = vp_file.getName();
                if (vp_docName == null || vp_fileName == null) {
                    continue;
                }
                if (vp_doc.length() == vp_file.length() && vp_docName.equals(vp_fileName)) {
                    return vp_file;
                }
            }
        }
        return null;
    }

    public static String getTrailPathFromUri(Uri uri) {
        String vp_path = uri.getPath();
        String[] vp_array = vp_path.split(":");
        if (vp_array.length > 1) {
            return vp_array[vp_array.length - 1];
        } else {
            return vp_path;
        }
    }

    public static String[] vp_getTrailFromUri(Uri uri) {
        if ("org.courville.nova.provider".equals(uri.getHost()) && ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            String vp_path = uri.getPath();
            if (vp_path.startsWith("/external_files/")) {
                return vp_path.substring("/external_files/".length()).split("/");
            }
        }
        return getTrailPathFromUri(uri).split("/");
    }

    private static String vp_getFileBaseName(String vp_name) {
        if (vp_name.indexOf(".") > 0)
            return vp_name.substring(0, vp_name.lastIndexOf("."));
        return vp_name;
    }

    public static DocumentFile vp_findSubtitle(DocumentFile vp_video) {
        DocumentFile vp_dir = vp_video.getParentFile();
        return vp_findSubtitle(vp_video, vp_dir);
    }

    public static DocumentFile vp_findSubtitle(DocumentFile vp_video, DocumentFile vp_dir) {
        String vp_videoName = vp_getFileBaseName(vp_video.getName());
        int videoFiles = 0;

        if (vp_dir == null || !vp_dir.isDirectory())
            return null;

        List<DocumentFile> vp_candidates = new ArrayList<>();

        for (DocumentFile vp_file : vp_dir.listFiles()) {
            if (vp_file.getName().startsWith("."))
                continue;
            if (vp_isSubtitleFile(vp_file))
                vp_candidates.add(vp_file);
            if (vp_isVideoFile(vp_file))
                videoFiles++;
        }

        if (videoFiles == 1 && vp_candidates.size() == 1) {
            return vp_candidates.get(0);
        }

        if (vp_candidates.size() >= 1) {
            for (DocumentFile vp_candidate : vp_candidates) {
                if (vp_candidate.getName().startsWith(vp_videoName + '.')) {
                    return vp_candidate;
                }
            }
        }

        return null;
    }

    public static DocumentFile vp_findNext(DocumentFile video) {
        DocumentFile dir = video.getParentFile();
        return vp_findNext(video, dir);
    }

    public static DocumentFile vp_findNext(DocumentFile video, DocumentFile dir) {
        DocumentFile vp_list[] = dir.listFiles();
        try {
            Arrays.sort(vp_list, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        } catch (NullPointerException e) {
            return null;
        }

        final String vp_videoName = video.getName();
        boolean matchFound = false;

        for (DocumentFile vp_file : vp_list) {
            if (vp_file.getName().equals(vp_videoName)) {
                matchFound = true;
            } else if (matchFound) {
                if (vp_isVideoFile(vp_file)) {
                    return vp_file;
                }
            }
        }

        return null;
    }

    public static boolean vp_isVideoFile(DocumentFile file) {
        return file.isFile() && file.getType().startsWith("video/");
    }

    public static boolean vp_isSubtitleFile(DocumentFile file) {
        if (!file.isFile())
            return false;
        final String name = file.getName().toLowerCase();
        return name.endsWith(".srt") || name.endsWith(".ssa") || name.endsWith(".ass")
                || name.endsWith(".vtt") || name.endsWith(".ttml");
    }

    public static boolean vp_isSubtitle(Uri uri, String mimeType) {
        if (mimeType != null) {
            for (String mime : VP_Utils.vp_supportedMimeTypesSubtitle) {
                if (mimeType.equals(mime)) {
                    return true;
                }
            }
            if (mimeType.equals("text/plain") || mimeType.equals("text/x-ssa") || mimeType.equals("application/octet-stream") ||
                    mimeType.equals("application/ass") || mimeType.equals("application/ssa") || mimeType.equals("application/vtt")) {
                return true;
            }
        }
        if (uri != null) {
            if (VP_Utils.vp_isSupportedNetworkUri(uri)) {
                String path = uri.getPath();
                if (path != null) {
                    path = path.toLowerCase();
                    for (String extension : VP_Utils.vp_supportedExtensionsSubtitle) {
                        if (path.endsWith("." + extension)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static void vp_clearCache(Context context) {
        try {
            for (File vp_file : context.getCacheDir().listFiles()) {
                if (vp_file.isFile()) {
                    vp_file.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Uri vp_convertToUTF(VP_Player activity, Uri vp_subtitleUri) {
        try {
            String vp_scheme = vp_subtitleUri.getScheme();
            if (vp_scheme != null && vp_scheme.toLowerCase().startsWith("http")) {
                List<Uri> vp_urls = new ArrayList<>();
                vp_urls.add(vp_subtitleUri);
                VP_SubtitleFetcher VPSubtitleFetcher = new VP_SubtitleFetcher(activity, vp_urls);
                VPSubtitleFetcher.start();
                return null;
            } else {
                InputStream vp_inputStream = activity.getContentResolver().openInputStream(vp_subtitleUri);
                return vp_convertInputStreamToUTF(activity, vp_subtitleUri, vp_inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vp_subtitleUri;
    }

    public static Uri vp_convertInputStreamToUTF(Context vp_context, Uri vp_subtitleUri, InputStream vp_inputStream) {
        try {
            final CharsetDetector vp_detector = new CharsetDetector();
            final BufferedInputStream bufferedInputStream = new BufferedInputStream(vp_inputStream);
            vp_detector.setText(bufferedInputStream);
            final CharsetMatch vp_charsetMatch = vp_detector.detect();

            if (!StandardCharsets.UTF_8.displayName().equals(vp_charsetMatch.getName())) {
                String filename = vp_subtitleUri.getPath();
                filename = filename.substring(filename.lastIndexOf("/") + 1);
                final File vp_file = new File(vp_context.getCacheDir(), filename);
                final BufferedReader vp_bufferedReader = new BufferedReader(vp_charsetMatch.getReader());
                final BufferedWriter vp_bufferedWriter = new BufferedWriter(new FileWriter(vp_file));
                char[] vp_buffer = new char[512];
                int vp_num;
                int vp_pass = 0;
                boolean vp_success = true;
                while ((vp_num = vp_bufferedReader.read(vp_buffer)) != -1) {
                    vp_bufferedWriter.write(vp_buffer, 0, vp_num);
                    vp_pass++;
                    if (vp_pass * 512 > 2_000_000) {
                        vp_success = false;
                        break;
                    }
                }
                vp_bufferedWriter.close();
                vp_bufferedReader.close();
                if (vp_success) {
                    vp_subtitleUri = Uri.fromFile(vp_file);
                } else {
                    vp_subtitleUri = null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vp_subtitleUri;
    }

    public static MediaItem.SubtitleConfiguration vp_buildSubtitle(Context context, Uri uri, String subtitleName, boolean selected) {
        final String vp_subtitleMime = VP_SubtitleUtils.getSubtitleMime(uri);
        final String vp_subtitleLanguage = VP_SubtitleUtils.getSubtitleLanguage(uri);
        if (vp_subtitleLanguage == null && subtitleName == null)
            subtitleName = VP_Utils.vp_getFileName(context, uri);

        MediaItem.SubtitleConfiguration.Builder vp_subtitleConfigurationBuilder = new MediaItem.SubtitleConfiguration.Builder(uri)
                .setMimeType(vp_subtitleMime)
                .setLanguage(vp_subtitleLanguage)
                .setRoleFlags(C.ROLE_FLAG_SUBTITLE)
                .setLabel(subtitleName);
        if (selected) {
            vp_subtitleConfigurationBuilder.setSelectionFlags(C.SELECTION_FLAG_DEFAULT);
        }
        return vp_subtitleConfigurationBuilder.build();
    }

    public static float vp_normalizeFontScale(float vp_fontScale, boolean vp_small) {
        // https://bbc.github.io/subtitle-guidelines/#Presentation-font-size
        float vp_newScale;
        // ¯\_(ツ)_/¯
        if (vp_fontScale > 1.01f) {
            if (vp_fontScale >= 1.99f) {
                // 2.0
                vp_newScale = (vp_small ? 1.15f : 1.2f);
            } else {
                // 1.5
                vp_newScale = (vp_small ? 1.0f : 1.1f);
            }
        } else if (vp_fontScale < 0.99f) {
            if (vp_fontScale <= 0.26f) {
                // 0.25
                vp_newScale = (vp_small ? 0.65f : 0.8f);
            } else {
                // 0.5
                vp_newScale = (vp_small ? 0.75f : 0.9f);
            }
        } else {
            vp_newScale = (vp_small ? 0.85f : 1.0f);
        }
        return vp_newScale;
    }
}
