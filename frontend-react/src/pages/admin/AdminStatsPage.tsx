import type { ReactNode } from 'react';
import { useEffect, useState } from 'react';
import axiosInstance from '@/api/axiosInstance';
import type { Stats } from '@/types/Stats';
import { Card, CardContent } from '@/components/ui/card';
import { Users, FileText, MessageSquare, Paperclip, Image, Folder, Tag } from 'lucide-react';

export default function AdminStatsPage() {
  const [totalStats, setTotalStats] = useState<Stats | null>(null);
  const [todayStats, setTodayStats] = useState<Stats | null>(null);

  useEffect(() => {
    const fetchStats = async () => {
      const [totalRes, todayRes] = await Promise.all([
        axiosInstance.get<Stats>('/admin/stats'),
        axiosInstance.get<Stats>('/admin/stats?scope=today'),
      ]);
      setTotalStats(totalRes.data);
      setTodayStats(todayRes.data);
    };
    fetchStats();
  }, []);

  const statItems: {
    key: keyof Stats;
    label: string;
    icon: ReactNode;
  }[] = [
    { key: 'memberCount', label: '회원 수', icon: <Users className="w-5 h-5" /> },
    { key: 'postCount', label: '게시글 수', icon: <FileText className="w-5 h-5" /> },
    { key: 'commentCount', label: '댓글 수', icon: <MessageSquare className="w-5 h-5" /> },
    { key: 'attachmentCount', label: '첨부파일 수', icon: <Paperclip className="w-5 h-5" /> },
    { key: 'editorImageCount', label: '에디터 이미지 수', icon: <Image className="w-5 h-5" /> },
    { key: 'categoryCount', label: '카테고리 수', icon: <Folder className="w-5 h-5" /> },
    { key: 'tagCount', label: '태그 수', icon: <Tag className="w-5 h-5" /> },
  ];

  return (
    <div className="p-6 space-y-6">
      <h2 className="text-2xl font-bold">📊 관리자 통계</h2>

      {/* 전체 통계 */}
      <div>
        <h3 className="text-lg font-semibold mb-2">전체 통계</h3>
        <div className="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-4 gap-4">
          {statItems.map((item) => (
            <Card
              key={item.key}
              className="rounded-2xl shadow p-4 flex items-center justify-between"
            >
              <CardContent className="flex items-center gap-3">
                {item.icon}
                <div>
                  <p className="text-sm text-gray-500">{item.label}</p>
                  <p className="text-xl font-bold">{totalStats ? totalStats[item.key] : '-'}</p>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      </div>

      {/* 오늘 통계 */}
      <div>
        <h3 className="text-lg font-semibold mb-2">오늘 생성</h3>
        <div className="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-4 gap-4">
          {statItems.map((item) => (
            <Card
              key={item.key}
              className="rounded-2xl shadow p-4 flex items-center justify-between"
            >
              <CardContent className="flex items-center gap-3">
                {item.icon}
                <div>
                  <p className="text-sm text-gray-500">{item.label}</p>
                  <p className="text-xl font-bold text-blue-600">
                    {todayStats ? todayStats[item.key] : '-'}
                  </p>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      </div>
    </div>
  );
}
