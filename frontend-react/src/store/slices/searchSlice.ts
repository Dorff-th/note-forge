import { createSlice } from '@reduxjs/toolkit';
import type { PayloadAction } from '@reduxjs/toolkit';

export interface SearchState {
  keyword: string;
  categoryId?: number | 'all';
  searchFields: string[]; // ['title','content','comment']
  dateFrom?: string;
  dateTo?: string;
}

const initialState: SearchState = {
  keyword: '',
  categoryId: 'all',
  searchFields: [],
  dateFrom: undefined,
  dateTo: undefined,
};

const searchSlice = createSlice({
  name: 'search',
  initialState,
  reducers: {
    setSearchCondition: (state, action: PayloadAction<SearchState>) => {
      return { ...state, ...action.payload };
    },
    setKeyword: (state, action: PayloadAction<string>) => {
      state.keyword = action.payload;
    },
    resetSearch: () => initialState,
  },
});

export const { setSearchCondition, setKeyword, resetSearch } = searchSlice.actions;
export default searchSlice.reducer;
