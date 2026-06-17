---
name: pvpeek-design
description: Use this skill to generate well-branded interfaces and assets for PvPeek, either for production or throwaway prototypes/mocks/etc. Contains essential design guidelines, colors, type, fonts, assets, and UI kit components for prototyping.
user-invocable: true
---

Read the `readme.md` file within this skill, and explore the other available files.
If creating visual artifacts (slides, mocks, throwaway prototypes, etc), copy assets out and create static HTML files for the user to view. If working on production code, you can copy assets and read the rules here to become an expert in designing with this brand.
If the user invokes this skill without any other guidance, ask them what they want to build or design, ask some questions, and act as an expert designer who outputs HTML artifacts _or_ production code, depending on the need.

## Quick orientation
- **Brand:** PvPeek — an on-device, dark-theme-first Android HUD utility for competitive monster-battler players. Glanceable verdict cards over a live game. Privacy-first ("nothing leaves your phone"). Esports stat-overlay aesthetic.
- **Tokens:** `styles.css` → `tokens/` (colors, typography, spacing, fonts). Link `styles.css` and use the CSS custom properties (`--gold`, `--surface-card`, `--tier-perfect`, `--font-mono`, …).
- **Rank-tier color language:** ELITE ≥99% gold · GREAT 95–99% green · GOOD 88–95% blue · FAIR 75–88% slate · WEAK <75% muted. Reuse it for any quality/score readout.
- **Type:** Chakra Petch (display) · Manrope (body) · JetBrains Mono w/ tabular-nums (all figures).
- **Components:** bundled at `window.PvPeekDesignSystem_53b813` (`Button`, `Switch`, `Card`, `Dialog`, `ListRow`, `Badge`, `IVStat`, `RankBadge`, `MoveChip`). Load `_ds_bundle.js` after React.
- **Assets:** `assets/logo-mark.svg`, `assets/logo-icon.svg`. Icons: Lucide (CDN).
- **UI kits:** `ui_kits/overlay/` (verdict card + floating button) and `ui_kits/app/` (launch, consent, about).

## Rules
- Dark base, accents sparingly. Gold = primary. Numbers are tabular and lead the hierarchy.
- Overlay surfaces over arbitrary backgrounds MUST use a strong scrim + blur.
- No Pokémon / game IP. Use neutral placeholders ("Specimen-12") and abstract backgrounds.
- Fonts are Google-hosted substitutes — flag this if producing production assets.
