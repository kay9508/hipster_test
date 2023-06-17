import dayjs from 'dayjs';
import { IBoardComment } from 'app/shared/model/board-comment.model';

export interface IBoard {
  id?: number;
  title?: string | null;
  content?: string | null;
  delAt?: boolean | null;
  boardComments?: IBoardComment[] | null;
}

export const defaultValue: Readonly<IBoard> = {
  delAt: false,
};
