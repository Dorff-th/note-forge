interface AvatarProps {
  src?: string | null;
  alt?: string;
  size?: number; // px 단위 (기본 40)
}

export default function Avatar({ src, alt, size = 40 }: AvatarProps) {
  return (
    <img
      src={src || '/default.png'}
      alt={alt || 'profile'}
      style={{ width: size, height: size }}
      className="rounded-full object-cover border"
    />
  );
}
