const NS = () => window.PvPeekDesignSystem_53b813;

// Local copy of the rank-tier mapping (lowercase exports aren't exposed on the namespace).
function tierColor(percent) {
  if (percent >= 99) return 'var(--tier-perfect)';
  if (percent >= 95) return 'var(--tier-great)';
  if (percent >= 88) return 'var(--tier-good)';
  if (percent >= 75) return 'var(--tier-fair)';
  return 'var(--tier-weak)';
}

function Section({ label, right, children }) {
  return (
    <div style={{ marginTop: 'var(--space-4)' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'baseline', marginBottom: 'var(--space-2)' }}>
        <span style={{ fontFamily: 'var(--font-display)', fontSize: 10, fontWeight: 600, letterSpacing: '0.14em', textTransform: 'uppercase', color: 'var(--text-tertiary)' }}>{label}</span>
        {right}
      </div>
      {children}
    </div>
  );
}

function IconBtn({ label, onClick, children }) {
  return (
    <button type="button" onClick={onClick} aria-label={label} style={{
      width: 28, height: 28, borderRadius: '50%', flex: '0 0 auto',
      background: 'var(--fill-ghost-hover)', border: '1px solid var(--border-subtle)', color: 'var(--text-secondary)',
      cursor: 'pointer', display: 'flex', alignItems: 'center', justifyContent: 'center',
    }}>{children}</button>
  );
}

function MiniStat({ label, value, color, sub }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 3, minWidth: 0 }}>
      <span style={{ fontFamily: 'var(--font-display)', fontSize: 9, fontWeight: 600, letterSpacing: '0.1em', color: 'var(--text-tertiary)' }}>{label}</span>
      <span style={{ fontFamily: 'var(--font-mono)', fontVariantNumeric: 'tabular-nums', fontSize: 15, fontWeight: 700, lineHeight: 1, color }}>{value}</span>
      {sub && <span style={{ fontFamily: 'var(--font-mono)', fontVariantNumeric: 'tabular-nums', fontSize: 10, fontWeight: 600, lineHeight: 1, color: 'var(--text-tertiary)', whiteSpace: 'nowrap' }}>{sub}</span>}
    </div>
  );
}

/**
 * VerdictCard — the hero overlay, as an EXPANDABLE bottom sheet.
 *  • compact (expanded=false): a slim peek — identity + CP + the three
 *    headline numbers (IV%, Great %, Ultra %), tier-colored. The game
 *    stays visible all around it; no full-screen scrim.
 *  • expanded (expanded=true): full detail — IV bars, both rank rows,
 *    the recommended moveset. Tap the chevron to toggle.
 */
function VerdictCard({ specimen, expanded = false, onToggle, onClose }) {
  const { Card, Badge, IVStat, RankBadge, MoveChip } = NS();
  const d = specimen || VerdictCard.sample;
  const ivColor = tierColor(d.ivPercent);
  const glColor = tierColor(d.great.percent);
  const ulColor = tierColor(d.ultra.percent);

  const Header = (
    <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
      <img src="../../assets/logo-mark.svg" width="26" height="26" alt="" style={{ flex: '0 0 auto' }} />
      <div style={{ flex: 1, minWidth: 0 }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
          <span style={{ fontFamily: 'var(--font-display)', fontSize: 16, fontWeight: 700, color: 'var(--text-primary)', letterSpacing: '-0.01em', whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>{d.name}</span>
          {d.hundo && <Badge tone="gold" solid>Hundo</Badge>}
        </div>
        <div style={{ display: 'flex', alignItems: 'baseline', gap: 5, marginTop: 1 }}>
          <span style={{ fontFamily: 'var(--font-mono)', fontVariantNumeric: 'tabular-nums', fontSize: 18, fontWeight: 700, color: 'var(--white)', lineHeight: 1 }}>{d.cp.toLocaleString()}</span>
          <span style={{ fontFamily: 'var(--font-display)', fontSize: 11, fontWeight: 600, color: 'var(--text-tertiary)', letterSpacing: '0.06em' }}>CP</span>
          <span style={{ fontFamily: 'var(--font-mono)', fontSize: 11, color: 'var(--text-tertiary)', marginLeft: 6, whiteSpace: 'nowrap' }}>Lv {d.level}</span>
        </div>
      </div>
      <IconBtn label={expanded ? 'Collapse' : 'Expand'} onClick={onToggle}>
        <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round" style={{ transform: expanded ? 'rotate(180deg)' : 'none', transition: 'transform var(--dur-base) var(--ease-out)' }}><path d="M6 15l6-6 6 6"/></svg>
      </IconBtn>
      <IconBtn label="Dismiss" onClick={onClose}>
        <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.4" strokeLinecap="round"><path d="M6 6l12 12M18 6L6 18"/></svg>
      </IconBtn>
    </div>
  );

  return (
    <Card variant="overlay" padding="14px 16px" style={{ width: 340, position: 'relative' }}>
      {/* grab handle */}
      <div style={{ width: 34, height: 4, borderRadius: 2, background: 'var(--border-strong)', margin: '-4px auto 12px' }} />
      {Header}

      {/* COMPACT: headline row — IV%, plus each league's RANK + percentile */}
      {!expanded && (
        <div style={{ display: 'flex', alignItems: 'flex-start', gap: 'var(--space-3)', marginTop: 'var(--space-3)', paddingTop: 'var(--space-3)', borderTop: '1px solid var(--border-subtle)' }}>
          <MiniStat label="IV" value={`${d.ivPercent}%`} color={ivColor} />
          <div style={{ width: 1, alignSelf: 'stretch', background: 'var(--border-subtle)' }} />
          <MiniStat label="GREAT" value={`#${d.great.rank}`} color={glColor} sub={`${d.great.percent}% · ${d.great.total}`} />
          <MiniStat label="ULTRA" value={`#${d.ultra.rank}`} color={ulColor} sub={`${d.ultra.percent}% · ${d.ultra.total}`} />
          <button type="button" onClick={onToggle} aria-label="Expand" style={{ marginLeft: 'auto', alignSelf: 'center', background: 'none', border: 'none', cursor: 'pointer', color: 'var(--text-tertiary)', display: 'flex', alignItems: 'center', padding: 4 }}>
            <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.2" strokeLinecap="round"><path d="M9 6l6 6-6 6"/></svg>
          </button>
        </div>
      )}

      {/* EXPANDED: full detail */}
      {expanded && (
        <>
          <Section label="IVs" right={<span style={{ fontFamily: 'var(--font-mono)', fontSize: 13, fontWeight: 700, color: ivColor }}>{d.ivPercent}%</span>}>
            <div style={{ display: 'flex', gap: 'var(--space-4)' }}>
              <IVStat label="ATK" value={d.iv.atk} />
              <IVStat label="DEF" value={d.iv.def} />
              <IVStat label="STA" value={d.iv.sta} />
            </div>
          </Section>

          <Section label="League ranks">
            <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-2)' }}>
              <RankBadge league="Great League" rank={d.great.rank} total={d.great.total} percent={d.great.percent} />
              <RankBadge league="Ultra League" rank={d.ultra.rank} total={d.ultra.total} percent={d.ultra.percent} />
            </div>
          </Section>

          <Section label="Recommended moveset">
            <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-2)' }}>
              <MoveChip kind="fast" name={d.moves.fast.name} count={d.moves.fast.turns} recommended />
              <div style={{ display: 'flex', gap: 'var(--space-2)' }}>
                {d.moves.charged.map((m, i) => (
                  <div key={i} style={{ flex: 1, minWidth: 0 }}>
                    <MoveChip kind="charged" name={m.name} count={m.count} recommended={i === 0} />
                  </div>
                ))}
              </div>
            </div>
          </Section>

          <div style={{ display: 'flex', alignItems: 'center', gap: 6, marginTop: 'var(--space-4)', paddingTop: 'var(--space-3)', borderTop: '1px solid var(--border-subtle)' }}>
            <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="var(--text-tertiary)" strokeWidth="2"><rect x="5" y="11" width="14" height="9" rx="2"/><path d="M8 11V8a4 4 0 0 1 8 0v3"/></svg>
            <span style={{ fontFamily: 'var(--font-body)', fontSize: 11, color: 'var(--text-tertiary)' }}>On-device · read in 0.4s · nothing left your phone</span>
          </div>
        </>
      )}
    </Card>
  );
}

VerdictCard.sample = {
  name: 'Specimen-12',
  cp: 1498,
  level: 31.5,
  hundo: false,
  ivPercent: 98.2,
  iv: { atk: 15, def: 14, sta: 13 },
  great: { rank: 12, total: 4096, percent: 99.2 },
  ultra: { rank: 188, total: 4096, percent: 95.4 },
  moves: {
    fast: { name: 'Quick Jab', turns: 3 },
    charged: [ { name: 'Surge Beam', count: 4 }, { name: 'Iron Crash', count: 6 } ],
  },
};

Object.assign(window, { VerdictCard });
