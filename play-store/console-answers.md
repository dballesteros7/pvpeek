# Play Console — questionnaire answers (PvPeek)

Map these to the closest option if the exact wording differs.

## Content rating questionnaire (IARC)
- **Category:** Utility, Productivity, Communication, or Other
- **Email:** your contact email (e.g. privacy@pollera-colora.com)

| Question | Answer |
|---|---|
| Violence (realistic or cartoon) | **No** |
| Fear / horror | **No** |
| Sexual content or nudity | **No** |
| Bad language / profanity | **No** |
| Controlled substances (drugs, alcohol, tobacco) | **No** |
| Gambling (simulated or real) | **No** |
| Do users interact, communicate, or share content with each other? | **No** |
| Does the app share the user's current physical location with other users? | **No** |
| Can users purchase digital goods? | **No** |
| Is it a web browser or search engine? | **No** |
| Does the app contain or promote any other mature content? | **No** |

→ Expected rating: **Everyone / PEGI 3**.

## Data safety form
- **Does your app collect or share any of the required user data types?** → **Yes**
- **Is all of the user data collected by your app encrypted in transit?** → **Yes**
- **Do you provide a way for users to request that their data is deleted?** → **Yes** (via the contact email)

For every data type below: **Collected = Yes, Shared = No, Processed ephemerally = No,
Collection is optional = "Users can choose whether this data is collected"** (analytics is opt-in;
crash reporting can be turned off in About & Legal). Purposes: **Analytics** + **App functionality**.

| Google data type | Collect? | What it is |
|---|---|---|
| App activity → **App interactions** | Yes | the per-scan `appraise_result` event |
| App info and performance → **Crash logs** | Yes | stack traces |
| App info and performance → **Diagnostics** | Yes | device model, OS version, app version |
| Device or other IDs → **Device or other IDs** | Yes | Firebase app-instance / installation ID |

**Mark "No" / not collected for everything else:** Location, Personal info (name, email, user IDs,
address, phone), Financial info, Health & fitness, Messages, Photos or videos, Audio files, Files
and docs, Calendar, Contacts, Web browsing history, Installed apps, Search history.
(Screen contents / OCR text are processed on-device only and are **not** collected or sent.)

## Other "App content" declarations
| Section | Answer |
|---|---|
| Privacy policy URL | https://dballesteros7.github.io/pvpeek/privacy.html |
| Ads — does your app contain ads? | **No** |
| App access — is all functionality available without special access? | **Yes** (no login/account) |
| Target audience — age groups | **16–17, 18+** (16 and over; matches the under-16 privacy policy + GDPR consent age) |
| Does the app appeal to children? | **No** |
| News app? | **No** |
| COVID-19 contact tracing / status? | **No** |
| Government app? | **No** |
| Financial features? | **No** |
| Health content? | **No** |
| Contains an in-app messaging/social feature? | **No** |
