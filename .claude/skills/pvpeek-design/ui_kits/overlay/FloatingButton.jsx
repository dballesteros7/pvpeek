/**
 * FloatingButton — the always-on overlay trigger. Sits on the screen
 * edge over the game. Idle shows the crown-swords mark; `scanning`
 * shows a rotating ring + scan sweep while PvPeek reads the screen.
 */
function FloatingButton({ state = 'idle', onClick, style = {} }) {
  const scanning = state === 'scanning';
  return (
    <button
      type="button"
      onClick={onClick}
      aria-label={scanning ? 'Scanning' : 'Open PvPeek'}
      style={{
        position: 'relative',
        width: 56, height: 56,
        borderRadius: '50%',
        border: 'none',
        padding: 0,
        cursor: 'pointer',
        background: 'radial-gradient(circle at 50% 35%, #2b313d, #15161A)',
        boxShadow: 'var(--elev-fab), 0 0 0 1.5px rgba(244,196,48,0.5), var(--glow-gold)',
        display: 'flex', alignItems: 'center', justifyContent: 'center',
        ...style,
      }}
    >
      {/* rotating scan ring */}
      <span style={{
        position: 'absolute', inset: -3, borderRadius: '50%',
        border: '2px solid transparent',
        borderTopColor: 'var(--gold)',
        borderRightColor: 'rgba(91,157,255,0.9)',
        opacity: scanning ? 1 : 0,
        animation: scanning ? 'pp-spin 0.8s linear infinite' : 'none',
      }} />
      {/* pulse halo when scanning */}
      <span style={{
        position: 'absolute', inset: 0, borderRadius: '50%',
        boxShadow: '0 0 0 0 rgba(244,196,48,0.5)',
        animation: scanning ? 'pp-pulse 1.2s var(--ease-out) infinite' : 'none',
      }} />
      <img src="../../assets/logo-mark.svg" width="34" height="34" alt="" style={{ filter: scanning ? 'saturate(1.2)' : 'none' }} />
      <style>{`
        @keyframes pp-spin { to { transform: rotate(360deg); } }
        @keyframes pp-pulse {
          0% { box-shadow: 0 0 0 0 rgba(244,196,48,0.45); }
          100% { box-shadow: 0 0 0 14px rgba(244,196,48,0); }
        }
      `}</style>
    </button>
  );
}

Object.assign(window, { FloatingButton });
