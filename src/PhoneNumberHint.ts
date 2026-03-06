import { NativeModules, Platform } from 'react-native';

const { PhoneNumberHint } = NativeModules;

const requestPhoneHint = (): Promise<string> => {
  if (Platform.OS !== 'android') {
    return Promise.reject(new Error('rn-phonenumber-detector: Android only'));
  }
  if (!PhoneNumberHint) {
    return Promise.reject(
      new Error('rn-phonenumber-detector: Native module not found. Did you rebuild the app?')
    );
  }
  return PhoneNumberHint.requestHint();
};

export default requestPhoneHint;