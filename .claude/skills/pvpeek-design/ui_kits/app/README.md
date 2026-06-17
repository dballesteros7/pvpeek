# PvPeek — App UI kit

The standalone (non-overlay) app surfaces: first-run **launch / onboarding**, the **consent** opt-in, and the **About & Legal** screen.

## Screens
- `LaunchScreen.jsx` — branding (app icon + wordmark), the "Glance · Verdict · Done" value prop, a private-by-design explainer card, and the primary **Start PvPeek** button.
- `ConsentDialog.jsx` — first-run anonymous-analytics opt-in built on the `Dialog` component. Symmetric choice, plain language, no dark patterns.
- `AboutScreen.jsx` — app identity + version, the **unofficial-tool affiliation disclaimer**, data-choice toggles (analytics, crash reports), and legal links (privacy, terms, open-source licenses).

## Demo (`index.html`)
Two phones side by side. Left: Launch → tap **Start** → consent dialog → lands on About. Right: the About screen standalone. Toggles are live.

## Components used from the design system
`Button`, `Dialog`, `ListRow`, `Switch`, `Badge` — via `window.PvPeekDesignSystem_53b813` (the `_ds_bundle.js`).

## Notes
- Everything reinforces the privacy promise — copy repeats "on-device" / "nothing leaves your phone".
- `../shared/PhoneFrame.jsx` provides the device shell.
