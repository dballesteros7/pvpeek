/* @ds-bundle: {"format":3,"namespace":"PvPeekDesignSystem_53b813","components":[{"name":"Badge","sourcePath":"components/core/Badge.jsx"},{"name":"Button","sourcePath":"components/core/Button.jsx"},{"name":"Card","sourcePath":"components/core/Card.jsx"},{"name":"Dialog","sourcePath":"components/core/Dialog.jsx"},{"name":"ListRow","sourcePath":"components/core/ListRow.jsx"},{"name":"Switch","sourcePath":"components/core/Switch.jsx"},{"name":"IVStat","sourcePath":"components/hud/IVStat.jsx"},{"name":"MoveChip","sourcePath":"components/hud/MoveChip.jsx"},{"name":"RankBadge","sourcePath":"components/hud/RankBadge.jsx"}],"sourceHashes":{"components/core/Badge.jsx":"7d45f356e447","components/core/Button.jsx":"e7764ad0f04a","components/core/Card.jsx":"d4dca3753951","components/core/Dialog.jsx":"79b7d81d6a5e","components/core/ListRow.jsx":"328ce83e1ba2","components/core/Switch.jsx":"bd918d2f8ba2","components/hud/IVStat.jsx":"594d0b9c3ed8","components/hud/MoveChip.jsx":"997d4f35084f","components/hud/RankBadge.jsx":"7c25c10a6de7","ui_kits/app/AboutScreen.jsx":"e07c4d404321","ui_kits/app/ConsentDialog.jsx":"69f79bde22a2","ui_kits/app/LaunchScreen.jsx":"6c0aebe09317","ui_kits/overlay/BusyBackground.jsx":"2d47164be16d","ui_kits/overlay/DismissTarget.jsx":"2f6a3b557ed6","ui_kits/overlay/FloatingButton.jsx":"ca50a2ad92f0","ui_kits/overlay/VerdictCard.jsx":"a9aefaa8130d","ui_kits/shared/PhoneFrame.jsx":"4532f08b1b39"},"inlinedExternals":[],"unexposedExports":[{"name":"ivTier","sourcePath":"components/hud/IVStat.jsx"},{"name":"rankTier","sourcePath":"components/hud/RankBadge.jsx"}]} */

(() => {

const __ds_ns = (window.PvPeekDesignSystem_53b813 = window.PvPeekDesignSystem_53b813 || {});

const __ds_scope = {};

(__ds_ns.__errors = __ds_ns.__errors || []);

// components/core/Badge.jsx
try { (() => {
function _extends() { return _extends = Object.assign ? Object.assign.bind() : function (n) { for (var e = 1; e < arguments.length; e++) { var t = arguments[e]; for (var r in t) ({}).hasOwnProperty.call(t, r) && (n[r] = t[r]); } return n; }, _extends.apply(null, arguments); }
const tones = {
  neutral: {
    bg: 'rgba(255,255,255,0.08)',
    fg: 'var(--text-secondary)',
    bd: 'var(--border-default)'
  },
  gold: {
    bg: 'rgba(244,196,48,0.16)',
    fg: 'var(--gold)',
    bd: 'rgba(244,196,48,0.4)'
  },
  blue: {
    bg: 'rgba(91,157,255,0.16)',
    fg: 'var(--blue-bright)',
    bd: 'rgba(91,157,255,0.4)'
  },
  red: {
    bg: 'rgba(255,77,94,0.16)',
    fg: 'var(--red-bright)',
    bd: 'rgba(255,77,94,0.4)'
  },
  green: {
    bg: 'rgba(56,217,150,0.16)',
    fg: 'var(--success)',
    bd: 'rgba(56,217,150,0.4)'
  }
};

/**
 * Badge — small status pill. Tone-colored, optional solid fill.
 */
function Badge({
  children,
  tone = 'neutral',
  solid = false,
  style = {},
  ...rest
}) {
  const t = tones[tone] || tones.neutral;
  return /*#__PURE__*/React.createElement("span", _extends({
    style: {
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
      ...style
    }
  }, rest), children);
}
Object.assign(__ds_scope, { Badge });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/core/Badge.jsx", error: String((e && e.message) || e) }); }

// components/core/Button.jsx
try { (() => {
function _extends() { return _extends = Object.assign ? Object.assign.bind() : function (n) { for (var e = 1; e < arguments.length; e++) { var t = arguments[e]; for (var r in t) ({}).hasOwnProperty.call(t, r) && (n[r] = t[r]); } return n; }, _extends.apply(null, arguments); }
const sizeStyles = {
  sm: {
    height: 36,
    padding: '0 14px',
    fontSize: 13,
    borderRadius: 'var(--radius-sm)',
    gap: 6
  },
  md: {
    height: 44,
    padding: '0 18px',
    fontSize: 15,
    borderRadius: 'var(--radius-md)',
    gap: 8
  },
  lg: {
    height: 52,
    padding: '0 24px',
    fontSize: 16,
    borderRadius: 'var(--radius-md)',
    gap: 10
  }
};
const variantStyles = {
  primary: {
    background: 'var(--gold)',
    color: 'var(--text-on-gold)',
    border: '1px solid transparent',
    fontWeight: 700
  },
  secondary: {
    background: 'var(--surface-raised)',
    color: 'var(--text-primary)',
    border: '1px solid var(--border-default)',
    fontWeight: 600
  },
  ghost: {
    background: 'transparent',
    color: 'var(--text-secondary)',
    border: '1px solid transparent',
    fontWeight: 600
  },
  danger: {
    background: 'transparent',
    color: 'var(--danger)',
    border: '1px solid var(--border-default)',
    fontWeight: 600
  }
};

/**
 * PvPeek primary action button. Gold = primary, used once per screen.
 */
function Button({
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
  if (!disabled && variant === 'secondary') bg = active ? 'var(--slate-600)' : hover ? 'var(--slate-700)' : v.background;
  if (!disabled && variant === 'ghost') bg = active ? 'var(--fill-ghost-press)' : hover ? 'var(--fill-ghost-hover)' : v.background;
  if (!disabled && variant === 'danger') bg = active ? 'rgba(255,77,94,0.18)' : hover ? 'rgba(255,77,94,0.10)' : v.background;
  return /*#__PURE__*/React.createElement("button", _extends({
    type: "button",
    onClick: disabled ? undefined : onClick,
    onMouseEnter: () => setHover(true),
    onMouseLeave: () => {
      setHover(false);
      setActive(false);
    },
    onMouseDown: () => setActive(true),
    onMouseUp: () => setActive(false),
    disabled: disabled,
    style: {
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
      ...style
    }
  }, rest), iconLeft, children, iconRight);
}
Object.assign(__ds_scope, { Button });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/core/Button.jsx", error: String((e && e.message) || e) }); }

// components/core/Card.jsx
try { (() => {
function _extends() { return _extends = Object.assign ? Object.assign.bind() : function (n) { for (var e = 1; e < arguments.length; e++) { var t = arguments[e]; for (var r in t) ({}).hasOwnProperty.call(t, r) && (n[r] = t[r]); } return n; }, _extends.apply(null, arguments); }
/**
 * Card — PvPeek surface container. The `overlay` variant carries the
 * strong scrim + elevation used by the verdict card floating over game art.
 */
function Card({
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
      boxShadow: 'var(--elev-2)'
    },
    inset: {
      background: 'var(--surface-inset)',
      border: '1px solid var(--border-subtle)',
      boxShadow: 'none'
    },
    overlay: {
      background: 'var(--surface-card)',
      border: '1px solid var(--border-strong)',
      boxShadow: 'var(--elev-overlay)',
      backdropFilter: 'saturate(1.1)'
    }
  };
  const v = variants[variant] || variants.default;
  return /*#__PURE__*/React.createElement("div", _extends({
    style: {
      borderRadius: variant === 'overlay' ? 'var(--radius-xl)' : 'var(--radius-lg)',
      padding,
      ...v,
      ...style
    }
  }, rest), children);
}
Object.assign(__ds_scope, { Card });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/core/Card.jsx", error: String((e && e.message) || e) }); }

// components/core/Dialog.jsx
try { (() => {
/**
 * Dialog — centered modal over a scrim. Used for the first-run consent
 * opt-in and other small confirmations.
 */
function Dialog({
  open = true,
  title,
  children,
  icon = null,
  actions = null,
  onDismiss,
  width = 340,
  style = {}
}) {
  if (!open) return null;
  return /*#__PURE__*/React.createElement("div", {
    onClick: onDismiss,
    style: {
      position: 'absolute',
      inset: 0,
      background: 'var(--scrim-dialog)',
      backdropFilter: 'blur(2px)',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      padding: 'var(--space-5)',
      zIndex: 50
    }
  }, /*#__PURE__*/React.createElement("div", {
    onClick: e => e.stopPropagation(),
    role: "dialog",
    "aria-modal": "true",
    style: {
      width,
      maxWidth: '100%',
      background: 'var(--surface-card)',
      border: '1px solid var(--border-strong)',
      borderRadius: 'var(--radius-xl)',
      boxShadow: 'var(--elev-3)',
      padding: 'var(--space-6)',
      ...style
    }
  }, icon && /*#__PURE__*/React.createElement("div", {
    style: {
      marginBottom: 'var(--space-4)'
    }
  }, icon), title && /*#__PURE__*/React.createElement("h2", {
    style: {
      margin: 0,
      fontFamily: 'var(--font-display)',
      fontSize: 'var(--text-h2)',
      fontWeight: 700,
      color: 'var(--text-primary)',
      letterSpacing: 'var(--ls-tight)'
    }
  }, title), /*#__PURE__*/React.createElement("div", {
    style: {
      marginTop: 'var(--space-3)',
      fontFamily: 'var(--font-body)',
      fontSize: 'var(--text-body)',
      lineHeight: 'var(--lh-normal)',
      color: 'var(--text-secondary)'
    }
  }, children), actions && /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      gap: 'var(--space-2)',
      marginTop: 'var(--space-6)'
    }
  }, actions)));
}
Object.assign(__ds_scope, { Dialog });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/core/Dialog.jsx", error: String((e && e.message) || e) }); }

// components/core/ListRow.jsx
try { (() => {
function _extends() { return _extends = Object.assign ? Object.assign.bind() : function (n) { for (var e = 1; e < arguments.length; e++) { var t = arguments[e]; for (var r in t) ({}).hasOwnProperty.call(t, r) && (n[r] = t[r]); } return n; }, _extends.apply(null, arguments); }
/**
 * ListRow — a settings / about-screen row: leading label + optional
 * supporting text, with an optional trailing control or value.
 */
function ListRow({
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
  return /*#__PURE__*/React.createElement("div", _extends({
    onClick: onClick,
    onMouseEnter: () => setHover(true),
    onMouseLeave: () => setHover(false),
    style: {
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
      ...style
    }
  }, rest), leading && /*#__PURE__*/React.createElement("div", {
    style: {
      flex: '0 0 auto',
      display: 'flex',
      color: 'var(--text-secondary)'
    }
  }, leading), /*#__PURE__*/React.createElement("div", {
    style: {
      flex: 1,
      minWidth: 0
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-body)',
      fontSize: 'var(--text-body)',
      fontWeight: 600,
      color: 'var(--text-primary)'
    }
  }, label), description && /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-body)',
      fontSize: 'var(--text-sm)',
      color: 'var(--text-tertiary)',
      marginTop: 2,
      lineHeight: 1.4
    }
  }, description)), trailing && /*#__PURE__*/React.createElement("div", {
    style: {
      flex: '0 0 auto',
      display: 'flex',
      alignItems: 'center',
      color: 'var(--text-secondary)'
    }
  }, trailing));
}
Object.assign(__ds_scope, { ListRow });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/core/ListRow.jsx", error: String((e && e.message) || e) }); }

// components/core/Switch.jsx
try { (() => {
function _extends() { return _extends = Object.assign ? Object.assign.bind() : function (n) { for (var e = 1; e < arguments.length; e++) { var t = arguments[e]; for (var r in t) ({}).hasOwnProperty.call(t, r) && (n[r] = t[r]); } return n; }, _extends.apply(null, arguments); }
/**
 * Switch — Android-style toggle for data-choice / settings rows.
 */
function Switch({
  checked = false,
  disabled = false,
  onChange,
  id,
  ...rest
}) {
  const trackOn = 'var(--gold)';
  const trackOff = 'var(--slate-600)';
  return /*#__PURE__*/React.createElement("button", _extends({
    type: "button",
    role: "switch",
    id: id,
    "aria-checked": checked,
    disabled: disabled,
    onClick: () => !disabled && onChange && onChange(!checked),
    style: {
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
      padding: 0
    }
  }, rest), /*#__PURE__*/React.createElement("span", {
    style: {
      position: 'absolute',
      top: 3,
      left: checked ? 23 : 3,
      width: 22,
      height: 22,
      borderRadius: '50%',
      background: checked ? 'var(--text-on-gold)' : 'var(--white)',
      boxShadow: '0 1px 3px rgba(0,0,0,0.5)',
      transition: 'left var(--dur-base) var(--ease-spring)'
    }
  }));
}
Object.assign(__ds_scope, { Switch });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/core/Switch.jsx", error: String((e && e.message) || e) }); }

// components/hud/IVStat.jsx
try { (() => {
/** Map an IV value (0–15) to a rank-tier color + short label. */
function ivTier(value) {
  if (value >= 15) return {
    color: 'var(--tier-perfect)',
    label: 'MAX'
  };
  if (value >= 13) return {
    color: 'var(--tier-great)',
    label: 'HIGH'
  };
  if (value >= 10) return {
    color: 'var(--tier-good)',
    label: 'OK'
  };
  return {
    color: 'var(--tier-fair)',
    label: 'LOW'
  };
}

/**
 * IVStat — one of the three IVs (Attack / Defense / Stamina), 0–15.
 * Renders a label, the value in tabular mono, and a 15-segment bar
 * tinted by the value's tier color. Built for fast scanning.
 */
function IVStat({
  label,
  value,
  max = 15,
  style = {}
}) {
  const tier = ivTier(value);
  const segs = Array.from({
    length: max
  }, (_, i) => i < value);
  return /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      flexDirection: 'column',
      gap: 6,
      minWidth: 0,
      ...style
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'baseline',
      justifyContent: 'space-between',
      gap: 6
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-display)',
      fontSize: 11,
      fontWeight: 600,
      letterSpacing: '0.08em',
      color: 'var(--text-tertiary)'
    }
  }, label), /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-mono)',
      fontVariantNumeric: 'tabular-nums',
      fontSize: 18,
      fontWeight: 700,
      lineHeight: 1,
      color: tier.color
    }
  }, value)), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      gap: 2,
      height: 6
    }
  }, segs.map((on, i) => /*#__PURE__*/React.createElement("span", {
    key: i,
    style: {
      flex: 1,
      borderRadius: 1,
      background: on ? tier.color : 'rgba(255,255,255,0.10)',
      boxShadow: on && value >= 15 ? '0 0 4px rgba(252,209,22,0.6)' : 'none'
    }
  }))));
}
Object.assign(__ds_scope, { ivTier, IVStat });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/hud/IVStat.jsx", error: String((e && e.message) || e) }); }

// components/hud/MoveChip.jsx
try { (() => {
/**
 * MoveChip — a recommended move on the verdict card.
 * `fast` moves show a turn count; `charged` moves show how many
 * fast moves are needed to charge them (the "energy" readout).
 */
function MoveChip({
  kind = 'fast',
  name,
  count,
  recommended = false,
  style = {}
}) {
  const isFast = kind === 'fast';
  const accent = isFast ? 'var(--move-fast)' : 'var(--move-charged)';
  const metaLabel = isFast ? `${count}t` : `${count}×`;
  return /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 'var(--space-2)',
      height: 38,
      padding: '0 var(--space-3) 0 var(--space-2)',
      borderRadius: 'var(--radius-sm)',
      background: 'var(--surface-inset)',
      border: `1px solid ${recommended ? accent : 'var(--border-subtle)'}`,
      boxShadow: recommended ? `inset 3px 0 0 ${accent}` : 'none',
      ...style
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      width: 8,
      height: 8,
      borderRadius: '50%',
      flex: '0 0 auto',
      background: accent,
      boxShadow: `0 0 6px ${accent}`
    }
  }), /*#__PURE__*/React.createElement("span", {
    style: {
      flex: 1,
      minWidth: 0,
      fontFamily: 'var(--font-body)',
      fontSize: 13,
      fontWeight: 600,
      color: 'var(--text-primary)',
      whiteSpace: 'nowrap',
      overflow: 'hidden',
      textOverflow: 'ellipsis'
    }
  }, name), /*#__PURE__*/React.createElement("span", {
    title: isFast ? 'turns per use' : 'fast moves to charge',
    style: {
      display: 'inline-flex',
      alignItems: 'center',
      gap: 3,
      fontFamily: 'var(--font-mono)',
      fontVariantNumeric: 'tabular-nums',
      fontSize: 12,
      fontWeight: 700,
      color: accent
    }
  }, !isFast && /*#__PURE__*/React.createElement("span", {
    style: {
      fontSize: 11
    }
  }, "\u26A1"), metaLabel));
}
Object.assign(__ds_scope, { MoveChip });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/hud/MoveChip.jsx", error: String((e && e.message) || e) }); }

// components/hud/RankBadge.jsx
try { (() => {
/** Map a rank percentage (0–100) to a tier color + label. */
function rankTier(percent) {
  if (percent >= 99) return {
    color: 'var(--tier-perfect)',
    label: 'ELITE'
  };
  if (percent >= 95) return {
    color: 'var(--tier-great)',
    label: 'GREAT'
  };
  if (percent >= 88) return {
    color: 'var(--tier-good)',
    label: 'GOOD'
  };
  if (percent >= 75) return {
    color: 'var(--tier-fair)',
    label: 'FAIR'
  };
  return {
    color: 'var(--tier-weak)',
    label: 'WEAK'
  };
}

/**
 * RankBadge — a league standing for the verdict card:
 * league name, rank "#12 / 4096", and the tier-colored percentage.
 */
function RankBadge({
  league,
  rank,
  total,
  percent,
  style = {}
}) {
  const tier = rankTier(percent);
  return /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 'var(--space-3)',
      padding: 'var(--space-3)',
      borderRadius: 'var(--radius-md)',
      background: 'var(--surface-inset)',
      borderLeft: `3px solid ${tier.color}`,
      ...style
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      flex: 1,
      minWidth: 0
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-display)',
      fontSize: 11,
      fontWeight: 600,
      letterSpacing: '0.08em',
      textTransform: 'uppercase',
      color: 'var(--text-tertiary)',
      marginBottom: 3
    }
  }, league), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'baseline',
      gap: 4
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-mono)',
      fontVariantNumeric: 'tabular-nums',
      fontSize: 20,
      fontWeight: 700,
      lineHeight: 1,
      color: 'var(--text-primary)'
    }
  }, "#", rank), /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-mono)',
      fontVariantNumeric: 'tabular-nums',
      fontSize: 12,
      color: 'var(--text-tertiary)'
    }
  }, "/ ", total))), /*#__PURE__*/React.createElement("div", {
    style: {
      textAlign: 'right'
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-mono)',
      fontVariantNumeric: 'tabular-nums',
      fontSize: 18,
      fontWeight: 700,
      lineHeight: 1,
      color: tier.color
    }
  }, percent.toFixed(1), "%"), /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-display)',
      fontSize: 10,
      fontWeight: 600,
      letterSpacing: '0.1em',
      color: tier.color,
      opacity: 0.85,
      marginTop: 3
    }
  }, tier.label)));
}
Object.assign(__ds_scope, { rankTier, RankBadge });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/hud/RankBadge.jsx", error: String((e && e.message) || e) }); }

// ui_kits/app/AboutScreen.jsx
try { (() => {
const NS = () => window.PvPeekDesignSystem_53b813;
const Chevron = () => /*#__PURE__*/React.createElement("svg", {
  width: "18",
  height: "18",
  viewBox: "0 0 24 24",
  fill: "none",
  stroke: "var(--text-tertiary)",
  strokeWidth: "2",
  strokeLinecap: "round",
  strokeLinejoin: "round"
}, /*#__PURE__*/React.createElement("path", {
  d: "M9 6l6 6-6 6"
}));
const ExternalIcon = () => /*#__PURE__*/React.createElement("svg", {
  width: "16",
  height: "16",
  viewBox: "0 0 24 24",
  fill: "none",
  stroke: "var(--text-tertiary)",
  strokeWidth: "2",
  strokeLinecap: "round",
  strokeLinejoin: "round"
}, /*#__PURE__*/React.createElement("path", {
  d: "M14 4h6v6"
}), /*#__PURE__*/React.createElement("path", {
  d: "M20 4l-9 9"
}), /*#__PURE__*/React.createElement("path", {
  d: "M18 14v4a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4"
}));
function GroupLabel({
  children
}) {
  return /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-display)',
      fontSize: 11,
      fontWeight: 600,
      letterSpacing: '0.12em',
      textTransform: 'uppercase',
      color: 'var(--text-tertiary)',
      margin: '0 0 6px 2px'
    }
  }, children);
}

/**
 * AboutScreen — version, affiliation disclaimer, privacy link,
 * data-choice toggles, and open-source attributions.
 */
function AboutScreen({
  onBack,
  analytics,
  setAnalytics,
  crash,
  setCrash
}) {
  const {
    ListRow,
    Switch,
    Badge
  } = NS();
  return /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      inset: 0,
      paddingTop: 36,
      display: 'flex',
      flexDirection: 'column',
      background: 'var(--bg-app)'
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 'var(--space-3)',
      padding: 'var(--space-3) var(--space-4)',
      borderBottom: '1px solid var(--border-subtle)'
    }
  }, /*#__PURE__*/React.createElement("button", {
    type: "button",
    onClick: onBack,
    "aria-label": "Back",
    style: {
      width: 36,
      height: 36,
      borderRadius: '50%',
      background: 'transparent',
      border: 'none',
      cursor: 'pointer',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center'
    }
  }, /*#__PURE__*/React.createElement("svg", {
    width: "22",
    height: "22",
    viewBox: "0 0 24 24",
    fill: "none",
    stroke: "var(--text-primary)",
    strokeWidth: "2",
    strokeLinecap: "round",
    strokeLinejoin: "round"
  }, /*#__PURE__*/React.createElement("path", {
    d: "M15 6l-6 6 6 6"
  }))), /*#__PURE__*/React.createElement("h1", {
    style: {
      margin: 0,
      fontFamily: 'var(--font-display)',
      fontSize: 20,
      fontWeight: 700,
      color: 'var(--text-primary)'
    }
  }, "About & Legal")), /*#__PURE__*/React.createElement("div", {
    style: {
      flex: 1,
      overflowY: 'auto',
      padding: 'var(--space-5) var(--space-5) var(--space-7)'
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 'var(--space-3)',
      marginBottom: 'var(--space-5)'
    }
  }, /*#__PURE__*/React.createElement("img", {
    src: "../../assets/logo-icon.svg",
    width: "52",
    height: "52",
    style: {
      borderRadius: 14
    },
    alt: ""
  }), /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 8
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-display)',
      fontSize: 20,
      fontWeight: 700,
      color: 'var(--white)'
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      color: 'var(--gold)'
    }
  }, "PvP"), "eek"), /*#__PURE__*/React.createElement(Badge, {
    tone: "neutral"
  }, "v1.4.0")), /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-mono)',
      fontSize: 12,
      color: 'var(--text-tertiary)',
      marginTop: 3
    }
  }, "build 1404 \xB7 on-device engine"))), /*#__PURE__*/React.createElement("div", {
    style: {
      background: 'var(--surface-inset)',
      border: '1px solid var(--border-subtle)',
      borderLeft: '3px solid var(--gold)',
      borderRadius: 'var(--radius-md)',
      padding: 'var(--space-4)',
      marginBottom: 'var(--space-5)'
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-body)',
      fontSize: 13,
      fontWeight: 700,
      color: 'var(--text-primary)',
      marginBottom: 4
    }
  }, "Unofficial tool"), /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-body)',
      fontSize: 13,
      lineHeight: 1.5,
      color: 'var(--text-tertiary)'
    }
  }, "PvPeek is a fan-made utility. It is not affiliated with, endorsed by, or sponsored by any game publisher. All trademarks belong to their respective owners.")), /*#__PURE__*/React.createElement(GroupLabel, null, "Data choices"), /*#__PURE__*/React.createElement("div", {
    style: {
      background: 'var(--surface-card)',
      border: '1px solid var(--border-subtle)',
      borderRadius: 'var(--radius-lg)',
      padding: '4px var(--space-4)',
      marginBottom: 'var(--space-5)'
    }
  }, /*#__PURE__*/React.createElement(ListRow, {
    label: "Anonymous analytics",
    description: "Aggregated taps & sessions. No screen data.",
    trailing: /*#__PURE__*/React.createElement(Switch, {
      checked: analytics,
      onChange: setAnalytics
    })
  }), /*#__PURE__*/React.createElement(ListRow, {
    label: "Crash reports",
    description: "Send diagnostics when the app crashes.",
    trailing: /*#__PURE__*/React.createElement(Switch, {
      checked: crash,
      onChange: setCrash
    }),
    divider: false
  })), /*#__PURE__*/React.createElement(GroupLabel, null, "Legal"), /*#__PURE__*/React.createElement("div", {
    style: {
      background: 'var(--surface-card)',
      border: '1px solid var(--border-subtle)',
      borderRadius: 'var(--radius-lg)',
      padding: '4px var(--space-4)',
      marginBottom: 'var(--space-5)'
    }
  }, /*#__PURE__*/React.createElement(ListRow, {
    label: "Privacy policy",
    trailing: /*#__PURE__*/React.createElement(ExternalIcon, null),
    onClick: () => {}
  }), /*#__PURE__*/React.createElement(ListRow, {
    label: "Terms of use",
    trailing: /*#__PURE__*/React.createElement(ExternalIcon, null),
    onClick: () => {}
  }), /*#__PURE__*/React.createElement(ListRow, {
    label: "Open-source licenses",
    description: "42 packages",
    trailing: /*#__PURE__*/React.createElement(Chevron, null),
    onClick: () => {},
    divider: false
  })), /*#__PURE__*/React.createElement("div", {
    style: {
      textAlign: 'center',
      fontFamily: 'var(--font-body)',
      fontSize: 11,
      color: 'var(--text-disabled)',
      lineHeight: 1.5
    }
  }, "Made by indie devs for the competitive community.", /*#__PURE__*/React.createElement("br", null), "\xA9 2026 PvPeek. All processing happens on your device.")));
}
Object.assign(window, {
  AboutScreen
});
})(); } catch (e) { __ds_ns.__errors.push({ path: "ui_kits/app/AboutScreen.jsx", error: String((e && e.message) || e) }); }

// ui_kits/app/ConsentDialog.jsx
try { (() => {
const NS = () => window.PvPeekDesignSystem_53b813;

/**
 * ConsentDialog — first-run anonymous-analytics opt-in. Plain language,
 * symmetric choice, no dark patterns. "No thanks" is a real option.
 */
function ConsentDialog({
  open,
  onAllow,
  onDecline
}) {
  const {
    Dialog,
    Button
  } = NS();
  return /*#__PURE__*/React.createElement(Dialog, {
    open: open,
    width: 320,
    onDismiss: onDecline,
    icon: /*#__PURE__*/React.createElement("div", {
      style: {
        width: 46,
        height: 46,
        borderRadius: 'var(--radius-md)',
        background: 'rgba(91,157,255,0.14)',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center'
      }
    }, /*#__PURE__*/React.createElement("svg", {
      width: "24",
      height: "24",
      viewBox: "0 0 24 24",
      fill: "none",
      stroke: "var(--blue-bright)",
      strokeWidth: "2"
    }, /*#__PURE__*/React.createElement("path", {
      d: "M3 12h4l3 8 4-16 3 8h4"
    }))),
    title: "Help improve PvPeek?",
    actions: /*#__PURE__*/React.createElement(React.Fragment, null, /*#__PURE__*/React.createElement(Button, {
      variant: "secondary",
      fullWidth: true,
      onClick: onDecline
    }, "No thanks"), /*#__PURE__*/React.createElement(Button, {
      variant: "primary",
      fullWidth: true,
      onClick: onAllow
    }, "Allow"))
  }, "Share anonymous, aggregated usage stats (taps, crashes) so we can prioritize fixes. We ", /*#__PURE__*/React.createElement("b", {
    style: {
      color: 'var(--text-secondary)'
    }
  }, "never"), " collect anything from your screen, and you can change this anytime in Settings.");
}
Object.assign(window, {
  ConsentDialog
});
})(); } catch (e) { __ds_ns.__errors.push({ path: "ui_kits/app/ConsentDialog.jsx", error: String((e && e.message) || e) }); }

// ui_kits/app/LaunchScreen.jsx
try { (() => {
const NS = () => window.PvPeekDesignSystem_53b813;

/**
 * LaunchScreen — first-run / onboarding. Branding, one-line value prop,
 * a short privacy explainer, and the primary "Start" action.
 */
function LaunchScreen({
  onStart
}) {
  const {
    Button
  } = NS();
  return /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      inset: 0,
      paddingTop: 36,
      display: 'flex',
      flexDirection: 'column',
      background: 'radial-gradient(120% 90% at 50% -10%, #232838 0%, var(--bg-app) 55%)'
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      flex: 1,
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      justifyContent: 'center',
      padding: '0 28px',
      textAlign: 'center'
    }
  }, /*#__PURE__*/React.createElement("img", {
    src: "../../assets/logo-icon.svg",
    width: "96",
    height: "96",
    alt: "PvPeek",
    style: {
      borderRadius: 24,
      boxShadow: 'var(--elev-3), var(--glow-gold)'
    }
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-display)',
      fontSize: 34,
      fontWeight: 700,
      letterSpacing: '-0.01em',
      marginTop: 'var(--space-5)',
      color: 'var(--white)'
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      color: 'var(--gold)'
    }
  }, "PvP"), "eek"), /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-display)',
      fontSize: 14,
      fontWeight: 600,
      letterSpacing: '0.18em',
      textTransform: 'uppercase',
      color: 'var(--text-tertiary)',
      marginTop: 6
    }
  }, "Glance \xB7 Verdict \xB7 Done"), /*#__PURE__*/React.createElement("p", {
    style: {
      fontFamily: 'var(--font-body)',
      fontSize: 15,
      lineHeight: 1.5,
      color: 'var(--text-secondary)',
      maxWidth: 260,
      marginTop: 'var(--space-5)'
    }
  }, "Tap once over your game and read a creature's IVs, league ranks, and best moveset in under two seconds.")), /*#__PURE__*/React.createElement("div", {
    style: {
      padding: '0 var(--space-5) var(--space-7)'
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      gap: 'var(--space-3)',
      alignItems: 'flex-start',
      background: 'var(--surface-card)',
      border: '1px solid var(--border-subtle)',
      borderRadius: 'var(--radius-lg)',
      padding: 'var(--space-4)',
      marginBottom: 'var(--space-4)'
    }
  }, /*#__PURE__*/React.createElement("svg", {
    width: "22",
    height: "22",
    viewBox: "0 0 24 24",
    fill: "none",
    stroke: "var(--gold)",
    strokeWidth: "2",
    style: {
      flex: '0 0 auto',
      marginTop: 1
    }
  }, /*#__PURE__*/React.createElement("rect", {
    x: "4",
    y: "11",
    width: "16",
    height: "10",
    rx: "2"
  }), /*#__PURE__*/React.createElement("path", {
    d: "M8 11V7a4 4 0 0 1 8 0v4"
  })), /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-body)',
      fontSize: 14,
      fontWeight: 700,
      color: 'var(--text-primary)'
    }
  }, "Private by design"), /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-body)',
      fontSize: 13,
      lineHeight: 1.45,
      color: 'var(--text-tertiary)',
      marginTop: 2
    }
  }, "Everything runs on-device. Nothing from your screen ever leaves your phone."))), /*#__PURE__*/React.createElement(Button, {
    variant: "primary",
    size: "lg",
    fullWidth: true,
    onClick: onStart
  }, "Start PvPeek"), /*#__PURE__*/React.createElement("div", {
    style: {
      textAlign: 'center',
      marginTop: 'var(--space-3)'
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-body)',
      fontSize: 12,
      color: 'var(--text-tertiary)'
    }
  }, "You'll be asked to allow \u201Cdraw over other apps\u201D."))));
}
Object.assign(window, {
  LaunchScreen
});
})(); } catch (e) { __ds_ns.__errors.push({ path: "ui_kits/app/LaunchScreen.jsx", error: String((e && e.message) || e) }); }

// ui_kits/overlay/BusyBackground.jsx
try { (() => {
/**
 * BusyBackground — an abstract, deliberately noisy "a game is running"
 * backdrop. NOT any real game: pure CSS shapes/gradients, used to prove
 * the verdict card's scrim keeps it legible over unpredictable colors.
 */
function BusyBackground() {
  return /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      inset: 0,
      overflow: 'hidden',
      background: '#1b6b4a'
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      inset: 0,
      background: 'linear-gradient(180deg,#7b4bd6 0%,#4aa3d6 38%,#3fb27a 60%,#1b6b4a 100%)'
    }
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      top: 70,
      right: 50,
      width: 150,
      height: 150,
      borderRadius: '50%',
      background: 'radial-gradient(circle,#fff7d6,#ffd23f 55%,rgba(255,210,63,0))'
    }
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      bottom: 0,
      left: 0,
      right: 0,
      height: 320,
      background: 'linear-gradient(180deg,#3fb27a,#176b46)'
    }
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      bottom: 120,
      left: -40,
      width: 300,
      height: 300,
      borderRadius: '50%',
      background: '#2a8f63',
      opacity: 0.8
    }
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      bottom: 60,
      right: -60,
      width: 260,
      height: 260,
      borderRadius: '50%',
      background: '#d9772b',
      opacity: 0.85
    }
  }), [['18%', '22%', 54, '#ff5da2'], ['70%', '30%', 38, '#ffd23f'], ['30%', '55%', 30, '#5bd6ff'], ['82%', '58%', 46, '#b06bff']].map(([l, t, s, c], i) => /*#__PURE__*/React.createElement("div", {
    key: i,
    style: {
      position: 'absolute',
      left: l,
      top: t,
      width: s,
      height: s,
      borderRadius: '50%',
      background: c,
      boxShadow: `0 0 24px ${c}`,
      opacity: 0.9
    }
  })), /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      top: '46%',
      left: 0,
      right: 0,
      height: 46,
      background: 'linear-gradient(90deg,#fff,rgba(255,255,255,0))',
      opacity: 0.5,
      transform: 'rotate(-4deg)'
    }
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      top: 48,
      left: 14,
      width: 90,
      height: 14,
      borderRadius: 7,
      background: 'rgba(255,255,255,0.85)'
    }
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      top: 48,
      left: 14,
      width: 60,
      height: 14,
      borderRadius: 7,
      background: '#ff4d4d'
    }
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      bottom: 24,
      left: '50%',
      transform: 'translateX(-50%)',
      display: 'flex',
      gap: 10
    }
  }, ['#ffd23f', '#5bd6ff', '#ff5da2'].map((c, i) => /*#__PURE__*/React.createElement("div", {
    key: i,
    style: {
      width: 52,
      height: 52,
      borderRadius: 14,
      background: c,
      opacity: 0.92
    }
  }))));
}
Object.assign(window, {
  BusyBackground
});
})(); } catch (e) { __ds_ns.__errors.push({ path: "ui_kits/overlay/BusyBackground.jsx", error: String((e && e.message) || e) }); }

// ui_kits/overlay/DismissTarget.jsx
try { (() => {
/**
 * DismissTarget — the drag-to-dismiss zone that fades in at the bottom
 * while the user drags the floating button. `armed` = button is hovering
 * over it (about to dismiss): it grows and turns red.
 */
function DismissTarget({
  visible = true,
  armed = false
}) {
  return /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      bottom: 40,
      left: '50%',
      transform: 'translateX(-50%)',
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      gap: 8,
      opacity: visible ? 1 : 0,
      transition: 'opacity var(--dur-base) var(--ease-out)',
      pointerEvents: 'none',
      zIndex: 40
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      width: armed ? 76 : 60,
      height: armed ? 76 : 60,
      borderRadius: '50%',
      background: armed ? 'rgba(206,17,38,0.92)' : 'rgba(20,21,26,0.78)',
      border: `1.5px solid ${armed ? 'var(--red-bright)' : 'rgba(255,255,255,0.3)'}`,
      backdropFilter: 'blur(8px)',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      color: '#fff',
      boxShadow: armed ? '0 0 28px rgba(255,77,94,0.6)' : '0 6px 18px rgba(0,0,0,0.5)',
      transition: 'all var(--dur-base) var(--ease-spring)'
    }
  }, /*#__PURE__*/React.createElement("svg", {
    width: "26",
    height: "26",
    viewBox: "0 0 24 24",
    fill: "none",
    stroke: "currentColor",
    strokeWidth: "2.4",
    strokeLinecap: "round"
  }, /*#__PURE__*/React.createElement("path", {
    d: "M6 6l12 12M18 6L6 18"
  }))), /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-display)',
      fontSize: 11,
      fontWeight: 600,
      letterSpacing: '0.08em',
      textTransform: 'uppercase',
      color: armed ? 'var(--red-bright)' : 'var(--white)',
      textShadow: '0 1px 4px rgba(0,0,0,0.7)'
    }
  }, armed ? 'Release to hide' : 'Drag here to hide'));
}
Object.assign(window, {
  DismissTarget
});
})(); } catch (e) { __ds_ns.__errors.push({ path: "ui_kits/overlay/DismissTarget.jsx", error: String((e && e.message) || e) }); }

// ui_kits/overlay/FloatingButton.jsx
try { (() => {
/**
 * FloatingButton — the always-on overlay trigger. Sits on the screen
 * edge over the game. Idle shows the crown-swords mark; `scanning`
 * shows a rotating ring + scan sweep while PvPeek reads the screen.
 */
function FloatingButton({
  state = 'idle',
  onClick,
  style = {}
}) {
  const scanning = state === 'scanning';
  return /*#__PURE__*/React.createElement("button", {
    type: "button",
    onClick: onClick,
    "aria-label": scanning ? 'Scanning' : 'Open PvPeek',
    style: {
      position: 'relative',
      width: 56,
      height: 56,
      borderRadius: '50%',
      border: 'none',
      padding: 0,
      cursor: 'pointer',
      background: 'radial-gradient(circle at 50% 35%, #2b313d, #15161A)',
      boxShadow: 'var(--elev-fab), 0 0 0 1.5px rgba(244,196,48,0.5), var(--glow-gold)',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      ...style
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      position: 'absolute',
      inset: -3,
      borderRadius: '50%',
      border: '2px solid transparent',
      borderTopColor: 'var(--gold)',
      borderRightColor: 'rgba(91,157,255,0.9)',
      opacity: scanning ? 1 : 0,
      animation: scanning ? 'pp-spin 0.8s linear infinite' : 'none'
    }
  }), /*#__PURE__*/React.createElement("span", {
    style: {
      position: 'absolute',
      inset: 0,
      borderRadius: '50%',
      boxShadow: '0 0 0 0 rgba(244,196,48,0.5)',
      animation: scanning ? 'pp-pulse 1.2s var(--ease-out) infinite' : 'none'
    }
  }), /*#__PURE__*/React.createElement("img", {
    src: "../../assets/logo-mark.svg",
    width: "34",
    height: "34",
    alt: "",
    style: {
      filter: scanning ? 'saturate(1.2)' : 'none'
    }
  }), /*#__PURE__*/React.createElement("style", null, `
        @keyframes pp-spin { to { transform: rotate(360deg); } }
        @keyframes pp-pulse {
          0% { box-shadow: 0 0 0 0 rgba(244,196,48,0.45); }
          100% { box-shadow: 0 0 0 14px rgba(244,196,48,0); }
        }
      `));
}
Object.assign(window, {
  FloatingButton
});
})(); } catch (e) { __ds_ns.__errors.push({ path: "ui_kits/overlay/FloatingButton.jsx", error: String((e && e.message) || e) }); }

// ui_kits/overlay/VerdictCard.jsx
try { (() => {
const NS = () => window.PvPeekDesignSystem_53b813;

// Local copy of the rank-tier mapping (lowercase exports aren't exposed on the namespace).
function tierColor(percent) {
  if (percent >= 99) return 'var(--tier-perfect)';
  if (percent >= 95) return 'var(--tier-great)';
  if (percent >= 88) return 'var(--tier-good)';
  if (percent >= 75) return 'var(--tier-fair)';
  return 'var(--tier-weak)';
}
function Section({
  label,
  right,
  children
}) {
  return /*#__PURE__*/React.createElement("div", {
    style: {
      marginTop: 'var(--space-4)'
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      justifyContent: 'space-between',
      alignItems: 'baseline',
      marginBottom: 'var(--space-2)'
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-display)',
      fontSize: 10,
      fontWeight: 600,
      letterSpacing: '0.14em',
      textTransform: 'uppercase',
      color: 'var(--text-tertiary)'
    }
  }, label), right), children);
}
function IconBtn({
  label,
  onClick,
  children
}) {
  return /*#__PURE__*/React.createElement("button", {
    type: "button",
    onClick: onClick,
    "aria-label": label,
    style: {
      width: 28,
      height: 28,
      borderRadius: '50%',
      flex: '0 0 auto',
      background: 'var(--fill-ghost-hover)',
      border: '1px solid var(--border-subtle)',
      color: 'var(--text-secondary)',
      cursor: 'pointer',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center'
    }
  }, children);
}
function MiniStat({
  label,
  value,
  color,
  sub
}) {
  return /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      flexDirection: 'column',
      gap: 3,
      minWidth: 0
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-display)',
      fontSize: 9,
      fontWeight: 600,
      letterSpacing: '0.1em',
      color: 'var(--text-tertiary)'
    }
  }, label), /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-mono)',
      fontVariantNumeric: 'tabular-nums',
      fontSize: 15,
      fontWeight: 700,
      lineHeight: 1,
      color
    }
  }, value), sub && /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-mono)',
      fontVariantNumeric: 'tabular-nums',
      fontSize: 10,
      fontWeight: 600,
      lineHeight: 1,
      color: 'var(--text-tertiary)',
      whiteSpace: 'nowrap'
    }
  }, sub));
}

/**
 * VerdictCard — the hero overlay, as an EXPANDABLE bottom sheet.
 *  • compact (expanded=false): a slim peek — identity + CP + the three
 *    headline numbers (IV%, Great %, Ultra %), tier-colored. The game
 *    stays visible all around it; no full-screen scrim.
 *  • expanded (expanded=true): full detail — IV bars, both rank rows,
 *    the recommended moveset. Tap the chevron to toggle.
 */
function VerdictCard({
  specimen,
  expanded = false,
  onToggle,
  onClose
}) {
  const {
    Card,
    Badge,
    IVStat,
    RankBadge,
    MoveChip
  } = NS();
  const d = specimen || VerdictCard.sample;
  const ivColor = tierColor(d.ivPercent);
  const glColor = tierColor(d.great.percent);
  const ulColor = tierColor(d.ultra.percent);
  const Header = /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 10
    }
  }, /*#__PURE__*/React.createElement("img", {
    src: "../../assets/logo-mark.svg",
    width: "26",
    height: "26",
    alt: "",
    style: {
      flex: '0 0 auto'
    }
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      flex: 1,
      minWidth: 0
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 6
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-display)',
      fontSize: 16,
      fontWeight: 700,
      color: 'var(--text-primary)',
      letterSpacing: '-0.01em',
      whiteSpace: 'nowrap',
      overflow: 'hidden',
      textOverflow: 'ellipsis'
    }
  }, d.name), d.hundo && /*#__PURE__*/React.createElement(Badge, {
    tone: "gold",
    solid: true
  }, "Hundo")), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'baseline',
      gap: 5,
      marginTop: 1
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-mono)',
      fontVariantNumeric: 'tabular-nums',
      fontSize: 18,
      fontWeight: 700,
      color: 'var(--white)',
      lineHeight: 1
    }
  }, d.cp.toLocaleString()), /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-display)',
      fontSize: 11,
      fontWeight: 600,
      color: 'var(--text-tertiary)',
      letterSpacing: '0.06em'
    }
  }, "CP"), /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-mono)',
      fontSize: 11,
      color: 'var(--text-tertiary)',
      marginLeft: 6,
      whiteSpace: 'nowrap'
    }
  }, "Lv ", d.level))), /*#__PURE__*/React.createElement(IconBtn, {
    label: expanded ? 'Collapse' : 'Expand',
    onClick: onToggle
  }, /*#__PURE__*/React.createElement("svg", {
    width: "15",
    height: "15",
    viewBox: "0 0 24 24",
    fill: "none",
    stroke: "currentColor",
    strokeWidth: "2.2",
    strokeLinecap: "round",
    strokeLinejoin: "round",
    style: {
      transform: expanded ? 'rotate(180deg)' : 'none',
      transition: 'transform var(--dur-base) var(--ease-out)'
    }
  }, /*#__PURE__*/React.createElement("path", {
    d: "M6 15l6-6 6 6"
  }))), /*#__PURE__*/React.createElement(IconBtn, {
    label: "Dismiss",
    onClick: onClose
  }, /*#__PURE__*/React.createElement("svg", {
    width: "15",
    height: "15",
    viewBox: "0 0 24 24",
    fill: "none",
    stroke: "currentColor",
    strokeWidth: "2.4",
    strokeLinecap: "round"
  }, /*#__PURE__*/React.createElement("path", {
    d: "M6 6l12 12M18 6L6 18"
  }))));
  return /*#__PURE__*/React.createElement(Card, {
    variant: "overlay",
    padding: "14px 16px",
    style: {
      width: 340,
      position: 'relative'
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      width: 34,
      height: 4,
      borderRadius: 2,
      background: 'var(--border-strong)',
      margin: '-4px auto 12px'
    }
  }), Header, !expanded && /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'flex-start',
      gap: 'var(--space-3)',
      marginTop: 'var(--space-3)',
      paddingTop: 'var(--space-3)',
      borderTop: '1px solid var(--border-subtle)'
    }
  }, /*#__PURE__*/React.createElement(MiniStat, {
    label: "IV",
    value: `${d.ivPercent}%`,
    color: ivColor
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      width: 1,
      alignSelf: 'stretch',
      background: 'var(--border-subtle)'
    }
  }), /*#__PURE__*/React.createElement(MiniStat, {
    label: "GREAT",
    value: `#${d.great.rank}`,
    color: glColor,
    sub: `${d.great.percent}% · ${d.great.total}`
  }), /*#__PURE__*/React.createElement(MiniStat, {
    label: "ULTRA",
    value: `#${d.ultra.rank}`,
    color: ulColor,
    sub: `${d.ultra.percent}% · ${d.ultra.total}`
  }), /*#__PURE__*/React.createElement("button", {
    type: "button",
    onClick: onToggle,
    "aria-label": "Expand",
    style: {
      marginLeft: 'auto',
      alignSelf: 'center',
      background: 'none',
      border: 'none',
      cursor: 'pointer',
      color: 'var(--text-tertiary)',
      display: 'flex',
      alignItems: 'center',
      padding: 4
    }
  }, /*#__PURE__*/React.createElement("svg", {
    width: "15",
    height: "15",
    viewBox: "0 0 24 24",
    fill: "none",
    stroke: "currentColor",
    strokeWidth: "2.2",
    strokeLinecap: "round"
  }, /*#__PURE__*/React.createElement("path", {
    d: "M9 6l6 6-6 6"
  })))), expanded && /*#__PURE__*/React.createElement(React.Fragment, null, /*#__PURE__*/React.createElement(Section, {
    label: "IVs",
    right: /*#__PURE__*/React.createElement("span", {
      style: {
        fontFamily: 'var(--font-mono)',
        fontSize: 13,
        fontWeight: 700,
        color: ivColor
      }
    }, d.ivPercent, "%")
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      gap: 'var(--space-4)'
    }
  }, /*#__PURE__*/React.createElement(IVStat, {
    label: "ATK",
    value: d.iv.atk
  }), /*#__PURE__*/React.createElement(IVStat, {
    label: "DEF",
    value: d.iv.def
  }), /*#__PURE__*/React.createElement(IVStat, {
    label: "STA",
    value: d.iv.sta
  }))), /*#__PURE__*/React.createElement(Section, {
    label: "League ranks"
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      flexDirection: 'column',
      gap: 'var(--space-2)'
    }
  }, /*#__PURE__*/React.createElement(RankBadge, {
    league: "Great League",
    rank: d.great.rank,
    total: d.great.total,
    percent: d.great.percent
  }), /*#__PURE__*/React.createElement(RankBadge, {
    league: "Ultra League",
    rank: d.ultra.rank,
    total: d.ultra.total,
    percent: d.ultra.percent
  }))), /*#__PURE__*/React.createElement(Section, {
    label: "Recommended moveset"
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      flexDirection: 'column',
      gap: 'var(--space-2)'
    }
  }, /*#__PURE__*/React.createElement(MoveChip, {
    kind: "fast",
    name: d.moves.fast.name,
    count: d.moves.fast.turns,
    recommended: true
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      gap: 'var(--space-2)'
    }
  }, d.moves.charged.map((m, i) => /*#__PURE__*/React.createElement("div", {
    key: i,
    style: {
      flex: 1,
      minWidth: 0
    }
  }, /*#__PURE__*/React.createElement(MoveChip, {
    kind: "charged",
    name: m.name,
    count: m.count,
    recommended: i === 0
  })))))), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 6,
      marginTop: 'var(--space-4)',
      paddingTop: 'var(--space-3)',
      borderTop: '1px solid var(--border-subtle)'
    }
  }, /*#__PURE__*/React.createElement("svg", {
    width: "13",
    height: "13",
    viewBox: "0 0 24 24",
    fill: "none",
    stroke: "var(--text-tertiary)",
    strokeWidth: "2"
  }, /*#__PURE__*/React.createElement("rect", {
    x: "5",
    y: "11",
    width: "14",
    height: "9",
    rx: "2"
  }), /*#__PURE__*/React.createElement("path", {
    d: "M8 11V8a4 4 0 0 1 8 0v3"
  })), /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-body)',
      fontSize: 11,
      color: 'var(--text-tertiary)'
    }
  }, "On-device \xB7 read in 0.4s \xB7 nothing left your phone"))));
}
VerdictCard.sample = {
  name: 'Specimen-12',
  cp: 1498,
  level: 31.5,
  hundo: false,
  ivPercent: 98.2,
  iv: {
    atk: 15,
    def: 14,
    sta: 13
  },
  great: {
    rank: 12,
    total: 4096,
    percent: 99.2
  },
  ultra: {
    rank: 188,
    total: 4096,
    percent: 95.4
  },
  moves: {
    fast: {
      name: 'Quick Jab',
      turns: 3
    },
    charged: [{
      name: 'Surge Beam',
      count: 4
    }, {
      name: 'Iron Crash',
      count: 6
    }]
  }
};
Object.assign(window, {
  VerdictCard
});
})(); } catch (e) { __ds_ns.__errors.push({ path: "ui_kits/overlay/VerdictCard.jsx", error: String((e && e.message) || e) }); }

// ui_kits/shared/PhoneFrame.jsx
try { (() => {
/**
 * PhoneFrame — a lightweight Android device shell for kit demos.
 * Fixed 360×760 viewport with a status bar; children fill the screen.
 */
function PhoneFrame({
  children,
  statusDark = false,
  bg = 'var(--bg-app)',
  label
}) {
  const fg = statusDark ? '#0E0F12' : 'var(--white)';
  return /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      gap: 10
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'relative',
      width: 360,
      height: 760,
      background: bg,
      borderRadius: 40,
      border: '10px solid #05060a',
      boxShadow: '0 30px 80px rgba(0,0,0,0.6), inset 0 0 0 1px rgba(255,255,255,0.04)',
      overflow: 'hidden'
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      top: 0,
      left: 0,
      right: 0,
      height: 36,
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'space-between',
      padding: '0 18px',
      zIndex: 30,
      pointerEvents: 'none',
      fontFamily: 'var(--font-mono)',
      fontSize: 12,
      fontWeight: 600,
      color: fg
    }
  }, /*#__PURE__*/React.createElement("span", null, "9:41"), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      gap: 6,
      alignItems: 'center',
      opacity: 0.9
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      fontSize: 11
    }
  }, "5G"), /*#__PURE__*/React.createElement("span", null, "\u25AE\u25AE\u25AE"), /*#__PURE__*/React.createElement("span", null, "78%"))), /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      top: 12,
      left: '50%',
      transform: 'translateX(-50%)',
      width: 8,
      height: 8,
      borderRadius: '50%',
      background: '#000',
      zIndex: 31,
      boxShadow: 'inset 0 0 0 1px rgba(255,255,255,0.1)'
    }
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      inset: 0,
      paddingTop: 36
    }
  }, children)), label && /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-mono)',
      fontSize: 11,
      color: 'var(--text-tertiary)'
    }
  }, label));
}
Object.assign(window, {
  PhoneFrame
});
})(); } catch (e) { __ds_ns.__errors.push({ path: "ui_kits/shared/PhoneFrame.jsx", error: String((e && e.message) || e) }); }

__ds_ns.Badge = __ds_scope.Badge;

__ds_ns.Button = __ds_scope.Button;

__ds_ns.Card = __ds_scope.Card;

__ds_ns.Dialog = __ds_scope.Dialog;

__ds_ns.ListRow = __ds_scope.ListRow;

__ds_ns.Switch = __ds_scope.Switch;

__ds_ns.IVStat = __ds_scope.IVStat;

__ds_ns.MoveChip = __ds_scope.MoveChip;

__ds_ns.RankBadge = __ds_scope.RankBadge;

})();
