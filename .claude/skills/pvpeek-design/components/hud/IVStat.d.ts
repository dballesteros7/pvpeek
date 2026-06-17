import React from 'react';

/**
 * IVStat — a single 0–15 IV (Attack / Defense / Stamina) with a
 * 15-segment bar tinted by tier color. The verdict card shows three.
 */
export interface IVStatProps {
  /** Short stat label, e.g. "ATK". */
  label: string;
  /** Value 0–15. */
  value: number;
  /** Max segments (default 15). */
  max?: number;
  style?: React.CSSProperties;
}

export function IVStat(props: IVStatProps): JSX.Element;

/** Map an IV value (0–15) to its tier color + short label. */
export function ivTier(value: number): { color: string; label: string };
