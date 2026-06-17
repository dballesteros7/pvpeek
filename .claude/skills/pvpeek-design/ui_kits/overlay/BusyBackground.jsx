/**
 * BusyBackground — an abstract, deliberately noisy "a game is running"
 * backdrop. NOT any real game: pure CSS shapes/gradients, used to prove
 * the verdict card's scrim keeps it legible over unpredictable colors.
 */
function BusyBackground() {
  return (
    <div style={{ position: 'absolute', inset: 0, overflow: 'hidden', background: '#1b6b4a' }}>
      {/* sky */}
      <div style={{ position: 'absolute', inset: 0, background: 'linear-gradient(180deg,#7b4bd6 0%,#4aa3d6 38%,#3fb27a 60%,#1b6b4a 100%)' }} />
      {/* sun / glare — a bright hot spot to challenge white text */}
      <div style={{ position: 'absolute', top: 70, right: 50, width: 150, height: 150, borderRadius: '50%', background: 'radial-gradient(circle,#fff7d6,#ffd23f 55%,rgba(255,210,63,0))' }} />
      {/* terrain bands */}
      <div style={{ position: 'absolute', bottom: 0, left: 0, right: 0, height: 320, background: 'linear-gradient(180deg,#3fb27a,#176b46)' }} />
      <div style={{ position: 'absolute', bottom: 120, left: -40, width: 300, height: 300, borderRadius: '50%', background: '#2a8f63', opacity: 0.8 }} />
      <div style={{ position: 'absolute', bottom: 60, right: -60, width: 260, height: 260, borderRadius: '50%', background: '#d9772b', opacity: 0.85 }} />
      {/* floating energy orbs */}
      {[['18%','22%',54,'#ff5da2'],['70%','30%',38,'#ffd23f'],['30%','55%',30,'#5bd6ff'],['82%','58%',46,'#b06bff']].map(([l,t,s,c],i)=>(
        <div key={i} style={{ position:'absolute', left:l, top:t, width:s, height:s, borderRadius:'50%', background:c, boxShadow:`0 0 24px ${c}`, opacity:0.9 }} />
      ))}
      {/* a bright stripe across the middle */}
      <div style={{ position:'absolute', top:'46%', left:0, right:0, height:46, background:'linear-gradient(90deg,#fff,rgba(255,255,255,0))', opacity:0.5, transform:'rotate(-4deg)' }} />
      {/* fake game HUD bits (abstract, top corners) */}
      <div style={{ position:'absolute', top:48, left:14, width:90, height:14, borderRadius:7, background:'rgba(255,255,255,0.85)' }} />
      <div style={{ position:'absolute', top:48, left:14, width:60, height:14, borderRadius:7, background:'#ff4d4d' }} />
      <div style={{ position:'absolute', bottom:24, left:'50%', transform:'translateX(-50%)', display:'flex', gap:10 }}>
        {['#ffd23f','#5bd6ff','#ff5da2'].map((c,i)=>(<div key={i} style={{width:52,height:52,borderRadius:14,background:c,opacity:0.92}} />))}
      </div>
    </div>
  );
}

Object.assign(window, { BusyBackground });
