/**
 * DismissTarget — the drag-to-dismiss zone that fades in at the bottom
 * while the user drags the floating button. `armed` = button is hovering
 * over it (about to dismiss): it grows and turns red.
 */
function DismissTarget({ visible = true, armed = false }) {
  return (
    <div style={{
      position: 'absolute', bottom: 40, left: '50%', transform: 'translateX(-50%)',
      display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 8,
      opacity: visible ? 1 : 0,
      transition: 'opacity var(--dur-base) var(--ease-out)',
      pointerEvents: 'none', zIndex: 40,
    }}>
      <div style={{
        width: armed ? 76 : 60, height: armed ? 76 : 60,
        borderRadius: '50%',
        background: armed ? 'rgba(206,17,38,0.92)' : 'rgba(20,21,26,0.78)',
        border: `1.5px solid ${armed ? 'var(--red-bright)' : 'rgba(255,255,255,0.3)'}`,
        backdropFilter: 'blur(8px)',
        display: 'flex', alignItems: 'center', justifyContent: 'center',
        color: '#fff',
        boxShadow: armed ? '0 0 28px rgba(255,77,94,0.6)' : '0 6px 18px rgba(0,0,0,0.5)',
        transition: 'all var(--dur-base) var(--ease-spring)',
      }}>
        <svg width="26" height="26" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.4" strokeLinecap="round">
          <path d="M6 6l12 12M18 6L6 18" />
        </svg>
      </div>
      <span style={{
        fontFamily: 'var(--font-display)', fontSize: 11, fontWeight: 600,
        letterSpacing: '0.08em', textTransform: 'uppercase',
        color: armed ? 'var(--red-bright)' : 'var(--white)',
        textShadow: '0 1px 4px rgba(0,0,0,0.7)',
      }}>{armed ? 'Release to hide' : 'Drag here to hide'}</span>
    </div>
  );
}

Object.assign(window, { DismissTarget });
