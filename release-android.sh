#!/usr/bin/env bash
# Build + push a PoGo PvP Overlay test build to friends via Firebase App
# Distribution — the Android analog of the sprout TestFlight script. Headless.
#
# WHY Firebase App Distribution (not Play Console): no store review delay, takes
# the debug-signed APK directly (no upload keystore, no $25 Play account), and
# testers install from an email/link like a TestFlight internal group.
#
# ONE-TIME SETUP (do these once, then this script is fully headless):
#   1. Install the Firebase CLI:
#        curl -sL https://firebase.tools | bash        # or: npm i -g firebase-tools
#   2. Firebase project + Android app:
#        - Reuse your existing project or create one at https://console.firebase.google.com
#        - Add an *Android app* with package name: com.pogopvp.overlay
#        - Copy its App ID (looks like 1:1234567890:android:abcdef0123) → FIREBASE_APP_ID below.
#   3. Headless auth via a service account (no browser login):
#        - In Google Cloud console for that project, create/reuse a service account,
#          grant it the "Firebase App Distribution Admin" role, download a JSON key.
#        - Save it (mirroring your ~/.appstoreconnect convention), e.g.:
#            ~/.firebase/pogo-pvp-distribution.json
#        - The admin-SDK JSON you already have can work IF it belongs to the same
#          project and has that role.
#   4. Make a tester group named "friends" in the App Distribution UI and add the
#      Z Flip owners' emails (they accept once, then auto-get every build).
#
# PER RELEASE: nothing to do — this script auto-bumps versionCode in
# app/build.gradle.kts before each build so every push is a distinct release
# (testers' App Tester dedupes by versionCode). Override with KEEP_VERSION=1.

set -euo pipefail
cd "$(dirname "$0")"

# --- config (override via env) ----------------------------------------------
FIREBASE_APP_ID="${FIREBASE_APP_ID:-1:212663733929:android:0d40fa051b76a8be9623b8}"
TESTER_GROUPS="${TESTER_GROUPS:-friends}"
RELEASE_NOTES="${RELEASE_NOTES:-Test build $(git rev-parse --short HEAD 2>/dev/null || date +%Y%m%d-%H%M)}"

# Auth: for local runs your `firebase login` session is enough. For headless/CI,
# drop a service-account JSON (App Distribution Admin role) and point this at it.
SERVICE_ACCOUNT="${GOOGLE_APPLICATION_CREDENTIALS:-$HOME/.firebase/pogo-pvp-distribution.json}"
if [ -f "$SERVICE_ACCOUNT" ]; then
  export GOOGLE_APPLICATION_CREDENTIALS="$SERVICE_ACCOUNT"
  echo "==> auth: service account ($SERVICE_ACCOUNT)"
else
  echo "==> auth: using your 'firebase login' session"
fi

# Keg-only JDK (not on PATH) — same toolchain the Gradle build needs.
export JAVA_HOME="${JAVA_HOME:-/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home}"

APK="app/build/outputs/apk/debug/app-debug.apk"
GRADLE_FILE="app/build.gradle.kts"

# Auto-bump versionCode (unless KEEP_VERSION=1) so each push is a distinct release.
if [ "${KEEP_VERSION:-0}" != "1" ]; then
  CURRENT_VC="$(perl -ne 'print $1 if /versionCode\s*=\s*(\d+)/' "$GRADLE_FILE")"
  if [ -z "$CURRENT_VC" ]; then
    echo "!! could not find versionCode in $GRADLE_FILE" >&2
    exit 1
  fi
  NEW_VC=$((CURRENT_VC + 1))
  perl -i -pe "s/versionCode\s*=\s*\d+/versionCode = $NEW_VC/" "$GRADLE_FILE"
  echo "==> versionCode $CURRENT_VC -> $NEW_VC"
fi

echo "==> building debug APK (debug-signed; installs for friends without a keystore)"
./gradlew :app:assembleDebug

echo "==> distributing $APK to groups: $TESTER_GROUPS"
firebase appdistribution:distribute "$APK" \
  --app "$FIREBASE_APP_ID" \
  --groups "$TESTER_GROUPS" \
  --release-notes "$RELEASE_NOTES"

echo "==> done — testers in '$TESTER_GROUPS' get an email/notification to install."
