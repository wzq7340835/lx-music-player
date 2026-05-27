# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt

# Keep JavaScript interface
-keep class cn.lxmusic.player.source.** { *; }

# Keep QuickJS classes
-keep class com.squareup.quickjs.** { *; }

# Keep ExoPlayer
-keep class androidx.media3.** { *; }

# Keep model classes
-keep class cn.lxmusic.player.model.** { *; }

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class cn.lxmusic.player.model.** { *; }
-keep class com.google.gson.** { *; }

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public static com.bumptech.glide.load.ImageHeaderParser$** from(java.lang.String);
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}
