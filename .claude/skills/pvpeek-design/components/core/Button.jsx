import React from 'react';

const sizeStyles = {
  sm: { height: 36, padding: '0 14px', fontSize: 13, borderRadius: 'var(--radius-sm)', gap: 6 },
  md: { height: 44, padding: '0 18px', fontSize: 15, borderRadius: 'var(--radius-md)', gap: 8 },
  lg: { height: 52, padding: '0 24px', fontSize: 16, borderRadius: 'var(--radius-md)', gap: 10 },
};

const variantStyles = {
  primary: {
    background: 'var(--gold)',
    color: 'var(--text-on-gold)',
    border: '1px solid transparent',
    fontWeight: 700,
  },
  secondary: {
    background: 'var(--surface-raised)',
    color: 'var(--text-primary)',
    border: '1px solid var(--border-default)',
    fontWeight: 600,
  },
  ghost: {
    background: 'transparent',
    color: 'var(--text-secondary)',
    border: '1px solid transparent',
    fontWeight: 600,
  },
  danger: {
    background: 'transparent',
    color: 'var(--danger)',
    border: '1px solid var(--border-default)',
    fontWeight: 600,
  },
};

/**
 * PvPeek primary action button. Gold = primary, used once per screen.
 */
export function Button({
  children,
  variant = 'primary',
  size = 'md',
  fullWidth = false,
  disabled = false,
  iconLeft = null,
  iconRight = null,
  onClick,
  style = {},
  ...rest
}) {
  const [hover, setHover] = React.useState(false);
  const [active, setActive] = React.useState(false);
  const v = variantStyles[variant] || variantStyles.primary;
  const s = sizeStyles[size] || sizeStyles.md;

  let bg = v.background;
  if (!disabled && variant === 'primary') bg = active ? 'var(--gold-deep)' : hover ? 'var(--gold-bright)' : v.background;
  if (!disabled && (variant === 'secondary')) bg = active ? 'var(--slate-600)' : hover ? 'var(--slate-700)' : v.background;
  if (!disabled && (variant === 'ghost')) bg = active ? 'var(--fill-ghost-press)' : hover ? 'var(--fill-ghost-hover)' : v.background;
  if (!disabled && (variant === 'danger')) bg = active ? 'rgba(255,77,94,0.18)' : hover ? 'rgba(255,77,94,0.10)' : v.background;

  return (
    <button
      type="button"
      onClick={disabled ? undefined : onClick}
      onMouseEnter={() => setHover(true)}
      onMouseLeave={() => { setHover(false); setActive(false); }}
      onMouseDown={() => setActive(true)}
      onMouseUp={() => setActive(false)}
      disabled={disabled}
      style={{
        display: fullWidth ? 'flex' : 'inline-flex',
        width: fullWidth ? '100%' : 'auto',
        alignItems: 'center',
        justifyContent: 'center',
        gap: s.gap,
        height: s.height,
        padding: s.padding,
        borderRadius: s.borderRadius,
        fontFamily: 'var(--font-display)',
        fontSize: s.fontSize,
        fontWeight: v.fontWeight,
        letterSpacing: '0.01em',
        lineHeight: 1,
        cursor: disabled ? 'not-allowed' : 'pointer',
        opacity: disabled ? 0.4 : 1,
        transform: active && !disabled ? 'scale(0.97)' : 'scale(1)',
        transition: 'background var(--dur-fast) var(--ease-out), transform var(--dur-instant) var(--ease-out)',
        userSelect: 'none',
        ...v,
        background: bg,
        ...style,
      }}
      {...rest}
    >
      {iconLeft}
      {children}
      {iconRight}
    </button>
  );
}
