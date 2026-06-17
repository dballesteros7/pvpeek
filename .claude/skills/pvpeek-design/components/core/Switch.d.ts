import React from 'react';

/**
 * Switch — binary toggle used in settings and consent rows.
 */
export interface SwitchProps {
  checked?: boolean;
  disabled?: boolean;
  onChange?: (next: boolean) => void;
  id?: string;
}

export function Switch(props: SwitchProps): JSX.Element;
