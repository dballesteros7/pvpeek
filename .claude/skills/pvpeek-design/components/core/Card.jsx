import React from 'react';

/**
 * Card — PvPeek surface container. The `overlay` variant carries the
 * strong scrim + elevation used by the verdict card floating over game art.
 */
export function Card({
  children,
  variant = 'default',
  padding = 'var(--space-5)',
  style = {},
  ...rest
}) {
  const variants = {
    default: {
      background: 'var(--surface-card)',
      border: '1px solid var(--border-subtle)',
      boxShadow: 'var(--elev-2)',
    },
    inset: {
      background: 'var(--surface-inset)',
      border: '1px solid var(--border-subtle)',
      boxShadow: 'none',
    },
    overlay: {
      background: 'var(--surface-card)',
      border: '1px solid var(--border-strong)',
      boxShadow: 'var(--elev-overlay)',
      backdropFilter: 'saturate(1.1)',
    },
  };
  const v = variants[variant] || variants.default;
  return (
    <div
      style={{
        borderRadius: variant === 'overlay' ? 'var(--radius-xl)' : 'var(--radius-lg)',
        padding,
        ...v,
        ...style,
      }}
      {...rest}
    >
      {children}
    </div>
  );
}
