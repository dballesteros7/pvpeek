import React from 'react';

/**
 * Dialog — centered modal over a scrim. Used for the first-run consent
 * opt-in and other small confirmations.
 */
export function Dialog({
  open = true,
  title,
  children,
  icon = null,
  actions = null,
  onDismiss,
  width = 340,
  style = {},
}) {
  if (!open) return null;
  return (
    <div
      onClick={onDismiss}
      style={{
        position: 'absolute',
        inset: 0,
        background: 'var(--scrim-dialog)',
        backdropFilter: 'blur(2px)',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        padding: 'var(--space-5)',
        zIndex: 50,
      }}
    >
      <div
        onClick={(e) => e.stopPropagation()}
        role="dialog"
        aria-modal="true"
        style={{
          width,
          maxWidth: '100%',
          background: 'var(--surface-card)',
          border: '1px solid var(--border-strong)',
          borderRadius: 'var(--radius-xl)',
          boxShadow: 'var(--elev-3)',
          padding: 'var(--space-6)',
          ...style,
        }}
      >
        {icon && <div style={{ marginBottom: 'var(--space-4)' }}>{icon}</div>}
        {title && (
          <h2 style={{
            margin: 0,
            fontFamily: 'var(--font-display)',
            fontSize: 'var(--text-h2)',
            fontWeight: 700,
            color: 'var(--text-primary)',
            letterSpacing: 'var(--ls-tight)',
          }}>{title}</h2>
        )}
        <div style={{
          marginTop: 'var(--space-3)',
          fontFamily: 'var(--font-body)',
          fontSize: 'var(--text-body)',
          lineHeight: 'var(--lh-normal)',
          color: 'var(--text-secondary)',
        }}>{children}</div>
        {actions && (
          <div style={{
            display: 'flex',
            gap: 'var(--space-2)',
            marginTop: 'var(--space-6)',
          }}>{actions}</div>
        )}
      </div>
    </div>
  );
}
