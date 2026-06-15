# Play Store listing — PvPeek

Assets in this folder: `icon-512.png` (512×512 app icon), `feature-graphic.png` (1024×500).
Phone screenshots still needed — see below.

## App details
| Field | Value |
|---|---|
| App name (≤30 chars) | **PvPeek: IVs, Rank & Moves** |
| Default language | English (US) |
| App or game | App |
| Free / paid | Free |
| Category | Tools |
| Contact email | privacy@pollera-colora.com (or set up support@…) |
| Website | https://dballesteros7.github.io/pvpeek/ |
| Privacy policy | https://dballesteros7.github.io/pvpeek/privacy.html |

## Short description (≤80 chars)
> PvP IVs, Great/Ultra League rank, and best moves — one tap, all on your device.

## Full description (≤4000 chars)
> PvPeek reads a Pokémon's PvP potential in one tap — no typing stats into a website.
>
> A small floating button sits on the edge of your screen. Open a Pokémon's Appraisal, tap the
> button, and PvPeek instantly shows you everything you need for Great League and Ultra League:
>
> • IVs — read directly from the appraisal bars
> • Great League & Ultra League rank — out of 4096, with how close it is to the #1 spread
> • Recommended moveset — the best fast move (with its turn count) and charged moves (with how
>   many fast moves are needed to fire each)
>
> It handles the tricky cases for you:
> • Tells regional variants apart by type (e.g. Galarian vs normal forms)
> • Reads the real species name even if you've nicknamed your Pokémon
>
> PRIVATE BY DESIGN
> Everything runs on your device. PvPeek captures a single screen frame only when you tap — the
> image and its text are analysed in memory and never stored, uploaded, or shared. Nothing from
> your screen ever leaves your phone. Optional, anonymous diagnostics help us fix bugs and improve
> accuracy, and you can switch them on or off anytime.
>
> HOW TO USE
> 1. Open PvPeek and grant the on-screen permissions.
> 2. Switch to the game and open a Pokémon's Appraisal.
> 3. Tap the floating button — your IVs, rank and moves appear.
> 4. Drag the button to move it, or drag it to the bottom of the screen to close.
>
> PvP data and rankings are derived from PvPoke.
>
> —
> PvPeek is an independent, unofficial tool. It is not affiliated with, endorsed by, or sponsored
> by Niantic, Nintendo, Game Freak, or The Pokémon Company. "Pokémon" and "Pokémon GO" are
> trademarks of their respective owners.

## Phone screenshots (TODO — min 2, up to 8)
Capture on a Flip (1080-wide). Best set, IP-light:
1. The **launch / privacy screen** (no game content).
2. The **About & Legal** screen.
3. A **result card** with the verdict (name, IVs, GL/UL rank, moves) — ideally over a neutral or
   blurred background rather than full game art.
(We can frame these into clean promo shots once captured.)

## Content rating questionnaire (answers)
- App category: Utility / Tools
- Violence, sexual content, profanity, drugs, gambling, UGC/social: **None**
- → Expected rating: **Everyone / PEGI 3**
(The crossed-swords icon is branding, not interactive content.)

## Target audience & content
- Target age group: **16+** (16–17 and 18+; matches the under-16 privacy policy + GDPR consent age)
- Appeals to children: No

## Data safety form (answers)
**Does the app collect or share user data?** Yes (diagnostics only; analytics is opt-in).
**Is all collected data encrypted in transit?** Yes.
**Do you provide a way to delete data?** Yes — via the contact email.

| Data type (Google category) | Collected | Shared | Optional | Purpose |
|---|---|---|---|---|
| Crash logs (App info & performance) | Yes | No | Yes (can opt out) | App functionality, diagnostics |
| Diagnostics (device model, OS/app version) | Yes | No | Yes | App functionality, analytics |
| Device or other IDs (Firebase app-instance / installation ID) | Yes | No | Yes | Analytics, app functionality |
| App activity — in-app actions (the per-scan `appraise_result` event) | Yes (only if opted in) | No | Yes | Analytics |

NOT collected: location, contacts, photos, messages, financial info, health, personal identifiers,
screen contents/images, or OCR text. Advertising IDs and ad-personalization are disabled.

## Other declarations
- Ads: **No**
- In-app purchases: **No**
- App access: all features available without login/special access — **Yes**
- Government / financial / news / health app: **No**

## ⚠️ Decide BEFORE first publish: the package name
The applicationId is currently **`com.pogopvp.overlay`** — it still contains "pogopvp", and a
package name **can never be changed once published**. Consider renaming to e.g. **`com.pvpeek.app`**
first. That requires re-registering the Android app in Firebase (new `google-services.json`, fresh
Crashlytics/Analytics), so it's a small chore best done now rather than never.
