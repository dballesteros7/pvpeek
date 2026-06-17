import React from 'react';

/**
 * RankBadge — a single league standing (rank + percentile) for the
 * verdict card. The percentage drives the tier color + label.
 */
export interface RankBadgeProps {
  /** League name, e.g. "Great League". */
  league: string;
  /** Rank position (1 = best). */
  rank: number;
  /** Size of the ranked pool. */
  total: number;
  /** Percentile 0–100; drives the tier color. */
  percent: number;
  style?: React.CSSProperties;
}

export function RankBadge(props: RankBadgeProps): JSX.Element;

/** Map a rank percentage (0–100) to a tier color + label. */
export function rankTier(percent: number): { color: string; label: string };
