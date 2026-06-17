const NS = () => window.PvPeekDesignSystem_53b813;

/**
 * LaunchScreen — first-run / onboarding. Branding, one-line value prop,
 * a short privacy explainer, and the primary "Start" action.
 */
function LaunchScreen({ onStart }) {
  const { Button } = NS();
  return (
    <div style={{ position: 'absolute', inset: 0, paddingTop: 36, display: 'flex', flexDirection: 'column', background: 'radial-gradient(120% 90% at 50% -10%, #232838 0%, var(--bg-app) 55%)' }}>
      {/* brand */}
      <div style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', padding: '0 28px', textAlign: 'center' }}>
        <img src="../../assets/logo-icon.svg" width="96" height="96" alt="PvPeek" style={{ borderRadius: 24, boxShadow: 'var(--elev-3), var(--glow-gold)' }} />
        <div style={{ fontFamily: 'var(--font-display)', fontSize: 34, fontWeight: 700, letterSpacing: '-0.01em', marginTop: 'var(--space-5)', color: 'var(--white)' }}>
          <span style={{ color: 'var(--gold)' }}>PvP</span>eek
        </div>
        <div style={{ fontFamily: 'var(--font-display)', fontSize: 14, fontWeight: 600, letterSpacing: '0.18em', textTransform: 'uppercase', color: 'var(--text-tertiary)', marginTop: 6 }}>
          Glance · Verdict · Done
        </div>
        <p style={{ fontFamily: 'var(--font-body)', fontSize: 15, lineHeight: 1.5, color: 'var(--text-secondary)', maxWidth: 260, marginTop: 'var(--space-5)' }}>
          Tap once over your game and read a creature's IVs, league ranks, and best moveset in under two seconds.
        </p>
      </div>

      {/* privacy explainer + CTA */}
      <div style={{ padding: '0 var(--space-5) var(--space-7)' }}>
        <div style={{ display: 'flex', gap: 'var(--space-3)', alignItems: 'flex-start', background: 'var(--surface-card)', border: '1px solid var(--border-subtle)', borderRadius: 'var(--radius-lg)', padding: 'var(--space-4)', marginBottom: 'var(--space-4)' }}>
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="var(--gold)" strokeWidth="2" style={{ flex: '0 0 auto', marginTop: 1 }}><rect x="4" y="11" width="16" height="10" rx="2"/><path d="M8 11V7a4 4 0 0 1 8 0v4"/></svg>
          <div>
            <div style={{ fontFamily: 'var(--font-body)', fontSize: 14, fontWeight: 700, color: 'var(--text-primary)' }}>Private by design</div>
            <div style={{ fontFamily: 'var(--font-body)', fontSize: 13, lineHeight: 1.45, color: 'var(--text-tertiary)', marginTop: 2 }}>Everything runs on-device. Nothing from your screen ever leaves your phone.</div>
          </div>
        </div>
        <Button variant="primary" size="lg" fullWidth onClick={onStart}>Start PvPeek</Button>
        <div style={{ textAlign: 'center', marginTop: 'var(--space-3)' }}>
          <span style={{ fontFamily: 'var(--font-body)', fontSize: 12, color: 'var(--text-tertiary)' }}>You'll be asked to allow “draw over other apps”.</span>
        </div>
      </div>
    </div>
  );
}

Object.assign(window, { LaunchScreen });
