import React from 'react';

/**
 * ListRow — a settings / about-screen row: leading label + optional
 * supporting text, with an optional trailing control or value.
 */
export function ListRow({
  label,
  description = null,
  trailing = null,
  leading = null,
  onClick,
  divider = true,
  style = {},
  ...rest
}) {
  const [hover, setHover] = React.useState(false);
  const interactive = !!onClick;
  return (
    <div
      onClick={onClick}
      onMouseEnter={() => setHover(true)}
      onMouseLeave={() => setHover(false)}
      style={{
        display: 'flex',
        alignItems: 'center',
        gap: 'var(--space-3)',
        minHeight: 'var(--tap-min)',
        padding: 'var(--space-3) var(--space-1)',
        borderBottom: divider ? '1px solid var(--border-subtle)' : 'none',
        cursor: interactive ? 'pointer' : 'default',
        background: interactive && hover ? 'var(--fill-ghost-hover)' : 'transparent',
        borderRadius: 'var(--radius-sm)',
        transition: 'background var(--dur-fast) var(--ease-out)',
        ...style,
      }}
      {...rest}
    >
      {leading && <div style={{ flex: '0 0 auto', display: 'flex', color: 'var(--text-secondary)' }}>{leading}</div>}
      <div style={{ flex: 1, minWidth: 0 }}>
        <div style={{
          fontFamily: 'var(--font-body)',
          fontSize: 'var(--text-body)',
          fontWeight: 600,
          color: 'var(--text-primary)',
        }}>{label}</div>
        {description && (
          <div style={{
            fontFamily: 'var(--font-body)',
            fontSize: 'var(--text-sm)',
            color: 'var(--text-tertiary)',
            marginTop: 2,
            lineHeight: 1.4,
          }}>{description}</div>
        )}
      </div>
      {trailing && <div style={{ flex: '0 0 auto', display: 'flex', alignItems: 'center', color: 'var(--text-secondary)' }}>{trailing}</div>}
    </div>
  );
}
