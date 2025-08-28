// src/types/Menu.ts
import { RoleType } from './Role';

export interface Menu {
  id: number;
  parentId?: number;
  name: string;
  path: string;
  role: RoleType;
  sortOrder: number;
  active: boolean;
}
