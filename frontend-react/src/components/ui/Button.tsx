// src/components/ui/Button.tsx
import { cn } from '@/utils/cn'; // tailwind 클래스 병합 유틸 (없으면 그냥 string 이어붙여도 됨)

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'default' | 'outline' | 'destructive' | 'secondary'; // ← 추가;
  size?: 'sm' | 'md' | 'lg';
}

export function Button({
  children,
  variant = 'default',
  size = 'md',
  className,
  ...props
}: ButtonProps) {
  const base =
    'rounded font-medium focus:outline-none focus:ring transition disabled:opacity-50 disabled:cursor-not-allowed';

  const variants: Record<typeof variant, string> = {
    default: 'bg-blue-600 text-white hover:bg-blue-700',
    outline: 'border border-gray-300 hover:bg-gray-100',
    destructive: 'bg-red-500 text-white hover:bg-red-600',
    secondary: 'bg-gray-200 text-gray-800 hover:bg-gray-300', // ← 추가
  };

  const sizes: Record<typeof size, string> = {
    sm: 'px-2 py-1 text-sm',
    md: 'px-4 py-2 text-base',
    lg: 'px-6 py-3 text-lg',
  };

  return (
    <button className={cn(base, variants[variant], sizes[size], className)} {...props}>
      {children}
    </button>
  );
}
