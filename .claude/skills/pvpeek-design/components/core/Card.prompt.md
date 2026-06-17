Card is the surface container. `default` for normal panels, `inset` for sunken sub-panels, `overlay` for the verdict card floating over arbitrary game art (strong shadow + stronger border).

```jsx
<Card variant="overlay" padding="var(--space-5)">…verdict…</Card>
<Card variant="inset" padding="var(--space-4)">…stat block…</Card>
```

The `overlay` variant uses `--radius-xl` and `--elev-overlay`. Always place it over a `--scrim-overlay` backdrop when it sits on unpredictable backgrounds.
