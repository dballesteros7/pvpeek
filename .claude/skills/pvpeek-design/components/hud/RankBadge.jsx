import React from 'react';

/** Map a rank percentage (0–100) to a tier color + label. */
export function rankTier(percent) {
  if (percent >= 99) return { color: 'var(--tier-perfect)', label: 'ELITE' };
  if (percent >= 95) return { color: 'var(--tier-great)', label: 'GREAT' };
  if (percent >= 88) return { color: 'var(--tier-good)', label: 'GOOD' };
  if (percent >= 75) return { color: 'var(--tier-fair)', label: 'FAIR' };
  return { color: 'var(--tier-weak)', label: 'WEAK' };
}

/**
 * RankBadge — a league standing for the verdict card:
 * league name, rank "#12 / 4096", and the tier-colored percentage.
 */
export function RankBadge({ league, rank, total, percent, style = {} }) {
  const tier = rankTier(percent);
  return (
    <div style={{
      display: 'flex',
      alignItems: 'center',
      gap: 'var(--space-3)',
      padding: 'var(--space-3)',
      borderRadius: 'var(--radius-md)',
      background: 'var(--surface-inset)',
      borderLeft: `3px solid ${tier.color}`,
      ...style,
    }}>
      <div style={{ flex: 1, minWidth: 0 }}>
        <div style={{
          fontFamily: 'var(--font-display)',
          fontSize: 11,
          fontWeight: 600,
          letterSpacing: '0.08em',
          textTransform: 'uppercase',
          color: 'var(--text-tertiary)',
          marginBottom: 3,
        }}>{league}</div>
        <div style={{ display: 'flex', alignItems: 'baseline', gap: 4 }}>
          <span style={{
            fontFamily: 'var(--font-mono)',
            fontVariantNumeric: 'tabular-nums',
            fontSize: 20,
            fontWeight: 700,
            lineHeight: 1,
            color: 'var(--text-primary)',
          }}>#{rank}</span>
          <span style={{
            fontFamily: 'var(--font-mono)',
            fontVariantNumeric: 'tabular-nums',
            fontSize: 12,
            color: 'var(--text-tertiary)',
          }}>/ {total}</span>
        </div>
      </div>
      <div style={{ textAlign: 'right' }}>
        <div style={{
          fontFamily: 'var(--font-mono)',
          fontVariantNumeric: 'tabular-nums',
          fontSize: 18,
          fontWeight: 700,
          lineHeight: 1,
          color: tier.color,
        }}>{percent.toFixed(1)}%</div>
        <div style={{
          fontFamily: 'var(--font-display)',
          fontSize: 10,
          fontWeight: 600,
          letterSpacing: '0.1em',
          color: tier.color,
          opacity: 0.85,
          marginTop: 3,
        }}>{tier.label}</div>
      </div>
    </div>
  );
}
