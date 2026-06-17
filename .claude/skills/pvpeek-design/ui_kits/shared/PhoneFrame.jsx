/**
 * PhoneFrame — a lightweight Android device shell for kit demos.
 * Fixed 360×760 viewport with a status bar; children fill the screen.
 */
function PhoneFrame({ children, statusDark = false, bg = 'var(--bg-app)', label }) {
  const fg = statusDark ? '#0E0F12' : 'var(--white)';
  return (
    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 10 }}>
      <div style={{
        position: 'relative',
        width: 360,
        height: 760,
        background: bg,
        borderRadius: 40,
        border: '10px solid #05060a',
        boxShadow: '0 30px 80px rgba(0,0,0,0.6), inset 0 0 0 1px rgba(255,255,255,0.04)',
        overflow: 'hidden',
      }}>
        {/* status bar */}
        <div style={{
          position: 'absolute', top: 0, left: 0, right: 0, height: 36,
          display: 'flex', alignItems: 'center', justifyContent: 'space-between',
          padding: '0 18px', zIndex: 30, pointerEvents: 'none',
          fontFamily: 'var(--font-mono)', fontSize: 12, fontWeight: 600, color: fg,
        }}>
          <span>9:41</span>
          <div style={{ display: 'flex', gap: 6, alignItems: 'center', opacity: 0.9 }}>
            <span style={{ fontSize: 11 }}>5G</span>
            <span>▮▮▮</span>
            <span>78%</span>
          </div>
        </div>
        {/* punch-hole camera */}
        <div style={{
          position: 'absolute', top: 12, left: '50%', transform: 'translateX(-50%)',
          width: 8, height: 8, borderRadius: '50%', background: '#000', zIndex: 31,
          boxShadow: 'inset 0 0 0 1px rgba(255,255,255,0.1)',
        }} />
        <div style={{ position: 'absolute', inset: 0, paddingTop: 36 }}>{children}</div>
      </div>
      {label && <div style={{ fontFamily: 'var(--font-mono)', fontSize: 11, color: 'var(--text-tertiary)' }}>{label}</div>}
    </div>
  );
}

Object.assign(window, { PhoneFrame });
