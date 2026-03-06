import { useState } from 'react';
import { Platform } from 'react-native';
import requestPhoneHint from './PhoneNumberHint';

interface UsePhoneHintOptions {
  countryCallingCode?: string;
  onSuccess?: (number: string) => void;
  onError?: (error: Error) => void;
}

interface UsePhoneHintResult {
  phoneNumber: string;
  loading: boolean;
  error: string | null;
  triggerHint: () => void;
}

const usePhoneHint = ({
  countryCallingCode = '91',
  onSuccess,
  onError,
}: UsePhoneHintOptions = {}): UsePhoneHintResult => {
  const [phoneNumber, setPhoneNumber] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const triggerHint = () => {
    if (Platform.OS !== 'android') return;

    setLoading(true);
    setError(null);

    requestPhoneHint()
      .then((raw) => {
        const stripped = raw
          .replace(new RegExp(`^\\+${countryCallingCode}`), '')
          .trim();
        setPhoneNumber(stripped);
        onSuccess?.(stripped);
      })
      .catch((err: Error) => {
        setError(err.message);
        onError?.(err);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  return { phoneNumber, loading, error, triggerHint };
};

export default usePhoneHint;