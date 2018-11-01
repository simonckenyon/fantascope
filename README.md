# Fantascope

See https://en.wikipedia.org/wiki/Phenakistiscope

This is an Android app which connects to themoviedb.org and gets information about movies.

It currently displays the top rated movies in descending order.

It is build using the latest recommendations from google (Architecture Components).

It uses:

- AndroidX
- LiveData
- ViewModel
- Room
- Retrofit2
- Moshi
- Glide
- Logback

The code is 100% Kotlin.

## NB

To get around a bug in the google libraries you need to add:

`android.useAndroidX=true
 android.enableJetifier=true`
 
to ~/.gradle/gradle.properties

And while you're there, add the moviedb.org API key:

`Fantascope_V3ApiKey="please put the V3 API key here"`

There should be double quotes around the key, which is a big hex number.