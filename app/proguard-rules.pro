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


# قواعد أساسية
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keepattributes Exceptions
-keepattributes InnerClasses

# احتفظ بأسماء المستويات لأغراض التصحيح
-keepattributes EnclosingMethod
-keepattributes LineNumberTable,SourceFile

# الحفاظ على الكلاسات الرئيسية للتطبيق
-keep public class com.noureddine.WriteFlow.** { *; }

# قواعد Room Database
-keep class androidx.room.** { *; }
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.**
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao interface *
-keep class **_Impl { *; }

# قواعد Retrofit
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keep class okio.** { *; }
-dontwarn retrofit2.**
-dontwarn okhttp3.**
-dontwarn okio.**
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# قواعد Gson
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# قواعد Firebase
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**
-keep class com.google.android.gms.** { *; }

# قواعد لـ ViewModel و LiveData
-keep class androidx.lifecycle.** { *; }
-keep class * extends androidx.lifecycle.ViewModel
-keep class * extends androidx.lifecycle.AndroidViewModel
-keep class * extends androidx.lifecycle.LiveData

# قواعد لنماذج البيانات (Models)
-keep class com.noureddine.WriteFlow.model.** { *; }
-keepclassmembers class com.noureddine.WriteFlow.model.** { *; }

# قواعد للمستودعات (Repositories)
-keep class com.noureddine.WriteFlow.repositorys.** { *; }

# قواعد لواجهات API
-keep interface com.noureddine.WriteFlow.interfaces.** { *; }

# قواعد Fragments
-keep class com.noureddine.WriteFlow.fragments.** { *; }
-keepclassmembers class com.noureddine.WriteFlow.fragments.** { *; }

# قواعد ViewModels
-keep class com.noureddine.WriteFlow.viewModels.** { *; }
-keepclassmembers class com.noureddine.WriteFlow.viewModels.** { *; }

# قواعد Utils
-keep class com.noureddine.WriteFlow.Utils.** { *; }

# قواعد لمكتبة Apache POI و iText
-keep class org.apache.poi.** { *; }
-dontwarn org.apache.poi.**
-keep class com.itextpdf.** { *; }
-dontwarn com.itextpdf.**

# قواعد Unity Ads
-keep class com.unity3d.ads.** { *; }
-dontwarn com.unity3d.ads.**

# قواعد Billing (الفوترة)
-keep class com.android.billingclient.** { *; }
-dontwarn com.android.billingclient.**

# قواعد للشيفرة المولدة
-keepclassmembers class * {
    native <methods>;
}

# قواعد للـ Enum
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# قواعد للـ Android Security Crypto
-keep class androidx.security.crypto.** { *; }
-dontwarn androidx.security.crypto.**

# قواعد لـ Google Sign In
-keep class com.google.android.gms.auth.** { *; }
-dontwarn com.google.android.gms.auth.**