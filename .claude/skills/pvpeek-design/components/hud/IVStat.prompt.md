IVStat shows one of the three IVs (0–15) with a 15-segment bar tinted by tier — gold at 15 (with a glow), green 13–14, blue 10–12, slate below. Use three side-by-side in the verdict card.

```jsx
<div style={{ display: 'flex', gap: 'var(--space-4)' }}>
  <IVStat label="ATK" value={15} />
  <IVStat label="DEF" value={14} />
  <IVStat label="STA" value={13} />
</div>
```

Value renders in tabular mono. `ivTier(value)` is exported if you need the color elsewhere.
