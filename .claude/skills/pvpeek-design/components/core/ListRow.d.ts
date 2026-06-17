import React from 'react';

/**
 * ListRow — label + optional description + optional trailing control,
 * used on the About / settings screens.
 */
export interface ListRowProps {
  label: React.ReactNode;
  description?: React.ReactNode;
  /** Trailing slot: a Switch, chevron, value text, etc. */
  trailing?: React.ReactNode;
  leading?: React.ReactNode;
  onClick?: () => void;
  divider?: boolean;
  style?: React.CSSProperties;
}

export function ListRow(props: ListRowProps): JSX.Element;
