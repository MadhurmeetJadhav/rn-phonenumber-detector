# rn-phonenumber-detector

Auto-detect and fetch phone number on Android via Google Identity API (no third-party dependencies). iOS uses native `textContentType` — no extra code needed.

## Demo

- Android → Google Hint API shows native bottom sheet with phone number
- User selects → input pre-filled automatically ✅

## Installation
```bash
yarn add rn-phonenumber-detector
# or
npm install rn-phonenumber-detector
```

### Android Setup

1. Add to `android/app/build.gradle`:
```gradle
dependencies {
    implementation 'com.google.android.gms:play-services-auth:21.0.0'
}
```

2. Register in `MainApplication.kt`:
```kotlin
import com.madhur.phonenumberdetector.PhoneNumberHintPackage

override fun getPackages() = PackageList(this).packages.apply {
    add(PhoneNumberHintPackage())
}
```

3. Rebuild:
```bash
cd android && ./gradlew clean
cd ..
npx react-native run-android
```

### iOS Setup

No setup needed. Use `textContentType='telephoneNumber'` on your `TextInput` — iOS handles the rest natively.

## Usage

### Hook (recommended)
```ts
import { usePhoneHint } from 'rn-phonenumber-detector';

const MyScreen = () => {
  const { triggerHint, phoneNumber, loading } = usePhoneHint({
    countryCallingCode: '91', // strips +91 from result
    onSuccess: (number) => console.log('Got:', number),
    onError: (err) => console.log('Error:', err),
  });

  useEffect(() => {
    triggerHint(); // trigger when screen is focused
  }, []);

  return (
    <TextInput
      value={phoneNumber}
      keyboardType="number-pad"
      textContentType="telephoneNumber"
    />
  );
};
```

### Direct call
```ts
import { requestPhoneHint } from 'rn-phonenumber-detector';

requestPhoneHint()
  .then((number) => console.log(number)) // +919876543210
  .catch(() => {}); // silent fail — user dismissed
```

## API

### `usePhoneHint(options)`

| Option | Type | Default | Description |
|---|---|---|---|
| `countryCallingCode` | `string` | `'91'` | Strips country code from result |
| `onSuccess` | `(number: string) => void` | — | Called with stripped number |
| `onError` | `(error: Error) => void` | — | Called on failure or dismiss |

Returns `{ phoneNumber, loading, error, triggerHint }`

### `requestPhoneHint()`

Returns `Promise<string>` — resolves with full number e.g. `+919876543210`

## Platform Support

| Platform | Support | Method |
|---|---|---|
| Android | ✅ | Google Identity API — native bottom sheet |
| iOS | ✅ | `textContentType='telephoneNumber'` — QuickType bar |

## Notes

- Works only on real devices (not emulators)
- Requires Google Play Services on Android
- Fails silently if no number found — user types manually
- Test on device with SIM + Google account linked

## License

MIT © Madhurmeet Jadhav