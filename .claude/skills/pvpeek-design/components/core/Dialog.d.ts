import React from 'react';

/**
 * Dialog — centered modal over a scrim (consent opt-in, confirmations).
 */
export interface DialogProps {
  open?: boolean;
  title?: React.ReactNode;
  children?: React.ReactNode;
  /** Optional icon/illustration above the title. */
  icon?: React.ReactNode;
  /** Footer action buttons (lay out Buttons here). */
  actions?: React.ReactNode;
  onDismiss?: () => void;
  width?: number;
  style?: React.CSSProperties;
}

export function Dialog(props: DialogProps): JSX.Element | null;
