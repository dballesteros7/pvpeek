# R8 / ProGuard rules for the release build.

# Keep file names + line numbers so Crashlytics stack traces are readable
# (the Crashlytics Gradle plugin uploads the mapping file to deobfuscate them).
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# ML Kit text recognition uses native code + reflection internally.
-keep class com.google.mlkit.** { *; }
-keep class com.google.android.gms.internal.mlkit_vision_text_common.** { *; }
-dontwarn com.google.mlkit.**

# Firebase (Crashlytics / Analytics) — libraries ship consumer rules; these are belt-and-braces.
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# We parse JSON manually with org.json (no reflection on our model classes), so no
# model-keep rules are required. Kotlin metadata and our code are handled by R8 defaults.
