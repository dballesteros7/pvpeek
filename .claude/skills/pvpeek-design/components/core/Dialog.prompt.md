Dialog is a centered modal over a dimmed scrim — the first-run consent opt-in and small confirmations. Renders absolutely within its positioned parent (a phone frame), not the whole viewport.

```jsx
<Dialog
  open={showConsent}
  title="Help improve PvPeek?"
  actions={<>
    <Button variant="ghost" onClick={decline}>No thanks</Button>
    <Button variant="primary" onClick={accept}>Allow</Button>
  </>}
  onDismiss={decline}
>
  Send anonymous, aggregated usage stats. Never anything from your screen.
</Dialog>
```

Backdrop tap calls `onDismiss`. Put 1–2 Buttons in `actions`.
