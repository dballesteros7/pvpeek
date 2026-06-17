const NS = () => window.PvPeekDesignSystem_53b813;

/**
 * ConsentDialog — first-run anonymous-analytics opt-in. Plain language,
 * symmetric choice, no dark patterns. "No thanks" is a real option.
 */
function ConsentDialog({ open, onAllow, onDecline }) {
  const { Dialog, Button } = NS();
  return (
    <Dialog
      open={open}
      width={320}
      onDismiss={onDecline}
      icon={
        <div style={{ width: 46, height: 46, borderRadius: 'var(--radius-md)', background: 'rgba(91,157,255,0.14)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="var(--blue-bright)" strokeWidth="2"><path d="M3 12h4l3 8 4-16 3 8h4"/></svg>
        </div>
      }
      title="Help improve PvPeek?"
      actions={
        <>
          <Button variant="secondary" fullWidth onClick={onDecline}>No thanks</Button>
          <Button variant="primary" fullWidth onClick={onAllow}>Allow</Button>
        </>
      }
    >
      Share anonymous, aggregated usage stats (taps, crashes) so we can prioritize fixes. We <b style={{ color: 'var(--text-secondary)' }}>never</b> collect anything from your screen, and you can change this anytime in Settings.
    </Dialog>
  );
}

Object.assign(window, { ConsentDialog });
