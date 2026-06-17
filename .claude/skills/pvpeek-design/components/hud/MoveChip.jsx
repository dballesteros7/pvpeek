import React from 'react';

/**
 * MoveChip — a recommended move on the verdict card.
 * `fast` moves show a turn count; `charged` moves show how many
 * fast moves are needed to charge them (the "energy" readout).
 */
export function MoveChip({ kind = 'fast', name, count, recommended = false, style = {} }) {
  const isFast = kind === 'fast';
  const accent = isFast ? 'var(--move-fast)' : 'var(--move-charged)';
  const metaLabel = isFast ? `${count}t` : `${count}×`;
  return (
    <div style={{
      display: 'flex',
      alignItems: 'center',
      gap: 'var(--space-2)',
      height: 38,
      padding: '0 var(--space-3) 0 var(--space-2)',
      borderRadius: 'var(--radius-sm)',
      background: 'var(--surface-inset)',
      border: `1px solid ${recommended ? accent : 'var(--border-subtle)'}`,
      boxShadow: recommended ? `inset 3px 0 0 ${accent}` : 'none',
      ...style,
    }}>
      <span style={{
        width: 8, height: 8, borderRadius: '50%', flex: '0 0 auto',
        background: accent, boxShadow: `0 0 6px ${accent}`,
      }} />
      <span style={{
        flex: 1, minWidth: 0,
        fontFamily: 'var(--font-body)',
        fontSize: 13,
        fontWeight: 600,
        color: 'var(--text-primary)',
        whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis',
      }}>{name}</span>
      <span title={isFast ? 'turns per use' : 'fast moves to charge'} style={{
        display: 'inline-flex', alignItems: 'center', gap: 3,
        fontFamily: 'var(--font-mono)',
        fontVariantNumeric: 'tabular-nums',
        fontSize: 12,
        fontWeight: 700,
        color: accent,
      }}>
        {!isFast && <span style={{ fontSize: 11 }}>⚡</span>}
        {metaLabel}
      </span>
    </div>
  );
}
