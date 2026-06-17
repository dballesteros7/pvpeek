ListRow is a settings/about-screen row: label, optional supporting description, and an optional trailing control (Switch, chevron, version string).

```jsx
<ListRow label="Anonymous analytics" description="Helps us prioritize fixes. No screen data." trailing={<Switch checked={a} onChange={setA} />} />
<ListRow label="Privacy policy" trailing={<span>↗</span>} onClick={open} />
<ListRow label="Version" trailing={<span className="t-num">1.4.0</span>} divider={false} />
```

Min height honours the 44px tap target. Pass `onClick` to make it interactive (hover fill appears).
