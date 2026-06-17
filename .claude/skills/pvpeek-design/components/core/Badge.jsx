import React from 'react';

const tones = {
  neutral: { bg: 'rgba(255,255,255,0.08)', fg: 'var(--text-secondary)', bd: 'var(--border-default)' },
  gold:    { bg: 'rgba(244,196,48,0.16)', fg: 'var(--gold)', bd: 'rgba(244,196,48,0.4)' },
  blue:    { bg: 'rgba(91,157,255,0.16)', fg: 'var(--blue-bright)', bd: 'rgba(91,157,255,0.4)' },
  red:     { bg: 'rgba(255,77,94,0.16)', fg: 'var(--red-bright)', bd: 'rgba(255,77,94,0.4)' },
  green:   { bg: 'rgba(56,217,150,0.16)', fg: 'var(--success)', bd: 'rgba(56,217,150,0.4)' },
};

/**
 * Badge — small status pill. Tone-colored, optional solid fill.
 */
export function Badge({ children, tone = 'neutral', solid = false, style = {}, ...rest }) {
  const t = tones[tone] || tones.neutral;
  return (
    <span
      style={{
        display: 'inline-flex',
        alignItems: 'center',
        gap: 4,
        height: 22,
        padding: '0 9px',
        borderRadius: 'var(--radius-full)',
        background: solid ? t.fg : t.bg,
        color: solid ? 'var(--slate-1000)' : t.fg,
        border: solid ? '1px solid transparent' : `1px solid ${t.bd}`,
        fontFamily: 'var(--font-display)',
        fontSize: 11,
        fontWeight: 600,
        letterSpacing: '0.04em',
        textTransform: 'uppercase',
        lineHeight: 1,
        whiteSpace: 'nowrap',
        ...style,
      }}
      {...rest}
    >
      {children}
    </span>
  );
}
