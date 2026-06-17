# PvPeek Design System

A cohesive, dark-theme-first visual system for **PvPeek** — an Android utility for competitive monster-battler players. A small floating button sits on top of the game; one tap shows a **verdict card** (a creature's IVs, league ranks, and best moveset) that the player absorbs in under two seconds, then dismisses. Everything runs on-device; **nothing from the screen ever leaves the phone**.

The aesthetic is a **sleek esports stat-overlay / HUD**: dark slate base, sharp technical type, tabular numerals, and Colombian-heritage accents (gold, blue, red) used sparingly for emphasis.

> **IP note:** PvPeek is an *unofficial* tool. This system uses **no** Pokémon / Pokémon GO artwork, real creature names, or game UI. Mockups use a neutral placeholder ("Specimen-12") and abstract backgrounds.

## Sources
This system was designed from a written brief — **no codebase or Figma file was provided**. There are therefore no external source links to record. If a real product repo or Figma exists, attach it and this README should be updated to reference it (full URLs / paths) so future work can cross-check against the source of truth.

---

## Content fundamentals
**Voice:** confident, fast, competitive — like a good coach who respects your time. Short declaratives. No hype, no fluff.

- **Tense & person:** address the player as **you** ("read in 0.4s", "nothing leaves your phone"). The app refers to itself as **PvPeek** or **we** only in consent/legal copy.
- **Casing:** Sentence case for body and most labels. **Display font for short labels is UPPERCASE with wide tracking** (overlines, tier names, stat labels: `IVS`, `ELITE`, `GREAT LEAGUE`). Button labels are Title-ish but short ("Start PvPeek", "No thanks").
- **Numbers lead.** The product is numeric — surface the figure first, the words second ("**99.2%** ELITE", "**#12** / 4096"). Always tabular.
- **Tagline:** `Glance · Verdict · Done.` Mid-dots separate beats.
- **Privacy is a recurring promise**, stated plainly and repeatedly: "On-device", "Nothing from your screen leaves your phone".
- **No dark patterns.** Consent copy is symmetric and honest ("No thanks" is a real, equally-weighted choice). Legal copy is direct: "PvPeek is a fan-made utility. It is not affiliated with…".
- **Emoji:** avoid in UI chrome. A single `⚡` glyph is used *functionally* as the charged-move/energy unit on MoveChip — that is the one exception, and it is part of a data readout, not decoration.
- **Examples:** "Tap once over your game and read a creature's IVs, league ranks, and best moveset in under two seconds." · "Aggregated taps & sessions. No screen data." · "Drag here to hide".

---

## Visual foundations
**Mood:** dark esports HUD — high contrast, sharp, fast. Color is scarce and meaningful; most of the screen is slate, and accents earn attention.

- **Color:** Built on a slate/navy ramp (`#0E0F12 → #15161A → #262B36 …`). Brand accents are the Colombian flag set — **gold `#F4C430`/`#FCD116`**, **blue `#003893`** (legible UI blue `#2E6BE6`/`#5B9DFF`), **red `#CE1126`** (`#FF4D5E` on dark). Gold is the primary accent (crown, primary button, peak emphasis). See `tokens/colors.css`.
- **Rank-tier system (the core visual language):** a percentile maps to a color the player learns to read instantly — **ELITE ≥99% gold**, **GREAT 95–99% green**, **GOOD 88–95% blue**, **FAIR 75–88% slate**, **WEAK <75% muted**. Used on IV bars, rank percentages, and tier labels.
- **Typography:** display **Chakra Petch** (sharp, technical, gamer) for headings/labels/buttons; body **Manrope** (clean, neutral); **JetBrains Mono** with `tabular-nums` for all figures (CP, IVs, ranks, turn counts). Type scale is mobile/HUD-oriented (display 34 → micro 10). See `tokens/typography.css`.
- **Spacing & radius:** 4px base grid. Radii: controls `12px`, cards `16px`, the verdict card / sheets `22px`, pills full. See `tokens/spacing.css`.
- **Backgrounds:** flat dark slate, occasionally a subtle radial glow behind the brand on the launch screen. **No photographic imagery, no busy gradients in chrome.** The only "busy" surface is the *game behind the overlay*, which is arbitrary and not ours — which is exactly why the verdict card carries a heavy scrim.
- **Cards:** flat slate fill, hairline border (`rgba(255,255,255,0.06–0.16)`), soft shadow. The **overlay** variant adds a strong wide shadow (`--elev-overlay`) + stronger border + blur so it floats legibly over anything. No colored-left-border-only "alert card" trope (except the deliberate tier accent bar on `RankBadge` / the disclaimer block, where the color is semantic).
- **Elevation:** cool, dark shadows. The floating button adds a faint **gold glow** for findability over bright game art. See the Elevation card.
- **Motion:** fast and snappy (`90–320ms`), decelerating `--ease-out` for entrances, a subtle `--ease-spring` overshoot for toggles and the verdict pop-in. The scanning state uses a rotating ring + pulse + a one-shot scan sweep. Respect reduced-motion. Nothing loops decoratively.
- **States:** hover = subtle lighten / ghost fill; **press = scale 0.97** + darker accent; focus = blue focus ring. Disabled = 40% opacity.
- **Transparency & blur:** reserved for surfaces that sit over unpredictable content — the verdict scrim (`rgba(10,11,14,.82)` + blur) and the dialog scrim (`+blur(2px)`). Chrome itself is opaque.
- **Imagery vibe:** none photographic; the visual identity is the **crown-swords mark** + the tier-color HUD. Cool, dark, neon-accent palette.

---

## Iconography
- **System:** [Lucide](https://lucide.dev) — 2px stroke, rounded caps/joins. It matches the sharp-but-friendly HUD tone. **Substitution flag:** no icon set was provided in a brief-only project, so Lucide is the chosen standard; swap it if the product adopts another set.
- **Loading:** link Lucide from CDN (`unpkg.com/lucide`) — see `foundations/iconography.html`. A handful of tiny, geometric icons (chevron, back arrow, external-link, lock, close ✕, energy bolt) are inlined as SVG in the kit screens; they intentionally match Lucide's stroke weight and corner style.
- **Default color** is `--text-secondary` (slate-300); accent icons use `--gold`. Touch targets stay ≥44px even when the glyph is 22–26px.
- **Key brand glyphs:** `crown` (ranking/champion), `swords` (battle), `eye`/`scan-line` (peek/scan), `zap` (charged move), `shield` (defense), `lock` (privacy).
- **Emoji:** not used as UI icons. The one `⚡` on MoveChip is a functional data unit (see Content fundamentals).
- **Logo:** the brand mark is a hand-authored SVG (`assets/logo-mark.svg`, `assets/logo-icon.svg`) — gold crown over crossed blue+red swords on dark slate. Do **not** redraw it; reference these files.

---

## Index / manifest
**Root**
- `styles.css` — global entry point (imports only). Consumers link this.
- `tokens/` — `colors.css`, `typography.css`, `spacing.css` (spacing + radius + elevation + motion), `fonts.css` (Google-hosted webfonts — see caveat below).
- `assets/` — `logo-mark.svg` (mark), `logo-icon.svg` (app-icon tile).
- `readme.md` (this file) · `SKILL.md` (Agent-Skill wrapper).

**Foundations** (`foundations/` — Design System tab cards)
- Colors: slate ramp, brand, rank-tier scale, semantic & move.
- Type: display, body, numerics, scale.
- Spacing: scale, radius, elevation.
- Brand: logo, logo-on-backgrounds, iconography.

**Components** (`components/` — bundled to `window.PvPeekDesignSystem_53b813`)
- `core/` — `Button`, `Switch`, `Card`, `Dialog`, `ListRow`, `Badge`.
- `hud/` — `IVStat`, `RankBadge`, `MoveChip` (the verdict-card readout primitives). Each exports a tier helper (`ivTier`, `rankTier`).

**UI kits** (`ui_kits/`)
- `overlay/` — verdict card over a busy background, floating button, drag-to-dismiss (the hero).
- `app/` — launch / onboarding, consent dialog, About & Legal.
- `shared/PhoneFrame.jsx` — device shell used by both kits.

---

## Caveats / substitutions
- **Fonts are Google-hosted substitutes** (Chakra Petch, Manrope, JetBrains Mono) loaded via `tokens/fonts.css`. They match the intended personality but are not licensed brand fonts. The DS compiler reports "0 fonts" because it doesn't follow the external `@import` — this is expected; fonts load at runtime. Drop real font files in `assets/fonts/` and swap the `@import` for `@font-face` rules if/when available.
- **Iconography is Lucide** (chosen, not extracted) — see Iconography.
- The **logo** is an original interpretation of the described mark (gold crown + crossed swords); refine if there's an existing brand asset.
