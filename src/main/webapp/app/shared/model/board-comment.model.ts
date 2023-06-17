import { IBoardReComment } from 'app/shared/model/board-re-comment.model';
import { IBoard } from 'app/shared/model/board.model';

export interface IBoardComment {
  id?: number;
  content?: string | null;
  delAt?: boolean | null;
  boardReComments?: IBoardReComment[] | null;
  board?: IBoard | null;
}

export const defaultValue: Readonly<IBoardComment> = {
  delAt: false,
};
