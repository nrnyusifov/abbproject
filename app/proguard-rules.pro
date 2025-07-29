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

# Preserve Firebase model classes (used by Firestore toObject mapping)
-keep class com.example.abbproject.data.model.** { *; }
# Keep application and activity classes referenced in AndroidManifest (prevent removal or obfuscation)
-keep class com.example.abbproject.di.MyApp { *; }
-keep class com.example.abbproject.MainActivity { *; }
# Preserve Dagger Hilt and dependency injection generated code
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class javax.annotation.** { *; }
-keep class com.example.abbproject.*Hilt* { *; }
-keep class com.example.abbproject.Hilt_* { *; }
-keepclassmembers class * {
    @javax.inject.Inject <fields>;
}
-keepclassmembers class * {
    @javax.inject.Inject <init>(...);
}
-keep,allowobfuscation,allowshrinking @dagger.hilt.EntryPoint class *
-keep,allowobfuscation,allowshrinking @dagger.hilt.android.EarlyEntryPoint class *
# Keep Jetpack Compose UI related classes/members and suppress warnings for hidden APIs
-dontwarn android.view.RenderNode
-dontwarn android.view.DisplayListCanvas
-keepclassmembers class androidx.compose.ui.platform.ViewLayerContainer {
    protected void dispatchGetDisplayList();
}
-keepclassmembers class androidx.compose.ui.platform.AndroidComposeView {
    android.view.View findViewByAccessibilityIdTraversal(int);
}
-keep,allowshrinking class * extends androidx.compose.ui.node.ModifierNodeElement
# Preserve coroutine continuation (to keep generic type information for suspend functions)
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
# Preserve Parcelable implementations (keep CREATOR fields for Android framework)
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
# Preserve OkHttp's internal PublicSuffixDatabase (used by Coil/OkHttp for domain handling)
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
# Keep runtime annotation metadata (avoid removing annotations needed for reflection)
-keepattributes *Annotation*
# Firebase Firestore annotations (important for reflection-based mapping)
-keepclassmembers class * {
    @com.google.firebase.firestore.PropertyName <methods>;
}
-keepclassmembers class * {
    @com.google.firebase.firestore.Exclude <fields>;
}
# General keep rules for Firebase modules
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# Save mapping file for debugging stack traces
-printmapping mapping.txt

# Print configuration being used
-printconfiguration config.txt