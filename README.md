# PvPeek

An efficient, **on-demand** Android overlay that helps analyse Pokémon for PvP. Tap the
floating button on a Pokémon's Appraisal screen → it screen-captures one frame, reads the
CP, name, type and IV bars on-device, and shows the Great/Ultra League rank, IVs, and
recommended moveset. Nothing from your screen ever leaves the device.

> **Disclaimer:** PvPeek is an independent, unofficial tool. It is **not affiliated with,
> endorsed by, or sponsored by** Niantic, Nintendo, Game Freak, or The Pokémon Company.
> "Pokémon" and "Pokémon GO" are trademarks of their respective owners.

**License:** MIT (see [`LICENSE`](LICENSE)). Game data & PvP rankings derived from
[PvPoke](https://pvpoke.com) (MIT) — see [`THIRD_PARTY_NOTICES.md`](THIRD_PARTY_NOTICES.md).
Privacy policy: [`docs/privacy.html`](docs/privacy.html).

## Why it's battery-friendly
The design is **event-driven, not polling**:
- Scanning happens **only on tap** — there is no background frame-diffing loop.
- The `VirtualDisplay` is created per-scan and **torn down immediately** after one frame,
  so nothing is rendered/mirrored while idle (`ScreenCapture.kt`).
- OCR runs over **cropped regions** (CP banner + name band), not the whole frame
  (`OcrScanner.kt`).
- The foreground service must exist (it holds the projection token + overlay window) but
  does no work between scans.

Trade-off vs. auto-detect scanners: you tap to scan instead of it popping up by itself —
which fits batch-appraising Pokémon, where you're tapping through them anyway.

## How it works (the pipeline)
`MainActivity` (collect consents) → `OverlayService` (floating SCAN button) →
`ScreenCapture` (one frame) → `OcrScanner` (cropped ML Kit OCR) → `ScanParser` →
`Species` + `PvpCalculator` → `Analyzer` (verdict in the overlay).

`PvpCalculator` is the real PvP engine: it scores all 4096 IV combinations at their
cap-maxed level and ranks them by stat product — the same thing the website does when your
friends type IVs in by hand.

## Permissions it requests
- **Draw over other apps** (`SYSTEM_ALERT_WINDOW`) — the floating button.
- **Screen capture** (`MediaProjection`) — granted once per session via the system dialog.
- **Notifications** (Android 13+) — for the required foreground-service notification.

None of these read Pokémon GO's internal data — it only reads pixels, like a screenshot.

## Build & run (CLI, no Android Studio)
This machine already has the full toolchain via Homebrew — used by the `sprout` Flutter app.
`openjdk@17` is keg-only (off PATH), so point `JAVA_HOME` at it explicitly:

```sh
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
./gradlew :app:assembleDebug      # → app/build/outputs/apk/debug/app-debug.apk
./gradlew installDebug            # build + install onto a connected device (API 26+)
```

`local.properties` already points `sdk.dir` at `/opt/homebrew/share/android-commandlinetools`.
Verified building against `android-34` with JDK 17 / Gradle 8.7 / AGP 8.5.2.

On device: tap **Start PvP Overlay**, grant the two permissions, switch to Pokémon GO, tap
**SCAN** on a Pokémon's detail screen.

## What's a stub vs. real
- **Real:** capture pipeline, overlay lifecycle, cropped OCR, CP formula, and the full
  4096-IV PvP ranking.
- **Stub / to extend:**
  - `Species.kt` holds ~7 species — replace with the full dex from
    [PvPoke's `gamemaster.json`](https://github.com/pvpoke/pvpoke). Load CPM from there too.
  - OCR crop fractions in `OcrScanner.kt` are tuned for a typical tall phone — expose them
    as adjustable settings per device/resolution.
  - **Exact IVs:** a single detail-screen scan gives CP + name. To report *this* Pokémon's
    rank (not just the target), add an appraisal-screen scan that reads the three IV bars,
    then call `PvpCalculator` with those IVs.

## ⚠️ ToS note
Keep it passive: screen-read + advise only. The human drives the game. Do **not** add
auto-tapping or game-process hooks — that's what gets accounts banned.
