# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\Dev\android_sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
# greenrobot eventbus
-keepclassmembers class ** {
    public void onEvent*(***);
}

# For using GSON @Expose annotation
-keepattributes *Annotation*
-keepattributes Signature

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class ru.ilyaeremin.vkdialogs.models.** { *; }

##---------------End: proguard configuration for Gson  ----------

# Glide specific rules #
# https://github.com/bumptech/glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

#vk
-keep class com.vk.** { *; }

#debug
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

