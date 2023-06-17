import { IBoardComment } from 'app/shared/model/board-comment.model';

export interface IBoardReComment {
  id?: number;
  content?: string | null;
  delAt?: boolean | null;
  boardComent?: IBoardComment | null;
  boardComment?: IBoardComment | null;
}

export const defaultValue: Readonly<IBoardReComment> = {
  delAt: false,
};
