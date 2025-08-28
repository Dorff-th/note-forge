export interface MenuTree {
  id: number;
  name: string;
  path: string;
  children?: MenuTree[];
}
