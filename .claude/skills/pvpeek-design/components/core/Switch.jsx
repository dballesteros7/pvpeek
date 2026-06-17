import React from 'react';

/**
 * Switch — Android-style toggle for data-choice / settings rows.
 */
export function Switch({ checked = false, disabled = false, onChange, id, ...rest }) {
  const trackOn = 'var(--gold)';
  const trackOff = 'var(--slate-600)';
  return (
    <button
      type="button"
      role="switch"
      id={id}
      aria-checked={checked}
      disabled={disabled}
      onClick={() => !disabled && onChange && onChange(!checked)}
      style={{
        position: 'relative',
        width: 48,
        height: 28,
        flex: '0 0 auto',
        borderRadius: 'var(--radius-full)',
        border: 'none',
        background: checked ? trackOn : trackOff,
        cursor: disabled ? 'not-allowed' : 'pointer',
        opacity: disabled ? 0.4 : 1,
        transition: 'background var(--dur-base) var(--ease-out)',
        padding: 0,
      }}
      {...rest}
    >
      <span
        style={{
          position: 'absolute',
          top: 3,
          left: checked ? 23 : 3,
          width: 22,
          height: 22,
          borderRadius: '50%',
          background: checked ? 'var(--text-on-gold)' : 'var(--white)',
          boxShadow: '0 1px 3px rgba(0,0,0,0.5)',
          transition: 'left var(--dur-base) var(--ease-spring)',
        }}
      />
    </button>
  );
}
