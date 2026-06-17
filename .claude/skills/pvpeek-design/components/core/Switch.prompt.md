Switch is the on/off toggle for data-choice and settings rows (analytics opt-in, crash reports). Gold track when on.

```jsx
<Switch checked={analytics} onChange={setAnalytics} id="analytics" />
```

Controlled component — pass `checked` and handle `onChange(next)`. Pair with a ListRow label. 28px tall thumb, spring-eased travel.
