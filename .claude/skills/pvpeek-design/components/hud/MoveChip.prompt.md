MoveChip is a recommended move row on the verdict card. `fast` moves show a turn count (`3t`); `charged` moves show fast-moves-to-charge with a bolt (`⚡4×`). Blue accent = fast, violet = charged.

```jsx
<MoveChip kind="fast" name="Quick Jab" count={3} recommended />
<MoveChip kind="charged" name="Surge Beam" count={4} recommended />
<MoveChip kind="charged" name="Iron Crash" count={6} />
```

`recommended` adds a colored inset bar + border. Counts are tabular mono.
