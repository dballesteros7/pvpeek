# Observability

Two Firebase signals, both in the **PogoPvp** project (`pogopvp-f4f1d`).

## 1. Crashes — Crashlytics
- Fatal crashes auto-captured.
- The scan/appraise paths report **non-fatals** via `OverlayService.reportCrash`, tagged with a
  `stage` custom key (`read` / `analyze`).
- Console: https://console.firebase.google.com/project/pogopvp-f4f1d/crashlytics

## 2. Usage — Analytics
One event per **ANALYZE** tap: `appraise_result` (`Analytics.kt`).

| Param            | Type   | Values                                   | Meaning |
|------------------|--------|------------------------------------------|---------|
| `outcome`        | text   | `ok` / `name_unrecognized` / `capture_failed` | did the scan resolve a species |
| `name_source`    | text   | `caught` / `card` / `none`               | where the name came from (caught-line vs card nickname) |
| `species`        | text   | OCR'd name (≤60 chars)                   | which Pokémon — incl. garbage on misreads |
| `cp_read`        | text   | `true` / `false`                         | was CP OCR'd |
| `frame`          | text   | `flip7` / `flip5` / `<w>x<h>`            | coarse device bucket |
| `types_detected` | number | 0 / 1 / 2                                | how many type words were read |

### Privacy
Only the derived signals above are sent. **Never**: screen content, images, raw OCR text, or the
caught-info line (which holds the user's location + catch date). The "nothing from your screen
leaves the phone" promise stays true — this is metadata about *how the read went*, not *what was on screen*.

## Viewing

### Real-time (testing) — DebugView
```sh
adb shell setprop debug.firebase.analytics.app com.pogopvp.overlay   # enable
# … scan a few Pokémon …
adb shell setprop debug.firebase.analytics.app .none                 # disable
```
Then Firebase → Analytics → **DebugView** (events appear within seconds).

### Dashboard — register custom definitions (one-time)
Custom params are collected immediately but only chartable after you register them.
Firebase → Analytics → **Custom definitions** → *Create*. Create these (all **event-scoped**):

| Dimension name | Event parameter |
|----------------|-----------------|
| outcome        | `outcome`       |
| name source    | `name_source`   |
| species        | `species`       |
| cp read        | `cp_read`       |
| frame          | `frame`         |

And one **custom metric**:

| Metric name    | Event parameter  | Unit     |
|----------------|------------------|----------|
| types detected | `types_detected` | Standard |

Data accrues from registration onward (no backfill), so register early. Standard reports lag ~24h.

### Raw / ad-hoc — BigQuery (most powerful)
Link once: Firebase → Project settings → **Integrations → BigQuery** → link (daily export, free tier).
Events land in `pogopvp-f4f1d.analytics_<PROPERTY_ID>.events_*`.

Failure rate by species:
```sql
SELECT
  (SELECT value.string_value FROM UNNEST(event_params) WHERE key='species')  AS species,
  (SELECT value.string_value FROM UNNEST(event_params) WHERE key='outcome')  AS outcome,
  COUNT(*) AS n
FROM `pogopvp-f4f1d.analytics_PROPERTY_ID.events_*`
WHERE event_name = 'appraise_result'
GROUP BY species, outcome
ORDER BY n DESC;
```

Where do unrecognized names come from (name source × device)?
```sql
SELECT
  (SELECT value.string_value FROM UNNEST(event_params) WHERE key='name_source') AS name_source,
  (SELECT value.string_value FROM UNNEST(event_params) WHERE key='frame')       AS frame,
  COUNT(*) AS n
FROM `pogopvp-f4f1d.analytics_PROPERTY_ID.events_*`
WHERE event_name = 'appraise_result'
  AND (SELECT value.string_value FROM UNNEST(event_params) WHERE key='outcome') = 'name_unrecognized'
GROUP BY name_source, frame
ORDER BY n DESC;
```

Overall success rate:
```sql
SELECT
  (SELECT value.string_value FROM UNNEST(event_params) WHERE key='outcome') AS outcome,
  COUNT(*) AS n
FROM `pogopvp-f4f1d.analytics_PROPERTY_ID.events_*`
WHERE event_name = 'appraise_result'
GROUP BY outcome;
```

Replace `PROPERTY_ID` with the number shown on the BigQuery dataset after linking.
