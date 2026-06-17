import React from 'react';

/** Map an IV value (0–15) to a rank-tier color + short label. */
export function ivTier(value) {
  if (value >= 15) return { color: 'var(--tier-perfect)', label: 'MAX' };
  if (value >= 13) return { color: 'var(--tier-great)', label: 'HIGH' };
  if (value >= 10) return { color: 'var(--tier-good)', label: 'OK' };
  return { color: 'var(--tier-fair)', label: 'LOW' };
}

/**
 * IVStat — one of the three IVs (Attack / Defense / Stamina), 0–15.
 * Renders a label, the value in tabular mono, and a 15-segment bar
 * tinted by the value's tier color. Built for fast scanning.
 */
export function IVStat({ label, value, max = 15, style = {} }) {
  const tier = ivTier(value);
  const segs = Array.from({ length: max }, (_, i) => i < value);
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 6, minWidth: 0, ...style }}>
      <div style={{ display: 'flex', alignItems: 'baseline', justifyContent: 'space-between', gap: 6 }}>
        <span style={{
          fontFamily: 'var(--font-display)',
          fontSize: 11,
          fontWeight: 600,
          letterSpacing: '0.08em',
          color: 'var(--text-tertiary)',
        }}>{label}</span>
        <span style={{
          fontFamily: 'var(--font-mono)',
          fontVariantNumeric: 'tabular-nums',
          fontSize: 18,
          fontWeight: 700,
          lineHeight: 1,
          color: tier.color,
        }}>{value}</span>
      </div>
      <div style={{ display: 'flex', gap: 2, height: 6 }}>
        {segs.map((on, i) => (
          <span key={i} style={{
            flex: 1,
            borderRadius: 1,
            background: on ? tier.color : 'rgba(255,255,255,0.10)',
            boxShadow: on && value >= 15 ? '0 0 4px rgba(252,209,22,0.6)' : 'none',
          }} />
        ))}
      </div>
    </div>
  );
}
