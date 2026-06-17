# PvPeek — Overlay UI kit

The hero surface: a glanceable **verdict card** that floats on top of an arbitrary, busy game, plus the **floating button** that summons it and the **drag-to-dismiss** target.

## Screens / states
- `index.html` — interactive demo over an abstract busy background:
  - **Tap** the floating button → scanning state → verdict card appears over a strong scrim.
  - **Drag** the button — it snaps to either screen edge.
  - Drag it to the **bottom target** to dismiss (the target arms red when hovered).

## Components
- `VerdictCard.jsx` — composes `Card` (overlay) + `IVStat` ×3 + `RankBadge` ×2 + `MoveChip` from the design system. Hierarchy: identity + CP → IVs → league ranks → moveset → on-device footer.
- `FloatingButton.jsx` — 56px circular trigger with the crown-swords mark; `idle` and `scanning` (rotating ring + pulse) states.
- `DismissTarget.jsx` — fades in while dragging; `armed` grows + turns red.
- `BusyBackground.jsx` — **abstract** CSS-only "a game is running" backdrop. No real game art — used purely to prove scrim legibility.
- `../shared/PhoneFrame.jsx` — shared 360×760 Android shell.

## Notes
- The verdict card always sits over `--scrim-overlay` + `backdrop-filter: blur` so it reads over any colors.
- Numbers (CP, IVs, ranks, turn counts) use tabular mono so they never jitter as values change.
- No Pokémon / game IP: placeholder creature is "Specimen-12"; background is abstract.
