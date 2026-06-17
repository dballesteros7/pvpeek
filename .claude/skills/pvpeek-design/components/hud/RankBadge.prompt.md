RankBadge is one league standing on the verdict card — league name, `#rank / total`, and the tier-colored percentile with a tier label. The percent drives the color (≥99 elite gold, ≥95 great green, ≥88 good blue, ≥75 fair slate, else weak).

```jsx
<RankBadge league="Great League" rank={12} total={4096} percent={99.2} />
<RankBadge league="Ultra League" rank={188} total={4096} percent={96.4} />
```

Left accent bar + percentage share the tier color so it reads in one glance. All figures are tabular mono. `rankTier(percent)` is exported.
