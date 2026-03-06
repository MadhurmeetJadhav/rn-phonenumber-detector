# rn-phonenumber-detector

<p align="center">
  <img 
    src="https://raw.githubusercontent.com/MadhurmeetJadhav/rn-phonenumber-detector/main/assets/banner.png" 
    width="100%" 
    alt="rn-phonenumber-detector banner"
  />

</p>

  <p align="center">
    <img 
      src="https://raw.githubusercontent.com/MadhurmeetJadhav/rn-phonenumber-detector/main/assets/ScreenShot.jpg" 
      width="300" 
      alt="Phone Number Hint Demo"
    />
</p>

<p align="center">
  <a href="https://www.npmjs.com/package/rn-phonenumber-detector">
    <img src="https://img.shields.io/npm/v/rn-phonenumber-detector.svg" alt="npm version" />
  </a>
  <a href="https://www.npmjs.com/package/rn-phonenumber-detector">
    <img src="https://img.shields.io/npm/dm/rn-phonenumber-detector.svg" alt="npm downloads" />
  </a>
  <a href="https://github.com/MadhurmeetJadhav/rn-phonenumber-detector/blob/main/LICENSE">
    <img src="https://img.shields.io/badge/license-MIT-blue.svg" alt="license" />
  </a>
  <img src="https://img.shields.io/badge/platform-Android%20%7C%20iOS-brightgreen" alt="platform" />
  <img src="https://img.shields.io/badge/react--native-%3E%3D0.70-blue" alt="react native version" />
</p>

<p align="center">
  Auto-detect and fetch phone number on Android via Google Identity API.<br/>
  Supports auto-linking — no manual native setup needed.
</p>

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
    countryCallingCode: '91',
    onSuccess: (number) => setInputValue(number),
    onError: () => {},
  });

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
  textContentType="telephoneNumber"
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
| Android | ✅ | Google Identity Hint API |
| iOS | ✅ | `textContentType='telephoneNumber'` |

---

## Notes

- Works only on **real devices** — not emulators
- Requires **Google Play Services** on Android
- Fails **silently** if no number found — user types manually
- Number format from `requestPhoneHint`: `+91XXXXXXXXXX`
- Number format from `usePhoneHint` onSuccess: `XXXXXXXXXX` (country code stripped)

---

## Changelog

### v1.0.7
- Added auto-linking support
- Improved README with complete usage docs
- Added more keywords for discoverability

### v1.0.1
- Initial release
- Android Google Identity API support
- `requestPhoneHint` direct call
- `usePhoneHint` hook

---

## Contributing

Contributions are welcome! Here's how:

1. Fork the repo
2. Create your branch:
```bash
git checkout -b feature/my-feature
```
3. Commit your changes:
```bash
git commit -m "add my feature"
```
4. Push to your branch:
```bash
git push origin feature/my-feature
```
5. Open a Pull Request on GitHub

Please make sure your code:
- Has no TypeScript errors (`npm run build`)
- Is tested on a real Android device
- Updates the README if needed

---

## License

MIT © [Madhurmeet Jadhav](https://github.com/MadhurmeetJadhav)

---

<p align="center">
  If this helped you, please ⭐ <a href="https://github.com/MadhurmeetJadhav/rn-phonenumber-detector">star the repo</a>!
</p>