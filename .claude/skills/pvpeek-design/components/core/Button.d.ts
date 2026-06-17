import React from 'react';

/**
 * Button — PvPeek's action control.
 */
export interface ButtonProps {
  children?: React.ReactNode;
  /** Visual emphasis. Gold `primary` used once per screen. */
  variant?: 'primary' | 'secondary' | 'ghost' | 'danger';
  size?: 'sm' | 'md' | 'lg';
  /** Stretch to container width (full-width primary CTA pattern). */
  fullWidth?: boolean;
  disabled?: boolean;
  iconLeft?: React.ReactNode;
  iconRight?: React.ReactNode;
  onClick?: (e: React.MouseEvent<HTMLButtonElement>) => void;
  style?: React.CSSProperties;
}

export function Button(props: ButtonProps): JSX.Element;
