# rn-phonenumber-detector

<p align="center">
  <img 
    src="https://raw.githubusercontent.com/MadhurmeetJadhav/rn-phonenumber-detector/main/assets/ScreenShot.jpg" 
    width="300" 
    alt="Phone Number Hint Demo"
  />
</p>

Auto-detect and fetch phone number on Android via Google Identity API. Supports auto-linking — no manual native setup needed.

---

## Installation
```bash
yarn add rn-phonenumber-detector
# or
npm install rn-phonenumber-detector
```

### Android Setup

Add to `android/app/build.gradle`:
```gradle
dependencies {
    implementation 'com.google.android.gms:play-services-auth:21.0.0'
}
```

Then rebuild:
```bash
cd android && ./gradlew clean
cd ..
npx react-native run-android
```

That's it! Auto-linking handles the rest. ✅

---

## Usage

### Option 1 — Direct call (recommended)

Best when you already have your own hook or state management:
```ts
import { requestPhoneHint } from 'rn-phonenumber-detector';
import { Platform } from 'react-native';

const triggerPhoneHint = () => {
  if (Platform.OS !== 'android') return;

  requestPhoneHint()
    .then((number) => {
      // number comes as +91XXXXXXXXXX
      const stripped = number.replace(/^\+91/, '').trim();
      setInputValue(stripped); // wire to your input state
    })
    .catch(() => {
      // silent fail — user dismissed or no number found
    });
};

// call it when screen is focused
useEffect(() => {
  triggerPhoneHint();
}, []);
```

### Option 2 — Hook

Best for simple use cases where you manage phone input directly:
```ts
import { usePhoneHint } from 'rn-phonenumber-detector';

const MyScreen = () => {
  const [inputValue, setInputValue] = useState('');

  const { triggerHint, loading } = usePhoneHint({
    countryCallingCode: '91',         // strips +91 from result
    onSuccess: (number) => setInputValue(number), // wire to your input
    onError: () => {},                // silent fail
  });

  // trigger when screen is focused
  useEffect(() => {
    triggerHint();
  }, []);

  return (
    <TextInput
      value={inputValue}
      onChangeText={setInputValue}
      keyboardType="number-pad"
      textContentType="telephoneNumber"
      placeholder="Enter phone number"
    />
  );
};
```

---

## iOS

No extra code needed. Just add these props to your `TextInput`:
```tsx
<TextInput
  keyboardType="number-pad"
  textContentType="telephoneNumber" // ✅ iOS shows number suggestion above keyboard
/>
```

---

## API

### `requestPhoneHint()`
```ts
requestPhoneHint(): Promise<string>
```

| | |
|---|---|
| Returns | `Promise<string>` — full number e.g. `+919876543210` |
| Platform | Android only |
| Fails | If user dismisses or no number found |

---

### `usePhoneHint(options)`
```ts
usePhoneHint(options?: UsePhoneHintOptions): UsePhoneHintResult
```

**Options:**

| Option | Type | Default | Description |
|---|---|---|---|
| `countryCallingCode` | `string` | `'91'` | Strips country code prefix from result |
| `onSuccess` | `(number: string) => void` | — | Called with stripped local number |
| `onError` | `(error: Error) => void` | — | Called on failure or dismiss |

**Returns:**

| Value | Type | Description |
|---|---|---|
| `phoneNumber` | `string` | Auto-fetched local number |
| `loading` | `boolean` | `true` while hint is being fetched |
| `error` | `string \| null` | Error message if failed |
| `triggerHint` | `() => void` | Call this to show the Google picker |

---

## Platform Support

| Platform | Support | Method |
|---|---|---|
| Android | ✅ | Google Identity Hint API — native bottom sheet |
| iOS | ✅ | `textContentType='telephoneNumber'` — QuickType bar |

---

## Notes

- Works only on **real devices** — not emulators
- Requires **Google Play Services** on Android
- Fails **silently** if no number found — user types manually
- Number format from `requestPhoneHint`: `+91XXXXXXXXXX`
- Number format from `usePhoneHint` onSuccess: `XXXXXXXXXX` (country code stripped)

---

## License

MIT © Madhurmeet Jadhav