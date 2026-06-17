const NS = () => window.PvPeekDesignSystem_53b813;

const Chevron = () => (
  <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="var(--text-tertiary)" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M9 6l6 6-6 6"/></svg>
);
const ExternalIcon = () => (
  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="var(--text-tertiary)" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M14 4h6v6"/><path d="M20 4l-9 9"/><path d="M18 14v4a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4"/></svg>
);

function GroupLabel({ children }) {
  return <div style={{ fontFamily: 'var(--font-display)', fontSize: 11, fontWeight: 600, letterSpacing: '0.12em', textTransform: 'uppercase', color: 'var(--text-tertiary)', margin: '0 0 6px 2px' }}>{children}</div>;
}

/**
 * AboutScreen — version, affiliation disclaimer, privacy link,
 * data-choice toggles, and open-source attributions.
 */
function AboutScreen({ onBack, analytics, setAnalytics, crash, setCrash }) {
  const { ListRow, Switch, Badge } = NS();
  return (
    <div style={{ position: 'absolute', inset: 0, paddingTop: 36, display: 'flex', flexDirection: 'column', background: 'var(--bg-app)' }}>
      {/* app bar */}
      <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-3)', padding: 'var(--space-3) var(--space-4)', borderBottom: '1px solid var(--border-subtle)' }}>
        <button type="button" onClick={onBack} aria-label="Back" style={{ width: 36, height: 36, borderRadius: '50%', background: 'transparent', border: 'none', cursor: 'pointer', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="var(--text-primary)" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M15 6l-6 6 6 6"/></svg>
        </button>
        <h1 style={{ margin: 0, fontFamily: 'var(--font-display)', fontSize: 20, fontWeight: 700, color: 'var(--text-primary)' }}>About &amp; Legal</h1>
      </div>

      <div style={{ flex: 1, overflowY: 'auto', padding: 'var(--space-5) var(--space-5) var(--space-7)' }}>
        {/* identity */}
        <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-3)', marginBottom: 'var(--space-5)' }}>
          <img src="../../assets/logo-icon.svg" width="52" height="52" style={{ borderRadius: 14 }} alt="" />
          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
              <span style={{ fontFamily: 'var(--font-display)', fontSize: 20, fontWeight: 700, color: 'var(--white)' }}><span style={{ color: 'var(--gold)' }}>PvP</span>eek</span>
              <Badge tone="neutral">v1.4.0</Badge>
            </div>
            <div style={{ fontFamily: 'var(--font-mono)', fontSize: 12, color: 'var(--text-tertiary)', marginTop: 3 }}>build 1404 · on-device engine</div>
          </div>
        </div>

        {/* affiliation disclaimer */}
        <div style={{ background: 'var(--surface-inset)', border: '1px solid var(--border-subtle)', borderLeft: '3px solid var(--gold)', borderRadius: 'var(--radius-md)', padding: 'var(--space-4)', marginBottom: 'var(--space-5)' }}>
          <div style={{ fontFamily: 'var(--font-body)', fontSize: 13, fontWeight: 700, color: 'var(--text-primary)', marginBottom: 4 }}>Unofficial tool</div>
          <div style={{ fontFamily: 'var(--font-body)', fontSize: 13, lineHeight: 1.5, color: 'var(--text-tertiary)' }}>PvPeek is a fan-made utility. It is not affiliated with, endorsed by, or sponsored by any game publisher. All trademarks belong to their respective owners.</div>
        </div>

        {/* data choices */}
        <GroupLabel>Data choices</GroupLabel>
        <div style={{ background: 'var(--surface-card)', border: '1px solid var(--border-subtle)', borderRadius: 'var(--radius-lg)', padding: '4px var(--space-4)', marginBottom: 'var(--space-5)' }}>
          <ListRow label="Anonymous analytics" description="Aggregated taps & sessions. No screen data." trailing={<Switch checked={analytics} onChange={setAnalytics} />} />
          <ListRow label="Crash reports" description="Send diagnostics when the app crashes." trailing={<Switch checked={crash} onChange={setCrash} />} divider={false} />
        </div>

        {/* legal links */}
        <GroupLabel>Legal</GroupLabel>
        <div style={{ background: 'var(--surface-card)', border: '1px solid var(--border-subtle)', borderRadius: 'var(--radius-lg)', padding: '4px var(--space-4)', marginBottom: 'var(--space-5)' }}>
          <ListRow label="Privacy policy" trailing={<ExternalIcon />} onClick={() => {}} />
          <ListRow label="Terms of use" trailing={<ExternalIcon />} onClick={() => {}} />
          <ListRow label="Open-source licenses" description="42 packages" trailing={<Chevron />} onClick={() => {}} divider={false} />
        </div>

        <div style={{ textAlign: 'center', fontFamily: 'var(--font-body)', fontSize: 11, color: 'var(--text-disabled)', lineHeight: 1.5 }}>
          Made by indie devs for the competitive community.<br/>© 2026 PvPeek. All processing happens on your device.
        </div>
      </div>
    </div>
  );
}

Object.assign(window, { AboutScreen });
