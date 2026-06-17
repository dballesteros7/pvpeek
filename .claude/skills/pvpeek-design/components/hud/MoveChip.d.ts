import React from 'react';

/**
 * MoveChip — a recommended fast or charged move on the verdict card.
 */
export interface MoveChipProps {
  kind?: 'fast' | 'charged';
  name: string;
  /** Fast: turns per use. Charged: fast moves needed to charge. */
  count: number;
  /** Highlight as the primary recommendation. */
  recommended?: boolean;
  style?: React.CSSProperties;
}

export function MoveChip(props: MoveChipProps): JSX.Element;
