# fastlane + Play Store (`supply`) — setup

This project can push its Play Store listing (text, screenshots, icon, feature graphic)
and release builds straight from the command line via fastlane's `supply`. One-time setup
below; after that it's a single command per update.

## What's already wired

```
Gemfile                     # fastlane dependency
fastlane/Appfile            # package name + service-account key path
fastlane/Fastfile           # lanes: validate / listing / deploy / promote
fastlane/metadata/android/en-US/
  title.txt                 # "PvPeek: IVs, Rank & Moves"  (≤30 chars)
  short_description.txt      # (≤80 chars)
  full_description.txt       # (≤4000 chars)
  changelogs/default.txt     # release notes (default) + 24.txt per versionCode
  images/icon.png            # 512×512 32-bit
  images/featureGraphic.png  # 1024×500
  images/phoneScreenshots/        1_*.png 2_*.png 3_*.png  (display order)
  images/sevenInchScreenshots/    7-inch tablet set
  images/tenInchScreenshots/      10-inch tablet set
```

## One-time setup

### 1. Ruby + fastlane — ✅ already installed
This machine runs Homebrew Ruby 4.0.5 (`/opt/homebrew/bin/ruby`) and fastlane **2.236.1**
was installed via Bundler; `Gemfile.lock` is committed. Verify any time with:

```bash
cd /Users/dballest/claude/el-negocio
bundle exec fastlane --version
```

If you ever hit a `cannot load ... bundler-x.y.z/exe/bundle` error (a Homebrew bundler-shim
version mismatch), repair it with `gem install bundler` and re-run `bundle install`.

> Heads-up: macOS *system* Ruby (`/usr/bin/ruby`, 2.6) is too old for fastlane — make sure
> Homebrew's Ruby is first on your `PATH` (it is by default in this shell).

### 2. Google Play service account (headless auth) — ⬜ the one step left
A service account lets fastlane authenticate to Google Play headlessly (no browser login).

1. In **Play Console → Setup → API access**, link a Google Cloud project (or create one).
2. Create a **service account** in Google Cloud → grant it access back in the Play Console
   (**Users & permissions → Invite** the service-account email; give it at least
   *Release manager*, or *Admin* for first setup).
3. Download the service account's **JSON key**.
4. Save it outside git and point fastlane at it — either:
   - place it at `fastlane/play-store-key.json` (already gitignored), **or**
   - `export PLAY_STORE_JSON_KEY=~/.gcloud/pvpeek-play.json` (the Appfile reads this env var).
5. Verify the key works:

```bash
bundle exec fastlane run validate_play_store_json_key
```

> Note: the **app must already exist** in the Play Console with at least one build uploaded
> manually before `supply` can update its listing (Google requires the first APK/AAB by hand).

## Everyday use

```bash
# Dry run — validate the listing & assets, upload nothing
bundle exec fastlane validate

# Push listing text + all screenshots + icon + feature graphic (no binary)
bundle exec fastlane listing

# Build a release AAB and upload it + listing to the internal track (as a draft)
bundle exec fastlane deploy
bundle exec fastlane deploy track:production      # or another track

# Promote an already-uploaded build between tracks
bundle exec fastlane promote from:internal to:production
```

## Notes
- `deploy` uploads as `release_status: "draft"` so you can review and roll out from the
  console for the first release. Change it in `fastlane/Fastfile` once you're comfortable.
- Screenshot display order follows the numeric filename prefix (`1_`, `2_`, `3_`).
- To regenerate the assets, the source screenshots live in `play-store/` and the generator
  is `/tmp/mkshots.sh`; copy refreshed files into `fastlane/metadata/.../images/`.
- The release build signs with the keystore from `keystore.properties` (see the project README).
