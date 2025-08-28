// src/api/adminMenuApi.ts
import axiosInstance from '@/api/axiosInstance';
import type { Menu } from '@/types/Menu';

export const fetchMenus = async (): Promise<Menu[]> => {
  const res = await axiosInstance.get('/admin/menus');
  return res.data;
};

export const fetchMenuById = async (id: number): Promise<Menu> => {
  const res = await axiosInstance.get(`/admin/menus/${id}`);
  return res.data;
};

export const createMenu = async (data: Omit<Menu, 'id'>) => {
  const res = await axiosInstance.post('/admin/menus', data);
  return res.data;
};

export const updateMenu = async (id: number, data: Omit<Menu, 'id'>) => {
  const res = await axiosInstance.put(`/admin/menus/${id}`, data);
  return res.data;
};

export const deleteMenu = async (id: number) => {
  await axiosInstance.delete(`/admin/menus/${id}`);
};
