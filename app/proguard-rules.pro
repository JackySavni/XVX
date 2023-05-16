# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keepattributes InnerClasses
-keep class com.video.player.videoplayer.xvxvideoplayer**
-keepclassmembers class com.video.player.videoplayer.xvxvideoplayer** {
    *;
}

#Method
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers class * {
    public void *ButtonClicked(android.view.View);
}

#Fragments
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Fragment

-keep interface com.google.android.material.** { *; }
-keep public class android.support.design.R$* { *; }
-keep public class com.google.android.material.R$* { *; }
-keep class com.google.android.gms.internal.** { *; }
-dontwarn android.support.v7.**
-dontwarn androidx.appcompat.**
-keep class android.support.v7.** { *; }
-keep class androidx.appcompat.** { *; }
-keep interface android.support.v7.** { *; }
-keep interface androidx.appcompat.** { *; }
-keep class com.firebase.** { *; }
-optimizationpasses 3
-optimizations !code/simplification/arithmetic
-verbose
-keepattributes LocalVariableTable,LocalVariableTypeTable
-allowaccessmodification

-assumenosideeffects class java.io.PrintStream {
     public void println(...);
     public void println(...);
 }
# Keep subscribe-methods from deletion
-keepclassmembers class ** {
  @com.google.common.eventbus.Subscribe <methods>;
}

# To make right deserialization
-keepclassmembers class ** {
  @com.video.player.videoplayer.xvxvideoplayer.server.JsonDeserializerWithOptions$FieldRequired public *;
}
-keep interface com.video.player.videoplayer.xvxvideoplayer.server.JsonDeserializerWithOptions$FieldRequired
-keep class com.video.player.videoplayer.xvxvideoplayer.server.JsonDeserializerWithOptions
-keep class com.facebook.ads.** { *; }
-keep class com.google.android.gms.**
-dontwarn org.apache.**
-keep class org.sqlite.** { *; }
-keep class org.sqlite.database.** { *; }
-dontwarn javax.**
-keepattributes Signature
-keep,allowshrinking,allowobfuscation enum com.video.player.videoplayer.xvxvideoplayer.**
-optimizations !class/unboxing/enum

-keep class com.video.player.videoplayer.xvxvideoplayer.model.** { *; }
-keepclassmembers class com.video.player.videoplayer.xvxvideoplayer.model.** { *; }
-keep class com.video.player.videoplayer.xvxvideoplayer.gallery.model.** { *; }
-keepclassmembers class com.video.player.videoplayer.xvxvideoplayer.gallery.model.** { *; }
-keep class com.video.player.videoplayer.xvxvideoplayer.zzzjjj.** { *; }
-assumenosideeffects class android.util.Log {
  public static *** v(...);
  public static *** d(...);
  public static *** i(...);
  public static *** w(...);
  public static *** e(...);
}

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule
#Android Image Cropper
-keep class androidx.appcompat.widget.** { *; }


-renamesourcefileattribute SourceFile

# Butterknife
-dontwarn butterknife.internal.**
# Retain generated class which implement Unbinder.
-keep public class * implements butterknife.Unbinder { public <init>(**, android.view.View); }

# Prevent obfuscation of types which use ButterKnife annotations since the simple name
# is used to reflectively look up the generated ViewBinding.
-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }

#calligraphy3
-keep class butterknife.** { *; }
-keep class **$$ViewBinder { *; }

#pl.droidsonroids.gif
-keep public class pl.droidsonroids.gif.GifIOException{<init>(int);}
-keep class pl.droidsonroids.gif.GifInfoHandle{<init>(long,int,int,int);}

#Dexter
-keepattributes InnerClasses, Signature, *Annotation*
-keep class com.karumi.dexter.** { *; }
-keep interface com.karumi.dexter.** { *; }
-keepclasseswithmembernames class com.karumi.dexter.** { *; }
-keepclasseswithmembernames interface com.karumi.dexter.** { *; }

#Lottie
-keep class com.airbnb.lottie.samples.** { *; }

#com.github.yalantis:ucrop
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

#jp.co.cyberagent.android:gpuimage
-dontwarn jp.co.cyberagent.android.gpuimage.**

# Picasso
-dontwarn com.squareup.okhttp.**

#pub.devrel:easypermissions
-keepclassmembers class * {
    @pub.devrel.easypermissions.AfterPermissionGranted <methods>;
}

#org.greenrobot:eventbus
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# And if you use AsyncExecutor:
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

#com.wang.avi:library
-keep class com.wang.avi.** { *; }
-keep class com.wang.avi.indicators.** { *; }

##ExoPlayer
-keepclasseswithmembernames,includedescriptorclasses class * {
     native <methods>;
 }
-keep class com.google.android.exoplayer2.** {*;}

-keep class com.getkeepsafe.taptargetview.** {*;}