# Roadmap — from friends build to a real Play Store app

## Where we are today
A working Android overlay (sideloaded to friends via Firebase App Distribution): a floating
**PvPeek button** → tap on an Appraisal → reads CP, name, IVs (pixel-analysed bars),
disambiguates regional variants by type, ignores nicknames (caught-line name), and shows
GL/UL rank + % + recommended moveset (with fast-move turns and charge counts). Crashlytics +
opt-in Analytics are wired in. Calibrated for **Galaxy Z Flip 5 / Flip 7** only.

Precedent that this *can* live on Play: **Poke Genie** and **Calcy IV** do essentially this.

## Status — Gate 1 (legal/branding) essentially DONE (updated 2026-06-15)
- ✅ Rebranded to **PvPeek**; new **crown-over-crossed-swords** icon (Colombian colours), all
  "Pokéball/PoGo" wording removed.
- ✅ Public repo: **github.com/dballesteros7/pvpeek** (MIT licensed; secrets gitignored).
- ✅ Privacy policy live (GDPR/CCPA/global, **opt-in consent** for analytics) →
  https://dballesteros7.github.io/pvpeek/privacy.html ; in-app About & Legal screen.
- ✅ Contact `privacy@pollera-colora.com`; **EU representative** Juan Carlos Vargas Carrillo
  (Germany), `privacy-eu@…` — mandate drafted (`private/`, to be signed).
- ✅ UK: "not available in the UK" notice (geo-restrict at listing instead of a UK rep).
- ⏳ Remaining in Gate 1: **Play Data Safety form**; sign the EU-rep mandate.

---

## The three gates (in "could block us" order)

### Gate 1 — Legal & branding  (low effort, mandatory)
- [ ] **Rebrand off Pokémon IP** — the Pokéball icon + "PoGo" name are Nintendo/Niantic/TPC
      trademarks → instant rejection. New generic name (e.g. "PvP IV Overlay"), non-Pokéball
      icon, and a visible **"Not affiliated with Niantic, Nintendo, or The Pokémon Company"**
      disclaimer (launch screen + store listing).
- [x] **PvPoke data license** — CONFIRMED **MIT** (not GPLv3). Permissive: use/modify/redistribute
      in a closed-source commercial app is fine. Sole obligation: **include PvPoke's MIT copyright
      + permission notice** in an in-app "Open-source licenses" screen, and credit PvPoke in the
      listing. (MIT covers PvPoke's data/code only — NOT the underlying Nintendo/Niantic IP, which
      the rebrand above handles.)
- [ ] **Privacy policy** (hosted URL) — required. Cover: screen content never leaves the device;
      analytics sends only derived signals (outcome/name_source/species/frame); Crashlytics.
- [ ] **Play Data Safety form** — declare the analytics/crash data we collect.
- [ ] **Niantic ToS risk** — screen-reading overlays are tolerated, not blessed. Accept that
      Niantic could complain / Google could pull it. Keep it strictly passive (no automation).

### Gate 2 — Play technical policy  (medium effort)
- [ ] **Drop `specialUse` foreground service** — Google rarely approves it. Revert to
      **consent-at-launch** (mediaProjection FGS for the whole session; the simpler, proven
      flow). UX tweak, not a rewrite. This also removes the riskiest Android-version code.
- [ ] **MediaProjection + SYSTEM_ALERT_WINDOW disclosure** — allowed for overlay tools with a
      prominent in-listing justification + in-app explanation (we already have the privacy text).
- [ ] **Release signing** — generate an upload keystore, enable **Play App Signing**.
- [ ] **Ship an AAB** (`bundleRelease`), not the debug APK.
- [ ] **Enable R8/minification** (`isMinifyEnabled = true`) + keep rules for ML Kit / Firebase /
      org.json; upload the mapping file to Crashlytics (`crashlytics:mappingfile:upload`).
- [ ] **Strip debug affordances** — the on-card `ERROR (stage)` text should be quieter in release.
- [ ] **New-account hoop** — a brand-new *personal* Play account must run a **closed test with
      12+ testers for 14 days** before production access. Start this clock early.

### Gate 3 — Works on phones beyond the Flips  (the big engineering lift)
Today the IV-bar *Y* is detected dynamically (robust), but the **CP/name/type/caught OCR boxes
and the bar X-extent are fixed fractions tuned to the Flip layout**. Other resolutions / aspect
ratios / GO UI scales will misread.
- [ ] **Auto-calibration / element detection** — find anchors on-screen instead of hardcoding:
      - CP: locate the "CP" glyph near the top, read digits to its right.
      - Name: largest text line on the card / parse the caught-line (already nickname-proof).
      - Type icons/text: detect the weight·type·height row.
      - IV bars: already dynamic on Y; make the **X-extent** dynamic too (find the rounded track).
  - Approach options: (a) heuristic anchor-finding from full-frame OCR + the bar detector we
    have; (b) per-device profiles keyed by resolution with a one-time in-app calibration wizard;
    (c) hybrid — auto by default, manual calibration fallback.
- [ ] **Test matrix** — a few real resolutions/aspect ratios (Pixel, a tablet, a 20:9 phone).

### Gate 4 — Data freshness  (medium effort)
Bundled `gamemaster`/`rankings`/`recommended_moves` freeze in time; GO reshuffles every season
+ rebalance.
- [ ] **Host the JSON** (tiny static bucket / GitHub raw / Firebase Hosting) and have the app
      **fetch + cache** on launch, falling back to the bundled snapshot offline.
- [ ] **A regeneration script** (we already have the Python that builds the 3 assets) run on a
      schedule to refresh the hosted files when PvPoke updates.

---

## Suggested sequencing
1. **Play on-ramp** (Gate 1 + Gate 2): rebrand, privacy policy, consent-at-launch, signing/R8/AAB,
   stand up a **closed-testing track** → starts the 14-day clock and surfaces policy issues while
   stakes are low. Move the same friends off Firebase onto Play.
2. **Multi-device robustness** (Gate 3) — the work that makes it usable by strangers.
3. **Data freshness** (Gate 4) — so it survives seasons.
4. **Launch** — store listing (screenshots, feature graphic), promote closed → production.

## Decisions (resolved)
| Decision | Resolution |
|---|---|
| App name | **PvPeek** ✅ |
| Icon | **crown over crossed swords**, Colombian colours ✅ |
| Data hosting | privacy policy on **GitHub Pages**; gamemaster/rankings still bundled (Gate 4 TBD) |
| Calibration | hybrid (auto + manual wizard fallback) — Gate 3, not yet built |
| Monetization | free first |
| Who owns Play account | the $25 account + console clicks are the owner's (Diego) |

## Risk register
- **IP/trademark** → mitigated by rebrand + disclaimer (precedent exists). ✅
- **Fork reuses our policy/PII** → mitigated by the "Forking & reuse" carve-out in README + policy;
  to fully eliminate, host the policy off the public repo (optional).
- **`specialUse` rejection** → mitigated by switching to consent-at-launch (still TODO).
- **Niantic pulls/complains** → inherent, low-likelihood; stay passive, no automation.
- **Multi-device misreads** → the main quality risk; Gate 3 addresses it; ship with a clear
  "calibrate" fallback.
- **Data staleness** → Gate 4; until then, a stale-data disclaimer.

## Pre-public-launch checklist (remaining)
- [ ] **Host the privacy policy off the public repo** — move the live policy to an external host
      (own domain `pollera-colora.com` or Firebase Hosting) so contacts/names/EU-rep PII aren't in
      forks; leave only a placeholder template in-repo; update `LegalActivity.PRIVACY_URL` + listing.
- [ ] **Play Data Safety form** answers (collects: crash + opt-in analytics; no PII/location).
- [ ] **Sign the EU-representative mandate** (`private/EU-Representative-Mandate.md`).
- [ ] **Drop `specialUse`** → consent-at-launch (Gate 2).
- [ ] **Release signing** (upload keystore + Play App Signing), **R8/minify**, ship an **AAB**.
- [ ] **Create the Play account** + a **closed-testing track** (starts the 14-day clock).
- [ ] **UK geo-restriction** in the Play Console listing.
- [ ] **Multi-device robustness** (Gate 3) — the big engineering lift before strangers use it.
- [ ] **Data freshness** (Gate 4) — host + fetch the gamemaster/rankings JSON.
- [ ] Final privacy review sign-off by Tania / JC.
