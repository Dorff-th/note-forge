import AppRouter from '@/routes/AppRouter';
import LoadingOverlay from '@/components/common/LoadingOverlay';
import ToastContainer from '@/components/common/ToastContainer';

function App() {
  return (
    <>
      <LoadingOverlay />
      <ToastContainer />
      <AppRouter />
    </>
  );
}

export default App;
