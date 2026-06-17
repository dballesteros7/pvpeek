import React from 'react';

/**
 * Badge — small status / label pill.
 */
export interface BadgeProps {
  children?: React.ReactNode;
  tone?: 'neutral' | 'gold' | 'blue' | 'red' | 'green';
  /** Solid fill instead of tinted background. */
  solid?: boolean;
  style?: React.CSSProperties;
}

export function Badge(props: BadgeProps): JSX.Element;
