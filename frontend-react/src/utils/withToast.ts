// src/utils/withToast.ts
import { store } from '@store/index';
import { showToast } from '@store/slices/toastSlice';

export async function withToast<T>(
  promise: Promise<T>,
  messages?: { success?: string; error?: string },
): Promise<T | null> {
  try {
    const result = await promise;
    if (messages?.success) {
      store.dispatch(showToast({ type: 'success', message: messages.success }));
    }
    return result;
  } catch (error) {
    if (messages?.error) {
      store.dispatch(showToast({ type: 'error', message: messages.error }));
    }
    return null;
  }
}
