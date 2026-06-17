Button is PvPeek's tappable action control — gold `primary` for the single key action on a screen, `secondary`/`ghost` for everything else, `danger` for destructive actions.

```jsx
<Button variant="primary" size="lg" fullWidth onClick={start}>Start PvPeek</Button>
<Button variant="ghost">Maybe later</Button>
<Button variant="secondary" iconLeft={<span>↗</span>}>Privacy policy</Button>
```

Variants: `primary` (gold fill, dark text), `secondary` (raised slate), `ghost` (transparent), `danger` (red text). Sizes `sm | md | lg`. Display font, slight press-shrink, fast transitions. Use exactly one `primary` per screen.
