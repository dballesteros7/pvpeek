import React from 'react';

/**
 * Card — surface container. `overlay` = the floating verdict-card treatment.
 */
export interface CardProps {
  children?: React.ReactNode;
  variant?: 'default' | 'inset' | 'overlay';
  /** CSS padding value. */
  padding?: string;
  style?: React.CSSProperties;
}

export function Card(props: CardProps): JSX.Element;
