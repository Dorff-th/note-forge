import React, { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import type { RootState, AppDispatch } from '@store/index';
import { hideToast } from '@store/slices/toastSlice';

const ToastContainer: React.FC = () => {
  const dispatch = useDispatch<AppDispatch>();
  const { message, type, visible } = useSelector((state: RootState) => state.toast);

  useEffect(() => {
    if (visible) {
      const timer = setTimeout(() => {
        dispatch(hideToast());
      }, 3000); // 3초 뒤 자동 닫힘
      return () => clearTimeout(timer);
    }
  }, [visible, dispatch]);

  if (!visible) return null;

  const bgColor =
    type === 'success' ? 'bg-green-500' : type === 'error' ? 'bg-red-500' : 'bg-gray-800';

  return (
    <div className="fixed bottom-5 right-5 z-50">
      <div className={`${bgColor} text-white px-4 py-2 rounded-lg shadow-lg transition-all`}>
        {message}
      </div>
    </div>
  );
};

export default ToastContainer;
